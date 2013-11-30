package cn.gswift.server;

import java.io.IOException;

import cn.gswift.engine.SocketHandlerFactory;

import cn.gswift.util.Runtime;


public class SocketServer {

    private final String ip;
    private final int port;
    private final int timeout;

	public ShutDownHandler shutDowunHandler;
	public SocketListener socketListener;
	private Object startUpShutdowLock;

	public SocketServer(String ip,int port,int timeout,SocketHandlerFactory socketHandlerFactory){
		this.ip = ip;
        this.port = port;
        this.timeout = timeout;
	    this.shutDowunHandler = new ShutDownHandler(this,new Runtime(java.lang.Runtime.getRuntime()));
	    this.socketListener = new SocketListener(ip,port,timeout,socketHandlerFactory);
	}

	public SocketServer(String ip,int port, int timeout, ShutDownHandler shutDowunHandler, SocketListener socketListener){
        this.ip = ip;
	    this.port = port;
        this.timeout = timeout;
        this.shutDowunHandler = shutDowunHandler;
	    this.shutDowunHandler.socketServer = this;
	    this.socketListener = socketListener;
	}

	public void start() throws IOException{
        synchronized (this.startUpShutdownLock){
            this.shutDowunHandler.addShutDownHook();
            this.socketListener.start();
            System.out.println("<-------------Server is Started------------->");
        }
    }

    public void stop() throws InterruptedException {
        synchronized (this.startUpShutdownLock){
            this.socketListener.stopListening();
            this.shutDowunHandler.removeShutDownHook();
            System.out.println("<-------------Server is Stopped------------->");
        }
    }

    private final Object startUpShutdownLock = new Object();

    public static class Buidler{
    	private String ip;
    	private Integer port;
        private Integer timeout;
        private SocketHandlerFactory socketHandlerFactory;
        
        public Buidler withIP(String ip){
        	this.ip = ip;
        	return this;
        }

        public Buidler withTimeout(int timeout){
            this.timeout = timeout;
            return this;
        }

        public Buidler onPort(int port){
            this.port = port;
            return this;
        }

        public Buidler withSocketHandlerFactory(SocketHandlerFactory socketHandlerFactory){
            this.socketHandlerFactory = socketHandlerFactory;
            return  this;
        }

        public SocketServer build(){
            if(port== null ||timeout ==null){
                throw  new IllegalStateException("port and timeout do not have defaults");
            }
            return  new SocketServer(ip,port,timeout,socketHandlerFactory);
        }
    }
}
