package cn.gswift.server;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;


public class Socket {
	public String ip;
    public int port;
    public int timeout;

    private ServerSocket serverSocket;

    public Socket(String ip,int port,int timeout) {
    	this.ip = ip;
        this.port = port;
        this.timeout = timeout;
    }

    public void open() throws IOException{
        this.serverSocket = new ServerSocket();
        this.serverSocket.bind(new InetSocketAddress(this.ip,this.port),this.timeout);
        this.serverSocket.setReuseAddress(true);
    }

    public void close() throws IOException{
        if(this.serverSocket !=null){
            this.serverSocket.close();
        }
    }

    public java.net.Socket accept() throws IOException {
        if(this.serverSocket == null){
            return null;
        }
        return  this.serverSocket.accept();
    }

    public boolean isBound() {
        return this.serverSocket != null&& this.serverSocket.isBound();
    }
}