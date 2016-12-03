package core;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.plaf.multi.MultiLabelUI;

import utility.ImageManipulator;
import core.JuPanel.GameListener;
//import core.animation.SpriteListener;
public class menuBar extends JFrame{
	
	
	
	
	private JButton m;
	private JButton b1,b2;
	private   JButton l,c,c1,l1,inf,l2;
	private  JuPanel panel=new JuPanel(800,600);
	private JPanel jk,jp,htp,result,gaming,gtp;
	public static  boolean y=true;
	Font f=new Font("sanserrif",20,20);
	
	
	
	public void start1(){
		
	
		int width = 800;
		int height = 600;
		setSize(width, height);
		

		    jp=new JPanel();
			Icon  b=new ImageIcon(ImageManipulator.loadImage("Data/Moloy/howToPlay.png"));
		    l=new JButton("BACK");
		    l1=new JButton("BACK");
		    l2=new JButton("BACK");
		    Icon v=new ImageIcon(ImageManipulator.loadImage("Data/Moloy/credit.png"));
		    Icon exitImageIcon = new ImageIcon(ImageManipulator.loadImage("Data/Moloy/exit.png"));
			Icon playImageIcon = new ImageIcon(ImageManipulator.loadImage("Data/Moloy/play.png"));
			Icon helpImageIcon = new ImageIcon(ImageManipulator.loadImage("Data/Moloy/help.png"));
			Icon scoreImageIcon = new ImageIcon(ImageManipulator.loadImage("Data/Moloy/score.png"));
			Icon aboutUsImageIcon = new ImageIcon(ImageManipulator.loadImage("Data/Moloy/aboutUs.png"));
		    Icon menuImageIcon = new ImageIcon(ImageManipulator.loadImage("Data/Moloy/menu.png"));
			m = new JButton(v);
			c1 = new JButton(b);
			b1=new JButton(playImageIcon);
			b2=new JButton(helpImageIcon);
			c=new JButton(scoreImageIcon);
			inf=new JButton(aboutUsImageIcon);
			JButton exitButton = new JButton(exitImageIcon);
			JButton menuImage = new JButton(menuImageIcon);
			
			
		jp.add(b1);
		jp.add(b2);
		jp.add(c);
		jp.add(inf);
		jp.add(exitButton);
		jp.add(menuImage);
		add(jp);
		

		exitButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
				
			}
		});
		b1.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent event) {
				
			
		/*		gaming=new JPanel();
				gaming.add(new JuPanel(800,600));
				gaming.setVisible(true);*/
				add(panel);
				remove(jp);
				revalidate();
			
			}
		});
		
		
		////the result for how to play
        htp=new JPanel();
        htp.add(c1);
        htp.add(l);
        
        gtp=new JPanel();
        gtp.add(m);
        gtp.add(l2);
		b2.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent ev) {
				
			    add(htp);
			    remove(jp);
			    htp.setVisible(true);
			    revalidate();

			}
		
		});
		result=new JPanel();
		
		int Z=0;
		int koma=0;
		String str="<html>";
		str=str+"NAME"+"    "+"SCORE"+"<br>";
		for(int Y=0;Y<5 ;Y++)
		{
			String s1,n1;
			if(readFile.score[0]>0)
			{
		       str=str+(Y+1)+"        "+readFile.name[Y]+"        "+readFile.score[Y]+"<br>";
			}
		}
		
			 
		 str=str+"</html>";
		
			JLabel lb=new JLabel(str,SwingConstants.CENTER);
			
			
			lb.setFont(f);
			lb.setForeground(Color.BLUE);
			//Z=Z+50000;
			
			//lb.setBounds(10,Z,20,20);
			
			
			result.add(lb);
			
			
		//}
		
		
		
		result.add(l1);
	//	result.add(m);
	
		
   c.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent ev) {
				
			    add(result);
			    remove(jp);
			    result.setVisible(true);
			    revalidate();

			}
		
		});
   
   
	
   inf.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent ev) {
				
			    add(gtp);
			    remove(jp);
			    gtp.setVisible(true);
			    revalidate();

			}
		
		});
   
   /// for back from the how to play
   
	l1.addActionListener(new ActionListener() {
		
		public void actionPerformed(ActionEvent e) {
		
			//remove(htp);
			
			getContentPane().add(jp);
			result.setVisible(false);
			getContentPane().remove(result);
			//getContentPane().remove(htp);
			getContentPane().revalidate();
			
		  
		   
		}
	});
   
	
	
l2.addActionListener(new ActionListener() {
		
		public void actionPerformed(ActionEvent e) {
		
			//remove(htp);
			
			getContentPane().add(jp);
			gtp.setVisible(false);
			getContentPane().remove(gtp);
			//getContentPane().remove(htp);
			getContentPane().revalidate();
			
		  
		   
		}
	});
	
   /// for back from the result
		
		l.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
			
				//remove(htp);
				
				getContentPane().add(jp);
				htp.setVisible(false);
				getContentPane().remove(htp);
				//getContentPane().remove(result);
				getContentPane().revalidate();
				
			  
			   
			}
		});
		
		setResizable(false);
		setTitle("JUMPING-JU v 1.0");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
  
		
	
	}
	
	
	
	
	public void start2(){
		
		
		
		JButton restart=new JButton("RESTART");
		JButton exit=new JButton("EXIT");
		JButton resume=new JButton("RESUME");
	    jk=new JPanel();
        
	    
	 
	    
		jk.add(exit);
		jk.add(resume);
		jk.add(restart);
	    add(jk);
	    remove(panel);
	    revalidate();
	
      restart.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent event) {
				
			System.out.println("ilove krsna");
				
	         // panel=new JuPanel(800, 600);
				add(panel);
				remove(jk);
				revalidate();
			
			}
		});
      
		
	}


}
