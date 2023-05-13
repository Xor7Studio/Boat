package xor7studio.boat.packet.command;

import lombok.Setter;
import xor7studio.boat.packet.Packet;
import xor7studio.boat.packet.command.handshake.HandshakeRequestPacket;
import xor7studio.boat.packet.command.handshake.HandshakeResponsePacket;
import xor7studio.boat.packet.command.heartbeat.HeartBeatRequestPacket;
import xor7studio.boat.packet.command.heartbeat.HeartBeatResponsePacket;

import java.util.HashMap;
import java.util.Map;

public class PacketCommandManager {
    public static final PacketCommandManager INSTANCE = new PacketCommandManager();
    @Setter
    private boolean is_client;
    private final Map<Byte,Class<? extends Packet>> packets=new HashMap<>();
    protected PacketCommandManager(){
        if(is_client){
            packets.put(PacketCommand.HEARTBEAT_RESPONSE, HeartBeatResponsePacket.class);
            packets.put(PacketCommand.HANDSHAKE_RESPONSE, HandshakeResponsePacket.class);
        }else{
            packets.put(PacketCommand.HEARTBEAT_REQUEST, HeartBeatRequestPacket.class);
            packets.put(PacketCommand.HANDSHAKE_REQUEST, HandshakeRequestPacket.class);
        }
    }
    public Class<? extends Packet> getPacket(byte command){
        return packets.get(command);
    }
}
