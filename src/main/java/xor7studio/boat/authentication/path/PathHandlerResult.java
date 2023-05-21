package xor7studio.boat.authentication.path;

import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PathHandlerResult {
    private String body="";
    private HttpResponseStatus status;
}
