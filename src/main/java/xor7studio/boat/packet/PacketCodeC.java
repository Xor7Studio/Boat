package xor7studio.boat.packet;

import io.netty.buffer.ByteBuf;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class PacketCodeC {
    public static final PacketCodeC INSTANCE = new PacketCodeC();
    private final ThreadLocal<LinkedBuffer> bufferThreadLocal = ThreadLocal.withInitial(() -> LinkedBuffer.allocate(512));
    private final ThreadLocal<Map<Class<? extends Packet>, Schema<? extends Packet>>> schemaMapThreadLocal = ThreadLocal.withInitial(HashMap::new);
    private PacketCodeC(){}
    public <T extends Packet> void encode(@NotNull T packet,@NotNull ByteBuf result) {
        @SuppressWarnings("unchecked")
        Class<T> clazz = (Class<T>) packet.getClass();
        LinkedBuffer buffer = bufferThreadLocal.get();
        Schema<T> schema = getSchema(clazz);
        byte[] bytes = ProtostuffIOUtil.toByteArray(packet, schema, buffer);
        buffer.clear();
        result.writeByte(packet.getCommand());
        result.writeInt(bytes.length);
        result.writeBytes(bytes);
    }
    public <T extends Packet> @NotNull T decode(@NotNull ByteBuf byteBuf)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        @SuppressWarnings("unchecked")
        Class<T> clazz = (Class<T>) PacketManager.INSTANCE.getPacket(byteBuf.readByte());
        int length = byteBuf.readInt();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        T packet = clazz.getConstructor().newInstance();
        Schema<T> schema = getSchema(clazz);
        ProtostuffIOUtil.mergeFrom(bytes, packet, schema);
        return packet;
    }
    @SuppressWarnings("unchecked")
    private <T extends Packet> Schema<T> getSchema(Class<T> clazz) {
        Map<Class<? extends Packet>, Schema<? extends Packet>> schemaMap = schemaMapThreadLocal.get();
        Schema<T> schema = (Schema<T>) schemaMap.get(clazz);
        if (schema == null) {
            schema = RuntimeSchema.getSchema(clazz);
            schemaMap.put(clazz, schema);
        }
        return schema;
    }
}
