package xor7studio.boat;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Config {
    public static final Config INSTANCE = new Config();
    private final File CONFIG_FILE = new File("config.toml");
    private final HashMap<String,Object> config;
    private final TomlWriter tomlWriter = new TomlWriter();
    protected Config(){
        HashMap<String, Object> defaultValues = new HashMap<>();
        defaultValues.put("mode","client");
        if(!CONFIG_FILE.exists()) {
            config = defaultValues;
        }else{
            config = (HashMap<String, Object>) new Toml().read(CONFIG_FILE).toMap();
            for(HashMap.Entry<String,Object> entry: defaultValues.entrySet())
                if(!config.containsKey(entry.getKey()))
                    config.put(entry.getKey(), entry.getValue());
        }
        sync();
    }
    public void sync(){
        try {
            tomlWriter.write(config, CONFIG_FILE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Object get(String key){
        return config.get(key);
    }

}
