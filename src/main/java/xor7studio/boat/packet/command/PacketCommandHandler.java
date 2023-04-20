package xor7studio.boat.packet.command;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.jetbrains.annotations.NotNull;
import xor7studio.boat.packet.Packet;

public class PacketCommandHandler extends SimpleChannelInboundHandler<Packet> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, @NotNull Packet packet) throws Exception {
        packet.getHandler().channelRead(ctx,packet);
    }
}
