package xor7studio.boat.traceback;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@ChannelHandler.Sharable
public class ServerTracebackHandler extends ChannelInboundHandlerAdapter {
    public static final ServerTracebackHandler INSTANCE = new ServerTracebackHandler();
    protected ServerTracebackHandler(){};
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf byteBuf=(ByteBuf) msg;
        if(byteBuf.readByte()!=0x07) ctx.close();
        
    }
}
