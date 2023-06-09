package xor7studio.boat;

import xor7studio.boat.authentication.AuthenticationService;
import xor7studio.boat.config.BoatConfigFile;
import xor7studio.boat.config.TracebackServiceConfig;
import xor7studio.boat.traceback.TracebackService;

public class BoatMain {
    public static void main(String[] args) {
        System.out.println(BoatConfigFile.DEFAULT.config.run_as);
        if(BoatConfigFile.DEFAULT.config.run_as.equals("server")){
            new Thread(() -> new AuthenticationService().start()).start();
            for(TracebackServiceConfig config:BoatConfigFile.DEFAULT.config.server.tracebacks)
                if(config.run)
                    new Thread(() -> new TracebackService(config).start()).start();
        }
        else new BoatClient().start();

//        System.out.println(HttpRequestUtil.post(""));
//        BoatConfigFile config=BoatConfigFile.loadDefaultFile();
//        config.config.costume=new MyConfig();
//        config.save();
//        Options options = new Options();
//        options.addOption("h","help",false,"获取帮助");
//        OptionGroup mode = new OptionGroup();
//        mode.addOption(new Option("c", "client", false, "启动客户端"));
//        mode.addOption(new Option("s", "server", false, "启动服务端"));
//        mode.setRequired(true);
//        options.addOptionGroup(mode);
//        CommandLineParser parser = new DefaultParser();
//        CommandLine cmd = null;
//        try {
//            cmd = parser.parse(options, args);
//        } catch (ParseException e) {
//            System.out.println(e.getMessage());
//            printHelp(options,1);
//        }
//        assert cmd != null;
//        if (cmd.hasOption("h")) printHelp(options,0);
//        else if (cmd.hasOption("c")) {
//            isClient=true;
//            new Client(null);
//        }else if (cmd.hasOption("s")) {
//            isClient=false;
//            new LongConnectionServer(new InetSocketAddress("localhost",11099));
//        }
    }
//    private static void printHelp(Options options,int exitCode){
//        HelpFormatter formatter = new HelpFormatter();
//        formatter.printHelp("Boat", options);
//        System.exit(exitCode);
//    }
}
