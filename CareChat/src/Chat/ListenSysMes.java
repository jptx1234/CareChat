package Chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.awt.TrayIcon;
import java.util.Arrays;

public class ListenSysMes extends Thread {
	public volatile MulticastSocket broadSocket;
	private boolean run=true;
	byte[] myuid=chat.uid;
	String myuidString=chat.uidString;
	
	public ListenSysMes(){
		try {
			this.broadSocket=new MulticastSocket(chat.muticastport);
			this.broadSocket.joinGroup(InetAddress.getByName(chat.muticast));
		} catch (IOException e) {
			// TODO ����
			if (chat.trayIcon != null) {
				chat.trayIcon.displayMessage("CareChat��ʼ������", "�㲥��Ϣ�����̴߳���ʧ�ܣ��������޷���ȡ�����б��뽫����Ŀ¼�µġ�������־.log���ļ���������", TrayIcon.MessageType.ERROR);
			}
			chat.catchexception(e);
			run=false;
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("�ѿ�ʼ");
		if (!run) {
			return;
		}
		DatagramPacket received;
		String mes;
		String hisname;
		InetAddress hisip;
		String frdshow;
		byte[] hisuid;
		String hisuidString;
		try {
			broadSocket.setSoTimeout(0);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			chat.catchexception(e1);
		}
		while (true) {
			received=new DatagramPacket(new byte[47], 47);
			try {
				broadSocket.receive(received);
				hisuid=Arrays.copyOfRange(received.getData(), 0, 8);
				StringBuilder sb=new StringBuilder();
				for (byte b : hisuid) {
					String b_temp=String.valueOf(b);
					if (b_temp.contains("-")) {
						b_temp=b_temp.replaceAll("-", "");
						sb.append("-");
					}
					for (int i = 3-b_temp.length(); i > 0; i--) {
						sb.append("0");
					}
					sb.append(b_temp);
				}
				hisuidString=sb.toString();
				System.out.println("�յ�"+hisuidString);
				hisip=InetAddress.getByAddress(Arrays.copyOfRange(received.getData(), 8, 12));
				System.out.println(hisip);
				mes=new String(received.getData(),12,received.getLength()-12);
				System.out.println(mes);
				if (hisuidString.equals(myuidString)) {
					continue;
				}
				chat.frd_online_time.put(hisuidString, System.currentTimeMillis());
				if (mes.startsWith("find=")) {
					hisname=mes.replaceAll("find=", ""); 
					if (chat.frdlist.containsKey(hisuidString)) {
						rename(hisuidString,hisip, hisname);
						returnmes(hisip);
					}else {
						online(hisuidString,hisip, hisname, "online");
						returnmes(hisip);
					}
				}else if (mes.startsWith("rtrn=")) {
					hisname=mes.replaceAll("rtrn=", "");
					if (chat.frdlist.containsKey(hisuidString)) {
						rename(hisuidString, hisip, hisname);
					}else {
						online(hisuidString,hisip, hisname, "find");
					}
				}else if (mes.startsWith("renm=")) {
					hisname=mes.replaceAll("renm=", "");
					if (!chat.frdlist.containsKey(hisuidString)) {
						online(hisuidString,hisip, hisname, "find");
						returnmes(hisip);
					}else {
						rename(hisuidString,hisip, hisname);
					}
				}else if (mes.startsWith("offl=")) {
					
					hisip=chat.frdlist.get(hisuidString);
					hisname=chat.frdname.get(hisip);
					frdshow=hisname+"---"+hisip.getHostAddress();
					chat.friendlsit.remove(frdshow);
					chat.frdlist.remove(hisuidString);
					chat.frdname.remove(hisip);
					chat.zhuangtailan.setText(frdshow+"������");
				}
			}catch (SocketException e1){
				broadSocket.close();
				break;
			}catch (IOException  e) {
				if (chat.trayIcon != null) {
					chat.trayIcon.displayMessage("�̳߳���", "�㲥��Ϣ�����̳߳��ִ����뽫����Ŀ¼�µġ�������־.log���ļ���������", TrayIcon.MessageType.ERROR);
				}
				chat.catchexception(e);
			}
			
		}
	}
	
	public void online(String hisuidString,InetAddress hisip,String hisname,String type){
		chat.frdlist.put(hisuidString, hisip);
		chat.frdname.put(hisip, hisname);
		String frdshow=hisname+"---"+hisip.getHostAddress();
		chat.friendlsit.add(frdshow);
		switch (type) {
		case "find":
			chat.zhuangtailan.setText("�����º��ѣ�"+frdshow);
			break;
		case "online":
			chat.zhuangtailan.setText(frdshow+"������");

		default:
			break;
		}
		
	}
	
	public void returnmes(InetAddress hisip){
		byte[] rtnmesString=("rtrn="+chat.username).getBytes();
		byte[] rtnmes=new byte[rtnmesString.length+12];
		System.arraycopy(myuid, 0, rtnmes, 0, 8);
		System.arraycopy(chat.localIPbytes, 0, rtnmes, 8, 4);
		System.arraycopy(rtnmesString, 0, rtnmes, 12, rtnmesString.length);
		DatagramPacket rtn=new DatagramPacket(rtnmes,rtnmes.length,hisip,chat.muticastport);
		try {
			DatagramSocket sender=new DatagramSocket();
			sender.send(rtn);
			sender.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			if (chat.trayIcon != null) {
				chat.trayIcon.displayMessage("Ӧ����Ϣ����ʧ��", "�����º��ѣ�������Ӧ����Ϣ���ͳ��ִ��󣬿�������Է������绷�������˱仯", TrayIcon.MessageType.ERROR);
			}
			chat.catchexception(e);
		}
	}
	
	public void rename(String hisuidString,InetAddress hisip,String hisname){
		InetAddress hisodip=chat.frdlist.get(hisuidString);
		String hisodnm=chat.frdname.get(hisodip);
		chat.frdname.put(hisip, hisname);
		chat.frdlist.put(hisuidString, hisip);
		String olditem=hisodnm+"---"+hisodip.getHostAddress();
		if (chat.private_chat_map.containsKey(hisip)) {
			chat.private_chat_map.get(hisip).w.setTitle(hisname+" --- "+hisip.getHostAddress()+"˽�� - CareChat");
		}
		for (int i = 0; i < chat.friendlsit.getItemCount(); i++) {
			if (chat.friendlsit.getItem(i).equals(olditem)) {
				chat.friendlsit.replaceItem(hisname+"---"+hisip.getHostAddress(), i);
				return;
			}
		}
		chat.friendlsit.add(hisname+"---"+hisip.getHostAddress());
	}
}

