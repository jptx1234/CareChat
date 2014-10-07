package Chat;

import javax.swing.JLabel;

public class TypeJlabel extends JLabel implements Runnable {

	private static final long serialVersionUID = 1L;
	String text;
	@Override
	public void setText(String text) {
		this.text = text;
		Thread thread=new Thread(this);
		thread.start();
	}
	
	public void type(String text){
		super.setText(text);
		chat.w.repaint();
	}

	@Override
	public void run() {
		char[] texts=text.toCharArray();
		StringBuilder temp=new StringBuilder();
		for (int i = 0; i < texts.length; i++) {
			temp.append(texts[i]);
			type(temp.toString());
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
			}
		}
		for (int i = 1; i < 7; i++) {
			if (i%2==0) {
				type(text);
			}else {
				type(text+"¨y");
			}
			try {
				Thread.sleep(150);
			} catch (InterruptedException e) {
			}
		}
		
	}
}
