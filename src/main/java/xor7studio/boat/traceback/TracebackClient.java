package xor7studio.boat.traceback;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import xor7studio.boat.config.BoatConfig;

import java.net.InetSocketAddress;

public class TracebackClient {
    private InetSocketAddress address;
    public TracebackClient(String address){
        this.address= BoatConfig.toInetSocketAddress(address);
    }
    public void connect(){
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel channel) {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,1));
                        pipeline.addLast(new ClientTracebackHandler());
                    }
                });
        bootstrap.connect(address);
    }
}
