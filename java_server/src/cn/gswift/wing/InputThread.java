package cn.gswift.wing;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import cn.gswift.net.bean.SocketMessage;


public class InputThread extends Thread {
	private InputStream inputstream;
	private OutputThread out;
	private OutputThreadMap map;
	private ObjectInputStream ois;
	private boolean isStart = true;

	public InputThread(InputStream is, OutputThread out, OutputThreadMap map) {
		this.inputstream = is;
		this.out = out; 
		this.map = map;
	}

	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}

	@Override
	public void run() {
		super.run();
		synchronized (this){
			try {	
				while (isStart) {
					System.out.println("------------InputThread running-------------");
					receiveMessage();
				}
				if (ois != null)
					ois.close();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 

	}

	public synchronized void receiveMessage() throws IOException, ClassNotFoundException {
		
		ois = new ObjectInputStream(this.inputstream);
		
		//receive message
		Object readObject = ois.readObject();

		if (readObject != null && readObject instanceof SocketMessage) {
			SocketMessage readMessage = (SocketMessage) readObject;
			String msg_txt = readMessage.messageTxt;
			int action = readMessage.action;
			
			System.out.println(">------------------Message-----------------");
			
			System.out.println(action);
			System.out.println("client message"+ msg_txt.toString());
			
			System.out.println("------------------Message----------------->");
			
			
			if(action == 4){
				
				SocketMessage sm = new SocketMessage();
				sm.action = 4;
				sm.messageTxt ="got it";
				
				out.setMessage(sm);
			}

		}
		
		
	}
}
