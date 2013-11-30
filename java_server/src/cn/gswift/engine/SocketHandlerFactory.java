package cn.gswift.engine;

import java.net.Socket;


/**
 * Created by davidlau on 13-11-27.
 */
public interface SocketHandlerFactory {
    public Runnable newSocketHandler(Socket socket);
}
