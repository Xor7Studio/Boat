package xor7studio.boat.server;

import io.javalin.Javalin;
import io.javalin.community.ssl.SSLPlugin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.HttpStatus;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import xor7studio.boat.Config;

import java.net.InetSocketAddress;

public class AuthenticationServer {
    private final Javalin javalin;
    @Getter
    private final InetSocketAddress listen;
    public AuthenticationServer(InetSocketAddress listen){
        this.listen=listen;
        javalin=Javalin.create(this::setJavalinConfig);
    }
    public void start(){
        javalin.start(listen.getHostName(),listen.getPort());
    }
    private void setJavalinConfig(@NotNull JavalinConfig config){
        config.plugins.register(new SSLPlugin(ssl->{
            ssl.pemFromPath((String) Config.INSTANCE.get("web_certificate"), (String) Config.INSTANCE.get("web_private_key"));
            ssl.securePort=listen.getPort();
            ssl.http2=true;
            ssl.secure=true;
            ssl.insecure=false;
        }));
        config.accessManager((handler, context, routeRoles) -> {
            if(context.path().equals(Path.SIGN_IN)) return;
            String authorizationHeader = context.header("Authorization");
            if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                context.status(HttpStatus.UNAUTHORIZED);
                return;
            }
            String token=authorizationHeader.substring(7);
        });
    }
    private interface Path{
        String SIGN_IN="/sign-in";
        String REFRESH="/refresh";
        String GET_TRACEBACK_ADDRESS="/get-traceback-address";
        String CREATE_SESSION="/create-session";
    }
}
