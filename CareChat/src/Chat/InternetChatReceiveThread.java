package Chat;

import java.io.DataInputStream;
import java.io.IOException;

public class InternetChatReceiveThread extends Thread {
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			if (!InternetChat.connected) {
				continue;
			}
			try {
				String data="";
				if ((data=(new DataInputStream(InternetChat.clientSocket.getInputStream())).readUTF()) != null) {
					if (data.startsWith("mes=")) {
						InternetChat.showmes(data.replaceFirst("mes=", ""));
					}else if (data.startsWith("cgnm=")) {
						String[] names=data.replaceFirst("cgnm=", "").split("|");
						InternetChat.cgnm(names[1], names[2]);
					}else if (data.startsWith("frdlst=")) {
						InternetChat.putfrdlst(data.replaceFirst("frdlst=", ""));
					}else if (data.startsWith("ol=")) {
						InternetChat.putfrdlst(data.replaceFirst("ol=", ""));
					}else if (data.startsWith("offl=")) {
						InternetChat.removefrdlst(data.replaceFirst("offl=", ""));
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				chat.catchexception(e);
			}
		}
	}

}
