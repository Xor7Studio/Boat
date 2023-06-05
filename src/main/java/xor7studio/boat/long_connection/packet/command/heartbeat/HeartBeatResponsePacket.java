package xor7studio.boat.long_connection.packet.command.heartbeat;

import xor7studio.boat.long_connection.packet.Packet;
import xor7studio.boat.long_connection.packet.command.PacketCommand;

public class HeartBeatResponsePacket extends Packet<HeartBeatResponseHandler> {
    @Override
    public Byte getCommand() {
        return PacketCommand.HEARTBEAT_RESPONSE;
    }
    @Override
    public HeartBeatResponseHandler getHandler() {
        return HeartBeatResponseHandler.INSTANCE;
    }
}
