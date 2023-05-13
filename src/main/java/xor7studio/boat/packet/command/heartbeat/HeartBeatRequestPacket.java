package xor7studio.boat.packet.command.heartbeat;

import lombok.Data;
import xor7studio.boat.packet.Packet;
import xor7studio.boat.packet.command.PacketCommand;

@Data
public class HeartBeatRequestPacket extends Packet<HeartBeatRequestHandler> {
    public HeartBeatRequestPacket(){}
    @Override
    public Byte getCommand() {
        return PacketCommand.HEARTBEAT_REQUEST;
    }
    @Override
    public HeartBeatRequestHandler getHandler() {
        return HeartBeatRequestHandler.INSTANCE;
    }
}
