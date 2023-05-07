package xor7studio.boat.packet.command.handshake;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class HandshakeResponseHandler extends SimpleChannelInboundHandler<HandshakeResponsePacket> {
    public static final HandshakeResponseHandler INSTANCE = new HandshakeResponseHandler();
    protected HandshakeResponseHandler(){}
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HandshakeResponsePacket packet) {
        if(packet.isSuccess()){
            System.out.println("握手成功");
        }
    }
}
