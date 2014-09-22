package Chat;

import java.awt.TrayIcon;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class AnnounceOLThread extends Thread {
	@Override
	public void run() {
		// TODO Auto-generated method stub
		InetAddress muticast = null;
		byte[] uid=chat.uid;
		try {
			muticast = InetAddress.getByName(chat.muticast);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			chat.catchexception(e1);
		}
		while (true) {
			try {
				byte[] findmesString=("find="+chat.username).getBytes();
				byte[] findmes=new byte[findmesString.length+12];
				System.arraycopy(uid, 0, findmes, 0, 8);
				System.arraycopy(chat.localIPbytes, 0, findmes, 8, 4);
				System.arraycopy(findmesString, 0, findmes, 12, findmesString.length);
				DatagramPacket pack=new DatagramPacket(findmes, findmes.length, muticast, chat.muticastport);
				
				
				MulticastSocket sender = new MulticastSocket();
				
				sender.send(pack);

				sender.close();
				Thread.sleep(10000);
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				if (chat.trayIcon != null) {
					chat.trayIcon.displayMessage("上线信息宣告线程创建失败", "上线信息宣告线程创建失败，其他好友可能无法接收到您的上线信息。", TrayIcon.MessageType.ERROR);
				}
				chat.zhuangtailan.setText("宣告上线消息失败！");
				chat.catchexception(e);
			}
		}
	}
}
