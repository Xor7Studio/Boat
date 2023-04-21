package xor7studio.boat.packet.codec;

import io.netty.buffer.ByteBuf;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import xor7studio.boat.packet.Packet;
import xor7studio.boat.packet.PacketManager;

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
    private final ThreadLocal<Map<Class<? extends Packet>, Schema<? extends Packet>>> schemaMapThreadLocal =
            ThreadLocal.withInitial(HashMap::new);
    @Getter
    @Setter
    private byte[] key=generateKey();
    protected PacketCodec(){}
    public <T extends Packet> void encode(@NotNull T packet,@NotNull ByteBuf result) {
        @SuppressWarnings("unchecked")
        Class<T> clazz = (Class<T>) packet.getClass();
        LinkedBuffer buffer = bufferThreadLocal.get();
        Schema<T> schema = getSchema(clazz);
        byte[] bytes = new byte[0];
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
    public <T extends Packet> @NotNull T decode(@NotNull ByteBuf byteBuf)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        @SuppressWarnings("unchecked")
        Class<T> clazz = (Class<T>) PacketManager.INSTANCE.getPacket(byteBuf.readByte());
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
        byte[] tmp = cipher.update(data);
        byte[] tail = cipher.doFinal();
        byte[] result = new byte[iv.length + tmp.length + tail.length];
        System.arraycopy(iv, 0, result, 0, iv.length);
        System.arraycopy(tmp, 0, result, iv.length, tmp.length);
        System.arraycopy(tail, 0, result, iv.length + tmp.length, tail.length);
        return result;
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
        byte[] tmp = cipher.update(bytes);
        byte[] tail = cipher.doFinal();
        byte[] result = new byte[tmp.length + tail.length];
        System.arraycopy(tmp,0,result,0,tmp.length);
        System.arraycopy(tail,0,result,tmp.length,tail.length);
        return result;
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
