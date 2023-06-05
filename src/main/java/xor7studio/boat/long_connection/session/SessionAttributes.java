package xor7studio.boat.long_connection.session;

import io.netty.util.AttributeKey;

public interface SessionAttributes {
    AttributeKey<String> SESSION_TOKEN = AttributeKey.newInstance("SESSION_TOKEN");
}
