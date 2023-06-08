package xor7studio.boat.traceback;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.haproxy.HAProxyMessageDecoder;
import io.netty.util.AttributeKey;
import xor7studio.boat.config.BoatConfig;
import xor7studio.boat.config.TracebackServiceConfig;

public class TracebackService {
    public static final AttributeKey<String> SOURCE=AttributeKey.newInstance("SOURCE");
    TracebackServiceConfig serviceConfig;
    public TracebackService(TracebackServiceConfig serviceConfig){
        this.serviceConfig=serviceConfig;
    }
    public void start(){
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.TCP_NODELAY,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            if(serviceConfig.enable_proxy_protocol){
                                pipeline.addLast(new HAProxyMessageDecoder());
                                pipeline.addLast(ServerHAProxyMessageHandler.INSTANCE);
                            }
                            pipeline.addLast(ServerTracebackHandler.INSTANCE);
                        }
                    });
            bootstrap.bind(BoatConfig.toInetSocketAddress(serviceConfig.listen))
                    .sync()
                    .channel()
                    .closeFuture()
                    .sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
    public void sendMsg(ChannelHandlerContext ctx) {

        ByteBuf res=ctx.alloc().ioBuffer();
        byte[] data= ctx.channel().attr(SOURCE).get().getBytes();
        System.out.println(ctx.channel().attr(SOURCE).get());
        res.writeByte(data.length);
        res.writeBytes(data);
        ctx.writeAndFlush(res).addListener(ChannelFutureListener.CLOSE);
    }
}
