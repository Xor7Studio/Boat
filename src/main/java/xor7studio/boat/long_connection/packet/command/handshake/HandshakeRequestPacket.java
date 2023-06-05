package xor7studio.boat.long_connection.packet.command.handshake;

import lombok.Data;
import xor7studio.boat.long_connection.packet.Packet;
import xor7studio.boat.long_connection.packet.command.PacketCommand;

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
