package xor7studio.boat.authentication.path;

import io.netty.handler.codec.http.FullHttpRequest;

public abstract class PathHandler {
    public PathHandlerResult parse(FullHttpRequest request){return PathHandlerResult.builder().build();}
}
