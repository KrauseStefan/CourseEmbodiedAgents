import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import lejos.nxt.LCD;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;


public class BluetoothValues implements Runnable {

	private BTConnection btc;
	private DataInputStream dataRecv ;
	private DataOutputStream dataSend;
	
	private byte[] reciveBuffer = new byte[1024];
	
	private Thread t;
	public BluetoothValues() {
		btc = Bluetooth.waitForConnection();
		dataRecv = btc.openDataInputStream();
		dataSend = btc.openDataOutputStream();

		t = new Thread(this);
	}
	
	public void startReciveLoop(){
		t.start();
	}
	
	public void run(){
		while(true){
			try {
				int off = 0;
				int readBytes = dataRecv.read(reciveBuffer, off, reciveBuffer.length);
				
				
			} catch (IOException e) {
				LCD.clear();
				LCD.drawString("Error reciving data!!!!", 0, 0);

				e.printStackTrace();
			}
		}
	}
		
	public int getInt(String id){
		
		return 0;
	}
	
	public long getLong(String id){
		
		return 0;
	}

	public float getFloat(String id){
		
		return 0;
	}

	public String getString(String id){
		
		return "";
	}

	public Object getObject(String id){
		
		return null;
	}

}
