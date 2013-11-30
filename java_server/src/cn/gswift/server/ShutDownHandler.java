package cn.gswift.server;

import cn.gswift.util.Runtime;

/**
 * Created by davidlau on 13-11-29.
 */
public class ShutDownHandler extends Thread{
    public SocketServer socketServer;
    public Runtime runtime;

    public ShutDownHandler(SocketServer socketServer2, Runtime runtime) {
        this.socketServer =socketServer2;
        this.runtime = runtime;
    }

    @Override
    public void run(){
        if(this.socketServer !=null){
            try {
                this.socketServer.stop();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void addShutDownHook() {
        try {
            this.runtime.addShutdownHook(this);
        }catch (IllegalStateException e){

        }catch (IllegalArgumentException e){

        }
    }

    public boolean removeShutDownHook() {
        try {
            return this.runtime.removeShutdownHook(this);
        }catch (IllegalStateException e){

        }
        return false;
    }
}
