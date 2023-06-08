package xor7studio.boat.traceback;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;

@ChannelHandler.Sharable
public class ServerTracebackHandler extends SimpleChannelInboundHandler<ByteBuf> {
    public static final ServerTracebackHandler INSTANCE = new ServerTracebackHandler();
    @Override
    public void channelActive(@NotNull ChannelHandlerContext ctx) {
        InetSocketAddress address=(InetSocketAddress)ctx.channel().remoteAddress();
        ctx.channel().attr(TracebackService.SOURCE).set(String.format(
                "%s:%d",
                address.getHostString(),
                address.getPort()));
    }
    protected ServerTracebackHandler(){}
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, @NotNull ByteBuf byteBuf) {
        if(byteBuf.readByte()!=0x07) ctx.close();
        ByteBuf result=ctx.alloc().ioBuffer();
        String data=ctx.channel().attr(TracebackService.SOURCE).get();
        result.writeByte(data.length());
        result.writeBytes(data.getBytes());
        ctx.writeAndFlush(result);
    }
}
