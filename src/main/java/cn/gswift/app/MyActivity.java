/**
 * Created by David Lau on 13-11-22.
 * http://zhiyong.sinaapp.com
 * email:gswift.cn@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/

 package cn.gswift.app;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.gswift.app.net.Client;
import cn.gswift.app.utils.Config;
import cn.gswift.net.bean.SocketMessage;


public class MyActivity extends Activity{

    private Client myClient;
    private MyApplication myApp;

    private Button btc,bts,disbt;
    private TextView msg_result,msg_status;
    private EditText ip,port,sendtxt;

    private H mH = new H();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ip = (EditText)findViewById(R.id.ip);
        port = (EditText)findViewById(R.id.port);

        btc = (Button)findViewById(R.id.btc);
        bts = (Button)findViewById(R.id.bts);
        disbt= (Button)findViewById(R.id.disbt);

        msg_result = (TextView)findViewById(R.id.msg_result);
        msg_status = (TextView)findViewById(R.id.msg_status);

        sendtxt = (EditText)findViewById(R.id.msg_et);

        myApp = (MyApplication) this.getApplicationContext();

        //connect the socket server
        btc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNetConfig();//init the client network config

                myApp.setHandler(mH);
                myClient = myApp.getClient();
                myApp.setClientStart(true);
                myApp.startClient();
            }
        });

        //send the message to the socket server
        bts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myApp.getClientStart()){
                    String sMsg = sendtxt.getText().toString();
                    SocketMessage sm = new SocketMessage();
                    sm.action = 2;
                    sm.messageTxt = sMsg;

                    myClient = myApp.getClient();
                    myClient.sendMsg(sm);
                }
            }
        });

        //disconnect from the client's socket
        disbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myClient = myApp.getClient();
                myClient.stop();
            }
        });
    }

    //init the client net-config,include server ip & port
    private  void setNetConfig(){
        String ip_str = ip.getText().toString();
        int port_int = Integer.valueOf(port.getText().toString());

         if(ip_str!=null&&port_int!=0){
            Config.SERVER_IP = ip_str;
            Config.SERVER_PORT = port_int;
        }
    }

    //show the client status
    private void setStatusTextView(String msg){
        this.msg_status.setText(msg);
        this.msg_status.setTextColor(Color.GREEN);
    }

    public final class H extends Handler{
        public static final int SOCKET_CONNECT_FAIL =0;
        public static final int SOCKET_CONNECT_SUCCESS =1;

        public static final int INPUT_THREAD_SUCCESS =2;
        public static final int INPUT_THREAD_FAIL =3;

        public static final int OUTPUT_THREAD_SUCCESS =4;
        public static final int OUTPUT_THREAD_FAIL =5;

        public static final int SEND_MSG_SUCCESS = 6;
        public static final int SEND_MSG_FAIL = 7;

        public static final int RECV_MSG_SUCCESS = 8;
        public static final int RECV_MSG_FAIL = 9;

        public static final int SHUTDOWN_CONNECT = 10;
        public static final int SOCKET_DISCONNECT = 11;

        public H(){
        }

        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case  SOCKET_CONNECT_FAIL:
                    btc.setTextColor(Color.BLACK);
                    disbt.setTextColor(Color.RED);
                    bts.setTextColor(Color.BLACK);
                    setStatusTextView("fail to connect.");
                    break;
                case  SOCKET_CONNECT_SUCCESS :
                    //connect
                    btc.setTextColor(Color.GREEN);
                    disbt.setTextColor(Color.BLACK);
                    setStatusTextView("success to connect.");
                    break;
                case INPUT_THREAD_SUCCESS:
                    bts.setTextColor(Color.GREEN);
                    setStatusTextView("success to create input thread.");
                    break;
                case INPUT_THREAD_FAIL:
                    setStatusTextView("fail to create input thread.");
                    break;
                case OUTPUT_THREAD_SUCCESS:
                    setStatusTextView("success to create output thread.");
                    break;
                case OUTPUT_THREAD_FAIL:
                    setStatusTextView("fail to create output thread.");
                    break;
                case SEND_MSG_SUCCESS:
                    sendtxt.setText("");
                    setStatusTextView("success to send message.");
                    break;
                case SEND_MSG_FAIL:
                    bts.setTextColor(Color.RED);
                    bts.setText("Re-SEND");
                    setStatusTextView("fail to send message.");
                    break;
                case RECV_MSG_SUCCESS:
                    setStatusTextView("message is coming.");
                    msg_result.setText("");

                    break;
                case RECV_MSG_FAIL:
                    setStatusTextView("fail to receive message.");
                    btc.setText("Re-Connect");
                    btc.setTextColor(Color.RED);
                    break;
                case SHUTDOWN_CONNECT:
                    btc.setText("Re-Connect");
                    btc.setTextColor(Color.RED);
                    disbt.setTextColor(Color.RED);
                    break;
                default:
                    break;
            }
        }
    }

}
