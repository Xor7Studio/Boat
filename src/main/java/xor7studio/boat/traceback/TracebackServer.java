package xor7studio.boat.traceback;

import java.net.InetSocketAddress;

public class TracebackServer {
    private final InetSocketAddress listen;
    public TracebackServer(InetSocketAddress listen){
        this.listen=listen;
    }
}
