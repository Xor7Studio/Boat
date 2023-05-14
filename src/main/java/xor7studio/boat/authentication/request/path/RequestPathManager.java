package xor7studio.boat.authentication.request.path;

import java.util.HashMap;
import java.util.Map;

public class RequestPathManager {
    public static final RequestPathManager INSTANCE = new RequestPathManager();
    protected RequestPathManager(){
        paths.put(RequestPath.SIGN_IN, SignInHandler.class);
    }
    private final Map<String,Class<? extends RequestPathHandler>> paths=new HashMap<>();
    public Class<? extends RequestPathHandler> getRequest(String path){
        return paths.get(path);
    }
}
