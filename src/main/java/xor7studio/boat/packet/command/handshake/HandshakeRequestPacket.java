package xor7studio.boat.packet.command.handshake;

import lombok.Data;
import xor7studio.boat.packet.Packet;
import xor7studio.boat.packet.command.PacketCommand;

@Data
public class HandshakeRequestPacket extends Packet<HandshakeRequestHandler> {
    public HandshakeRequestPacket(){}
    private String sessionToken;
    @Override
    public Byte getCommand() {
        return PacketCommand.HANDSHAKE_REQUEST;
    }
    @Override
    public HandshakeRequestHandler getHandler() {
        return HandshakeRequestHandler.INSTANCE;
    }
}
