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
				chat.trayIcon.displayMessage("��Ϣ�����̴߳���ʧ��", "��Ϣ�����̴߳���ʧ�ܣ��������޷����յ����ѵ���Ϣ�������Ƿ�ͬʱ�������������������ϱ����������45001�˿�ռ�������", TrayIcon.MessageType.ERROR);
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
					chat.trayIcon.displayMessage("��Ϣ�����̳߳���", "��Ϣ�����̳߳��ִ��������������������������һֱ���֣��뽫����Ŀ¼�µġ�������־.log���ļ��������ߡ�", TrayIcon.MessageType.ERROR);
				}
				chat.catchexception(e);
			}
		}
	}
}
