package core;

import javax.swing.JFrame;

public class JuFrame extends JFrame {
	public JuFrame() {
		
//		int width = 800;
//		int height = 600;
//		setSize(width, height);
//		JuPanel panel = new JuPanel(width, height);
//		add(panel);
//		setResizable(false);
//		setTitle("JUMPING-JU v 1.0");
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setVisible(true);
		
		
		menuBar l=new menuBar();
		l.start1();
		
	}
	public static void main(String[] args){
		
	
	
		readFile r=new readFile();
		r.openFile();
		r.readfile1();
		r.readfile();
		r.closeFile();
		
		new JuFrame();
	
		
	}
}
