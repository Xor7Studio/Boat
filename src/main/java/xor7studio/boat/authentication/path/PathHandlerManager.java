package xor7studio.boat.authentication.path;

import io.jsonwebtoken.JwtException;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import org.jetbrains.annotations.NotNull;
import xor7studio.boat.authentication.AuthenticationUtils;
import xor7studio.boat.authentication.path.gettracebackaddress.GetTracebackAddressPathHandler;
import xor7studio.boat.authentication.path.refresh.RefreshPathHandler;
import xor7studio.boat.authentication.path.signin.SignInPathHandler;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class PathHandlerManager {
    public static final PathHandlerManager INSTANCE = new PathHandlerManager();
    protected PathHandlerManager(){
        pathHandlers.put(Path.SIGN_IN,new SignInPathHandler());
        pathHandlers.put(Path.REFRESH,new RefreshPathHandler());
        pathHandlers.put(Path.GET_TRACEBACK_ADDRESS,new GetTracebackAddressPathHandler());
    }
    private final Map<String, PathHandler> pathHandlers=new HashMap<>();
    public FullHttpResponse parseRequest(@NotNull FullHttpRequest request){
        PathHandlerResult parseResult;
        String path=request.uri();
        if(path.endsWith("/"))
            path=path.substring(0,path.length()-1);
        path=path.toLowerCase();
        System.out.println(path);
        PathHandler handler = pathHandlers.get(path);
        System.out.println(handler);
        if(handler == null) parseResult=PathHandlerResult.builder()
                .status(HttpResponseStatus.NOT_FOUND).body("").build();
        else {
            try{
                if(!path.equals(Path.SIGN_IN)){
                    String[] authorizationData=request
                            .headers()
                            .get("Authorization")
                            .split(" ");
                    if(authorizationData.length != 2 ||
                            !authorizationData[0].equalsIgnoreCase("Bearer"))
                        parseResult = PathHandlerResult.builder()
                                .status(HttpResponseStatus.UNAUTHORIZED)
                                .body("").build();
                    else {
                        AuthenticationUtils.BearerTokenData tokenData=AuthenticationUtils.INSTANCE
                                .readBearerToken(authorizationData[1]);
                        if(tokenData.isValid)
                            parseResult=handler.parse(PathRequestData.builder()
                                    .httpRequest(request)
                                    .tokenSubject(tokenData.subject).build());
                        else throw new JwtException("");
                    }
                }else parseResult=handler.parse(PathRequestData.builder()
                        .httpRequest(request)
                        .tokenSubject("").build());
            }catch (JwtException ignore){
                parseResult = PathHandlerResult.builder()
                        .body("")
                        .status(HttpResponseStatus.UNAUTHORIZED).build();
            }catch (Exception ignore){
                parseResult = PathHandlerResult.builder()
                        .body("")
                        .status(HttpResponseStatus.BAD_REQUEST).build();
            }
        }
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                parseResult.getStatus(),
                Unpooled.copiedBuffer(parseResult
                        .getBody()
                        .getBytes(StandardCharsets.UTF_8)));
        if(parseResult.getStatus().equals(HttpResponseStatus.OK))
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,
                    "application/json; charset=UTF-8");
        return response;
    }
}
