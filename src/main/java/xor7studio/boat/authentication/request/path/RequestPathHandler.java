package xor7studio.boat.authentication.request.path;

import io.netty.handler.codec.http.FullHttpRequest;

public abstract class RequestPathHandler {
    public abstract void parse(FullHttpRequest request);
}
