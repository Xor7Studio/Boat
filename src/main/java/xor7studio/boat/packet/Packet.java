package xor7studio.boat.packet;

import io.netty.channel.SimpleChannelInboundHandler;

public abstract class Packet {
    public abstract Byte getCommand();
    public abstract <T extends SimpleChannelInboundHandler<? extends Packet>> T getHandler();
}
