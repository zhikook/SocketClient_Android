package cn.gswift.app.net;

import android.os.Handler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import cn.gswift.app.MyActivity.H;
import cn.gswift.app.utils.Config;
import cn.gswift.net.bean.SocketMessage;

public class ThreadFactory extends Thread{
    private boolean clientThreadStart = false;
    private boolean isStart = false;

    private H mH;

    public InputThread in;
    public OutputThread out;

    private Socket csocket;

    public ThreadFactory(Handler handler){
        this.mH = (H)handler;
        this.csocket = new Socket();
    }

    @Override
    public void run() {
        if (clientThreadStart){
           try {
               csocket.connect(new InetSocketAddress(Config.SERVER_IP,Config.SERVER_PORT),Config.CONNECT_TIMEOUT);
               isStart();
           } catch (IOException e) {
               e.printStackTrace();
           }

           if(isStart){
               mH.removeMessages(mH.SOCKET_CONNECT_SUCCESS);
               mH.sendMessage(mH.obtainMessage(mH.SOCKET_CONNECT_SUCCESS));
           }else{
               mH.removeMessages(mH.SOCKET_CONNECT_FAIL);
               mH.sendMessage(mH.obtainMessage(mH.SOCKET_CONNECT_FAIL));
           }
        }

        while (csocket.isClosed()){
            mH.removeMessages(mH.SOCKET_DISCONNECT);
            mH.sendMessage(mH.obtainMessage(mH.SOCKET_DISCONNECT));
        }
    }

    //disconnect the socket
    public void disconnect(){
        try {
            doExec(false);
            if(csocket!=null)
                csocket.close();

            mH.removeMessages(mH.SHUTDOWN_CONNECT);
            mH.sendMessage(mH.obtainMessage(mH.SHUTDOWN_CONNECT));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void isStart(){
        this.isStart= csocket.isConnected();
    }

    //set lock of the client thread
    public void exec() {
        if(csocket!=null){
            if(createThread(csocket))
                doExec(true);
        }
    }

    public boolean getStartStatus() {
        return isStart;
    }





    public void sendMsgAction(OutputThread out,SocketMessage msg){
        if(out!=null){
            //send message to the ClientOutputThread
            out.setMessage(msg);
            //send a log message to UI
            mH.removeMessages(mH.SEND_MSG_SUCCESS);
            mH.sendMessage(mH.obtainMessage(mH.SEND_MSG_SUCCESS));

        }else {
            mH.removeMessages(mH.SEND_MSG_FAIL);
            mH.sendMessage(mH.obtainMessage(mH.SEND_MSG_FAIL));
        }
    }

    public  void setMessageListenerInstance(InputThread in){

        //create a messagelistener instance.
        in.setMessageListener_InputThread(new MessageListener() {
            @Override
            public void handleMessage(SocketMessage message) {
                if(message!=null){
                    mH.removeMessages(mH.RECV_MSG_SUCCESS);
                    mH.sendMessage(mH.obtainMessage(mH.RECV_MSG_SUCCESS));
                }

                System.out.println(message.messageTxt);

            }
        });
    }

    public OutputThread getOut(){
        return this.out;
    }

    public InputThread getIn(){
        return this.in;
    }

    public void setClientThreadStart(boolean s){
        this.clientThreadStart = s;
    }

    private void doExec(boolean t){
        in.setStart(t);
        out.setStart(t);
        if(t){
            in.start();
            out.start();

            mH.removeMessages(mH.OUTPUT_THREAD_SUCCESS);
            mH.sendMessage(mH.obtainMessage(mH.OUTPUT_THREAD_SUCCESS));

            mH.removeMessages(mH.INPUT_THREAD_SUCCESS);
            mH.sendMessage(mH.obtainMessage(mH.INPUT_THREAD_SUCCESS));
        }
    }

    private boolean createThread(Socket socket) {
        //server is started?
        if(socket.isConnected()){
            //create a client input thread.
            try{
                out = new OutputThread(socket,socket.getOutputStream());
                in = new InputThread(socket,socket.getInputStream());
                setMessageListenerInstance(in);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            mH.removeMessages(mH.SOCKET_CONNECT_FAIL);
            mH.sendMessage(mH.obtainMessage(mH.SOCKET_CONNECT_FAIL));
        }
        return true;
    }




}