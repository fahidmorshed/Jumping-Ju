package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class readFile {


	int n=0;
	private Scanner x,y;
	public void openFile(){
		
		try{
			x= new Scanner (new File("dd.txt"));
			y=new Scanner (new File("ddnath.txt"));
		}
		
		catch(Exception e){
			
		}
	}
	
	public void readfile1(){
		while( x.hasNext()){
		
			x.next();
			n++;
			//System.out.println(n);
		}
		x.close();
	}
	
	int i=0;
	
	public static String name[];
	public static int score[]={0};
	public void readfile()
	{
		
		try
		{
			name=new String[n];
		   x= new Scanner (new File("dd.txt"));
		//y=new Scanner (new File("ddnath.txt"));
		}
		catch(Exception e)
		{
			
			
		}
		
		
		//System.out.println("Check "+n);
		while( x.hasNext())
		{
			//System.out.println("check 1");
			String a= x.next();
			////String b= y.next();
			name[i]=a;
			//score[i]=Integer.parseInt(b);
			i++;
			//System.out.println(a+"\t"+b);
		}
		
		
		try
		{
			score=new int[n];
		  // x= new Scanner (new File("dd.txt"));
		y=new Scanner (new File("ddnath.txt"));
		}
		catch(Exception e)
		{
			
			
		}
		i=0;
		while( y.hasNext())
		{
			//System.out.println("check 1");
			String  a= y.next();
			////String b= y.next();
			score[i]=Integer.parseInt(a); 
			
			//score[i]=Integer.parseInt(b);
			i++;
			//System.out.println(a+"\t"+b);
		}
		y.close();
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				if( score[i] > score[j]){
					int t=score[i];
					score[i]=score[j];
					score[j]=t;
					String te=name[i];
					name[i]=name[j];
					name[j]=te;
				}
			}
		}
//		for(int i=0;i<5;i++){
//			System.out.println(name[i]+"\t"+score[i]);
//		}
		
	}
	
	public void closeFile(){
		x.close();
	}
}
