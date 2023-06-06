package xor7studio.boat.config;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;

import java.io.File;
import java.io.IOException;

public class BoatConfigFile {
    public static final BoatConfigFile DEFAULT = new BoatConfigFile();
    private final File file;
    private final TomlWriter tomlWriter = new TomlWriter();
    public BoatConfig config;
    private BoatConfigFile(){
        file = new File("boat.toml");
        if(!file.exists()) {
            try {
                if(!file.createNewFile())
                    throw new Error("无法创建配置文件，请尝试提升程序权限或手动创建boat.toml文件");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        config=new Toml().read(file).to(BoatConfig.class);
        for(int i=0;config.server.tracebacks.size()<2;i++)
            config.server.tracebacks.add(new TracebackServiceConfig(11097+i));
        save();
    }
    public void save(){
        try {
            tomlWriter.write(config,file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
