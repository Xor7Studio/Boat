package xor7studio.boat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xor7studio.boat.packet.PacketCodeCHandler;

import java.net.InetSocketAddress;

public class Client {
    private final Logger logger = LoggerFactory.getLogger("Boat Client");
    private final InetSocketAddress authServerAddress;
    @Getter
    private InetSocketAddress centerServerAddress;
    @Getter
    private String appID;
    private String appToken;
    public Client(InetSocketAddress authServerAddr){
        this.authServerAddress=authServerAddr;
    }
    private boolean createApp(){
        if(true){

            appID="";
            appToken="";
            return true;
        }
        return false;
    }
    private void connectApp(){
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel channel) {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast(PacketCodeCHandler.INSTANCE);
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(centerServerAddress).sync();
            channelFuture.addListener(future -> {
                if(future.isSuccess())
                    logger.info("成功连接到中心服务器");
            });
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
