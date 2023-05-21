package xor7studio.boat.authentication.path.signin;

import com.google.gson.JsonSyntaxException;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.jetbrains.annotations.NotNull;
import xor7studio.boat.GsonUtils;
import xor7studio.boat.authentication.path.PathHandler;
import xor7studio.boat.authentication.path.PathHandlerResult;

public class SignInPathHandler extends PathHandler {
    @Override
    public PathHandlerResult parse(@NotNull FullHttpRequest request) {
        try{
            SignInRequestData requestData =
                    GsonUtils.getGsonInstance().fromJson(
                            request.content().toString(),
                            SignInRequestData.class);
            if(true){//IGNORE DO AUTH
                SignInResponseData responseData = new SignInResponseData();
                return PathHandlerResult.builder()
                        .status(HttpResponseStatus.OK)
                        .body(GsonUtils.getGsonInstance().toJson(
                                        responseData,
                                        SignInResponseData.class)).build();
            }else{
                return PathHandlerResult.builder()
                        .status(HttpResponseStatus.UNAUTHORIZED).build();
            }
        }catch (JsonSyntaxException e){
            return PathHandlerResult.builder()
                    .status(HttpResponseStatus.BAD_REQUEST).build();
        }
    }
}
