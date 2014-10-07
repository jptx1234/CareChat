package Chat;

import java.awt.TrayIcon;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class SendThread extends Thread {
	private byte[] mes;
	int port=chat.group_chat_port;
	public SendThread(byte[] mes) {
		super();
		this.mes = mes;
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		for (InetAddress ip : chat.frdlist.values()) {
			for (int i = 0; i < mes.length; i+=1024) {
				if (mes.length-i>1024) {
					send(ip, Arrays.copyOfRange(mes, i, i+1024));
				}else {
					send(ip, mes);
				}
			}
		}
	}
	
	public void send(InetAddress ip,byte[] mes){
		DatagramPacket dp = new DatagramPacket(mes, mes.length, ip, port);
		try {
			DatagramSocket ds = new DatagramSocket();
			ds.send(dp);
			ds.close();
		} catch (Exception e) {
			// TODO: handle exception
			if (chat.trayIcon != null) {
				chat.trayIcon.displayMessage(null, "向好友发送消息失败", TrayIcon.MessageType.ERROR);
			}
			chat.zhuangtailan.setText("向"+ip.getHostAddress()+"---"+chat.frdname.get(ip)+"发送失败");
			chat.catchexception(e);
		}
		
	}
}
