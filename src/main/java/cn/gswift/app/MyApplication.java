package cn.gswift.app;

import android.app.Application;
import android.os.Handler;

import cn.gswift.app.net.Client;

/**
 * Created by davidlau on 13-11-22.
 */
public class MyApplication extends Application{
    public Client client;
    private boolean isClientStart;

    public  Handler h;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public void setHandler(Handler handler){
        this.h = handler;
    }

    public void startClient(){
        client = new Client(h);
        if(isClientStart){
            client.start();
        }
    }

    public Client getClient(){
        return client;
    }

    public boolean getClientStart(){
        return isClientStart;
    }

    public void setClientStart (boolean isClientStart){
        this.isClientStart = isClientStart;
    }

    public Handler getH(){
        return h;
    }
}
