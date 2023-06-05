package xor7studio.boat.long_connection.packet.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import xor7studio.boat.long_connection.packet.Packet;
import xor7studio.boat.long_connection.packet.command.PacketCommandManager;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class PacketCodec {
    public static final PacketCodec INSTANCE = new PacketCodec();
    private final ThreadLocal<LinkedBuffer> bufferThreadLocal =
            ThreadLocal.withInitial(() -> LinkedBuffer.allocate(512));
    private final ThreadLocal<Map<Class<? extends Packet<?>>, Schema<? extends Packet<?>>>> schemaMapThreadLocal =
            ThreadLocal.withInitial(HashMap::new);
    @Getter
    @Setter
    private byte[] key=generateKey();
    protected PacketCodec(){}
    public <T extends Packet<?>> void encode(@NotNull T packet,@NotNull ByteBuf result) {
        @SuppressWarnings("unchecked")
        Class<T> clazz = (Class<T>) packet.getClass();
        LinkedBuffer buffer = bufferThreadLocal.get();
        Schema<T> schema = getSchema(clazz);
        byte[] bytes;
        try {
            bytes = encrypt(ProtostuffIOUtil.toByteArray(packet, schema, buffer));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException |
                 InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
        buffer.clear();
        result.writeByte(packet.getCommand());
        result.writeInt(bytes.length);
        result.writeBytes(bytes);
    }
    public <T extends Packet<?>> @NotNull T decode(@NotNull ByteBuf byteBuf)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        @SuppressWarnings("unchecked")
        Class<T> clazz = (Class<T>) PacketCommandManager.INSTANCE.getPacket(byteBuf.readByte());
        byteBuf.skipBytes(4);
        T packet = clazz.getConstructor().newInstance();
        Schema<T> schema = getSchema(clazz);
        try {
            ProtostuffIOUtil.mergeFrom(decrypt(byteBuf), packet, schema);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException |
                 InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }
    private byte @NotNull [] encrypt(byte[] data)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        ByteBuf result = ByteBufAllocator.DEFAULT.buffer();
        result.writeBytes(iv);
        result.writeBytes(cipher.update(data));
        result.writeBytes(cipher.doFinal());
        byte[] res = new byte[result.readableBytes()];
        result.readBytes(res);
        result.release();
        return res;
    }
    private byte @NotNull [] decrypt(@NotNull ByteBuf byteBuf)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
        if (byteBuf.readableBytes() < 16) throw new RuntimeException();
        byte[] iv = new byte[16];
        byteBuf.readBytes(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        ByteBuf result = ByteBufAllocator.DEFAULT.buffer();
        result.writeBytes(cipher.update(bytes));
        result.writeBytes(cipher.doFinal());
        byte[] res = new byte[result.readableBytes()];
        result.readBytes(res);
        result.release();
        return res;
    }
    @SuppressWarnings("unchecked")
    private <T extends Packet<?>> Schema<T> getSchema(Class<T> clazz) {
        Map<Class<? extends Packet<?>>, Schema<? extends Packet<?>>> schemaMap = schemaMapThreadLocal.get();
        Schema<T> schema = (Schema<T>) schemaMap.get(clazz);
        if (schema == null) {
            schema = RuntimeSchema.getSchema(clazz);
            schemaMap.put(clazz, schema);
        }
        return schema;
    }
    @Contract(value = " -> new", pure = true)
    @SneakyThrows
    private byte @NotNull [] generateKey() {
//        SecureRandom secureRandom = new SecureRandom();
//        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
//        keyGenerator.init(128, secureRandom);
//        SecretKey secretKey = keyGenerator.generateKey();
//        return secretKey.getEncoded();
        return "boatboatboatboat".getBytes();
    }
}
