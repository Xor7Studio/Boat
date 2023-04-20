package xor7studio.boat.packet.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

@ChannelHandler.Sharable
public class PacketAesCodec extends MessageToMessageCodec<ByteBuf,ByteBuf> {
    public static final PacketAesCodec INSTANCE = new PacketAesCodec();
    protected PacketAesCodec(){}
    @Getter
    @Setter
    private byte[] key=generateKey();
    @Override
    protected void decode(ChannelHandlerContext ctx, @NotNull ByteBuf byteBuf, List<Object> out) throws Exception {
        if (byteBuf.readableBytes() < 16) return;
        byte[] iv = new byte[16];
        byteBuf.readBytes(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        out.addAll(Arrays.asList(Unpooled.wrappedBuffer(cipher.update(bytes)), Unpooled.wrappedBuffer(cipher.doFinal())));
    }
    @Override
    protected void encode(ChannelHandlerContext ctx, @NotNull ByteBuf byteBuf, @NotNull List<Object> out) throws Exception {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        out.add(Unpooled.wrappedBuffer(iv));
        out.add(Unpooled.wrappedBuffer(cipher.update(byteBuf.nioBuffer().array()), cipher.doFinal()));
    }
    @SneakyThrows
    byte[] generateKey() {
//        SecureRandom secureRandom = new SecureRandom();
//        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
//        keyGenerator.init(128, secureRandom);
//        SecretKey secretKey = keyGenerator.generateKey();
//        return secretKey.getEncoded();
        return "boatboatboatboat".getBytes();
    }
}
