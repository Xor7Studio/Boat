package xor7studio.boat.authentication.path.refresh;

import io.netty.handler.codec.http.HttpResponseStatus;
import org.jetbrains.annotations.NotNull;
import xor7studio.boat.GsonUtils;
import xor7studio.boat.authentication.AuthenticationUtils;
import xor7studio.boat.authentication.path.PathHandler;
import xor7studio.boat.authentication.path.PathHandlerResult;
import xor7studio.boat.authentication.path.PathRequestData;

import java.nio.charset.StandardCharsets;
import java.time.temporal.ChronoUnit;

public class RefreshPathHandler extends PathHandler {
    @Override
    public PathHandlerResult parse(@NotNull PathRequestData request){
        RefreshRequestData requestData =
                GsonUtils.fromJson(
                        GsonUtils.getGsonInstance(),
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
                .body(GsonUtils.getGsonInstance().toJson(
                        responseData,
                        RefreshResponseData.class))
                .status(HttpResponseStatus.OK).build();
    }
}
