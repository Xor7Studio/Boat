package xor7studio.boat.authentication.path.gettracebackaddress;

import io.netty.handler.codec.http.HttpResponseStatus;
import org.jetbrains.annotations.NotNull;
import xor7studio.boat.GsonUtils;
import xor7studio.boat.authentication.path.PathHandler;
import xor7studio.boat.authentication.path.PathHandlerResult;
import xor7studio.boat.authentication.path.PathRequestData;
import xor7studio.boat.config.BoatConfigFile;
import xor7studio.boat.config.TracebackServiceConfig;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class GetTracebackAddressPathHandler extends PathHandler {
    public static final CopyOnWriteArrayList<TracebackServiceConfig> validTracebackServices = new CopyOnWriteArrayList<>();
    public GetTracebackAddressPathHandler() {
        for(TracebackServiceConfig serviceConfig: BoatConfigFile.INSTANCE.config.server.tracebacks)
            if(serviceConfig.run)
                validTracebackServices.add(serviceConfig);
        if(validTracebackServices.size()<2) throw new Error("可用的Traceback Service不足。");
    }
    @Override
    public PathHandlerResult parse(PathRequestData request) {
        GetTracebackAddressResponseData responseData = new GetTracebackAddressResponseData();
        Random random=new Random();
        int index=getRandomInt(random,-1);
        responseData.address1=validTracebackServices.get(index).listen;
        responseData.address2=validTracebackServices.get(getRandomInt(random,index)).listen;
        return PathHandlerResult.builder()
                .body(GsonUtils.getGsonInstance().toJson(
                        responseData,
                        GetTracebackAddressResponseData.class))
                .status(HttpResponseStatus.OK).build();
    }
    private int getRandomInt(@NotNull Random random, int last){
        while(true){
            int var=random.nextInt(validTracebackServices.size());
            if(var!=last)
                return var;
        }
    }
}
