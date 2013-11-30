package cn.gswift.app.net;

/**
 * Created by davidlau on 13-11-22.
 */
public interface MessageListener {
    public void handleMessage(SocketMessage message);
}
