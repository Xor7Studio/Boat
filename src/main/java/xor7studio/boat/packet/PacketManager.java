package xor7studio.boat.packet;

import java.util.HashMap;
import java.util.Map;

public class PacketManager {
    public static final PacketManager INSTANCE = new PacketManager();
    private final Map<Byte,Class<? extends Packet>> packets=new HashMap<>();
    private PacketManager(){}
    public Class<? extends Packet> getPacket(byte command){
        return packets.get(command);
    }
    public void registerPacket(byte command,Class<? extends Packet> clazz){
        packets.put(command,clazz);
    }
}
