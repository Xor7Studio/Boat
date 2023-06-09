package xor7studio.boat.traceback;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import xor7studio.boat.Signal;

public class ClientTracebackHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private final Signal signal;
    @Getter
    private String result;
    public ClientTracebackHandler(Signal signal){
        this.signal=signal;
    }
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
        ctx.close();
        this.result=new String(data);
        signal.emitSignal();
    }
}
