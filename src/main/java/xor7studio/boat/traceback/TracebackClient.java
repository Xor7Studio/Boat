package xor7studio.boat.traceback;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import xor7studio.boat.Signal;
import xor7studio.boat.config.BoatConfig;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class TracebackClient {
    private Signal signal;
    private ClientTracebackHandler clientTracebackHandler;
    private final String[] address;
    private int port;
    public TracebackClient(String[] address){
        this.address=address;
    }
    public boolean isHardNAT(){
        try {
            ServerSocket socket=new ServerSocket(0);
            port = socket.getLocalPort();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        connect(address[0]);
        String var = waitForResult();
        connect(address[1]);
        return !var.equals(waitForResult());
    }
    private void connect(String address){
        signal=new Signal();
        clientTracebackHandler=new ClientTracebackHandler(signal);
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel channel) {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,1));
                        pipeline.addLast(clientTracebackHandler);
                    }
                });
        bootstrap.connect(BoatConfig.toInetSocketAddress(address),new InetSocketAddress(port));
    }
    private String waitForResult(){
        signal.waitForSignal();
        return clientTracebackHandler.getResult();
    }
}
