package cn.gswift.app.net;

import java.io.Serializable;

/**
 * Created by davidlau on 13-11-22.
 */
public class SocketMessage  implements Serializable {


    private static final long serialVersionUID = 5357205540518219512L;
    public int action;
    public String messageTxt;

}
