package Chat;

import java.awt.TrayIcon;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class receiveThread extends Thread {
	@SuppressWarnings("resource")
	@Override
	public void run() {
		// TODO Auto-generated method stub
		DatagramSocket ds = null;
		try {
			ds = new DatagramSocket(45001);
		} catch (Exception e) {
			// TODO: handle exception
			if (chat.trayIcon != null) {
				chat.trayIcon.displayMessage("消息接收线程创建失败", "消息接收线程创建失败，您可能无法接收到好友的消息，请检查是否同时运行了两个及两个以上本软件，或检查45001端口占用情况。", TrayIcon.MessageType.ERROR);
			}
			chat.catchexception(e);
		}
		while (true) {
			try {
				DatagramPacket dp=new DatagramPacket(new byte[1024], 1024);
				ds.receive(dp);
				chat.showmes(new String(dp.getData(), 0, dp.getLength()),chat.frdname.get(dp.getAddress()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				if (chat.trayIcon != null) {
					chat.trayIcon.displayMessage("消息接收线程出错", "消息接收线程出现错误，请重启本软件。如果这个问题一直出现，请将程序目录下的“错误日志.log”文件发给作者。", TrayIcon.MessageType.ERROR);
				}
				chat.catchexception(e);
			}
		}
	}
}
