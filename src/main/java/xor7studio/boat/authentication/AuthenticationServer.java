package xor7studio.boat.authentication;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import xor7studio.boat.authentication.request.RequestHandler;
import xor7studio.boat.config.BoatConfig;
import xor7studio.boat.config.ServerConfig;

public class AuthenticationServer {
    private final ServerConfig config;
    public AuthenticationServer(ServerConfig config){
        this.config=config;
    }
    public void start(){
        NioEventLoopGroup bossEventLoopGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerEventLoopGroup = new NioEventLoopGroup();
//        SslContext sslContext;
//        try {
//            sslContext = SslContextBuilder
//                    .forServer(new File(config.authentication.cert_file),new File(config.authentication.key_file))
//                    .build();
//        } catch (SSLException e) {
//            throw new RuntimeException(e);
//        }
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossEventLoopGroup,workerEventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel channel) {
                        ChannelPipeline pipeline = channel.pipeline();
//                        pipeline.addLast(sslContext.newHandler(channel.alloc()));
                        pipeline.addLast(new HttpServerCodec());
                        pipeline.addLast(new HttpObjectAggregator(65536));
                        pipeline.addLast(RequestHandler.INSTANCE);
                    }
                });
        serverBootstrap.bind(BoatConfig.toInetSocketAddress(config.authentication.listen));
    }
}
