package xor7studio.boat.authentication.path.refresh;

import xor7studio.boat.GsonUtils;
import xor7studio.boat.authentication.AuthenticationUtils;
import xor7studio.boat.authentication.path.PathHandler;
import xor7studio.boat.authentication.path.PathHandlerResult;
import xor7studio.boat.authentication.path.PathRequestData;
import xor7studio.boat.authentication.path.signin.SignInRequestData;
import xor7studio.boat.authentication.path.signin.SignInResponseData;

import java.nio.charset.StandardCharsets;
import java.time.temporal.ChronoUnit;

public class RefreshPathHandler extends PathHandler {
    @Override
    public PathHandlerResult parse(PathRequestData request){
        RefreshRequestData requestData =
                GsonUtils.getGsonInstance().fromJson(
                        request.getHttpRequest()
                                .content()
                                .toString(StandardCharsets.UTF_8),
                        RefreshRequestData.class);
        RefreshResponseData responseData = new RefreshResponseData();
        responseData.token = AuthenticationUtils.INSTANCE.generateBearerToken(
                request.getTokenSubject(),
                requestData.expire_in,
                ChronoUnit.DAYS);
        return PathHandlerResult.builder()
                .body(AuthenticationUtils.INSTANCE.generateBearerToken()).build();
    }
}
