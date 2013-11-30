package cn.gswift.engine;

import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by davidlau on 13-11-27.
 */
public class WorkSocketHandlerFactory implements SocketHandlerFactory{
    private final AtomicInteger nextId = new AtomicInteger();

    @Override
    public Runnable newSocketHandler(Socket socket) {
        int id = nextId.getAndIncrement();
        return new WorkSocketHandler(id,socket);
    }
}
