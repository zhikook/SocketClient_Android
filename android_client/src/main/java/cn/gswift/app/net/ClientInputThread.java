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

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class ClientInputThread extends Thread{

    private InputStream is;
    private ObjectInputStream ois;
    private SocketMessage sMessage;

    private MessageListener messageListener_input;
    private boolean isRecevStart = false;

    ClientInputThread(InputStream is){
        this.is =is;
        try {
            ois = new ObjectInputStream(this.is);
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public void setStartRecev(boolean start){
        this.isRecevStart = start;
    }

    @Override
    public void run(){
        super.run();
        synchronized (this){
            while(isRecevStart){
                try{
                    Object robj = ois.readObject();
                    getMessage(robj);
                }catch (IOException e1){
                    e1.printStackTrace();
                }
                catch (ClassNotFoundException e2) {
                    e2.printStackTrace();
                }

                //close the objectInputStream
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }

    }

    //set the message listener
    public void setMessageListener_InputThread(MessageListener messageListener){
        this.messageListener_input = messageListener;
    }

    //the method of instance of socketmessage
    public void getMessage(Object obj)  {
        if (obj != null && obj instanceof SocketMessage) {
            sMessage = (SocketMessage) obj;
                   messageListener_input.handleMessage(sMessage);
        }
    }
}
