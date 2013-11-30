package cn.gswift.server;

import java.io.IOException;

import cn.gswift.engine.SocketHandlerFactory;

import static java.util.concurrent.Executors.newCachedThreadPool;

/**
 * Created by davidlau on 13-11-29.
 */
public class SocketListener extends Thread{

    public Socket socket;
    public SocketHandlerFactory socketHandlerFactory;
    public DispatcherPool dispatcherPool;

    public SocketListener(String ip,int port, int timeout,SocketHandlerFactory socketHandlerFactory) {
        this.socket = new Socket(ip,port,timeout);
        this.socketHandlerFactory = socketHandlerFactory;
        this.dispatcherPool = new DispatcherPool(newCachedThreadPool());
    }

    public SocketListener(Socket socket,SocketHandlerFactory socketHandlerFactory,DispatcherPool dispatcherPool){
        this.socket = socket;
        this.socketHandlerFactory = socketHandlerFactory;
        this.dispatcherPool = dispatcherPool;
    }

    @Override
    public void run(){
        try {
            startListening();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void startListening() throws IOException {
        initialize();
        acceptConnections();
    }

    private void acceptConnections() throws IOException {
        System.out.println("<-----SocketListener accept connection------>");
        
        while (this.socket.isBound()){
            java.net.Socket socket = this.socket.accept();
            
            String ip = socket.getInetAddress().toString();
			System.out.println(">--------------------------------------------");
			System.out.println(" Client£º" + ip + " is connected!");
			String mDate = new java.util.Date().toString();
			System.out.println("Time is : "+ mDate);
			System.out.println("-------------------------------------------->");			           
            
            this.dispatcherPool.execute(this.socketHandlerFactory.newSocketHandler(socket));
        }
    }

    private void initialize() throws IOException {
    	System.out.println("<---------SocketListener initialize--------->");
        this.socket.open();
    }

    public void stopListening() throws InterruptedException {
    	System.out.println("<-------SocketListener stop Listening------->");
        
        this.clean();
        if(this.isAlive()){
            try {
                this.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void clean(){
        try {
            this.socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        this.dispatcherPool.shutdown();
    }
}
