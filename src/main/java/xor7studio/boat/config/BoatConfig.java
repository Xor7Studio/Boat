package xor7studio.boat.config;

import java.net.InetSocketAddress;

public class BoatConfig {
    public String run_as="client";
    public ServerConfig server=new ServerConfig();
    public ClientConfig client=new ClientConfig();
    public CostumeConfig costume=null;
    public static InetSocketAddress toInetSocketAddress(String address){
        if(!address.contains(":")) throw new IllegalArgumentException("Listen必须含有端口");
        String[] tmp=address.split(":");
        return new InetSocketAddress(tmp[0],Integer.parseInt(tmp[1]));
    }
}