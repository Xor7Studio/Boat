package xor7studio.boat.long_connection.session;

import io.netty.util.AttributeKey;

public interface SessionAttributes {
    AttributeKey<String> SESSION_TOKEN = AttributeKey.newInstance("SESSION_TOKEN");
    AttributeKey<String> SESSION_PWD = AttributeKey.newInstance("SESSION_PWD");
    AttributeKey<String> UUID = AttributeKey.newInstance("UUID");
    AttributeKey<String> NAT_TYPE = AttributeKey.newInstance("NAT_TYPE");
}
