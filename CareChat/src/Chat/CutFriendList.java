package Chat;

import java.net.InetAddress;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class CutFriendList extends Thread {
	@Override
	public void run() {
		while (true) {
			try {
			Iterator<Entry<String, Long>> iter=chat.frd_online_time.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String,Long> entry = iter.next();
				if ((System.currentTimeMillis() - ((Long) entry.getValue()).longValue() ) > 25000) {
					String hisuidString=entry.getKey();
					InetAddress hisip=chat.frdlist.get(hisuidString);
					String frdshow=chat.frdname.get(hisip)+"---"+hisip.getHostAddress();
					chat.friendlsit.remove(frdshow);
					chat.frdlist.remove(hisuidString);
					chat.frdname.remove(hisip);
					chat.frd_online_time.remove(hisuidString);
					chat.zhuangtailan.setText("与"+frdshow+"失去联系");
				}
			}
			} catch (NullPointerException e1){
			}
			try {
				sleep(15000);
			} catch (InterruptedException e) {
				chat.catchexception(e);
			}
		}
	}
}
