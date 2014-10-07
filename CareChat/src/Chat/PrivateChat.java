package Chat;

import java.awt.BorderLayout;
import java.awt.Font;
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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class PrivateChat {
	Font uifont=chat.uifont;
	String hisname=null;
	InetAddress hisip=null;
	JLabel zhuangtailan=new JLabel(" ");
	JTextArea his=new JTextArea();
	File historyFile=null;
	JFrame w=null;
	int port=chat.private_chat_port;

	public PrivateChat(InetAddress hisip) {
		chat.private_chat_map.put(hisip, this);
		hisname=chat.frdname.get(hisip);
		this.hisip=hisip;
		historyFile=new File(hisname+" - 私聊历史记录.txt");
		initui();
	}
	
	void initui(){
//		TODO 初始化界面
		w=new JFrame(hisname+" --- "+hisip.getHostAddress()+"私聊 - CareChat");
		w.setLayout(new BorderLayout());
		
//		消息区域开始
		JScrollPane his_area=new JScrollPane(his);
		his_area.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		his_area.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
		his.setEditable(false);
		his.setFont(uifont);
		his.setLineWrap(true);
		his.setWrapStyleWord(true);
//		消息区域结束
		
		JPanel underJPanel=new JPanel(new BorderLayout());
		JPanel sendJPanel=new JPanel(new BorderLayout());
		zhuangtailan.setFont(uifont);
		
//		发送消息栏开始
		JTextArea mesbox=new JTextArea(2,2);
		mesbox.setFont(uifont);
		JButton sendButton=new JButton("发送 (Enter)");
		sendButton.setFont(uifont);
		
		sendJPanel.add(mesbox,"Center");
		sendJPanel.add(sendButton,"East");
//		发送消息栏结束
		
		
		underJPanel.add(sendJPanel,"Center");
		underJPanel.add(zhuangtailan,"South");
		
		w.add(his_area,"Center");
		w.add(underJPanel,"South");
		
		w.setSize(400, 500);
		w.setLocationRelativeTo(null);
		w.setVisible(true);
		
		w.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {}
			
			@Override
			public void windowIconified(WindowEvent e) {}
			
			@Override
			public void windowDeiconified(WindowEvent e) {}
			
			@Override
			public void windowDeactivated(WindowEvent e) {}
			
			@Override
			public void windowClosing(WindowEvent e) {
				chat.private_chat_map.remove(hisip);
				w.dispose();
				w=null;
			}
			
			@Override
			public void windowClosed(WindowEvent e) {}
			
			@Override
			public void windowActivated(WindowEvent e) {}
		});
		
		mesbox.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyReleased(KeyEvent e) {}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					e.consume();
					byte[] mes=mesbox.getText().getBytes();
					if (mes.length == 0) {
						zhuangtailan.setText("发送的消息不能为空");
					}else {
						if (send(mes)) {
							mesbox.setText("");
							showmes(new String(mes), chat.username);
						}
					}
				}
			}
		});
		sendButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				byte[] mes=mesbox.getText().getBytes();
				if (mes.length == 0) {
					zhuangtailan.setText("发送的消息不能为空");
				}else {
					if (send(mes)) {
						mesbox.setText("");
						showmes(new String(mes), chat.username);
					}
				}
			}
		});
//		右键菜单开始
//		TODO 右键菜单
		JPopupMenu menu_his=new JPopupMenu();
		JMenuItem copyItem_his=new JMenuItem("复制");
		JMenuItem hideItem_his=new JMenuItem("隐藏界面");
		JMenuItem group_chatItem_his=new JMenuItem("群组聊天");
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
		group_chatItem_his.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				chat.w.setVisible(true);
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
		menu_his.add(group_chatItem_his);
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
		JMenuItem group_chatItem_mes=new JMenuItem("群组聊天");
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
		group_chatItem_mes.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				chat.w.setVisible(true);
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
		menu_mes.add(group_chatItem_mes);
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
//		TODO 右键结束
//		右键菜单结束
	}
	
	public void showmes(String mes,String user){
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
			chat.catchexception(e);
			if (chat.trayIcon != null) {
				chat.trayIcon.displayMessage("私聊历史记录写入失败", "与"+hisname+"的私聊历史记录写入失败，现在您的聊天记录无法保存。请检查您对程序目录或历史记录文件是否具有写入权限", TrayIcon.MessageType.WARNING);
			}
			zhuangtailan.setText("写入聊天历史记录文件失败，请检查程序对文件"+historyFile+"是否具有写入权限");
		}
	}
	
	boolean send(byte[] mes){
		for (int i = 0; i < mes.length; i+=1024) {
			if (mes.length-i > 1024) {
				DatagramPacket dp = new DatagramPacket(Arrays.copyOfRange(mes, i, i+1024), 1024, hisip, port);
				try {
					DatagramSocket ds = new DatagramSocket();
					ds.send(dp);
					ds.close();
					zhuangtailan.setText("已发送");
				} catch (Exception e) {
					if (chat.trayIcon != null) {
						chat.trayIcon.displayMessage(null, "消息发送失败", TrayIcon.MessageType.ERROR);
					}
					zhuangtailan.setText("消息发送失败");
					chat.catchexception(e);
					return false;
				}
			}else {
				DatagramPacket dp = new DatagramPacket(mes, mes.length, hisip, port);
				try {
					DatagramSocket ds = new DatagramSocket();
					ds.send(dp);
					ds.close();
					zhuangtailan.setText("已发送");
				} catch (Exception e) {
					if (chat.trayIcon != null) {
						chat.trayIcon.displayMessage(null, "消息发送失败", TrayIcon.MessageType.ERROR);
					}
					zhuangtailan.setText("消息发送失败");
					chat.catchexception(e);
					return false;
				}
			}
		}
		return true;
	}
}
