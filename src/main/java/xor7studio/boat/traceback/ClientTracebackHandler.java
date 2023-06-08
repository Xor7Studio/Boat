package xor7studio.boat.traceback;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.jetbrains.annotations.NotNull;

public class ClientTracebackHandler extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    public void channelActive(@NotNull ChannelHandlerContext ctx) {
        ByteBuf byteBuf=ctx.alloc().ioBuffer();
        byteBuf.writeByte(0x07);
        ctx.writeAndFlush(byteBuf);
    }

    @Override
    protected void channelRead0(@NotNull ChannelHandlerContext ctx, @NotNull ByteBuf byteBuf) {
        int length=byteBuf.readByte();
        byte[] data=new byte[length];
        byteBuf.readBytes(data);
        System.out.println(new String(data));
        ctx.close();
    }
}
