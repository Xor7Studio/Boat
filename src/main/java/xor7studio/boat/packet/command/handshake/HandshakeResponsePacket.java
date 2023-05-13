package xor7studio.boat.packet.command.handshake;

import lombok.Data;
import xor7studio.boat.packet.Packet;
import xor7studio.boat.packet.command.PacketCommand;

@Data
public class HandshakeResponsePacket extends Packet<HandshakeResponseHandler> {
    private boolean isSuccess;
    @Override
    public Byte getCommand() {
        return PacketCommand.HANDSHAKE_RESPONSE;
    }
    @Override
    public HandshakeResponseHandler getHandler() {
        return HandshakeResponseHandler.INSTANCE;
    }
}
