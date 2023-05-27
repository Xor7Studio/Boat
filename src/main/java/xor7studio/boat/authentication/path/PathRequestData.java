package xor7studio.boat.authentication.path;

import io.netty.handler.codec.http.FullHttpRequest;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PathRequestData {
    private FullHttpRequest httpRequest;
    private String tokenSubject;
}
