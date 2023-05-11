package xor7studio.boat.config;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class BoatConfigFile {
    private final File file;
    private final TomlWriter tomlWriter = new TomlWriter();
    public BoatConfig config;
    private BoatConfigFile(File file){
        this.file = file;
        config=new Toml().to(BoatConfig.class);
        if(config.server.tracebacks.size()<2){
            config.server.tracebacks.add(new ServerConfig.TracebackServiceConfig());
            setTracebacksDefault(2);
            setTracebacksDefault(1);
        }
        save();
    }
    private void setTracebacksDefault(int size){
        if(config.server.tracebacks.size()<size)
            config.server.tracebacks.add(new ServerConfig.TracebackServiceConfig());
    }
    public void save(){
        try {
            tomlWriter.write(config,file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull BoatConfigFile loadCostumeFile(File file){
        return new BoatConfigFile(file);
    }
    @Contract(" -> new")
    public static @NotNull BoatConfigFile loadDefaultFile(){
        return loadCostumeFile(new File("boat.toml"));
    }
}
