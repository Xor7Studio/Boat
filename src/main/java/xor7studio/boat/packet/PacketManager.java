package xor7studio.boat.packet;

import xor7studio.boat.packet.command.PacketCommand;
import xor7studio.boat.packet.command.handshake.HandshakeRequestPacket;

import java.util.HashMap;
import java.util.Map;

public class PacketManager {
    public static final PacketManager INSTANCE = new PacketManager();
    private final Map<Byte,Class<? extends Packet>> packets=new HashMap<>();
    protected PacketManager(){
        packets.put(PacketCommand.HANDSHAKE_REQUEST, HandshakeRequestPacket.class);
    }
    public Class<? extends Packet> getPacket(byte command){
        return packets.get(command);
    }
}
