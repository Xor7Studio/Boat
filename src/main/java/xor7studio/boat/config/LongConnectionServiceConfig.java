package xor7studio.boat.config;

public class LongConnectionServiceConfig extends ServiceConfig {
    public LongConnectionServiceConfig() {
        listen = BoatConfig.DEFAULT_SERVER_ADDRESS_HOST+":11093";
    }
}
