package xor7studio.boat.authentication.request.path.signIn;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class SignInRequest {
    private String server;
    private Integer expire_in;
    private Map<String,Object> other_entries;
}
