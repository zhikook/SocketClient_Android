package cn.gswift.util;


public class Configs {
    public static final int TIME_OUT = 300;
	public static String SERVER_IP ="192.168.1.100";
    public static final int SERVER_PORT = 5555;

    public static enum MessageType{
        CLIENT_SOCKET_NOTIFY,
        CLIENT_SOCKET_IN_MESSAGE,
        CLIENT_SOCKET_OUT_MESSAGE,
        CLIENT_SOCKET_COMMAND,
    }

}
