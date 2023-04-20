package xor7studio.boat.packet.command.handshake;

import xor7studio.boat.packet.Packet;
import xor7studio.boat.packet.command.PacketCommand;

public class HandshakeResponsePacket extends Packet {
    @Override
    public Byte getCommand() {
        return PacketCommand.HANDSHAKE_RESPONSE;
    }
    @Override
    @SuppressWarnings("unchecked")
    public HandshakeResponseHandler getHandler() {
        return HandshakeResponseHandler.INSTANCE;
    }
}
