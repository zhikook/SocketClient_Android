/**
 * Created by David Lau on 13-11-22.
 * http://zhiyong.sinaapp.com
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
 */

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

import cn.gswift.app.utils.Config;
import cn.gswift.app.net.Client;
import cn.gswift.app.net.SocketMessage;


public class MyActivity extends Activity{

    private Client myClient;
    private MyApplication myApp;

    private Button btc,bts,btr,disbt;
    private TextView msg_result,msg_status;
    private EditText ip,port,sendtxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ip = (EditText)findViewById(R.id.ip);
        port = (EditText)findViewById(R.id.port);

        btc = (Button)findViewById(R.id.btc);
        bts = (Button)findViewById(R.id.bts);
        btr = (Button)findViewById(R.id.btr);
        disbt= (Button)findViewById(R.id.disbt);

        msg_result = (TextView)findViewById(R.id.msg_result);
        msg_status = (TextView)findViewById(R.id.msg_status);

        sendtxt = (EditText)findViewById(R.id.msg_et);

        myApp = (MyApplication) this.getApplicationContext();

        //connect the socket server
        btc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initNetConfig();//init the client network config

                myApp.setHandler(mHander);
                myClient = myApp.getClient();
                myApp.setClientStart(true);
                myApp.startClient();
            }
        });

        //send the message to the socket server
        bts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myApp.isClientStart()){
                    String sMsg = sendtxt.getText().toString();
                    SocketMessage sm = new SocketMessage();
                    sm.action = 2;
                    sm.messageTxt = sMsg;

                    myClient = myApp.getClient();
                    myClient.sendMsg(sm);
                }
            }
        });

        //receive the message from the socket server
        btr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myApp.isClientStart()){
                    myClient = myApp.getClient();
                    myClient.receiveMsg();
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

    //handle the client thread 's message
    private Handler mHander = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 0:
                    //disconnect
                    btc.setTextColor(Color.BLACK);
                    disbt.setTextColor(Color.RED);
                    bts.setTextColor(Color.BLACK);
                    btr.setTextColor(Color.BLACK);
                    setStatusTextView(msg.getData().getString("msg_result"));
                    break;
                case 1:
                    //connect
                    btc.setTextColor(Color.GREEN);
                    disbt.setTextColor(Color.BLACK);
                    setStatusTextView("connect");
                    break;
                case 2:
                    //Send Message
                    bts.setTextColor(Color.GREEN);
                    setStatusTextView("message has been sent.");
                   break;
                case 3:
                    //Receive Message
                    btr.setTextColor(Color.GREEN);
                    setStatusTextView("message is comming...");
                    String msgText = msg_result.getText().toString();
                    msgText  = msgText+"\n"+msg.getData().getString("msg_result");
                    msg_result.setText(msgText);
                    break;
                default:
                    break;
            }
        }
    };

    //init the client net-config,include server ip & port
    public  void initNetConfig(){
        String ip_str = ip.getText().toString();
        int port_int = Integer.valueOf(port.getText().toString());

         if(ip_str!=null&&port_int!=0){
            Config.SERVER_IP = ip_str;
            Config.SERVER_PORT = port_int;
        }
    }

    //show the client status
    public void setStatusTextView(String msg){
        this.msg_status.setText(msg);
        this.msg_status.setTextColor(Color.GREEN);
    }

}
