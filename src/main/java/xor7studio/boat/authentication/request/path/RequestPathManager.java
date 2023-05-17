package xor7studio.boat.authentication.request.path;

import xor7studio.boat.authentication.request.path.signIn.SignInHandler;

import java.util.HashMap;
import java.util.Map;

public class RequestPathManager {
    public static final RequestPathManager INSTANCE = new RequestPathManager();
    protected RequestPathManager(){
        pathHandlers.put(RequestPath.SIGN_IN, new SignInHandler());
    }
    private final Map<String,RequestPathHandler> pathHandlers=new HashMap<>();
    public RequestPathHandler getRequestPathHandler(String path){
        return pathHandlers.getOrDefault(path,NotFoundHandler.INSTANCE);
    }
}
