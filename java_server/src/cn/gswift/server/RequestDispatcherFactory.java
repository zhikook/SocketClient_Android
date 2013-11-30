package cn.gswift.server;
import java.net.Socket;

public interface RequestDispatcherFactory {

    public Runnable create(Socket socket);

}
