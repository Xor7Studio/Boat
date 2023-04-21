package xor7studio.boat;

import org.apache.commons.cli.*;
import xor7studio.boat.client.Client;
import xor7studio.boat.server.center.CenterServer;

import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("h","help",false,"获取帮助");
        OptionGroup mode = new OptionGroup();
        mode.addOption(new Option("c", "client", false, "启动客户端"));
        mode.addOption(new Option("a", "auth-server", false, "启动身份验证服务端"));
        mode.addOption(new Option("s", "center-server", false, "启动中心服务端"));
        mode.setRequired(true);
        options.addOptionGroup(mode);
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            printHelp(options,1);
        }
        assert cmd != null;
        if (cmd.hasOption("h")) printHelp(options,0);
        else if (cmd.hasOption("c")) {
            new Client(null);
        }else if (cmd.hasOption("a")) {

        }else if (cmd.hasOption("s")) {
            new CenterServer(new InetSocketAddress("localhost",11099));
        }
    }
    private static void printHelp(Options options,int exitCode){
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Boat", options);
        System.exit(exitCode);
    }
}
