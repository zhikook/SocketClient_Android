package cn.gswift;


import cn.gswift.engine.SocketHandlerFactory;
import cn.gswift.server.SocketServer;
import cn.gswift.util.Configs;

public class MyClass {

    public static void main(String[] args)throws Exception{

//        if(args.length!= 3){
//            System.err.println("Required parameters: <port> <timeout> <SocketHandlerFactoryClassName>");
//            return;
//        }
//
//        int port = Integer.parseInt(args[0]);
//        int timeout = Integer.parseInt(args[1]);
//
//        SocketHandlerFactory factory = (SocketHandlerFactory)Class.forName(args[2]).newInstance();
    	
    	String ip = Configs.SERVER_IP;
    	int port = Configs.SERVER_PORT;
    	int timeout = Configs.TIME_OUT;
    	
    	SocketHandlerFactory factory = (SocketHandlerFactory)Class.forName("cn.gswift.engine.WorkSocketHandlerFactory").newInstance();
    	
        SocketServer server = new SocketServer
                .Buidler()
        		.withIP(ip)
                .withTimeout(timeout)
                .onPort(port)
                .withSocketHandlerFactory(factory)
                .build();
        server.start();
    }
}
