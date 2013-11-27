
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

package cn.gswift.app.net;

import android.os.Handler;

/**
 * Created by davidlau on 13-11-22.
 */
public class Client {
    private Handler cHandler;
    private ClientThread clientThread;

    private SocketMessage client_sm;

    public Client(Handler handler){
        this.cHandler = handler;
        clientThread = new ClientThread(handler);
    }

    //start client thread
    public void start() {
        clientThread.setClientThreadStart(true);
        clientThread.start();

    }

    //disconnect the socket and stop client thread.
    public void stop(){
        clientThread.disconnect();
        clientThread.setClientThreadStart(false);
    }

    //Action to ClientOutputThread for receiving message from server.
    public  void receiveMsg(){
           clientThread.receiveMsgAction();
    }

    //Action to ClientInputThread for sending message to server.
    public void sendMsg(SocketMessage sm) {
        this.client_sm = sm;
        if(clientThread.isStart())
            clientThread.sendMsgAction(client_sm);
    }
}
