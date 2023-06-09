package xor7studio.boat.long_connection.packet.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import org.jetbrains.annotations.NotNull;
import xor7studio.boat.long_connection.packet.Packet;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@ChannelHandler.Sharable
public class PacketCodecHandler extends MessageToMessageCodec<ByteBuf, Packet<?>> {
    public static final PacketCodecHandler INSTANCE = new PacketCodecHandler();
    protected PacketCodecHandler(){}
    @Override
    protected void encode(@NotNull ChannelHandlerContext ctx, Packet<?> packet, @NotNull List<Object> out) {
        ByteBuf byteBuf = ctx.channel().alloc().ioBuffer();
        PacketCodec.INSTANCE.encode(packet,byteBuf);
        out.add(byteBuf);
    }
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, @NotNull List<Object> out){
        try {
            out.add(PacketCodec.INSTANCE.decode(byteBuf));
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
