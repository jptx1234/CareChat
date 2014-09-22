package Chat;

import java.awt.TrayIcon;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SendThread extends Thread {
	private byte[] mes;
	public SendThread(byte[] mes) {
		super();
		this.mes = mes;
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		for (InetAddress ip : chat.frdlist.values()) {
			for (int i = 0; i < mes.length; i+=1024) {
				byte[] mes_temp=new byte[1024];
				System.arraycopy(mes, i, mes_temp, 0, 1024);
				send(ip, mes_temp);
			}
		}
	}
	
	public void send(InetAddress ip,byte[] mes){
		DatagramPacket dp = new DatagramPacket(mes, mes.length, ip, 45001);
		try {
			DatagramSocket ds = new DatagramSocket();
			ds.send(dp);
			ds.close();
		} catch (Exception e) {
			// TODO: handle exception
			if (chat.trayIcon != null) {
				chat.trayIcon.displayMessage(null, "向好友发送消息失败", TrayIcon.MessageType.ERROR);
			}
			chat.zhuangtailan.setText("向"+ip.getHostAddress()+"---"+chat.frdlist.get(ip)+"发送失败");
			chat.catchexception(e);
		}
		
	}
}
