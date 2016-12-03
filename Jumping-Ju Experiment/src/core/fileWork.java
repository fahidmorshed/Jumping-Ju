package core;
import java.util.*;
import java.io.*;
public class fileWork {

	
	fileWork(String n,String s){
	
		System.out.println();
		try
		{
		    String filename= "dd.txt";
		    String filename1= "ddnath.txt";
		    FileWriter fw = new FileWriter(filename,true); 
		    FileWriter fw1 = new FileWriter(filename1,true);
		    //the true will append the new data
		    fw.write(n);
		    fw.write(System.lineSeparator());
		    
		    //appends the string to the file
		    fw1.write(s);
		    fw1.write(System.lineSeparator());
		    fw1.close();
		    fw.close();
		}
		catch(IOException ioe)
		{
		    System.err.println("IOException: " + ioe.getMessage());
		}
		
	
	}
}
