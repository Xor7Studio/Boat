package xor7studio.boat.authentication.request.path;

import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParseResult {
    private String result;
    private HttpResponseStatus status;
}
