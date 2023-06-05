package xor7studio.boat.config;

import java.util.concurrent.CopyOnWriteArrayList;

public class ServerConfig {
    public String authentication_keypair_file="AUTHENTICATION_KEYPAIR_FILE";
    public Integer session_key_expire_in=30;
    public AuthenticationServiceConfig authentication=new AuthenticationServiceConfig();
    public LongConnectionServiceConfig longConnection=new LongConnectionServiceConfig();
    public CopyOnWriteArrayList<TracebackServiceConfig> tracebacks= new CopyOnWriteArrayList<>();
}