package xor7studio.boat.packet.command.handshake;

import io.netty.channel.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xor7studio.boat.session.SessionAttributes;

@ChannelHandler.Sharable
public class HandshakeRequestHandler extends SimpleChannelInboundHandler<HandshakeRequestPacket> {
    private final Logger logger = LoggerFactory.getLogger("Boat Server");
    public static final HandshakeRequestHandler INSTANCE = new HandshakeRequestHandler();
    protected HandshakeRequestHandler(){}
    @Override
    protected void channelRead0(@NotNull ChannelHandlerContext ctx, @NotNull HandshakeRequestPacket packet) {
        ctx.channel().attr(SessionAttributes.SESSION_TOKEN).set(packet.getSessionToken());
        System.out.println(packet.getSessionToken());
        HandshakeResponsePacket response = new HandshakeResponsePacket();
        response.setSuccess(true);
        System.out.println(response);
        logger.info(String.valueOf(ctx.isRemoved()));
        logger.info(String.valueOf(ctx.channel().isActive()));
        ctx.channel().writeAndFlush(response).addListener((ChannelFutureListener) channelFuture -> {
            logger.info(String.valueOf(ctx.isRemoved()));
            logger.info(String.valueOf(ctx.channel().isActive()));
        });
    }
}
