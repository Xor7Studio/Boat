package xor7studio.boat.config;

import java.util.HashSet;
import java.util.Set;

public class ServerConfig {
    public String authentication_keypair_file="AUTHENTICATION_KEYPAIR_FILE";
    public AuthenticationServiceConfig authentication=new AuthenticationServiceConfig();
    public LongConnectionServiceConfig longConnection=new LongConnectionServiceConfig();
    public Set<TracebackServiceConfig> tracebacks= new HashSet<>();
    private static class ServiceConfig {
        public boolean run=true;
        public String listen;
    }
    public static class AuthenticationServiceConfig extends ServiceConfig {
        public AuthenticationServiceConfig(){
            listen="localhost:1109";
        }
        public boolean enable_ssl=false;
        public String key_file="KEY";
        public String cert_file="CERT";
    }
    public static class LongConnectionServiceConfig extends ServiceConfig {
        public LongConnectionServiceConfig(){
            listen="localhost:11093";
        }
    }
    public static class TracebackServiceConfig extends ServiceConfig {
        public TracebackServiceConfig(){
            listen="localhost:11097";
        }
    }
}