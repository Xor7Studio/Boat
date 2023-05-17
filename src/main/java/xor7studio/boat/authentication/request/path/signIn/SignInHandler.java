package xor7studio.boat.authentication.request.path.signIn;

import io.netty.handler.codec.http.HttpResponseStatus;
import xor7studio.boat.GsonUtils;
import xor7studio.boat.authentication.AuthenticationManager;
import xor7studio.boat.authentication.request.path.ParseResult;
import xor7studio.boat.authentication.request.path.RequestPathHandler;

import java.time.temporal.ChronoUnit;

public class SignInHandler extends RequestPathHandler {
    @Override
    public ParseResult parse(String body) {
        SignInRequest request=GsonUtils.getGsonInstance().fromJson(body, SignInRequest.class);

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
