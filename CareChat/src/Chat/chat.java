package Chat;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Label;
import java.awt.List;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
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
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.UIManager;

public class chat {

	/**
	 * @param args
	 */
//	TODO ����ȫ��
	public	static JFrame w=new JFrame();
	public	static JTextArea mesbox=new JTextArea(2,2);
	public	static JTextArea his=new JTextArea();
	public	static String username="���û�";
	public	static List friendlsit=new List(10, true);  
	public	static JLabel zhuangtailan=new JLabel(" ");
	public	static HashMap<InetAddress, String> frdlist=new HashMap<>();
	public	static HashMap<InetAddress, Long> frdol=new HashMap<>();
	public	static int LocalX=Toolkit.getDefaultToolkit().getScreenSize().width/2-225;
	public	static int LocalY=Toolkit.getDefaultToolkit().getScreenSize().height/2-250;
	public	static int Width=450;
	public	static int Height=500;
	public	static File historyFile=new File("�����������¼.txt");
	public	static File internetHistory=new File("���������¼.txt");
	public	static 	File frdFile=new File("�����б�.lst");
	public	static boolean AskOnClose=true;
	public	static String serverip="8.8.8.8";
	public	static int serverport=7000;
	public	static JButton changetointernet=new JButton("��������");
	public	static final Font uifont=new Font("΢���ź�",Font.PLAIN,13);
	public	static String muticast="235.0.0.1";
	public	static int muticastport=40100;
	public	static String localIP="";
	public	static byte[] localIPbytes=new byte[4];
	public	static ListenSysMes lstnsysmes=new ListenSysMes();
	public	static Vector<String> nets=getNetwork();
	public	static int nets_use_item=0;
	public	static String read_netinterface="";
	public	static boolean nic_is_same=false;
	

	
	public static void formload(JWindow load){
		w.setSize(Width, Height);
		w.setLocation(LocalX, LocalY);
		
		
//		��������ʽ������
		w.setLayout(new BorderLayout());
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			catchexception(e2);
		} 
		//����ʽ������  ����
		
		JPanel info=new JPanel();
		JPanel wangka=new JPanel();
		JPanel info_lan=new JPanel(new BorderLayout());
		JPanel send=new JPanel(new BorderLayout());
		JPanel sendlan=new JPanel(new BorderLayout());
		
//		�ǳ����ò��ֿ�ʼ
		JLabel nictips=new JLabel("�����ǳ� ", Label.RIGHT);
		nictips.setFont(uifont);
		try {
			username=InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			catchexception(e1);
		}
		final JTextField nicnametx=new JTextField(username,15);
		nicnametx.setFont(uifont);
		
		JButton nicyes=new JButton("ȷ�ϸ���");
		nicyes.setFont(uifont);
//		�ǳ����ò��ֽ���
		
//		�������ÿ�ʼ
		JLabel wangka_tip=new JLabel("����ѡ��");
		wangka_tip.setFont(uifont);
		final JComboBox<String> wangkalist=new JComboBox<>(nets);
		wangkalist.setSelectedIndex(nets_use_item);
		wangkalist.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO �л�����
				
				lstnsysmes=new ListenSysMes();
				if (!checkip(wangkalist.getSelectedItem().toString().split("\\| ")[1])) {
					JOptionPane.showMessageDialog(null, "����"+wangkalist.getSelectedItem().toString()+" δ���������IP���ò���ȷ��");
					wangkalist.setSelectedIndex(nets_use_item);
					return;
					
				}
				w.setTitle(username+" - ����IP��"+localIP+" - CareChat");
				nets_use_item=wangkalist.getSelectedIndex();
				lstnsysmes.start();
			}
		});
//		�������ý���
		
		
//		������Ϣ���ְ�ť����Ϣ����ʽ
		JButton sendtext=new JButton("���� (Enter)");
		sendtext.setFont(uifont);
		mesbox.setFont(uifont);
		mesbox.setLineWrap(true);
		mesbox.setWrapStyleWord(true);
//		����
		                                                               

		JPanel frlist=new JPanel(new BorderLayout());
		JLabel frlist_tips=new JLabel("�����б�");
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
		
		info.add(nictips);
		info.add(nicnametx);
		info.add(nicyes);
		
		wangka.add(wangka_tip);
		wangka.add(wangkalist);
		
		send.add(mesbox,"Center");
		send.add(sendtext,"East");
		
		info_lan.add(info,"North");
		info_lan.add(wangka,"South");
		
		sendlan.add(send,"North");
		sendlan.add(zhuangtailan,"South");
		
		frlist.add(frlist_tips,"North");
		frlist.add(friendlsit,"Center");
		
		w.add(info_lan,"North");
		w.add(his_area,"Center");
		w.add(sendlan,"South");
		w.add(frlist,"East");
		
		
		w.setTitle(username + " - ����IP��" + localIP +" - CareChat");
		load.setVisible(false);
		w.setVisible(true);
		load.dispose();
		load=null;
		
//		������ɫ
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
		
//		������ɫ����
		
//		�����¼���ʼ
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
				// TODO Auto-generated method stub
				saveconfig();
				zhuangtailan.setText("��ǰϵͳ�����ѱ���");
				if (AskOnClose) {
					Object[] options = { "�˳�", "����" };
					JOptionPane pane2 = new JOptionPane("ȷ��Ҫ�˳���?", JOptionPane.QUESTION_MESSAGE,JOptionPane.YES_NO_OPTION, null, options, options[1]);
					JDialog dialog = pane2.createDialog(w,"�˳�ȷ��");
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
//		�����¼�����
		
//		�����ǳƿ��¼���ʼ
		nicnametx.addKeyListener(new KeyListener() {
		
		@Override
		public void keyTyped(KeyEvent e) {}
		
		@Override
		public void keyReleased(KeyEvent e) {}
		
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode()==10) {
				e.consume();
				changename(nicnametx.getText());
			}
		}
	});
//		�����ǳƿ��¼�����
		
//		�����ǳư�ť�¼���ʼ
		nicyes.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				changename(nicnametx.getText());
			}
		});
//		�����ǳư�ť�¼�����
		
		
		
		
//		������Ϣ��ť�¼���ʼ
		sendtext.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub                
				send(mesbox.getText().getBytes());                
			}                                                     
		});  
//		������Ϣ��ť�¼�����
		
//		������Ϣ���¼���ʼ
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
//		������Ϣ���¼�����	
		
		
//		�л������������찴ť�¼���ʼ
		changetointernet.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				InternetChat.show();
//				JOptionPane.showMessageDialog(null,"�ⲿ�ֻ�ûд��");
			}
		});
		
//		�л������������찴ť�¼�����
		
	}
	
	
	public static void send(byte[] mes){
			
			if (mes.length == 0) {
				zhuangtailan.setText("���͵���Ϣ����Ϊ��");
				return;
			}
			new Thread(new SendThread(mes)).start();
			showmes(mesbox.getText(), username);
			mesbox.setText("");
			zhuangtailan.setText("�ѷ���");
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
			// TODO Auto-generated catch block
			catchexception(e);
			JOptionPane.showMessageDialog(null,"д��������ʷ��¼�ļ�ʧ�ܣ����������ļ�"+historyFile+"�Ƿ����д��Ȩ��");
			zhuangtailan.setText("д��������ʷ��¼�ļ�ʧ�ܣ����������ļ�"+historyFile+"�Ƿ����д��Ȩ��");
		}
		
	}
	
	public static void changename(String name){
		if (name.getBytes().length > 30) {
			JOptionPane.showMessageDialog(null, "�ǳƲ��ܳ���30���ַ���15�����֣�");
			zhuangtailan.setText("�ǳƲ��ܳ���30���ַ���15�����֣�");
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
			w.setTitle(name+" - "+localIP +" - CareChat");
			username=name;
		} catch ( IOException e) {
			// TODO Auto-generated catch block
			catchexception(e);
		}

	}
	
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
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	    return networks;
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
			// TODO Auto-generated catch block
			catchexception(e);
		}
		return false;
	}
	
	
	public static void showTray(){
		SystemTray tray = SystemTray.getSystemTray();
		Image image=null;
		try {
			image=(new ImageIcon(chat.class.getResource("/images/mao_XL.png"))).getImage();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		PopupMenu popupMenu = new PopupMenu();
		MenuItem show = new MenuItem("��ʾ����");
		MenuItem change = new MenuItem("��������");
		MenuItem setting = new MenuItem("ϵͳ����");
		MenuItem exit = new MenuItem("�˳�����");
		popupMenu.add(show);
		popupMenu.add(change);
		popupMenu.add(setting);
		popupMenu.add(exit);
		popupMenu.setFont(uifont);
		
		show.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (!w.isActive()) {
					w.setVisible(true);
				}
			}
		});
		change.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JOptionPane.showMessageDialog(null, "��ûд��");
			}
		});
		setting.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				config.showon();
			}
		});
		exit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				saveconfig();
				zhuangtailan.setText("��ǰϵͳ�����ѱ���");
				if (AskOnClose) {
					Object[] options = { "�˳�", "����" };
					JOptionPane pane2 = new JOptionPane("ȷ��Ҫ�˳���?", JOptionPane.QUESTION_MESSAGE,JOptionPane.YES_NO_OPTION, null, options, options[1]);
					JDialog dialog = pane2.createDialog(w,"�˳�ȷ��");
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
		
		TrayIcon trayIcon=new TrayIcon(image, w.getTitle(), popupMenu);
		trayIcon.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if (!w.isActive()) {
					w.setVisible(true);
				}
			}
		});
		trayIcon.setImageAutoSize(true);
		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		trayIcon.displayMessage("��Ŀ���������ⳤ������������������", "\r\n\r\n\r\n                                 �ڴ˴��Ҽ��и�������\r\n\r\n\r\n", TrayIcon.MessageType.INFO);
	}
	
	
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
			// TODO Auto-generated catch block
			catchexception(e);
		}
	}
	
	
	
	public static void saveconfig(){
		File confFile=new File("�����ļ�.ini");
		try {
			zhuangtailan.setText("���ڱ��������ļ�����");
			if (!confFile.exists()) {
				confFile.createNewFile();
			}
			BufferedWriter bw=new BufferedWriter(new FileWriter(confFile));
			bw.write("����λ��X="+(int)w.getLocationOnScreen().getX()+"\r\n");
			bw.write("����λ��Y="+(int)w.getLocationOnScreen().getY()+"\r\n");
			bw.write("���ڿ��="+w.getWidth()+"\r\n");
			bw.write("���ڸ߶�="+w.getHeight()+"\r\n");
			bw.write("�����¼="+historyFile.getPath()+"\r\n");
			bw.write("�û���="+username+"\r\n");
			bw.write("�˳�ȷ��="+AskOnClose+"\r\n");
			bw.write("����ѡ��="+lstnsysmes.broadSocket.getNetworkInterface().getDisplayName()+" | "+lstnsysmes.broadSocket.getNetworkInterface().getName()+"\r\n");
			
			bw.close();
			zhuangtailan.setText("�����ļ��������");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			catchexception(e);
		}
		
		
	}
	
	
	public static void readconfig(){
		File confFile=new File("�����ļ�.ini");
		try {
			BufferedReader br=new BufferedReader(new FileReader(confFile));
			String br_temp;
			while (!((br_temp = br.readLine()) == null)) {
				int temp;
				switch (br_temp.split("=")[0]) {
				case "����λ��X":
					LocalX=Integer.valueOf(br_temp.split("=")[1]);
					break;
				case "����λ��Y":
					LocalY=Integer.valueOf(br_temp.split("=")[1]);
					break;
				case "���ڿ��":
					if ((temp=Integer.valueOf(br_temp.split("=")[1])) > 0) {
						Width=temp;
					}
					break;
				case "���ڸ߶�":
					if ((temp=Integer.valueOf(br_temp.split("=")[1])) > 0) {
						Height=temp;
					}
					break;
				case "�����¼":
					historyFile=new File(br_temp.split("=")[1]);
					if (!historyFile.exists()) {
						historyFile.createNewFile();
					}
					break;
				case "�û���":
					String[] usernamearr=br_temp.split("=");
					StringBuilder usrnamesb=new StringBuilder();
					for (int i = 1; i < usernamearr.length; i++) {
						usrnamesb.append(usernamearr[i]);
					}
					username=usrnamesb.toString();
					break;
				case "�˳�ȷ��":
					AskOnClose=Boolean.valueOf(br_temp.split("=")[1]);
					break;
				case "����ѡ��":
					read_netinterface=br_temp.split("\\|")[1].trim();
					

				default:
					break;
				}
			}
			br.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			zhuangtailan.setText("�޷���ȡ�����ļ�");
			catchexception(e);
		}
	}
	
	
	
	public static void catchexception(Exception e){
		e.printStackTrace();
		File errlog=new File("������־.log");
		if (!errlog.exists()) {
			try {
				errlog.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				zhuangtailan.setText("������־����ʧ�ܣ�");
			}
		}
		try {
			BufferedWriter bw=new BufferedWriter(new FileWriter(errlog,true));
			ByteArrayOutputStream b=new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(b));
			bw.write(new SimpleDateFormat("yyyy��MM��dd��  HH:mm:ss\r\n").format(new Date())+b.toString()+"\r\n");
			bw.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			zhuangtailan.setText("������־д��ʧ�ܣ�");
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
		lstnsysmes.start();
		new Thread(new receiveThread()).start();
		new Thread(new AnnounceOLThread()).start();
		if (SystemTray.isSupported()) {
			showTray();
		}
	}

}
