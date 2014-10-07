package Chat;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.List;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
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
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import javax.swing.BorderFactory;
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
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JWindow;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

public class chat {

	/**
	 * @param args
	 */
//	TODO ����ȫ��
	public	static JFrame w=new JFrame();
	public	static JTextArea mesbox=new JTextArea(3,2);
	public	static JTextArea his=new JTextArea();
	public	static String username="���û�";
	public	static List friendlsit=new List(10, false);
	public	static JLabel zhuangtailan=new TypeJlabel();
	public	static HashMap<String, InetAddress> frdlist=new HashMap<>();
	public	static HashMap<InetAddress, String> frdname=new HashMap<>();
	public static HashMap<InetAddress, PrivateChat> private_chat_map=new HashMap<>();
	public static volatile HashMap<String, Long> frd_online_time=new HashMap<>();
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
	public	static int muticastport=45450;
	public static int group_chat_port=45451;
	public static int private_chat_port=45452;
	public	static String localIP="";
	public	static byte[] localIPbytes=new byte[4];
	public	static ListenSysMes lstnsysmes=new ListenSysMes();
	public static AnnounceOLThread an_ol_Thread=null;
	public	static Vector<String> nets=getNetwork();
	public	static int nets_use_item=0;
	public	static String read_netinterface="";
	public	static boolean nic_is_same=false;
	public static TrayIcon trayIcon=null;
	public static PopupMenu tray_popupMenu;
	public static byte[] uid= null;
	public static String uidString=null;
	public static double divider=0.75;
	public static JLabel titile=new JLabel(w.getTitle());
	public static Color backgroundColor=new Color(0,40,90,200);
	public static Color nullColor=new Color(0,0,0,0);
	public static Color panelColor=new Color(2,70,143,150);
	public static Color ui_textColor=new Color(0,140,255);
	static Point origin = new Point();
//	TODO ȫ�ֽ���
	
	/**
	 * ������Ҫһ����JPanel���������е�JPanelȫװ���棬
	 * Ȼ�������JPanel��Χ�ĸ����Ϊ�ա��߿���ɫΪbackgroundcolor���߿���Ϊ1��JPanel��
	 * ���ĸ�JPanel��������¼��������������ڴ�С
	 * 
	 * �������������������������л���һ������3D�ķ������
	 * 
	 * �����ļ���д��δ���
	 * 
	 * ������֧���в�����
	 * 
	 * ϵͳ���û���Ҫ�����ɫ���ã�������ʵʱԤ��
	 */

	
	public static void formload(JWindow load){
		try {
			username=InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e3) {
			catchexception(e3);
		}
		w.setSize(Width, Height);
		w.setLocation(LocalX, LocalY);
		
		
//		TODO ��������ʽ������
		w.setLayout(new BorderLayout(0,10));
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e2) {
			catchexception(e2);
		}
		w.setUndecorated(true);
		w.setBackground(backgroundColor);
		
		JPanel headJPanel=new JPanel(new BorderLayout());
		JPanel w_main=new JPanel(new BorderLayout());
		JPanel send=new JPanel(new BorderLayout());
		JPanel sendlan=new JPanel(new BorderLayout());
		headJPanel.setBackground(nullColor);
		w_main.setBackground(nullColor);
		send.setBackground(nullColor);
		sendlan.setBackground(nullColor);
		
		//����ʽ������  ����
		
//		ͷ����ʼ
		titile.setFont(new Font("΢���ź�", Font.BOLD, 17));
		titile.setText("  ");
		titile.setForeground(ui_textColor);
		JPanel buttJPanel=new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
		buttJPanel.setBackground(nullColor);
		try {
			JLabel settingJLabel=new JLabel(new ImageIcon(chat.class.getResource("/images/setting.png")));
			JLabel min=new JLabel(new ImageIcon(chat.class.getResource("/images/min.png")));
			JLabel max=new JLabel(new ImageIcon(chat.class.getResource("/images/max.png")));
			JLabel close=new JLabel(new ImageIcon(chat.class.getResource("/images/close.png")));
			buttJPanel.add(settingJLabel);
			buttJPanel.add(min);
			buttJPanel.add(max);
			buttJPanel.add(close);
			close.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {
					
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
					
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					close.setBackground(null);
					close.setBackground(null);
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					close.setOpaque(true);
					close.setBackground(new Color(255, 0, 0));
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					System.out.println("����");
				}
			});
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "��Դ�ļ����ƻ������������ػ򿽱�������");
			System.exit(0);
		}
		headJPanel.add(titile,"West");
		headJPanel.add(buttJPanel,"East");
		headJPanel.setBorder(new LineBorder(nullColor, 10));
//		ͷ������
		
//		������Ϣ���ְ�ť����Ϣ����ʽ
		JButton sendtext=new JButton("���� (Enter)");
		sendtext.setFont(uifont);
		sendtext.setBackground(nullColor);
		sendtext.setForeground(ui_textColor);
		sendtext.setBorder(BorderFactory.createLineBorder(ui_textColor, 1));
		
		mesbox.setFont(uifont);
		mesbox.setLineWrap(true);
		mesbox.setWrapStyleWord(true);
		mesbox.setBackground(panelColor);
		mesbox.setForeground(ui_textColor);
		mesbox.setBorder(BorderFactory.createLineBorder(ui_textColor, 1));
//		����
		                                                               

		JPanel frlist=new JPanel(new BorderLayout());
		frlist.setBackground(nullColor);
		JLabel frlist_tips=new JLabel("�����б�");
		frlist_tips.setForeground(ui_textColor);
		frlist_tips.setFont(uifont);
		frlist.setFont(uifont);
		friendlsit.setBackground(panelColor);
		friendlsit.setForeground(ui_textColor);
		frlist.setBorder(new LineBorder(ui_textColor, 1));
		
		JScrollPane his_area=new JScrollPane(his);
		his_area.setBackground(nullColor);
		his_area.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		his_area.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
		his.setEditable(false);
		his.setFont(uifont);
		his.setLineWrap(true);
		his.setWrapStyleWord(true);
		his.setBackground(panelColor);
		his.setForeground(ui_textColor);
		his.setBorder(new LineBorder(ui_textColor, 1));
		
		JScrollPane send_area=new JScrollPane(mesbox);
		send_area.setBackground(nullColor);
		send_area.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		send_area.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
		sendtext.setPreferredSize(new Dimension(123, 20));
		
		zhuangtailan.setForeground(ui_textColor);
		zhuangtailan.setText("  ");
		zhuangtailan.setFont(uifont);
		
		
		send.add(send_area,"Center");
		send.add(sendtext,"East");
		
		
		sendlan.add(send,"North");
		sendlan.add(zhuangtailan,"South");
		
		frlist.add(frlist_tips,"North");
		frlist.add(friendlsit,"Center");
		
		JSplitPane topJSplitPane=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, his_area, frlist);
		topJSplitPane.setDividerSize(1);
		topJSplitPane.setBackground(nullColor);
		topJSplitPane.setBorder(new LineBorder(ui_textColor, 1));
		w_main.add(topJSplitPane,"Center");
		w_main.add(sendlan,"South");
		
		w.add(headJPanel, "North");
		w.add(w_main,"Center");
		
//		�����Ӿ���ʼ
		
		
		
//		�����Ӿ�����
		
		updatetitile();
		load.setVisible(false);
		w.setVisible(true);
		load.dispose();
		load=null;
		
		topJSplitPane.setDividerLocation(0.75);
		w.setVisible(false);
		w.setVisible(true);
		
		
		topJSplitPane.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getX()==topJSplitPane.getDividerLocation()) {
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {
				divider=(double)topJSplitPane.getDividerLocation()/(double)topJSplitPane.getWidth();
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
		
//		TODO ����¼����ı䴰��λ�����С
		headJPanel.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {
				origin.x = e.getX();
				origin.y = e.getY();
			}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
		headJPanel.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				Point p = w.getLocation();
				w.setLocation(p.x + e.getX() - origin.x, p.y + e.getY()- origin.y);
			}
		});
		
		
//		TODO �Ҽ��˵�
		JPopupMenu menu_his=new JPopupMenu();
		JMenuItem copyItem_his=new JMenuItem("����");
		JMenuItem hideItem_his=new JMenuItem("���ؽ���");
		JMenuItem changetointernetItem_his=new JMenuItem("��������");
		JMenuItem configItem_his=new JMenuItem("ϵͳ����");
		
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
				JOptionPane.showMessageDialog(null, "ûд��");
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
		JMenuItem copyItem_mes=new JMenuItem("����");
		JMenuItem cutItem_mes=new JMenuItem("����");
		JMenuItem pasteItem_mes=new JMenuItem("ճ��");
		JMenuItem hideItem_mes=new JMenuItem("���ؽ���");
		JMenuItem changetointernetItem_mes=new JMenuItem("��������");
		JMenuItem configItem_mes=new JMenuItem("ϵͳ����");
		
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
				JOptionPane.showMessageDialog(null, "��ûд��");
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
		
		JPopupMenu menu_frdlist=new JPopupMenu();
		JMenuItem private_chatItem_frdlist=new JMenuItem("����˽��");
		JMenuItem hideItem_frdlist=new JMenuItem("���ؽ���");
		JMenuItem changetointernetItem_frdlist=new JMenuItem("��������");
		JMenuItem configItem_frdlist=new JMenuItem("ϵͳ����");
		
		private_chatItem_frdlist.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String[] ip_temp=friendlsit.getSelectedItem().split("---");
					InetAddress hisip=InetAddress.getByName(ip_temp[ip_temp.length-1]);
					if (private_chat_map.containsKey(hisip)) {
						private_chat_map.get(hisip).w.setVisible(true);
					}else {
						new PrivateChat(hisip);
					}
				} catch (UnknownHostException e1) {
					catchexception(e1);
				}
			}
		});
		hideItem_frdlist.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				w.setVisible(false);
			}
		});
		changetointernetItem_frdlist.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "��ûд��");
			}
		});
		configItem_frdlist.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				config.showon();
			}
		});
		
		menu_frdlist.add(private_chatItem_frdlist);
		menu_frdlist.add(hideItem_frdlist);
		menu_frdlist.add(changetointernetItem_frdlist);
		menu_frdlist.add(configItem_frdlist);
		
		friendlsit.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == 3) {
					if (friendlsit.getSelectedIndex() == -1) {
						private_chatItem_frdlist.setEnabled(false);
					}else {
						private_chatItem_frdlist.setEnabled(true);
					}
					menu_frdlist.show(friendlsit, e.getX(), e.getY());
				}
			}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					if (friendlsit.getSelectedIndex() != -1){
						try {
							String[] ip_temp=friendlsit.getSelectedItem().split("---");
							InetAddress hisip=InetAddress.getByName(ip_temp[ip_temp.length-1]);
							if (private_chat_map.containsKey(hisip)) {
								private_chat_map.get(hisip).w.setVisible(true);
							}else {
								new PrivateChat(hisip);
							}
						} catch (UnknownHostException e1) {
							catchexception(e1);
						}
					}
				}
			}
		});
//		�Ҽ��˵�����
		
		
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
				// TODO ���ڹر�
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
		w.addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent e) {}
			
			@Override
			public void componentResized(ComponentEvent e) {
				topJSplitPane.setDividerLocation(divider);
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {}
			
			@Override
			public void componentHidden(ComponentEvent e) {}
		});
//		�����¼�����
		
		
//		������Ϣ��ť�¼���ʼ
		sendtext.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
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
			new SendThread(mes).start();
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
			username=name;
			updatetitile();
		} catch ( IOException e) {
			catchexception(e);
		}

	}
	
//	TODO ��ȡ�����б�
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
	
	public static void getuid(){
		byte[] uid_temp=new byte[8];
		(new Random()).nextBytes(uid_temp);
		StringBuilder sb=new StringBuilder(32);
		for (byte b : uid_temp) {
			String b_temp=String.valueOf(b);
			if (b_temp.contains("-")) {
				b_temp=b_temp.replaceAll("-", "");
				sb.append("-");
			}
			for (int i = 3-b_temp.length(); i > 0; i--) {
				sb.append(0);
			}
			sb.append(b_temp);
		}
		uid=uid_temp;
		uidString=sb.toString();
		System.out.println("�Լ�"+uidString);
	}
	
	
	public static boolean  checkip(String interfacename) {
		NetworkInterface nowiInterface;
		String nowip;
		try {
			if ((nowiInterface=(NetworkInterface.getByName(interfacename)))!=null && nowiInterface.isUp()&& nowiInterface.getInterfaceAddresses().size() != 0 && !(nowip=nowiInterface.getInterfaceAddresses().get(0).toString().split("/")[1]).startsWith("169") && !nowip.contains(":")) {
//				while (lstnsysmes.isAlive()) {
//					lstnsysmes.broadSocket.close();
//				}
				lstnsysmes=new ListenSysMes();
//				lstnsysmes.broadSocket.setNetworkInterface(nowiInterface);
				if (an_ol_Thread != null && an_ol_Thread.isAlive()) {
					an_ol_Thread.runable=false;
				}
				an_ol_Thread=new AnnounceOLThread(nowiInterface);
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
		String ti=username+" - "+localIP+" - CareChat";
		w.setTitle(ti);
		titile.setText(ti);
		titile.setToolTipText(ti);
		if (trayIcon != null) {
			trayIcon.setToolTip(ti);
		}
	}
	
	public static void showTray(){
//		TODO ��ʾ����ͼ��
		SystemTray tray = SystemTray.getSystemTray();
		Image image=null;
		try {
			image=(new ImageIcon(chat.class.getResource("/images/mao_XL.png"))).getImage();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "��Դ�ļ����ƻ������������ػ򿽱�������");
		}
		tray_popupMenu = new PopupMenu();
		MenuItem show = new MenuItem("��ʾ����");
		show.setFont(uifont);
		MenuItem change = new MenuItem("��������");
		MenuItem setting = new MenuItem("ϵͳ����");
		MenuItem exit = new MenuItem("�˳�����");
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
				JOptionPane.showMessageDialog(null, "��ûд��");
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
//		trayIcon.displayMessage("��Ŀ���������ⳤ������������������", "\r\n\r\n\r\n                                 �ڴ˴��Ҽ��и�������\r\n\r\n\r\n", TrayIcon.MessageType.INFO);
	}
	
//	TODO �˳�
	public static void exit(){
		try {
			byte[] offlnmesString=("offl=").getBytes();
			byte[] offlnmes=new byte[offlnmesString.length+12];
			System.arraycopy(uid, 0, offlnmes, 0, 8);
			System.arraycopy(localIPbytes, 0, offlnmes, 8, 4);
			System.arraycopy(offlnmesString, 0, offlnmes, 12, offlnmesString.length);
			DatagramPacket pack=new DatagramPacket(offlnmes, offlnmes.length, InetAddress.getByName(muticast), muticastport);
			MulticastSocket sender=new MulticastSocket();
			sender.setNetworkInterface(NetworkInterface.getByName(nets.get(nets_use_item).split("\\| ")[1]));
			sender.setTimeToLive(255);
			sender.send(pack);
			sender.close();
		} catch (IOException e) {
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
			return;
		} catch (IOException e) {
			zhuangtailan.setText("�޷���ȡ�����ļ�");
			catchexception(e);
		}
	}
	
	
	
	// TODO ץȡ�쳣��������־
	public static void catchexception(Exception e){
		e.printStackTrace();
		File errlog=new File("������־.log");
		if (!errlog.exists()) {
			try {
				errlog.createNewFile();
			} catch (IOException e1) {
				if (chat.trayIcon != null) {
					chat.trayIcon.displayMessage("����������־���ִ���", "����������־���ִ����������Ե�ǰĿ¼�Ƿ���д��Ȩ��", TrayIcon.MessageType.ERROR);
				}
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
			if (trayIcon != null) {
				trayIcon.displayMessage("д�������־���ִ���", "д�������־���ִ������顰������־.log���ļ��Ƿ�����д��", TrayIcon.MessageType.ERROR);
			}
			zhuangtailan.setText("������־д��ʧ�ܣ�");
		}
		
	}
	
	public static void mainly(JWindow load) {
		// TODO main
		readconfig();
		getuid();
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
		an_ol_Thread.start();
		new receiveThread().start();
		new Private_Chat_Receive().start();
		new CutFriendList().start();
	}

}
