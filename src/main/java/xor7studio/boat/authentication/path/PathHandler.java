package xor7studio.boat.authentication.path;

import io.netty.handler.codec.http.FullHttpRequest;

public abstract class PathHandler {
    public abstract PathHandlerResult parse(FullHttpRequest request);
}
