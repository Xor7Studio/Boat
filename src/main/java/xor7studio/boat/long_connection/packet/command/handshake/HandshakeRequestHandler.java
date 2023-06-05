package xor7studio.boat.long_connection.packet.command.handshake;

import io.netty.channel.*;
import org.jetbrains.annotations.NotNull;
import xor7studio.boat.long_connection.session.SessionAttributes;

@ChannelHandler.Sharable
public class HandshakeRequestHandler extends SimpleChannelInboundHandler<HandshakeRequestPacket> {
    public static final HandshakeRequestHandler INSTANCE = new HandshakeRequestHandler();
    protected HandshakeRequestHandler(){}
    @Override
    protected void channelRead0(@NotNull ChannelHandlerContext ctx, @NotNull HandshakeRequestPacket packet) {
        ctx.channel().attr(SessionAttributes.SESSION_TOKEN).set(packet.getSessionToken());
        HandshakeResponsePacket response = new HandshakeResponsePacket();
        response.setSuccess(true);
    }
}
