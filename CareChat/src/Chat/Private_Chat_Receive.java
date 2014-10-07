package Chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Private_Chat_Receive extends Thread {
	DatagramSocket receive_socket=null;
	DatagramPacket dp=new DatagramPacket(new byte[1024], 1024);
	
	public Private_Chat_Receive(){
		try {
			receive_socket=new DatagramSocket(chat.private_chat_port);
			receive_socket.setSoTimeout(0);
		} catch (SocketException e) {
			chat.catchexception(e);
		}
	}
	@Override
	public void run() {
		PrivateChat window_to_show=null;
		while (true) {
			try {
				receive_socket.receive(dp);
				System.out.println("ÊÕµ½Ë½ÁÄ£¡£¡£¡£¡£¡£¡£¡£¡£¡£¡£¡£¡£¡£¡£¡£¡£¡£¡£¡");
				InetAddress hisip=dp.getAddress();
				if ((window_to_show = chat.private_chat_map.get(hisip)) == null) {
					window_to_show = new PrivateChat(hisip);
					window_to_show.showmes(new String(dp.getData(),0,dp.getLength()),chat.frdname.get(hisip));
				}else {
					window_to_show.showmes(new String(dp.getData(),0,dp.getLength()),chat.frdname.get(hisip));
					window_to_show.w.setVisible(true);
				}
			} catch (IOException e) {
				chat.catchexception(e);
			}
		}
	}
}
