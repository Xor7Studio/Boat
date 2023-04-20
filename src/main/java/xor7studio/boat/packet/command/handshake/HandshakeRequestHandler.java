package xor7studio.boat.packet.command.handshake;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class HandshakeRequestHandler extends SimpleChannelInboundHandler<HandshakeRequestPacket> {
    public static final HandshakeRequestHandler INSTANCE = new HandshakeRequestHandler();
    protected HandshakeRequestHandler(){}
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HandshakeRequestPacket packet) throws Exception {

    }
}
