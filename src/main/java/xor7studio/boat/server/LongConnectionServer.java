package xor7studio.boat.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import xor7studio.boat.packet.codec.PacketCodecHandler;
import xor7studio.boat.packet.command.PacketCommandHandler;
import xor7studio.boat.packet.command.PacketCommandManager;

import java.net.InetSocketAddress;

public class LongConnectionServer {
    @Getter
    private final InetSocketAddress listen;
    public LongConnectionServer(@NotNull InetSocketAddress listen){
        this.listen=listen;
    }
    public void start(){
        NioEventLoopGroup bossEventLoopGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerEventLoopGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossEventLoopGroup,workerEventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel channel) {
                        PacketCommandManager.INSTANCE.set_client(false);
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,1,4));
                        pipeline.addLast(PacketCodecHandler.INSTANCE);
                        pipeline.addLast(PacketCommandHandler.INSTANCE);
                    }
                });
        serverBootstrap.bind(listen);
    }
}
