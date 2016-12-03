package objects.creatures;

import java.awt.image.BufferedImage;

import objects.base.Creature;
import utility.ImageManipulator;
import core.animation.Animation;
import core.tile.TileMap;

public class Mushroom extends Creature{
	private Animation redMushroom;
	private int updateNum;
	
	public Mushroom(int pixelX, int pixelY) {
		super(pixelX, pixelY);
		setIsItem(true);
		setIsAlwaysRelevant(true);
		BufferedImage bImage = ImageManipulator.loadImage("Data/Items/mushroom.png");
		redMushroom = new Animation().addFrame(bImage, 1000).addFrame(bImage, 1000);
		setAnimation(redMushroom);
		updateNum = 0;
		dy = -.07f;
		dx = .05f;
	}
	
	public void updateCreature(TileMap map, int time){
		if(updateNum < 10){
			setX(getX() + getdX()*time);
			setY(getY() + getdY()*time);
		}
		else if(updateNum < 200){
			super.updateCreature(map, time);
		}
		else if(updateNum < 300){
			if(updateNum % 4 == 0 || updateNum % 4 == 1) {
				setIsInvisible(true);
			} else {
				setIsInvisible(false);
			}
			super.updateCreature(map, time);
		}
		else {
			kill();
		}
		updateNum += 1;
	}
}
