package xor7studio.boat.authentication.request.path;

import io.netty.handler.codec.http.HttpResponseStatus;
import xor7studio.boat.GsonUtils;
import xor7studio.boat.authentication.AuthenticationManager;

import java.time.temporal.ChronoUnit;
import java.util.Map;

public class SignInHandler extends RequestPathHandler {
    @Override
    public ParseResult parse(String body) {
        Map<String,Object> data= GsonUtils
                .getGsonInstance()
                .fromJson(body, GsonUtils.DEFAULT_MAP_TYPE);
        HttpResponseStatus status = HttpResponseStatus.OK;
        AuthenticationManager.INSTANCE
                .generateBearerToken(
                        "",
                        100,
                        ChronoUnit.DAYS);
        return ParseResult.builder()
                .result("")
                .status(status)
                .build();
    }
}
