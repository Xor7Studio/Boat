package xor7studio.boat.authentication;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import static io.netty.handler.codec.http.HttpUtil.is100ContinueExpected;

@ChannelHandler.Sharable
public class RequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    public static final RequestHandler INSTANCE = new RequestHandler();
    protected RequestHandler(){}
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest request) {
        if(is100ContinueExpected(request))
            channelHandlerContext.write(
                    new DefaultFullHttpResponse(
                            HttpVersion.HTTP_1_1,
                            HttpResponseStatus.CONTINUE));
//        PathHandlerManager.INSTANCE.parseRequest(request)
    }
}
