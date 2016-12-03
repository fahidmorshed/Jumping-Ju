package core.animation;

import java.awt.image.BufferedImage;

import objects.ju.Ju;
import utility.ImageManipulator;

public class HUD extends Sprite{
	
	BufferedImage[] scoreImages = {ImageManipulator.loadImage("Data/Score/0.png"), ImageManipulator.loadImage("Data/Score/1.png"),
			ImageManipulator.loadImage("Data/Score/2.png"), ImageManipulator.loadImage("Data/Score/3.png"),
			ImageManipulator.loadImage("Data/Score/4.png"), ImageManipulator.loadImage("Data/Score/5.png"),
			ImageManipulator.loadImage("Data/Score/6.png"), ImageManipulator.loadImage("Data/Score/7.png"),
			ImageManipulator.loadImage("Data/Score/8.png"), ImageManipulator.loadImage("Data/Score/9.png")};
	
	int point = Ju.getPoint();
	String pointString = Integer.toString(point);
	
	public HUD(){
		x = 700;
		y = 50;
		if(pointString.charAt(0) == '0'){
			
		}
	}
}
