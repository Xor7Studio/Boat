package xor7studio.boat.config;

public class TracebackServiceConfig extends ServiceConfig {
    public TracebackServiceConfig() {}
    public Boolean valid=false;
    public Boolean enable_proxy_protocol=false;
    public TracebackServiceConfig(int port){
        listen = BoatConfig.DEFAULT_SERVER_ADDRESS_HOST+":"+port;
    }
}
