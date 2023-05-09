package xor7studio.boat.config;

public class ServerData {
    public String authentication_keypair_cache_file;
    public AuthenticationSubServer authentication;
    public LongConnectionSubServer longConnection;
    public TracebackSubServer traceback;
    private static class BaseSubServer{
        public boolean run;
        public String listen;
    }
    public static class AuthenticationSubServer extends BaseSubServer{
        public String key_file;
        public String cert_file;
    }
    public static class LongConnectionSubServer extends BaseSubServer{

    }
    public static class TracebackSubServer extends BaseSubServer{

    }
}