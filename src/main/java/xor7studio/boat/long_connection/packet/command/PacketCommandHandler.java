package xor7studio.boat.long_connection.packet.command;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.jetbrains.annotations.NotNull;
import xor7studio.boat.long_connection.packet.Packet;

@ChannelHandler.Sharable
public class PacketCommandHandler extends SimpleChannelInboundHandler<Packet<?>> {
    public static final PacketCommandHandler INSTANCE = new PacketCommandHandler();
    protected PacketCommandHandler(){}
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, @NotNull Packet packet) throws Exception {
        packet.getHandler().channelRead(ctx,packet);
    }
}
