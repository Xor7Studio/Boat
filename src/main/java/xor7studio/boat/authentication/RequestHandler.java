package xor7studio.boat.authentication;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.jetbrains.annotations.NotNull;
import xor7studio.boat.authentication.path.PathHandlerManager;

import static io.netty.handler.codec.http.HttpUtil.is100ContinueExpected;

@ChannelHandler.Sharable
public class RequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    public static final RequestHandler INSTANCE = new RequestHandler();
    protected RequestHandler(){}
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, @NotNull FullHttpRequest request) {
        if(!request.method().equals(HttpMethod.POST)) ctx.close();
        if(is100ContinueExpected(request))
            ctx.write(
                    new DefaultFullHttpResponse(
                            HttpVersion.HTTP_1_1,
                            HttpResponseStatus.CONTINUE));
        ctx.writeAndFlush(PathHandlerManager.INSTANCE.parseRequest(request))
                .addListener(ChannelFutureListener.CLOSE);
    }
}
