
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

import java.io.IOException;

import cn.gswift.net.bean.SocketMessage;

/**
 * Created by davidlau on 13-11-22.
 */
public class Client {
    private ThreadFactory factory;

    private SocketMessage client_sm;

    public Client(Handler handler){
        this.factory = new ThreadFactory(handler);
        factory.setClientThreadStart(true);
    }

    //start client thread
    public void start() {
        factory.start();
        try {
            exec();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //disconnect the socket and stop client thread.
    public void stop(){
       factory.disconnect();
    }

    //Action to ClientInputThread for sending message to server.
    public void sendMsg(SocketMessage sm) {
        this.client_sm = sm;
        OutputThread t = factory.getOut();
        if(t!=null)
            factory.sendMsgAction(t,client_sm);
    }

    private void exec() throws IOException {
        System.out.println(factory.getStartStatus());
        if(factory.getStartStatus()){
            factory.exec();
        }
    }


}
