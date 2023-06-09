package xor7studio.boat.config;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;

public class BoatConfig {
    public static final String  DEFAULT_SERVER_ADDRESS_HOST="localhost.xor7.cn";
    public String run_as="client";
    public ServerConfig server=new ServerConfig();
    public ClientConfig client=new ClientConfig();
    public CostumeConfig costume=null;
    @Contract("_ -> new")
    public static @NotNull InetSocketAddress toInetSocketAddress(@NotNull String address){
        if(!address.contains(":")) throw new IllegalArgumentException("Listen必须含有端口");
        if(address.contains("//")) address=address.split("//")[1];
        String[] tmp=address.split(":");
        return new InetSocketAddress(tmp[0],Integer.parseInt(tmp[1]));
    }
}