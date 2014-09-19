package Chat;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JWindow;

public class preloader {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JWindow load=new JWindow();
		load.setSize(500, 250);
		load.setLocationRelativeTo(null);
		try {
			ImageIcon img=new ImageIcon(preloader.class.getResource("/images/load.png"));
			JLabel im=new JLabel(img);
			load.add(im);
		} catch (Exception e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(null, "资源文件被破坏，请重新下载或拷贝本程序！","找不到资源 - CareChat",JOptionPane.ERROR_MESSAGE);
			return;
		}
		load.setVisible(true);
		chat.mainly(load);
	}

}
