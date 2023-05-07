package xor7studio.boat.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xor7studio.boat.packet.codec.PacketCodecHandler;
import xor7studio.boat.packet.command.PacketCommandHandler;
import xor7studio.boat.session.SessionAttributes;

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
        if(!createApp()){
            logger.error("创建App时发生错误");
            return;
        }
        connectApp();
    }
    private boolean createApp(){
        if(true){
            centerServerAddress=new InetSocketAddress("localhost",11099);
            appID="";
            appToken="";
            return true;
        }
        return false;
    }
    private void connectApp(){
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel channel) {
                        ChannelPipeline pipeline = channel.pipeline();
                        channel.attr(SessionAttributes.SESSION_TOKEN).set("My Session Token");
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,1,4));
                        pipeline.addLast(PacketCodecHandler.INSTANCE);
                        pipeline.addLast(PacketCommandHandler.INSTANCE);
                        pipeline.addLast(new ClientHandler());
                    }
                });
        bootstrap.connect(centerServerAddress);
    }
}
