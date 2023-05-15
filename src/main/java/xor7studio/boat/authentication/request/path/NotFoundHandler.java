package xor7studio.boat.authentication.request.path;

import io.netty.handler.codec.http.HttpResponseStatus;

public class NotFoundHandler extends RequestPathHandler{
    public static final NotFoundHandler INSTANCE = new NotFoundHandler();
    protected NotFoundHandler(){}
    @Override
    public ParseResult parse(String body) {
        return ParseResult.builder()
                .status(HttpResponseStatus.NOT_FOUND)
                .build();
    }
}
