package xor7studio.boat.serialize;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import xor7studio.boat.packet.Packet;

public class PacketSerializer {
    private static final ThreadLocal<LinkedBuffer> BUFFER = ThreadLocal.withInitial(() -> LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
    private static final Schema<Packet> PACKET_SCHEMA = RuntimeSchema.getSchema(Packet.class);

    public static byte[] serialize(Packet packet) {
        LinkedBuffer buffer = BUFFER.get();
        try {
            return ProtostuffIOUtil.toByteArray(packet, PACKET_SCHEMA, buffer);
        } finally {
            buffer.clear();
        }
    }

    public static Packet deserialize(byte[] data) {
        Packet packet = PACKET_SCHEMA.newMessage();
        ProtostuffIOUtil.mergeFrom(data, packet, PACKET_SCHEMA);
        return packet;
    }
}

