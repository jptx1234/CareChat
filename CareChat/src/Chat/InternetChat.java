package Chat;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class InternetChat {
	static JFrame w=new JFrame("正在切换到互联网聊天模式……");
	static JTextArea mesbox=new JTextArea(2,2);
	static JTextArea his=new JTextArea();
	static JLabel zhuangtailan=new JLabel(" ");
	static List friendlsit=new List(10, true);  
	static boolean isload=false;
	static JButton changetolan=new JButton("内网聊天");
	static Socket clientSocket=new Socket();
	static boolean connected=false;
	static String username=chat.username;
	static InetAddress serverip;



	public static void initui() {
//		设置总样式、布局
		w.setBounds(chat.w.getBounds());
		chat.w.setVisible(false);
		w.setVisible(true);
		w.setLayout(new BorderLayout());
		w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			chat.catchexception(e2);
		} 
		//总样式、字体、布局  结束
		
		JPanel send=new JPanel(new BorderLayout());
		
//		昵称设置部分开始
		JPanel info_nic=new JPanel();
		JLabel nictips=new JLabel("更改昵称 ", Label.RIGHT);
		nictips.setFont(chat.uifont);
		final JTextField nicnametx=new JTextField(username,15);
		nicnametx.setFont(chat.uifont);
		
		JButton nicyes=new JButton("确认更改");
		nicyes.setFont(chat.uifont);
//		昵称设置部分结束
		
//		发送消息部分按钮、消息框样式]
		JPanel send_lan=new JPanel(new BorderLayout());
		JButton sendtext=new JButton("发送 (Enter)");
		sendtext.setFont(chat.uifont);
		mesbox.setFont(chat.uifont);
		mesbox.setLineWrap(true);
		mesbox.setWrapStyleWord(true);
//		结束
		
//		好友列表开始
		JPanel frlist=new JPanel(new BorderLayout());
		JLabel frlist_tips=new JLabel("好友列表");
		frlist_tips.setFont(chat.uifont);
		frlist.setFont(chat.uifont);
//		好友列表结束
		
//		聊天消息区域开始
		JScrollPane his_area=new JScrollPane(his);
		his_area.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		his_area.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
		his.setEditable(false);
		his.setFont(chat.uifont);
		his.setLineWrap(true);
		his.setWrapStyleWord(true);
		sendtext.setPreferredSize(new Dimension(123, 20));
//		聊天消息区域结束
		
		zhuangtailan.setFont(chat.uifont);
		
		changetolan.setFont(chat.uifont);
		changetolan.setUI(chat.changetointernet.getUI());
		
		info_nic.add(changetolan);
		info_nic.add(nictips);
		info_nic.add(nicnametx);
		info_nic.add(nicyes);
		
		send_lan.add(mesbox,"Center");
		send_lan.add(sendtext,"East");
		
		send.add(send_lan, "North");
		send.add(zhuangtailan,"South");
		
		frlist.add(frlist_tips,"North");
		frlist.add(friendlsit,"Center");
		
		w.add(info_nic,"North");
		w.add(send,"South");
		w.add(his_area,"Center");
		w.add(frlist,"East");
		
		sendtext.setEnabled(false); //等与服务器连接完成后再改true
		
		w.setVisible(true);
		
		isload=true;
		
//		内网聊天按钮事件
		changetolan.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				chat.w.setBounds(w.getBounds());
				w.setVisible(false);
				chat.w.setVisible(true);
			}
		});
//		内网聊天按钮事件结束
		
//		初始化与服务器的连接并把发送按钮可用
		initsocket();
		if (connected) {
			sendtext.setEnabled(true);
			changename(nicnametx.getText());
		}
//		结束
		
//		发送消息框事件
		mesbox.addKeyListener(new KeyListener() {                      
            
			@Override                                                  
			public void keyTyped(KeyEvent e) {}                                                          
			                                                           
			@Override                                                  
			public void keyReleased(KeyEvent e) {                      
				// TODO Auto-generated method stub                     
				if (e.getKeyCode() == 10) {
					send();
				}
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {}
		});
//		发送消息框事件结束
		
//		发送消息按钮事件
		sendtext.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				send();
			}
		});
//		发送消息按钮结束
		
//		更改昵称框事件开始
		nicnametx.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				if (e.getKeyCode() == 10) {
					String usrnmtemp=username;
					String newusrname=nicnametx.getText();
					if (changename(newusrname)) {
						nicnametx.setText(newusrname);
					}else {
						nicnametx.setText(usrnmtemp);
					}
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {}
		});
//		更改昵称框事件结束
		
//		更改昵称按钮事件
		nicyes.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String usrnmtemp=username;
				String newusrname=nicnametx.getText();
				if (changename(newusrname)) {
					nicnametx.setText(newusrname);
				}else {
					nicnametx.setText(usrnmtemp);
				}
			}
		});
//		更改昵称按钮事件结束
	} 
	
	public static void  initsocket() {
		try {
			zhuangtailan.setText("正在连接服务器……");
			
			clientSocket=new Socket(chat.serverip,8090);
			
			connected=true;
			zhuangtailan.setText("已连接服务器");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			chat.catchexception(e);
			Object[] options = { "重试 ", "放弃" };
			JOptionPane aksreconnect = new JOptionPane("与服务器连接失败，重试吗？", JOptionPane.QUESTION_MESSAGE,JOptionPane.YES_NO_OPTION, null, options, options[0]);
			JDialog dialog = aksreconnect.createDialog(w,"连接失败");
			dialog.setVisible(true);
			Object selectedValue = aksreconnect.getValue();
			if (selectedValue == null || selectedValue == options[0]) {
				initsocket();
			} else if (selectedValue == options[0]) {
				return;
			}
		}
	}
	
	public static void  send() {
		String mes=mesbox.getText();
		if (mes != null) {
			try {
				new DataOutputStream(clientSocket.getOutputStream()).writeUTF("mes="+mes);
				showmes(username + "   " + new SimpleDateFormat("HH:mm:ss").format(new Date()) +  "\r\n" + mes + "\r\n");
				mesbox.setText("");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				chat.catchexception(e);
				zhuangtailan.setText("发送失败，请重试！");
			}
		}
	}
	
	
	public static void showmes(String mes){
		
//		String message=user + "   " + new SimpleDateFormat("HH:mm:ss").format(new Date()) +  "\r\n" + mes + "\r\n";
		his.append(mes);
		try {
			if (!chat.internetHistory.exists()) {
				chat.internetHistory.createNewFile();
			}
			BufferedWriter bw=new BufferedWriter(new FileWriter(chat.internetHistory,true));
			bw.write(mes);
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			chat.catchexception(e);
			JOptionPane.showMessageDialog(null,"写入聊天历史记录文件失败，请检查程序对文件"+chat.internetHistory+"是否具有写入权限");
			zhuangtailan.setText("写入聊天历史记录文件失败，请检查程序对文件"+chat.internetHistory+"是否具有写入权限");
		}
		
	}
	
	public static boolean changename(String newnicname){
		if (newnicname.contains("|")) {
			JOptionPane.showMessageDialog(null,"昵称中不能含有“|”字符");
			zhuangtailan.setText("昵称中不能含有“|”字符");
			return false;
		}
		try {
			new DataOutputStream(clientSocket.getOutputStream()).writeUTF("usrname="+newnicname);
			username=newnicname;
			zhuangtailan.setText("昵称更改成功");
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			chat.catchexception(e);
			zhuangtailan.setText("昵称更改失败，请重试！");
			Object[] options = { "重试", "放弃" };
			JOptionPane aksreconnect = new JOptionPane("昵称更改失败，重试吗？\r\n（如果多次失败，请尝试重新连接服务器）", JOptionPane.QUESTION_MESSAGE,JOptionPane.YES_NO_OPTION, null, options, options[0]);
			JDialog dialog = aksreconnect.createDialog(w,"昵称更改失败");
			dialog.setVisible(true);
			Object selectedValue = aksreconnect.getValue();
			if (selectedValue == null || selectedValue == options[0]) {
				changename(newnicname);
			} else if (selectedValue == options[0]) {
				return false;
			}
			
		}
		return false;
	}
	
	public static void putfrdlst(String names){
		String[] name=names.split("|");
		for (String string : name) {
			friendlsit.add(string);
		}
	}
	
	public static void cgnm(String oldnm,String newnm){
		boolean changed=false;
		for (int i = 0; i < friendlsit.getItemCount(); i++) {
			if (friendlsit.getItem(i)==oldnm) {
				friendlsit.replaceItem(newnm, i);
				changed=true;
			}
		}
		if (!changed) {
			putfrdlst(newnm);
		}
	}
	
	public static void removefrdlst(String name){
		for (int i = 0; i < friendlsit.getItemCount(); i++) {
			if (friendlsit.getItem(i)==name) {
				friendlsit.remove(i);
			}
		}
		
	}
	
	public static void show(){
		if (isload) {
			w.setBounds(chat.w.getBounds());
			chat.w.setVisible(false);
			w.setVisible(true);
		}else {
			initui();
		}
	}
}
