package xor7studio.boat.traceback;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.haproxy.HAProxyMessage;
import org.jetbrains.annotations.NotNull;

@ChannelHandler.Sharable
public class ServerHAProxyMessageHandler extends SimpleChannelInboundHandler<HAProxyMessage> {
    public static final ServerHAProxyMessageHandler INSTANCE = new ServerHAProxyMessageHandler();
    protected ServerHAProxyMessageHandler(){}
    @Override
    protected void channelRead0(@NotNull ChannelHandlerContext ctx, @NotNull HAProxyMessage haProxyMessage) {
        ctx.channel().attr(TracebackService.SOURCE).set(String.format(
                "%s:%d",
                haProxyMessage.sourceAddress(),
                haProxyMessage.sourcePort()));
    }
}
