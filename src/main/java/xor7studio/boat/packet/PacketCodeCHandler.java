package xor7studio.boat.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@ChannelHandler.Sharable
public class PacketCodeCHandler extends MessageToMessageCodec<ByteBuf,Packet> {
    public static final PacketCodeCHandler INSTANCE = new PacketCodeCHandler();
    private PacketCodeCHandler(){}
    @Override
    protected void encode(@NotNull ChannelHandlerContext channelHandlerContext, Packet packet, @NotNull List<Object> list) throws Exception {
        ByteBuf byteBuf = channelHandlerContext.channel().alloc().ioBuffer();
        PacketCodeC.INSTANCE.encode(packet,byteBuf);
        list.add(byteBuf);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, @NotNull List<Object> list) throws Exception {
        list.add(PacketCodeC.INSTANCE.decode(byteBuf));
    }
}
