package xor7studio.boat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.ToNumberPolicy;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class GsonUtils {
    @Contract(" -> new")
    public static @NotNull Gson getGsonInstance(){
        return new GsonBuilder()
                .setObjectToNumberStrategy(ToNumberPolicy.LAZILY_PARSED_NUMBER)
                .create();
    }
    public static <T> T fromJson(@NotNull Gson gson, String json, Type type){
        T res = gson.fromJson(json,type);
        for(Field field:res.getClass().getDeclaredFields()){
            field.setAccessible(true);
            try {
                if (field.get(res) == null)
                    throw new JsonSyntaxException("");
            } catch (IllegalAccessException e) {
                throw new JsonSyntaxException(e);
            }
        }
        return res;
    }
}