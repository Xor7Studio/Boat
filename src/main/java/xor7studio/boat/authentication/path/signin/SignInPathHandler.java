package xor7studio.boat.authentication.path.signin;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import xor7studio.boat.authentication.path.PathHandler;
import xor7studio.boat.authentication.path.PathHandlerResult;

public class SignInPathHandler extends PathHandler {
    @Override
    public PathHandlerResult parse(FullHttpRequest request) {
        return PathHandlerResult.builder().status(HttpResponseStatus.OK).build();
    }
}
