package xor7studio.boat.authentication.path.create_session;

import io.netty.handler.codec.http.HttpResponseStatus;
import org.jetbrains.annotations.NotNull;
import xor7studio.boat.GsonUtils;
import xor7studio.boat.authentication.AuthenticationUtils;
import xor7studio.boat.authentication.path.PathHandler;
import xor7studio.boat.authentication.path.PathHandlerResult;
import xor7studio.boat.authentication.path.PathRequestData;
import xor7studio.boat.config.BoatConfigFile;

import java.nio.charset.StandardCharsets;
import java.time.temporal.ChronoUnit;

public class CreateSessionPathHandler extends PathHandler {
    @Override
    public PathHandlerResult parse(@NotNull PathRequestData request) {
        if(!BoatConfigFile.INSTANCE.config.server.longConnection.run)
            return PathHandlerResult.builder()
                    .status(HttpResponseStatus.INTERNAL_SERVER_ERROR).body("").build();
        CreateSessionRequestData requestData =
                GsonUtils.fromJson(
                        GsonUtils.getGsonInstance(),
                        request.getHttpRequest()
                                .content()
                                .toString(StandardCharsets.UTF_8),
                        CreateSessionRequestData.class);
        if(!requestData.nat_type.equals("H") && !requestData.nat_type.equals("N"))
            return PathHandlerResult.builder()
                    .status(HttpResponseStatus.BAD_REQUEST).body("").build();
        CreateSessionResponseData responseData = new CreateSessionResponseData();
        responseData.session_token = AuthenticationUtils.INSTANCE.generateBearerToken(
                String.format("%s:%s:%s",
                        request.getTokenSubject(),
                        requestData.nat_type,
                        requestData.session_pwd),
                BoatConfigFile.INSTANCE.config.server.session_key_expire_in,
                ChronoUnit.SECONDS);
        responseData.expire_in=BoatConfigFile.INSTANCE.config.server.session_key_expire_in;
        responseData.address=BoatConfigFile.INSTANCE.config.server.longConnection.listen;
        return PathHandlerResult.builder()
                .status(HttpResponseStatus.OK)
                .body(GsonUtils.getGsonInstance().toJson(
                        responseData,
                        CreateSessionResponseData.class)).build();
    }
}
