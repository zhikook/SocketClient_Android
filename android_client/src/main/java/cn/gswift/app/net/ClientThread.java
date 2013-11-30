package cn.gswift.app.net;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import cn.gswift.app.utils.Config;

public class ClientThread extends Thread{
    private boolean clientThreadStart = false;
    private Handler clientH;

    private ClientInputThread client_in;
    private ClientOutputThread client_out;

    private InputStream inputStream;
    private OutputStream outputStream;

    private Socket csocket;

    public ClientThread(Handler handler){
        this.clientH = handler;
        this.csocket = new Socket();
    }

    @Override
    public void run() {
       super.run();
       while (clientThreadStart){
           try {
               csocket.connect(new InetSocketAddress(Config.SERVER_IP,Config.SERVER_PORT),Config.CONNECT_TIMEOUT);
               inputStream = csocket.getInputStream();
               outputStream = csocket.getOutputStream();

           } catch (IOException e) {
               e.printStackTrace();
           }

           //server is started?
           if(csocket.isConnected()){
               //create a client input thread.
               client_in = new ClientInputThread(inputStream);

               client_out = new ClientOutputThread(outputStream);

               Message m = clientH.obtainMessage();
               m.what = 1;//success to connect
               clientH.sendMessage(m);

           }else{
               Message m = clientH.obtainMessage();
               m.what = 0;//can not connect server
               Bundle b = new Bundle();
               b.putString("msg_result","The server is not started");
               m.setData(b);

               clientH.sendMessage(m);
               break;
           }
       }
    }

    //disconnect the socket
    public void disconnect(){
        try {
            csocket.close();

            Message m = clientH.obtainMessage();
            m.what = 0;
            clientH.sendMessage(m);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isStart(){
        return csocket.isConnected();
    }

    //set lock of the client thread
    public void setClientThreadStart(boolean b) {
        this.clientThreadStart = b;

    }

    public void sendMsgAction(SocketMessage msg){
        if(isStart()){
            //send message to the ClientOutputThread
            client_out.setStart(true);
            client_out.start();

            client_out.setMessage(msg);

            //send a log message to UI
            Message m = clientH.obtainMessage();
            m.what = 2;
            clientH.sendMessage(m);
        }
    }

    public  void receiveMsgAction(){
        if(isStart()){

            //create a messagelistener instance.
            client_in.setMessageListener_InputThread(new MessageListener() {
                @Override
                public void handleMessage(SocketMessage message) {

                    String msg_txt = message.messageTxt;

                    Message m = clientH.obtainMessage();
                    m.what = 3;
                    Bundle b = new Bundle();
                    b.putString("msg_result",msg_txt);
                    m.setData(b);
                    clientH.sendMessage(m);

                }
            });

            client_in.setStartRecev(true);

            client_in.start();

        }
    }
}