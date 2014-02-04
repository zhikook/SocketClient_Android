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

import android.util.Log;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import cn.gswift.net.bean.SocketMessage;

public class OutputThread extends Thread{
    private Socket socket;

    private OutputStream os;
    private ObjectOutputStream oos;
    private SocketMessage sMessage;

    private boolean isStart = true;

    public OutputThread(Socket s,OutputStream os) {
        this.socket = s;
        this.os = os;
    }

    public void setStart(boolean start) {
        this.isStart = start;
    }

    public void setMessage(SocketMessage msg){
        this.sMessage = msg;

        synchronized (this){
            notify();
        }
    }

    @Override
    public void run(){
        try{
            while (isStart){
                if(sMessage != null){
                    try{
                        oos = new ObjectOutputStream(os);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.v("CLIENT_SEND_MSG",sMessage.messageTxt);
                    oos.writeObject(sMessage);
                    oos.flush();

                    synchronized (this){
                        wait();
                    }
                }
            }
            oos.close();
            os.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}