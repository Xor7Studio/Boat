package xor7studio.boat.config;

public class TracebackServiceConfig extends ServiceConfig {
    public TracebackServiceConfig() {}
    public TracebackServiceConfig(int port){
        listen = "localhost:"+port;
    }
}
