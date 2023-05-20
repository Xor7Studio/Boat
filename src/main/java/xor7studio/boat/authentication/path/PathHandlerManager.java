package xor7studio.boat.authentication.path;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class PathHandlerManager {
    public static final PathHandlerManager INSTANCE = new PathHandlerManager();
    protected PathHandlerManager(){}
    private final Map<String, PathHandler> pathHandlers=new HashMap<>();
    public FullHttpResponse parseRequest(@NotNull FullHttpRequest request){
        PathHandlerResult parseResult;
        String path=request.uri();
        if(path.endsWith("/")) path=path.substring(0,path.length()-1);
        path=path.toLowerCase();
        System.out.println(path);
        PathHandler handler = pathHandlers.get(path);
        if(handler==null) parseResult=PathHandlerResult.builder().status(HttpResponseStatus.NOT_FOUND).build();
        else if(!path.equals(Path.SIGN_IN)){
            //DO AUTH
            parseResult = PathHandlerResult.builder().build();
        }else parseResult=handler.parse(request);
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                parseResult.getStatus(),
                Unpooled.copiedBuffer(parseResult.getBody().getBytes(StandardCharsets.UTF_8)));
        if(parseResult.getStatus().equals(HttpResponseStatus.OK))
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,
                    "application/json; charset=UTF-8");
        return response;
    }
}
