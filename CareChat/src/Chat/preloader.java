package Chat;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
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
			e.printStackTrace();
		}
		load.setVisible(true);
		chat.mainly(load);
	}

}
