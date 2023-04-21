package xor7studio.boat.packet.command.heartbeat;

import lombok.Data;
import xor7studio.boat.packet.Packet;
import xor7studio.boat.packet.command.PacketCommand;

@Data
public class HeartBeatRequestPacket extends Packet {
    public HeartBeatRequestPacket(){}
    @Override
    public Byte getCommand() {
        return PacketCommand.HANDSHAKE_REQUEST;
    }
    String appID;
    @Override
    @SuppressWarnings("unchecked")
    public HeartBeatRequestHandler getHandler() {
        return HeartBeatRequestHandler.INSTANCE;
    }
}
