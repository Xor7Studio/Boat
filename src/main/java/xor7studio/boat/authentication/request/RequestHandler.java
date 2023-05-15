package xor7studio.boat.authentication.request;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.jetbrains.annotations.NotNull;
import xor7studio.boat.authentication.request.path.RequestPath;
import xor7studio.boat.authentication.request.path.RequestPathManager;

import java.nio.charset.StandardCharsets;

import static io.netty.handler.codec.http.HttpUtil.is100ContinueExpected;

@ChannelHandler.Sharable
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
        if(!path.equals(RequestPath.SIGN_IN)){
            //AUTH
        }
        System.out.println(path);
        channelHandlerContext.writeAndFlush(new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.copiedBuffer(
                        RequestPathManager.INSTANCE
                                .getRequestPathHandler(path)
                                .parse(fullHttpRequest
                                        .content()
                                        .toString(CharsetUtil.UTF_8))
                                .getBytes(StandardCharsets.UTF_8))))
                .addListener(ChannelFutureListener.CLOSE);

    }
}
