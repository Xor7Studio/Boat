package xor7studio.boat.config;

public class TracebackServiceConfig extends ServiceConfig {
    public TracebackServiceConfig() {}
    public TracebackServiceConfig(int port){
        listen = BoatConfig.DEFAULT_SERVER_ADDRESS_HOST+":"+port;
    }
}
