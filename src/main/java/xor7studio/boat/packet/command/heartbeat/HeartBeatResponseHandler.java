package xor7studio.boat.packet.command.heartbeat;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class HeartBeatResponseHandler extends SimpleChannelInboundHandler<HeartBeatResponseHandler> {
    public static final HeartBeatResponseHandler INSTANCE = new HeartBeatResponseHandler();
    protected HeartBeatResponseHandler(){}
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartBeatResponseHandler packet) throws Exception {

    }
}
