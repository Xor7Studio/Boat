package xor7studio.boat.authentication.path.sign_in;

import io.netty.handler.codec.http.HttpResponseStatus;
import org.jetbrains.annotations.NotNull;
import xor7studio.boat.GsonUtils;
import xor7studio.boat.authentication.AuthenticationUtils;
import xor7studio.boat.authentication.path.PathHandler;
import xor7studio.boat.authentication.path.PathHandlerResult;
import xor7studio.boat.authentication.path.PathRequestData;

import java.nio.charset.StandardCharsets;
import java.time.temporal.ChronoUnit;

public class SignInPathHandler extends PathHandler {
    @Override
    public PathHandlerResult parse(@NotNull PathRequestData request) {
        SignInRequestData requestData =
                GsonUtils.fromJson(
                        request.getHttpRequest()
                                .content()
                                .toString(StandardCharsets.UTF_8),
                        SignInRequestData.class);
        boolean isValid = true;
        String uuid="uuid";
        //DO AUTH
        SignInResponseData responseData = new SignInResponseData();
        responseData.token = AuthenticationUtils.INSTANCE.generateBearerToken(
                uuid,
                requestData.expire_in,
                ChronoUnit.DAYS);
        if(isValid) return PathHandlerResult.builder()
                .status(HttpResponseStatus.OK)
                .body(GsonUtils.getGsonInstance().toJson(
                        responseData,
                        SignInResponseData.class)).build();
        else return PathHandlerResult.builder()
                .body("")
                .status(HttpResponseStatus.UNAUTHORIZED).build();
    }
}
