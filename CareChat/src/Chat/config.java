package Chat;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.NetworkInterface;
import java.net.SocketException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class config extends JDialog {

	private static final long serialVersionUID = 4367525227211353992L;
	public static config thiswindow;
	private static boolean nic_is_same=chat.nic_is_same;
	private JButton yes=new JButton("ȷ��");
	private volatile boolean internameblock=false;
	private volatile boolean localnameblock=false;

	public config(){
		setSize(40, 40);
		setLocationRelativeTo(chat.w);
		setVisible(true);
		setResizable(false);
		setTitle("ϵͳ����");
		setLayout(new FlowLayout());
		Font uifont=chat.uifont;
		
//		ȫ�����ÿ�ʼ
		JPanel common=new JPanel(new GridLayout(0, 1));
		common.setBorder(new TitledBorder(new LineBorder(Color.CYAN, 2),"ȫ������",TitledBorder.DEFAULT_JUSTIFICATION,TitledBorder.DEFAULT_POSITION ,uifont));
		
//		ȫ������-�˳�ȷ��
		JCheckBox ask_on_close_CheckBox=new JCheckBox("�˳�ʱȷ��");
		ask_on_close_CheckBox.setSelected(chat.AskOnClose);
		ask_on_close_CheckBox.setFont(uifont);
//		�˳�ȷ�Ͻ���
		
		common.add(ask_on_close_CheckBox);
//		ȫ�����ý���
		
//		�������������ÿ�ʼ
		JPanel localJPanel=new JPanel();
		localJPanel.setLayout(new BoxLayout(localJPanel, BoxLayout.Y_AXIS));
		localJPanel.setBorder(new TitledBorder(new LineBorder(Color.CYAN,2), "��������������",TitledBorder.DEFAULT_JUSTIFICATION,TitledBorder.DEFAULT_POSITION ,uifont));
		
//		�ǳ����ÿ�ʼ
		JPanel nicJPanel=new JPanel(new FlowLayout(FlowLayout.LEFT,5,-4));
		JLabel nictips=new JLabel("�����ǳ� ", Label.RIGHT);
		nictips.setFont(uifont);
		JTextField nicnametx=new JTextField();
		nicnametx.setFont(uifont);
		nicnametx.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		nicnametx.setBackground(null);
		JCheckBox same_to_inter=new JCheckBox("ͬ���������������ǳ�");
		same_to_inter.setFont(uifont);
		same_to_inter.setSelected(nic_is_same);
		
		nicJPanel.add(nictips);
		nicJPanel.add(nicnametx);
		nicJPanel.add(same_to_inter);
		
		
		JPanel nic_mes=new JPanel(new FlowLayout(FlowLayout.LEFT,5,-1));
		JLabel nic_mes_blank=new JLabel("              ");
		JLabel nic_mes_warn=new JLabel("�ǳƲ��ܳ���30���ַ�(15������)");
		nic_mes_blank.setFont(uifont);
		nic_mes_warn.setFont(uifont);
		nic_mes_warn.setBackground(Color.ORANGE);
		nic_mes_warn.setOpaque(true);
		
		
		nic_mes.add(nic_mes_blank);
		nic_mes.add(nic_mes_warn);
		nic_mes.setVisible(false);
		
		
//		�ǳ����ý���
		
//		�������ÿ�ʼ
		
		JPanel netcard=new JPanel(new FlowLayout(FlowLayout.LEFT,5,15));
		JLabel wangka_tip=new JLabel("����ѡ�� ");
		wangka_tip.setFont(uifont);
		JComboBox<String> wangkalist=new JComboBox<>(chat.nets);
		wangkalist.setFont(uifont);
		wangkalist.setSelectedIndex(chat.nets_use_item);
		netcard.add(wangka_tip);
		netcard.add(wangkalist);
		
		JPanel netcard_mes_JPanel=new JPanel(new FlowLayout(FlowLayout.LEFT,5,-2));
		JLabel net_mes_blank=new JLabel("          ");
		JLabel netcard_mes=new JLabel();
		netcard_mes.setText(getip(wangkalist.getSelectedItem().toString().split("\\| ")[1], netcard_mes));
		netcard_mes.setBackground(Color.ORANGE);
		netcard_mes.setFont(uifont);
		netcard_mes_JPanel.add(net_mes_blank);
		netcard_mes_JPanel.add(netcard_mes);
		
		wangkalist.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				netcard_mes.setText(getip(wangkalist.getSelectedItem().toString().split("\\| ")[1], netcard_mes));
				
			}
		});
//		�������ý���
		
		localJPanel.add(nicJPanel);
		localJPanel.add(nic_mes);
		localJPanel.add(netcard);
		localJPanel.add(netcard_mes_JPanel);
		
		
//		�������������ý���
		
//		�������������ÿ�ʼ
		JPanel internet=new JPanel();
		internet.setLayout(new BoxLayout(internet, BoxLayout.Y_AXIS));
		internet.setBorder(new TitledBorder(new LineBorder(Color.CYAN,2), "��������������",TitledBorder.DEFAULT_JUSTIFICATION,TitledBorder.DEFAULT_POSITION ,uifont));
		
//		�����������ǳƿ�ʼ
		JPanel inter_nic=new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel inter_nic_tips=new JLabel("�����ǳ� ");
		inter_nic_tips.setFont(uifont);
		JTextField inter_nicJTextField=new JTextField();
		inter_nicJTextField.setFont(uifont);
		JCheckBox same_to_lan=new JCheckBox("ͬ���������������ǳ�");
		same_to_lan.setFont(uifont);
		same_to_lan.setSelected(nic_is_same);
		inter_nic.add(inter_nic_tips);
		inter_nic.add(inter_nicJTextField);
		inter_nic.add(same_to_lan);
//		�����������ǳƿ����
		
//		���ķ�����IP��ʼ
		JPanel serverJPanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel server_tips=new JLabel("������IP ");
		server_tips.setFont(uifont);
		JTextField serverJTextField=new JTextField(chat.serverip,17);
		serverJTextField.setFont(uifont);
		JLabel serverport_tips=new JLabel(" �˿�");
		serverport_tips.setFont(uifont);
		JTextField serverportJTextField=new JTextField(String.valueOf(chat.serverport),4);
		serverportJTextField.setFont(uifont);
		serverJPanel.add(server_tips);
		serverJPanel.add(serverJTextField);
		serverJPanel.add(serverport_tips);
		serverJPanel.add(serverportJTextField);
		
		JPanel server_checkJPanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel server_blank=new JLabel("         ");
		server_blank.setFont(uifont);
		JLabel servermes=new JLabel();
		servermes.setFont(uifont);
		servermes.setVisible(false);
		server_checkJPanel.add(server_blank);
		server_checkJPanel.add(servermes);
		server_checkJPanel.setVisible(false);
//		���ķ�����IP����
		
		internet.add(inter_nic);
		internet.add(serverJPanel);
		internet.add(server_checkJPanel);
//		�������������ý���
		
		
//		ȷ��&ȡ����ť��ʼ
		JPanel saveconfigJPanel=new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 10));
		JButton ignore=new JButton("ȡ��");
		yes.setFont(uifont);
		ignore.setFont(uifont);
		saveconfigJPanel.add(yes);
		saveconfigJPanel.add(ignore);
		
//		ȷ��&ȡ����ť����
		
		add(common);
		add(localJPanel);
		add(internet);
		add(saveconfigJPanel);
		setVisible(false);
		pack();
		int wangka_width=0;
		if ((wangka_width=(int) (wangkalist.getBounds().getWidth() + wangka_tip.getBounds().getWidth())) > 400) {
			setSize(wangka_width+50, 370);
		}else {
			setSize(400, 370);
		}
		nicnametx.setPreferredSize(new Dimension((int)(localJPanel.getBounds().getWidth()-34-nictips.getBounds().getWidth()-same_to_inter.getBounds().getWidth()), (int)(nicnametx.getBounds().getHeight())));
		inter_nicJTextField.setPreferredSize(new Dimension((int)(localJPanel.getBounds().getWidth()-34-nictips.getBounds().getWidth()-same_to_inter.getBounds().getWidth()), (int)(nicnametx.getBounds().getHeight())));
		common.setPreferredSize(new Dimension((int)(localJPanel.getBounds().getWidth()),(int) (common.getBounds().getHeight())));
		
		nicnametx.setText(chat.username);
		inter_nicJTextField.setText(InternetChat.username);
		nicnametx.requestFocus();
		
		
		setLocationRelativeTo(chat.w);
		setVisible(true);
		
		same_to_inter.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean is_selected=same_to_inter.isSelected();
				nic_is_same=is_selected;
				same_to_lan.setSelected(is_selected);
				if (is_selected) {
					internameblock=true;
					inter_nicJTextField.setText(nicnametx.getText());
					internameblock=false;
				}
			}
		});
		same_to_lan.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean is_selected=same_to_lan.isSelected();
				nic_is_same=is_selected;
				same_to_inter.setSelected(is_selected);
				if (is_selected) {
					localnameblock=true;
					nicnametx.setText(nicnametx.getText());
					System.out.println("������");
					localnameblock=false;
				}
				
			}
		});
		inter_nicJTextField.addCaretListener(new CaretListener() {
			
			@Override
			public void caretUpdate(CaretEvent e) {
				if (nic_is_same & !internameblock) {
					localnameblock=true;
					nicnametx.setText(inter_nicJTextField.getText());
					localnameblock=false;
				}
			}
		});
		
		nicnametx.addCaretListener(new CaretListener() {
			
			@Override
			public void caretUpdate(CaretEvent e) {
				if (nic_is_same & !localnameblock) {
					internameblock=true;
					inter_nicJTextField.setText(nicnametx.getText());
					internameblock=false;
				}
				if (nicnametx.getText().getBytes().length > 30) {
					nic_mes.setVisible(true);
					nicnametx.setBackground(Color.ORANGE);
					nicnametx.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.RED));
				}else {
					nic_mes.setVisible(false);
					nicnametx.setBackground(null);
					nicnametx.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.CYAN));
				}
			}
		});
		nicnametx.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				nicnametx.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				nicnametx.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.CYAN));
			}
		});
		
		addWindowListener(new WindowListener() {
			
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
				thiswindow=null;
				setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			}
			
			@Override
			public void windowClosed(WindowEvent e) {}
			
			@Override
			public void windowActivated(WindowEvent e) {}
		});
	}
	
	public String getip(String netcard,JLabel show){
		NetworkInterface nowiInterface;
		String nowip;
		try {
			if ((nowiInterface=(NetworkInterface.getByName(netcard)))!=null && nowiInterface.isUp()&& nowiInterface.getInterfaceAddresses().size() != 0 && !(nowip=nowiInterface.getInterfaceAddresses().get(0).toString().split("/")[1]).startsWith("169") && !nowip.contains(":")) {
				chat.localIP=nowip;
				show.setOpaque(false);
				return "������IP  "+nowip;
			}
		} catch (SocketException e) {
			chat.catchexception(e);
		}
		show.setOpaque(true);
		return "��ǰ��������Ч��IP����";
	}
	
	
	
	public static void showon(){
		if (thiswindow != null) {
			thiswindow.setVisible(false);
			thiswindow.setLocationRelativeTo(chat.w);
			thiswindow.setVisible(true);
		}else {
			thiswindow=new config();
		}
	}
	
}
