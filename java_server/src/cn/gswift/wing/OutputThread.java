package cn.gswift.wing;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import cn.gswift.net.bean.SocketMessage;


public class OutputThread extends Thread {
	private OutputThreadMap map;
	private ObjectOutputStream oos;
	private SocketMessage socketmessage;
	private boolean isStart = true;
	private OutputStream outputstream;

	public OutputThread(OutputStream outputstream,OutputThreadMap map) {
            this.map = map;
			this.outputstream = outputstream;	
				
	}

	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}


    public void setMessage(SocketMessage object) {
		this.socketmessage = object;
		synchronized (this) {
			notify();
		}
	}

	@Override
	public void run() {
		super.run();
		
		try {
			while (isStart) {
				System.out.println("------------OutputThread running-------------");
				oos = new ObjectOutputStream(outputstream);
//				synchronized (this) {
//					wait();
//				}if (socketmessage != null) {
//					System.out.println("outputthread"+socketmessage.messageTxt);
//					oos.writeObject(socketmessage);
//					oos.flush();
//				}
			    SocketMessage sm =new SocketMessage();
                sm.action = 4;
                String sendMsgTime = new Date().toGMTString();
                sm.messageTxt = "from server:"+ sendMsgTime;
                oos.writeObject(sm);
				oos.flush();
				try {
					this.sleep(30000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			if (oos != null)
				oos.close();
		
//		} catch (InterruptedException e) {
//			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	 void doStuff()throws IOException{

	        sendResponse();
	    }

	private void sendResponse() throws IOException{
	        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(this.outputstream));
	        
	        output.write("Received your command ! Processing...");
	        
	        output.flush();
	        printDots(output);
	        output.newLine();
	        output.write("OK");
	        output.newLine();
	        output.flush();
	}

	private void printDots(BufferedWriter output) {
	        Random random = new Random();
	        int count = random.nextInt(10) + 1;
	        for(int i = 0;i<count;i++){
	            try {
	                output.write(".");
	                output.flush();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            try{
	                Thread.sleep(TimeUnit.SECONDS.toMillis(1L));
	            }catch (InterruptedException e){
	                e.printStackTrace();
	            }
	        }
	}

}
