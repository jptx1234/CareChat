package Chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

public class ListenSysMes extends Thread {
	public volatile MulticastSocket broadSocket;
	private boolean run=true;
	
	public ListenSysMes(){
		try {
			this.broadSocket=new MulticastSocket(chat.muticastport);
			this.broadSocket.joinGroup(InetAddress.getByName(chat.muticast));
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
		InetAddress localip = null;
		try {
			broadSocket.setSoTimeout(0);
			localip=InetAddress.getByName(chat.localIP);
		} catch (SocketException | UnknownHostException e1) {
			// TODO Auto-generated catch block
			chat.catchexception(e1);
		}
		while (true) {
			received=new DatagramPacket(new byte[35], 35);
			try {
				broadSocket.receive(received);
				hisip=InetAddress.getByAddress(Arrays.copyOfRange(received.getData(), 0, 4));
				mes=new String(received.getData(),4,received.getLength()-4);
				System.out.println(mes);
				if (hisip.equals(localip)) {
					continue;
				}
				if (mes.startsWith("find=")) {
					hisname=mes.replaceAll("find=", "");
					if (chat.frdlist.containsKey(hisip)) {
						rename(hisip, hisname);
						returnmes(hisip);
					}else {
						online(hisip, hisname, "online");
						returnmes(hisip);
					}
				}else if (mes.startsWith("rtrn=")) {
					hisname=mes.replaceAll("rtrn=", "");
					if (chat.frdlist.containsKey(hisip)) {
						rename(hisip, hisname);
					}else {
						online(hisip, hisname, "find");
					}
				}else if (mes.startsWith("renm=")) {
					hisname=mes.replaceAll("renm=", "");
					if (!chat.frdlist.containsKey(hisip)) {
						online(hisip, hisname, "find");
						returnmes(hisip);
					}else {
						rename(hisip, hisname);
					}
				}else if (mes.startsWith("offl=")) {
					hisname=chat.frdlist.get(hisip);
					frdshow=hisname+"---"+hisip.toString().split("/")[1];
					chat.friendlsit.remove(frdshow);
					chat.frdlist.remove(hisip);
					chat.zhuangtailan.setText(frdshow+"下线了");
				}
			}catch (SocketException e1){
				broadSocket.close();
				break;
			}catch (IOException  e) {
				// TODO Auto-generated catch block
				chat.catchexception(e);
			}
			
		}
	}
	
	public void online(InetAddress hisip,String hisname,String type){
		chat.frdlist.put(hisip, hisname);
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
		byte[] rtnmes=new byte[rtnmesString.length+4];
		System.arraycopy(chat.localIPbytes, 0, rtnmes, 0, 4);
		System.arraycopy(rtnmesString, 0, rtnmes, 4, rtnmesString.length);
		DatagramPacket rtn=new DatagramPacket(rtnmes,rtnmes.length,hisip,chat.muticastport);
		try {
			DatagramSocket sender=new DatagramSocket();
			sender.send(rtn);
			sender.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			chat.catchexception(e);
		}
	}
	
	public void rename(InetAddress hisip,String hisname){
		String hisodnm=chat.frdlist.get(hisip);
		for (int i = 0; i < chat.friendlsit.getItemCount(); i++) {
			String olditem=hisodnm+"---"+hisip.toString().split("/")[1];
			if (chat.friendlsit.getItem(i).endsWith((olditem))) {
				chat.friendlsit.replaceItem(hisname+"---"+hisip.toString().split("/")[1], i);
				break;
			}
		}
		chat.frdlist.put(hisip, hisname);
	}
	
	
}

