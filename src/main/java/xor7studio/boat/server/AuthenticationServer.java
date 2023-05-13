package xor7studio.boat.server;

import lombok.Getter;
import xor7studio.boat.config.ServerConfig;

import java.net.InetSocketAddress;

public class AuthenticationServer {
//    private final Javalin javalin;
    private final ServerConfig config;
    @Getter
    private final InetSocketAddress listen;
    public AuthenticationServer(InetSocketAddress listen, ServerConfig config){
        this.listen=listen;
        this.config=config;
        //        javalin=Javalin.create(this::setJavalinConfig);
    }
    public void start(){
//        javalin.start(listen.getHostName(),listen.getPort());
    }
//    private void setJavalinConfig(@NotNull JavalinConfig config){
//        config.plugins.register(new SSLPlugin(ssl->{
////            ssl.pemFromPath((String) Config.INSTANCE.get("web_certificate"), (String) Config.INSTANCE.get("web_private_key"));
//            ssl.securePort=listen.getPort();
//            ssl.http2=true;
//            ssl.secure=true;
//            ssl.insecure=false;
//        }));
//        config.accessManager((handler, context, routeRoles) -> {
//            if(context.path().equals(Path.SIGN_IN)) return;
//            String authorizationHeader = context.header("Authorization");
//            if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
//                context.status(HttpStatus.UNAUTHORIZED);
//                return;
//            }
//            String token=authorizationHeader.substring(7);
//        });
//    }
    private interface Path{
        String SIGN_IN="/sign-in";
        String REFRESH="/refresh";
        String GET_TRACEBACK_ADDRESS="/get-traceback-address";
        String CREATE_SESSION="/create-session";
    }
}
