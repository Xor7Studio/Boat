package xor7studio.boat.authentication.request;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.jetbrains.annotations.NotNull;
import xor7studio.boat.authentication.request.path.RequestPathManager;

import static io.netty.handler.codec.http.HttpUtil.is100ContinueExpected;

public class RequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    public static final RequestHandler INSTANCE = new RequestHandler();
    protected RequestHandler(){}
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, @NotNull FullHttpRequest fullHttpRequest) {
        if(is100ContinueExpected(fullHttpRequest))
            channelHandlerContext.write(
                    new DefaultFullHttpResponse(
                            HttpVersion.HTTP_1_1,
                            HttpResponseStatus.CONTINUE));
        String path=fullHttpRequest.uri();
        if(path.contains("?")) return;
        if(path.endsWith("/")) path=path.substring(0,path.length()-1);
        path=path.toLowerCase();
        FullHttpResponse response=
                new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1,
                        HttpResponseStatus.OK,
                        Unpooled.copiedBuffer(
                                RequestPathManager.INSTANCE
                                        .getRequestPathHandler(path)
                                        .parse(null).getBytes()));
        RequestPathManager.INSTANCE
                .getRequestPathHandler(path)
                .parse(null);

        channelHandlerContext.writeAndFlush("");
    }
}
