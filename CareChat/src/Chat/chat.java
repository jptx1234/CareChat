package Chat;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.List;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JWindow;
import javax.swing.UIManager;

public class chat {

	/**
	 * @param args
	 */
//	TODO 声明全局
	public	static JFrame w=new JFrame();
	public	static JTextArea mesbox=new JTextArea(2,2);
	public	static JTextArea his=new JTextArea();
	public	static String username="新用户";
	public	static List friendlsit=new List(10, true);  
	public	static JLabel zhuangtailan=new JLabel(" ");
	public	static HashMap<byte[], InetAddress> frdlist=new HashMap<>();
	public	static HashMap<InetAddress, String> frdname=new HashMap<>();
	public	static int LocalX=Toolkit.getDefaultToolkit().getScreenSize().width/2-225;
	public	static int LocalY=Toolkit.getDefaultToolkit().getScreenSize().height/2-250;
	public	static int Width=450;
	public	static int Height=500;
	public	static File historyFile=new File("局域网聊天记录.txt");
	public	static File internetHistory=new File("互联聊天记录.txt");
	public	static 	File frdFile=new File("好友列表.lst");
	public	static boolean AskOnClose=true;
	public	static String serverip="8.8.8.8";
	public	static int serverport=7000;
	public	static JButton changetointernet=new JButton("公网聊天");
	public	static final Font uifont=new Font("微软雅黑",Font.PLAIN,13);
	public	static String muticast="235.0.0.1";
	public	static int muticastport=40100;
	public	static String localIP="";
	public	static byte[] localIPbytes=new byte[4];
	public	static ListenSysMes lstnsysmes=new ListenSysMes();
	public	static Vector<String> nets=getNetwork();
	public	static int nets_use_item=0;
	public	static String read_netinterface="";
	public	static boolean nic_is_same=false;
	public static TrayIcon trayIcon=null;
	public static PopupMenu tray_popupMenu;
	public static byte[] uid= getuid();
//	TODO 全局结束
	

	
	public static void formload(JWindow load){
		w.setSize(Width, Height);
		w.setLocation(LocalX, LocalY);
		
		
//		TODO 设置总样式、布局
		w.setLayout(new BorderLayout());
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e2) {
			catchexception(e2);
		} 
		//总样式、布局  结束
		
		JPanel send=new JPanel(new BorderLayout());
		JPanel sendlan=new JPanel(new BorderLayout());
		
//		发送消息部分按钮、消息框样式
		JButton sendtext=new JButton("发送 (Enter)");
		sendtext.setFont(uifont);
		mesbox.setFont(uifont);
		mesbox.setLineWrap(true);
		mesbox.setWrapStyleWord(true);
//		结束
		                                                               

		JPanel frlist=new JPanel(new BorderLayout());
		JLabel frlist_tips=new JLabel("好友列表");
		frlist_tips.setFont(uifont);
		frlist.setFont(uifont);
		
		JScrollPane his_area=new JScrollPane(his);
		his_area.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		his_area.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
		his.setEditable(false);
		his.setFont(uifont);
		his.setLineWrap(true);
		his.setWrapStyleWord(true);
		sendtext.setPreferredSize(new Dimension(123, 20));
		
		zhuangtailan.setFont(uifont);
		
		
		send.add(mesbox,"Center");
		send.add(sendtext,"East");
		
		
		sendlan.add(send,"North");
		sendlan.add(zhuangtailan,"South");
		
		frlist.add(frlist_tips,"North");
		frlist.add(friendlsit,"Center");
		
		w.add(his_area,"Center");
		w.add(sendlan,"South");
		w.add(frlist,"East");
		
		
		updatetitile();
		load.setVisible(false);
		w.setVisible(true);
		load.dispose();
		load=null;
		
//		设置颜色
//		mesbox.setBackground(new Color(76, 194, 255));
//		his.setBackground(new Color(46, 184, 255));
//		LineBorder border=new LineBorder(Color.BLUE, 3, true);
//		mesbox.setBorder(border);
//		his.setBorder(border);
//		mesbox.addFocusListener(new FocusListener() {
//			
//			@Override
//			public void focusLost(FocusEvent e) {
//				mesbox.setBorder(border);
//			}
//			
//			@Override
//			public void focusGained(FocusEvent e) {
//				mesbox.setBorder(new LineBorder(Color.CYAN, 2,true));
//			}
//		});
		
//		设置颜色结束
		
//		右键菜单开始
		JPopupMenu menu_his=new JPopupMenu();
		JMenuItem copyItem_his=new JMenuItem("复制");
		JMenuItem hideItem_his=new JMenuItem("隐藏界面");
		JMenuItem changetointernetItem_his=new JMenuItem("公网聊天");
		JMenuItem configItem_his=new JMenuItem("系统设置");
		
		copyItem_his.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				his.copy();
			}
		});
		hideItem_his.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				w.setVisible(false);
			}
		});
		changetointernetItem_his.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "没写完");
			}
		});
		configItem_his.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				config.showon();
			}
		});
		
		menu_his.add(copyItem_his);
		menu_his.add(hideItem_his);
		menu_his.add(changetointernetItem_his);
		menu_his.add(configItem_his);
		
		his.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == 3) {
					if (his.getSelectedText() == null) {
						copyItem_his.setEnabled(false);
					}else {
						copyItem_his.setEnabled(true);
					}
					menu_his.show(his, e.getX(), e.getY());
				}
			}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
		
		JPopupMenu menu_mes=new JPopupMenu();
		JMenuItem copyItem_mes=new JMenuItem("复制");
		JMenuItem cutItem_mes=new JMenuItem("剪切");
		JMenuItem pasteItem_mes=new JMenuItem("粘贴");
		JMenuItem hideItem_mes=new JMenuItem("隐藏界面");
		JMenuItem changetointernetItem_mes=new JMenuItem("公网聊天");
		JMenuItem configItem_mes=new JMenuItem("系统设置");
		
		copyItem_mes.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mesbox.copy();
			}
		});
		cutItem_mes.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mesbox.cut();
			}
		});
		pasteItem_mes.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mesbox.paste();
			}
		});
		hideItem_mes.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				w.setVisible(false);
			}
		});
		changetointernetItem_mes.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "还没写完");
			}
		});
		configItem_mes.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				config.showon();
			}
		});
		
		menu_mes.add(copyItem_mes);
		menu_mes.add(cutItem_mes);
		menu_mes.add(pasteItem_mes);
		menu_mes.add(hideItem_mes);
		menu_mes.add(changetointernetItem_mes);
		menu_mes.add(configItem_mes);
		
		mesbox.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == 3) {
					if (mesbox.getSelectedText() == null) {
						copyItem_mes.setEnabled(false);
						cutItem_mes.setEnabled(false);
					}else {
						copyItem_mes.setEnabled(true);
						cutItem_mes.setEnabled(true);
					}
					Transferable contents=(Toolkit.getDefaultToolkit().getSystemClipboard()).getContents(mesbox);
					if(contents==null || !contents.isDataFlavorSupported(DataFlavor.stringFlavor)){
						pasteItem_mes.setEnabled(false);
					}else {
						pasteItem_mes.setEnabled(true);
					}
					menu_mes.show(mesbox, e.getX(), e.getY());
				}
			}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
//		右键菜单结束
		
		
//		窗口事件开始
		w.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {	}
			
			@Override
			public void windowIconified(WindowEvent e) {
				w.setVisible(false);
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {}
			
			@Override
			public void windowDeactivated(WindowEvent e) {}
			
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO 窗口关闭
				saveconfig();
				zhuangtailan.setText("当前系统设置已保存");
				if (AskOnClose) {
					Object[] options = { "退出", "返回" };
					JOptionPane pane2 = new JOptionPane("确定要退出吗?", JOptionPane.QUESTION_MESSAGE,JOptionPane.YES_NO_OPTION, null, options, options[1]);
					JDialog dialog = pane2.createDialog(w,"退出确认");
					dialog.setVisible(true);
					Object selectedValue = pane2.getValue();
					if (selectedValue == null || selectedValue == options[1]) {
						w.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
					} else if (selectedValue == options[0]) {
						exit();
						w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					}
				}else {
					exit();
					w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				}
				
			}
			@Override
			public void windowClosed(WindowEvent e) {}
			
			@Override
			public void windowActivated(WindowEvent e) {}
		});
//		窗口事件结束
		
		
//		发送消息按钮事件开始
		sendtext.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				send(mesbox.getText().getBytes());                
			}                                                     
		});  
//		发送消息按钮事件结束
		
//		输入消息框事件开始
		mesbox.addKeyListener(new KeyListener() {                      
            
			@Override                                                  
			public void keyTyped(KeyEvent e) {}                                                          
			                                                           
			@Override                                                  
			public void keyReleased(KeyEvent e) {}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					e.consume();
					send(mesbox.getText().getBytes());
				}
			}
		});
//		输入消息框事件结束	
		
		
//		切换到互联网聊天按钮事件开始
		changetointernet.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				InternetChat.show();
//				JOptionPane.showMessageDialog(null,"这部分还没写完");
			}
		});
		
//		切换到互联网聊天按钮事件结束
		
	}
	
	
	public static void send(byte[] mes){
			
			if (mes.length == 0) {
				zhuangtailan.setText("发送的消息不能为空");
				return;
			}
			new Thread(new SendThread(mes)).start();
			showmes(mesbox.getText(), username);
			mesbox.setText("");
			zhuangtailan.setText("已发送");
	}
	
	
	public static void showmes(String mes,String user){
		
		String message=user + "   " + new SimpleDateFormat("HH:mm:ss").format(new Date()) +  "\r\n" + mes + "\r\n";
		his.append(message);
		his.setCaretPosition(his.getText().length());
		try {
			if (!historyFile.exists()) {
				historyFile.createNewFile();
			}
			BufferedWriter bw=new BufferedWriter(new FileWriter(historyFile,true));
			bw.write(message);
			bw.close();
		} catch (IOException e) {
			catchexception(e);
			JOptionPane.showMessageDialog(null,"写入聊天历史记录文件失败，请检查程序对文件"+historyFile+"是否具有写入权限");
			zhuangtailan.setText("写入聊天历史记录文件失败，请检查程序对文件"+historyFile+"是否具有写入权限");
		}
		
	}
	
	public static void changename(String name){
		if (name.getBytes().length > 30) {
			JOptionPane.showMessageDialog(null, "昵称不能超过30个字符（15个汉字）");
			zhuangtailan.setText("昵称不能超过30个字符（15个汉字）");
			return;
		}
		try {
			byte[] renmmesString=("renm="+name).getBytes();
			byte[] renmmes=new byte[renmmesString.length+4];
			System.arraycopy(localIPbytes, 0, renmmes, 0, 4);
			System.arraycopy(renmmesString, 0, renmmes, 4, renmmesString.length);
			DatagramPacket pack=new DatagramPacket(renmmes, renmmes.length, InetAddress.getByName(muticast), chat.muticastport);
			DatagramSocket sender=new DatagramSocket();
			sender.send(pack);
			sender.close();
			updatetitile();
			username=name;
		} catch ( IOException e) {
			catchexception(e);
		}

	}
	
//	TODO 获取网卡列表
	public static Vector<String> getNetwork(){
		Vector<String> networks=new Vector<>();
		Enumeration<NetworkInterface> en;
		try {
			en = NetworkInterface.getNetworkInterfaces();
			while (en.hasMoreElements()) {
				NetworkInterface ni = en.nextElement();
				if ((ni.getHardwareAddress() == null)||(ni.getHardwareAddress().length != 6)||!(ni.supportsMulticast())) {
					continue;
				}
				networks.add(ni.getDisplayName()+" | "+ni.getName());
			}
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		
	    return networks;
	}
	
	public static byte[] getuid(){
		byte[] uid=new byte[8];
		(new Random()).nextBytes(uid);
		return uid;
	}
	
	
	public static boolean  checkip(String interfacename) {
		NetworkInterface nowiInterface;
		String nowip;
		try {
			if ((nowiInterface=(NetworkInterface.getByName(interfacename)))!=null && nowiInterface.isUp()&& nowiInterface.getInterfaceAddresses().size() != 0 && !(nowip=nowiInterface.getInterfaceAddresses().get(0).toString().split("/")[1]).startsWith("169") && !nowip.contains(":")) {
				while (lstnsysmes.isAlive()) {
					lstnsysmes.broadSocket.close();
				}
				lstnsysmes=new ListenSysMes();
				lstnsysmes.broadSocket.setNetworkInterface(nowiInterface);
				localIP=nowip;
				localIPbytes=InetAddress.getByName(nowip).getAddress();
				return true;
			}
		} catch (SocketException | UnknownHostException e) {
			catchexception(e);
		}
		return false;
	}
	
	public static void updatetitile(){
		w.setTitle(username+" - 本机IP："+localIP+" - CareChat");
	}
	
	public static void showTray(){
//		TODO 显示托盘图标
		SystemTray tray = SystemTray.getSystemTray();
		Image image=null;
		try {
			image=(new ImageIcon(chat.class.getResource("/images/mao_XL.png"))).getImage();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "资源文件被破坏，请重新下载或拷贝本程序！");
		}
		tray_popupMenu = new PopupMenu();
		MenuItem show = new MenuItem("显示界面");
		show.setFont(uifont);
		MenuItem change = new MenuItem("公网聊天");
		MenuItem setting = new MenuItem("系统设置");
		MenuItem exit = new MenuItem("退出程序");
		tray_popupMenu.add(show);
		tray_popupMenu.add(change);
		tray_popupMenu.add(setting);
		tray_popupMenu.add(exit);
		tray_popupMenu.setFont(uifont);
		
		show.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!w.isActive()) {
					w.setVisible(true);
				}
			}
		});
		change.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "还没写完");
			}
		});
		setting.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				config.showon();
			}
		});
		exit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				saveconfig();
				zhuangtailan.setText("当前系统设置已保存");
				if (AskOnClose) {
					Object[] options = { "退出", "返回" };
					JOptionPane pane2 = new JOptionPane("确定要退出吗?", JOptionPane.QUESTION_MESSAGE,JOptionPane.YES_NO_OPTION, null, options, options[1]);
					JDialog dialog = pane2.createDialog(w,"退出确认");
					dialog.setVisible(true);
					Object selectedValue = pane2.getValue();
					if (selectedValue == null || selectedValue == options[1]) {
						return;
					} else if (selectedValue == options[0]) {
						exit();
						System.exit(0);
					}
				}else {
					exit();
					System.exit(0);
				}
			}
		});
		
		trayIcon=new TrayIcon(image, w.getTitle(), tray_popupMenu);
		trayIcon.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				w.setVisible(true);
			}
		});
		trayIcon.setImageAutoSize(true);
		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			e.printStackTrace();
		}
//		trayIcon.displayMessage("醒目！！！标题长长长长长长长长……", "\r\n\r\n\r\n                                 在此处右键有更多内容\r\n\r\n\r\n", TrayIcon.MessageType.INFO);
	}
	
//	TODO 退出
	public static void exit(){
		try {
			byte[] offlnmesString=("offl=").getBytes();
			byte[] offlnmes=new byte[offlnmesString.length+4];
			System.arraycopy(localIPbytes, 0, offlnmes, 0, 4);
			System.arraycopy(offlnmesString, 0, offlnmes, 4, offlnmesString.length);
			DatagramPacket pack=new DatagramPacket(offlnmes, offlnmes.length, InetAddress.getByName(muticast), chat.muticastport);
			DatagramSocket sender=new DatagramSocket();
			sender.send(pack);
			sender.close();
		} catch (IOException e) {
			catchexception(e);
		}
	}
	
	
	
	public static void saveconfig(){
		File confFile=new File("配置文件.ini");
		try {
			zhuangtailan.setText("正在保存配置文件……");
			if (!confFile.exists()) {
				confFile.createNewFile();
			}
			BufferedWriter bw=new BufferedWriter(new FileWriter(confFile));
			bw.write("窗口位置X="+(int)w.getLocationOnScreen().getX()+"\r\n");
			bw.write("窗口位置Y="+(int)w.getLocationOnScreen().getY()+"\r\n");
			bw.write("窗口宽度="+w.getWidth()+"\r\n");
			bw.write("窗口高度="+w.getHeight()+"\r\n");
			bw.write("聊天记录="+historyFile.getPath()+"\r\n");
			bw.write("用户名="+username+"\r\n");
			bw.write("退出确认="+AskOnClose+"\r\n");
			bw.write("网卡选择="+lstnsysmes.broadSocket.getNetworkInterface().getDisplayName()+" | "+lstnsysmes.broadSocket.getNetworkInterface().getName()+"\r\n");
			
			bw.close();
			zhuangtailan.setText("配置文件保存完毕");
		} catch (IOException e) {
			catchexception(e);
		}
		
		
	}
	
	
	public static void readconfig(){
		File confFile=new File("配置文件.ini");
		try {
			BufferedReader br=new BufferedReader(new FileReader(confFile));
			String br_temp;
			while (!((br_temp = br.readLine()) == null)) {
				int temp;
				switch (br_temp.split("=")[0]) {
				case "窗口位置X":
					LocalX=Integer.valueOf(br_temp.split("=")[1]);
					break;
				case "窗口位置Y":
					LocalY=Integer.valueOf(br_temp.split("=")[1]);
					break;
				case "窗口宽度":
					if ((temp=Integer.valueOf(br_temp.split("=")[1])) > 0) {
						Width=temp;
					}
					break;
				case "窗口高度":
					if ((temp=Integer.valueOf(br_temp.split("=")[1])) > 0) {
						Height=temp;
					}
					break;
				case "聊天记录":
					historyFile=new File(br_temp.split("=")[1]);
					if (!historyFile.exists()) {
						historyFile.createNewFile();
					}
					break;
				case "用户名":
					String[] usernamearr=br_temp.split("=");
					StringBuilder usrnamesb=new StringBuilder();
					for (int i = 1; i < usernamearr.length; i++) {
						usrnamesb.append(usernamearr[i]);
					}
					username=usrnamesb.toString();
					break;
				case "退出确认":
					AskOnClose=Boolean.valueOf(br_temp.split("=")[1]);
					break;
				case "网卡选择":
					read_netinterface=br_temp.split("\\|")[1].trim();
					

				default:
					break;
				}
			}
			br.close();
			
		} catch (FileNotFoundException e) {
			return;
		} catch (IOException e) {
			zhuangtailan.setText("无法读取配置文件");
			catchexception(e);
		}
	}
	
	
	
	// TODO 抓取异常，创建日志
	public static void catchexception(Exception e){
		e.printStackTrace();
		File errlog=new File("错误日志.log");
		if (!errlog.exists()) {
			try {
				errlog.createNewFile();
			} catch (IOException e1) {
				if (chat.trayIcon != null) {
					chat.trayIcon.displayMessage("创建错误日志出现错误", "创建错误日志出现错误，请检查程序对当前目录是否有写入权限", TrayIcon.MessageType.ERROR);
				}
				zhuangtailan.setText("错误日志创建失败！");
			}
		}
		try {
			BufferedWriter bw=new BufferedWriter(new FileWriter(errlog,true));
			ByteArrayOutputStream b=new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(b));
			bw.write(new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss\r\n").format(new Date())+b.toString()+"\r\n");
			bw.close();
		} catch (IOException e1) {
			if (trayIcon != null) {
				trayIcon.displayMessage("写入错误日志出现错误", "写入错误日志出现错误，请检查“错误日志.log”文件是否允许写入", TrayIcon.MessageType.ERROR);
			}
			zhuangtailan.setText("错误日志写入失败！");
		}
		
	}
	
	public static void mainly(JWindow load) {
		// TODO main
		readconfig();
		if (checkip(read_netinterface)) {
			for (int i = 0; i < nets.size(); i++) {
				if (nets.get(i).endsWith(read_netinterface)) {
					nets_use_item=i;
				}
			}
		}else {
			for (int i=0;i<nets.size();i++) {
				if (checkip(nets.get(i).split("\\| ")[1])) {
					nets_use_item=i;
					break;
				}
			}
			
		}
		
		formload(load);
		if (SystemTray.isSupported()) {
			showTray();
		}
		lstnsysmes.start();
		new Thread(new receiveThread()).start();
		new Thread(new AnnounceOLThread()).start();
	}

}
