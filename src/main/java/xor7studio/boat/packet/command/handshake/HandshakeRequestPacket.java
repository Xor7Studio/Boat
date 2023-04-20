package xor7studio.boat.packet.command.handshake;

import lombok.Builder;
import lombok.Data;
import xor7studio.boat.packet.Packet;
import xor7studio.boat.packet.command.PacketCommand;

@Data
@Builder
public class HandshakeRequestPacket extends Packet {
    private String appID;
    @Override
    public Byte getCommand() {
        return PacketCommand.HANDSHAKE_REQUEST;
    }
    @Override
    @SuppressWarnings("unchecked")
    public HandshakeRequestHandler getHandler() {
        return HandshakeRequestHandler.INSTANCE;
    }
}
