package xor7studio.boat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.Map;

public class GsonUtils {
    @Contract(" -> new")
    public static @NotNull Gson getGsonInstance(){
        return new GsonBuilder()
                .setObjectToNumberStrategy(ToNumberPolicy.LAZILY_PARSED_NUMBER)
                .create();
    }
    public static final Type DEFAULT_MAP_TYPE = new TypeToken<Map<String,Object>>(){}.getType();
}
