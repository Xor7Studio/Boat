package xor7studio.boat;

import cn.zhxu.okhttps.*;
import cn.zhxu.okhttps.gson.GsonMsgConvertor;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.io.Decoders;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.jetbrains.annotations.NotNull;
import xor7studio.boat.authentication.AuthenticationTokenData;
import xor7studio.boat.authentication.path.Path;
import xor7studio.boat.authentication.path.create_session.CreateSessionRequestData;
import xor7studio.boat.authentication.path.create_session.CreateSessionResponseData;
import xor7studio.boat.authentication.path.get_traceback_address.GetTracebackAddressResponseData;
import xor7studio.boat.authentication.path.refresh.RefreshRequestData;
import xor7studio.boat.authentication.path.refresh.RefreshResponseData;
import xor7studio.boat.authentication.path.sign_in.SignInRequestData;
import xor7studio.boat.authentication.path.sign_in.SignInResponseData;
import xor7studio.boat.config.BoatConfig;
import xor7studio.boat.config.BoatConfigFile;
import xor7studio.boat.long_connection.ClientLongConnectionHandler;
import xor7studio.boat.long_connection.packet.codec.PacketCodecHandler;
import xor7studio.boat.long_connection.packet.command.PacketCommandHandler;
import xor7studio.boat.long_connection.packet.command.PacketCommandManager;
import xor7studio.boat.long_connection.session.SessionAttributes;
import xor7studio.boat.traceback.TracebackClient;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.net.InetSocketAddress;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class BoatClient {
    private InetSocketAddress longConnectionAddress;
    private String[] tracebackAddress;
    private final HTTP http;
    private String nat_type="E";
    private String session_token;
    public BoatClient(){
        http = HTTP.builder()
                .addMsgConvertor(new GsonMsgConvertor())
                .baseUrl("https://"+BoatConfigFile.DEFAULT.config.client.boat_server)
                .bodyType(OkHttps.JSON)
                .addPreprocessor((Preprocessor.PreChain chain) -> {
                    HttpTask<?> task = chain.getTask();
                    if (task.isTagged(Path.SIGN_IN)) {
                        chain.proceed();
                        return;
                    }
                    task.addHeader(
                            "Authorization",
                            "Bearer "+BoatConfigFile.DEFAULT.config.client.boat_token);
                    chain.proceed();})
                .responseListener((HttpTask<?> task, HttpResult result) -> {
                    if (result.isSuccessful()) return true;
                    System.out.println(result.getState().name());
                    //TODO 错误处理
                    return false;
                })
                .build();
    }
    public void start(){
        doAuthentication();
//        startLongConnection();
    }
    private void doAuthentication(){
//        signIn("data");
//        refresh();
        getTracebackAddress();
//        for(int i = 0 ; i < 100 ; i++)
//            new Thread(()->
                    new TracebackClient(tracebackAddress[0]).connect();
//            );//+"1"
//        createSession();
    }
    private void signIn(String data){
        try {
            if(BoatConfigFile.DEFAULT.config.client.boat_token == null) throw new JwtException("");
            BoatConfigFile.DEFAULT.config.client.uuid=readToken(BoatConfigFile.DEFAULT.config.client.boat_token);
            BoatConfigFile.DEFAULT.save();
        }catch (Exception ignored){
            SignInRequestData requestData=new SignInRequestData();
            requestData.data=data;
            //TODO 自定义以下两行内容
            requestData.server="server";
            requestData.expire_in=1;
            SignInResponseData responseData=http
                    .sync(Path.SIGN_IN)
                    .tag(Path.SIGN_IN)
                    .setBodyPara(requestData)
                    .post()
                    .getBody()
                    .toBean(SignInResponseData.class);
            BoatConfigFile.DEFAULT.config.client.boat_token=responseData.token;
            BoatConfigFile.DEFAULT.save();
            signIn(data);
        }
    }
    private void refresh(){
        RefreshRequestData requestData=new RefreshRequestData();
        //TODO 自定义下一行内容
        requestData.expire_in=1;
        RefreshResponseData responseData=http
                .sync(Path.REFRESH)
                .setBodyPara(requestData)
                .post()
                .getBody()
                .toBean(RefreshResponseData.class);
        BoatConfigFile.DEFAULT.config.client.boat_token=responseData.token;
        BoatConfigFile.DEFAULT.config.client.uuid=readToken(responseData.token);
        BoatConfigFile.DEFAULT.save();
    }
    private void getTracebackAddress(){
        GetTracebackAddressResponseData responseData=http
                .sync(Path.GET_TRACEBACK_ADDRESS)
                .post()
                .getBody()
                .toBean(GetTracebackAddressResponseData.class);
        tracebackAddress=new String[]{
                responseData.address_1,
                responseData.address_2};
    }
    private void createSession(){
        CreateSessionRequestData requestData=new CreateSessionRequestData();
        requestData.nat_type=nat_type;
        requestData.session_pwd=generateKey();
        CreateSessionResponseData responseData=http
                .sync(Path.CREATE_SESSION)
                .setBodyPara(requestData)
                .post()
                .getBody()
                .toBean(CreateSessionResponseData.class);
        System.out.println(responseData.address);
        longConnectionAddress= BoatConfig.toInetSocketAddress(responseData.address);
        this.session_token=responseData.session_token;
        //TODO 解析expire_in 虽然不知道这玩意有啥用
    }
    private String generateKey(){
        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        keyGenerator.init(128);
        SecretKey secretKey = keyGenerator.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }
    private String readToken(@NotNull String token){
        AuthenticationTokenData tokenData = GsonUtils.fromJson(
                        new String(
                                Decoders.BASE64.decode(token.split("\\.")[1])),
                        AuthenticationTokenData.class);
        if(!(tokenData.aud.equals("Authentication") &&
                tokenData.iss.equals("Boat") &&
                tokenData.exp>=System.currentTimeMillis()/1000L))
            throw new JwtException("");
        return tokenData.sub;
    }
    private void startLongConnection(){
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel channel) {
                        PacketCommandManager.INSTANCE.set_client(true);
                        ChannelPipeline pipeline = channel.pipeline();
                        channel.attr(SessionAttributes.SESSION_TOKEN).set(BoatConfigFile.DEFAULT.config.client.boat_token);
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,1,4));
                        pipeline.addLast(PacketCodecHandler.INSTANCE);
                        pipeline.addLast(PacketCommandHandler.INSTANCE);
                        pipeline.addLast(new ClientLongConnectionHandler());
                    }
                });
        bootstrap.connect(longConnectionAddress);
    }
}
