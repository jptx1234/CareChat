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
	static JFrame w=new JFrame("�����л�������������ģʽ����");
	static JTextArea mesbox=new JTextArea(2,2);
	static JTextArea his=new JTextArea();
	static JLabel zhuangtailan=new JLabel(" ");
	static List friendlsit=new List(10, true);  
	static boolean isload=false;
	static JButton changetolan=new JButton("��������");
	static Socket clientSocket=new Socket();
	static boolean connected=false;
	static String username=chat.username;
	static InetAddress serverip;



	public static void initui() {
//		��������ʽ������
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
		//����ʽ�����塢����  ����
		
		JPanel send=new JPanel(new BorderLayout());
		
//		�ǳ����ò��ֿ�ʼ
		JPanel info_nic=new JPanel();
		JLabel nictips=new JLabel("�����ǳ� ", Label.RIGHT);
		nictips.setFont(chat.uifont);
		final JTextField nicnametx=new JTextField(username,15);
		nicnametx.setFont(chat.uifont);
		
		JButton nicyes=new JButton("ȷ�ϸ���");
		nicyes.setFont(chat.uifont);
//		�ǳ����ò��ֽ���
		
//		������Ϣ���ְ�ť����Ϣ����ʽ]
		JPanel send_lan=new JPanel(new BorderLayout());
		JButton sendtext=new JButton("���� (Enter)");
		sendtext.setFont(chat.uifont);
		mesbox.setFont(chat.uifont);
		mesbox.setLineWrap(true);
		mesbox.setWrapStyleWord(true);
//		����
		
//		�����б�ʼ
		JPanel frlist=new JPanel(new BorderLayout());
		JLabel frlist_tips=new JLabel("�����б�");
		frlist_tips.setFont(chat.uifont);
		frlist.setFont(chat.uifont);
//		�����б����
		
//		������Ϣ����ʼ
		JScrollPane his_area=new JScrollPane(his);
		his_area.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		his_area.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
		his.setEditable(false);
		his.setFont(chat.uifont);
		his.setLineWrap(true);
		his.setWrapStyleWord(true);
		sendtext.setPreferredSize(new Dimension(123, 20));
//		������Ϣ�������
		
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
		
		sendtext.setEnabled(false); //���������������ɺ��ٸ�true
		
		w.setVisible(true);
		
		isload=true;
		
//		�������찴ť�¼�
		changetolan.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				chat.w.setBounds(w.getBounds());
				w.setVisible(false);
				chat.w.setVisible(true);
			}
		});
//		�������찴ť�¼�����
		
//		��ʼ��������������Ӳ��ѷ��Ͱ�ť����
		initsocket();
		if (connected) {
			sendtext.setEnabled(true);
			changename(nicnametx.getText());
		}
//		����
		
//		������Ϣ���¼�
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
//		������Ϣ���¼�����
		
//		������Ϣ��ť�¼�
		sendtext.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				send();
			}
		});
//		������Ϣ��ť����
		
//		�����ǳƿ��¼���ʼ
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
//		�����ǳƿ��¼�����
		
//		�����ǳư�ť�¼�
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
//		�����ǳư�ť�¼�����
	} 
	
	public static void  initsocket() {
		try {
			zhuangtailan.setText("�������ӷ���������");
			
			clientSocket=new Socket(chat.serverip,8090);
			
			connected=true;
			zhuangtailan.setText("�����ӷ�����");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			chat.catchexception(e);
			Object[] options = { "���� ", "����" };
			JOptionPane aksreconnect = new JOptionPane("�����������ʧ�ܣ�������", JOptionPane.QUESTION_MESSAGE,JOptionPane.YES_NO_OPTION, null, options, options[0]);
			JDialog dialog = aksreconnect.createDialog(w,"����ʧ��");
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
				zhuangtailan.setText("����ʧ�ܣ������ԣ�");
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
			JOptionPane.showMessageDialog(null,"д��������ʷ��¼�ļ�ʧ�ܣ����������ļ�"+chat.internetHistory+"�Ƿ����д��Ȩ��");
			zhuangtailan.setText("д��������ʷ��¼�ļ�ʧ�ܣ����������ļ�"+chat.internetHistory+"�Ƿ����д��Ȩ��");
		}
		
	}
	
	public static boolean changename(String newnicname){
		if (newnicname.contains("|")) {
			JOptionPane.showMessageDialog(null,"�ǳ��в��ܺ��С�|���ַ�");
			zhuangtailan.setText("�ǳ��в��ܺ��С�|���ַ�");
			return false;
		}
		try {
			new DataOutputStream(clientSocket.getOutputStream()).writeUTF("usrname="+newnicname);
			username=newnicname;
			zhuangtailan.setText("�ǳƸ��ĳɹ�");
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			chat.catchexception(e);
			zhuangtailan.setText("�ǳƸ���ʧ�ܣ������ԣ�");
			Object[] options = { "����", "����" };
			JOptionPane aksreconnect = new JOptionPane("�ǳƸ���ʧ�ܣ�������\r\n��������ʧ�ܣ��볢���������ӷ�������", JOptionPane.QUESTION_MESSAGE,JOptionPane.YES_NO_OPTION, null, options, options[0]);
			JDialog dialog = aksreconnect.createDialog(w,"�ǳƸ���ʧ��");
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
