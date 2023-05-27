package xor7studio.boat.authentication;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import org.jetbrains.annotations.NotNull;
import xor7studio.boat.config.BoatConfig;
import xor7studio.boat.config.ServerConfig;

import javax.net.ssl.SSLException;
import java.io.File;
import java.net.InetSocketAddress;

public class AuthenticationServer{
    private final ServerConfig config;
    public AuthenticationServer(@NotNull ServerConfig config){
        this.config=config;
    }
    public void start(){
        InetSocketAddress listen = BoatConfig.toInetSocketAddress(config.authentication.listen);
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            if(config.authentication.enable_ssl)
                                pipeline.addLast(new SslHandler(generateSslContext().newEngine(ch.alloc())));
                            pipeline.addLast(new HttpServerCodec());
                            pipeline.addLast(new HttpObjectAggregator(65536));
                            pipeline.addLast(RequestHandler.INSTANCE);
                        }
                    });
            bootstrap.bind(listen).sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
    public SslContext generateSslContext(){
        try {
            return SslContextBuilder
                    .forServer(
                            new File(config.authentication.cert_file),
                            new File(config.authentication.key_file))
                    .build();
        } catch (SSLException e) {
            throw new RuntimeException(e);
        }
    }
}
