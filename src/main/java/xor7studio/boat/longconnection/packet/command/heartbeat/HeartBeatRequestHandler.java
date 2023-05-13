package xor7studio.boat.longconnection.packet.command.heartbeat;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class HeartBeatRequestHandler extends SimpleChannelInboundHandler<HeartBeatRequestPacket> {
    public static final HeartBeatRequestHandler INSTANCE = new HeartBeatRequestHandler();
    protected HeartBeatRequestHandler(){}
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartBeatRequestPacket packet) {
    }
}
