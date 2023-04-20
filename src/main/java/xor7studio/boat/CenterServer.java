package xor7studio.boat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xor7studio.boat.packet.PacketCodeCHandler;

import java.net.BindException;
import java.net.InetSocketAddress;

public class CenterServer {
    @Getter
    private final InetSocketAddress listenAddress;
    private final Logger logger = LoggerFactory.getLogger("Boat Center Server");
    public CenterServer(@NotNull InetSocketAddress listenAddr){
        listenAddress=listenAddr;
        NioEventLoopGroup bossEventLoopGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerEventLoopGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossEventLoopGroup,workerEventLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel channel) {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast(PacketCodeCHandler.INSTANCE);
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(listenAddress).sync();
            channelFuture.addListener(future -> {
                String addr = listenAddress.getHostName() + ":" + listenAddress.getPort();
                if (future.isSuccess())
                    logger.info("成功绑定到地址：" + addr);
                else throw new BindException(addr);
            });
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
