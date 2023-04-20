package xor7studio.boat.packet.handshake;

import xor7studio.boat.packet.Packet;
import xor7studio.boat.packet.PacketCommand;

public class HandshakeRequestPacket extends Packet {
    @Override
    public Byte getCommand() {
        return PacketCommand.HandshakeRequest;
    }
}
