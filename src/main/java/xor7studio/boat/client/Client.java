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
import xor7studio.boat.long_connection.packet.codec.PacketCodecHandler;
import xor7studio.boat.long_connection.packet.command.PacketCommandHandler;
import xor7studio.boat.long_connection.packet.command.PacketCommandManager;
import xor7studio.boat.long_connection.session.SessionAttributes;

import java.net.InetSocketAddress;

public class Client {
    private final InetSocketAddress authServerAddress;
    @Getter
    private InetSocketAddress centerServerAddress;
    public Client(InetSocketAddress authServerAddr){
        this.authServerAddress=authServerAddr;

        startLongConnection();
    }
    private void startAuthentication(){
    }
    private void startLongConnection(){
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel channel) {
                        PacketCommandManager.INSTANCE.set_client(true);
                        ChannelPipeline pipeline = channel.pipeline();
                        channel.attr(SessionAttributes.SESSION_TOKEN).set("My Session Token");
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,1,4));
                        pipeline.addLast(PacketCodecHandler.INSTANCE);
                        pipeline.addLast(PacketCommandHandler.INSTANCE);
                        pipeline.addLast(new ClientLongConnectionHandler());
                    }
                });
        bootstrap.connect(centerServerAddress);
    }
}
