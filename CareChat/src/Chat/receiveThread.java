package Chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class receiveThread extends Thread {
	@SuppressWarnings("resource")
	@Override
	public void run() {
		// TODO Auto-generated method stub
		DatagramSocket ds = null;
		try {
			ds = new DatagramSocket(45001,InetAddress.getByName("0.0.0.0"));
		} catch (Exception e) {
			// TODO: handle exception
			chat.catchexception(e);
		}
		while (true) {
			try {
				DatagramPacket dp=new DatagramPacket(new byte[1024], 1024);
				ds.receive(dp);
				String mes=new String(dp.getData(),0,dp.getLength());
				InetAddress ip=dp.getAddress();
				chat.showmes(mes, chat.frdlist.get(ip));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				chat.catchexception(e);
			}
		}
	}
}
