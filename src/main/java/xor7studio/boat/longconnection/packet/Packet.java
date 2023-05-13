package xor7studio.boat.longconnection.packet;

import io.netty.channel.SimpleChannelInboundHandler;

public abstract class Packet<T extends SimpleChannelInboundHandler<? extends Packet<?>>> {
    public abstract Byte getCommand();
    public abstract T getHandler();
}
