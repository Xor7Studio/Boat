package xor7studio.boat.long_connection;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import org.jetbrains.annotations.NotNull;
import xor7studio.boat.long_connection.packet.command.handshake.HandshakeRequestPacket;
import xor7studio.boat.long_connection.session.SessionAttributes;

public class ClientLongConnectionHandler extends ChannelDuplexHandler {
    @Override
    public void channelActive(@NotNull ChannelHandlerContext ctx) {
        HandshakeRequestPacket packet = new HandshakeRequestPacket();
        packet.setSessionToken(ctx.channel().attr(SessionAttributes.SESSION_TOKEN).get());
        ctx.channel().writeAndFlush(packet);
        ctx.pipeline().remove(this);
    }
}
