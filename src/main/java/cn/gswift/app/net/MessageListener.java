package cn.gswift.app.net;

import cn.gswift.net.bean.SocketMessage;

/**
 * Created by davidlau on 13-11-22.
 */
public interface MessageListener {
    public void handleMessage(SocketMessage message);
}
