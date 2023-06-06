package xor7studio.boat.authentication.path.get_traceback_address;

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
    private static boolean tracebackServiceValid=false;
    public GetTracebackAddressPathHandler() {
        for(TracebackServiceConfig serviceConfig: BoatConfigFile.DEFAULT.config.server.tracebacks)
            if(serviceConfig.run)
                validTracebackServices.add(serviceConfig);
        if(validTracebackServices.size()>=2) tracebackServiceValid=true;
    }
    @Override
    public PathHandlerResult parse(PathRequestData request) {
        if(!tracebackServiceValid)
            return PathHandlerResult.builder()
                    .status(HttpResponseStatus.INTERNAL_SERVER_ERROR).body("").build();
        GetTracebackAddressResponseData responseData = new GetTracebackAddressResponseData();
        Random random=new Random();
        int index=getRandomInt(random,-1);
        responseData.address_1=validTracebackServices.get(index).listen;
        responseData.address_2=validTracebackServices.get(getRandomInt(random,index)).listen;
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
