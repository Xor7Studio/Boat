package xor7studio.boat.authentication.request;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

public class RequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    public static final RequestHandler INSTANCE = new RequestHandler();
    protected RequestHandler (){}
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) {

    }
}
