package xor7studio.boat.packet.command.heartbeat;

import xor7studio.boat.packet.Packet;
import xor7studio.boat.packet.command.PacketCommand;

public class HeartBeatResponsePacket extends Packet {
    @Override
    public Byte getCommand() {
        return PacketCommand.HANDSHAKE_RESPONSE;
    }
    @Override
    @SuppressWarnings("unchecked")
    public HeartBeatResponseHandler getHandler() {
        return HeartBeatResponseHandler.INSTANCE;
    }
}
