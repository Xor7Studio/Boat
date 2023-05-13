package xor7studio.boat.longconnection.packet.command.heartbeat;

import xor7studio.boat.longconnection.packet.Packet;
import xor7studio.boat.longconnection.packet.command.PacketCommand;

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
