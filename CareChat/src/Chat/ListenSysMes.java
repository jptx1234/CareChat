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
	
	public ListenSysMes(){
		try {
			this.broadSocket=new MulticastSocket(chat.muticastport);
			this.broadSocket.joinGroup(InetAddress.getByName(chat.muticast));
		} catch (IOException e) {
			// TODO 构建
			if (chat.trayIcon != null) {
				chat.trayIcon.displayMessage("CareChat初始化错误", "广播信息接收线程创建失败，您可能无法获取好友列表。请将程序目录下的“错误日志.log”文件发给作者", TrayIcon.MessageType.ERROR);
			}
			chat.catchexception(e);
			run=false;
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (!run) {
			return;
		}
		DatagramPacket received;
		String mes;
		String hisname;
		InetAddress hisip;
		String frdshow;
		byte[] hisuid;
		for (byte b : myuid) {
			System.out.println(b);
		}
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
				hisip=InetAddress.getByAddress(Arrays.copyOfRange(received.getData(), 8, 12));
				mes=new String(received.getData(),12,received.getLength()-12);
				System.out.println(mes);
				if (Arrays.equals(hisuid, myuid)) {
					continue;
				}
				if (mes.startsWith("find=")) {
					hisname=mes.replaceAll("find=", "");
					if (chat.frdlist.containsKey(hisuid)) {
						rename(hisuid,hisip, hisname);
						returnmes(hisip);
					}else {
						online(hisuid,hisip, hisname, "online");
						returnmes(hisip);
					}
				}else if (mes.startsWith("rtrn=")) {
					hisname=mes.replaceAll("rtrn=", "");
					if (chat.frdlist.containsKey(hisuid)) {
						rename(hisuid, hisip, hisname);
					}else {
						online(hisuid,hisip, hisname, "find");
					}
				}else if (mes.startsWith("renm=")) {
					hisname=mes.replaceAll("renm=", "");
					if (!chat.frdlist.containsKey(hisip)) {
						online(hisuid,hisip, hisname, "find");
						returnmes(hisip);
					}else {
						rename(hisuid,hisip, hisname);
					}
				}else if (mes.startsWith("offl=")) {
					hisip=chat.frdlist.get(hisuid);
					hisname=chat.frdname.get(hisip);
					frdshow=hisname+"---"+hisip.toString().split("/")[1];
					chat.friendlsit.remove(frdshow);
					chat.frdlist.remove(hisuid);
					chat.frdname.remove(hisip);
					chat.zhuangtailan.setText(frdshow+"下线了");
				}
			}catch (SocketException e1){
				broadSocket.close();
				break;
			}catch (IOException  e) {
				if (chat.trayIcon != null) {
					chat.trayIcon.displayMessage("线程出错", "广播信息接收线程出现错误，请将程序目录下的“错误日志.log”文件发给作者", TrayIcon.MessageType.ERROR);
				}
				chat.catchexception(e);
			}
			
		}
	}
	
	public void online(byte[] hisuid,InetAddress hisip,String hisname,String type){
		chat.frdlist.put(hisuid, hisip);
		chat.frdname.put(hisip, hisname);
		String frdshow=hisname+"---"+hisip.toString().split("/")[1];
		chat.friendlsit.add(frdshow);
		switch (type) {
		case "find":
			chat.zhuangtailan.setText("发现新好友："+frdshow);
			break;
		case "online":
			chat.zhuangtailan.setText(frdshow+"上线了");

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
				chat.trayIcon.displayMessage("应答信息发送失败", "发现新好友，但网络应答信息发送出现错误，可能您或对方的网络环境发生了变化", TrayIcon.MessageType.ERROR);
			}
			chat.catchexception(e);
		}
	}
	
	public void rename(byte[] hisuid,InetAddress hisip,String hisname){
		InetAddress hisodip=chat.frdlist.get(hisuid);
		String hisodnm=chat.frdname.get(hisodip);
		chat.frdname.put(hisip, hisname);
		chat.frdlist.put(hisuid, hisip);
		String olditem=hisodnm+"---"+hisodip.toString().split("/")[1];
		for (int i = 0; i < chat.friendlsit.getItemCount(); i++) {
			if (chat.friendlsit.getItem(i) == olditem) {
				chat.friendlsit.replaceItem(hisname+"---"+hisip.toString().split("/")[1], i);
				return;
			}
		}
		chat.friendlsit.add(hisname+"---"+hisip.toString().split("/")[1]);
	}
	
	
}

