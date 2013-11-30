package cn.gswift.engine;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;


import cn.gswift.net.bean.SocketMessage;
import cn.gswift.wing.InputThread;
import cn.gswift.wing.OutputThread;
import cn.gswift.wing.OutputThreadMap;


/**
 * Created by davidlau on 13-11-27.
 */
public class WorkSocketHandler implements Runnable{
    private final int id;
    private OutputThreadMap map;
    private final Socket clientSocket;
    
    private InputStream is ;
    private OutputStream os;
    
    private String sendMsgTime ;

    public WorkSocketHandler(int id, Socket clientSocket) {
        this.id = id;
        this.clientSocket = clientSocket;
        map = OutputThreadMap.getInstance();
        System.out.println("<----------WorkSocketHandler construct--------->");
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("<--_______________Work socket Handler__________________-->");
		try{
            if(clientSocket.isConnected()){
            	
            	is = clientSocket.getInputStream();
            	os = clientSocket.getOutputStream();
            	 //new two thread
                OutputThread out = new OutputThread( os,map);
                
                InputThread in = new InputThread(is,out,map);
                
                //start read/write thread 
                in.start();
                
//                SocketMessage sm =new SocketMessage();
//                sm.action = 4;
//                sendMsgTime = new Date().toGMTString();
//                sm.messageTxt = "from server:"+ sendMsgTime;
//                
//                
//                out.setMessage(sm);
//                System.out.println(sm.messageTxt);
                out.setStart(true);
                out.start();
                
                
            }
                       
            //System.out.println("(Request #{0}) Reading output from{1}"+id+inputStream);

        }catch (IOException ex){
            ex.printStackTrace();
        }
	}
	
	  private void closeQuietly(Closeable closeable) {
	        if(closeable != null)
	            try {
	                closeable.close();
	            }catch (IOException e){
	                e.printStackTrace();
	            } finally { /* we tried! */ }
	    }

}
