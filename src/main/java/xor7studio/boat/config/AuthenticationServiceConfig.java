package xor7studio.boat.config;

public class AuthenticationServiceConfig extends ServiceConfig {
    public AuthenticationServiceConfig() {
        listen = BoatConfig.DEFAULT_SERVER_ADDRESS_HOST+":1109";
    }

    public Boolean enable_ssl = false;
    public String key_file = "KEY";
    public String cert_file = "CERT";
}
