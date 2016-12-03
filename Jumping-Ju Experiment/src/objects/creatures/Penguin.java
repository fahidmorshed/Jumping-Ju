package objects.creatures;

import java.awt.image.BufferedImage;
import java.util.Random;

import objects.base.Creature;
import utility.ImageManipulator;
import core.animation.Animation;

public class Penguin extends Creature {
	private Animation walkLeft, walkRight, deadLeft, deadRight, flipLeft, flipRight;
	private boolean isLeft = true;
	public Penguin(int x, int y){
		super(x, y); 
		// a sound obj
		BufferedImage wl1 = ImageManipulator.loadImage("Data/Baddies/penguin1.png");
		BufferedImage wl2 = ImageManipulator.loadImage("Data/Baddies/penguin2.png");
		BufferedImage wl3 = ImageManipulator.loadImage("Data/Baddies/penguin3.png");
		
		BufferedImage wr1 = ImageManipulator.horizontalFlip(wl1);
		BufferedImage wr2 = ImageManipulator.horizontalFlip(wl2);
		BufferedImage wr3 = ImageManipulator.horizontalFlip(wl3);
		
		BufferedImage dl1 = ImageManipulator.loadImage("Data/Baddies/penguinDead1.png");
		BufferedImage dl2 = ImageManipulator.loadImage("Data/Baddies/penguinDead2.png");
		BufferedImage dl3 = ImageManipulator.loadImage("Data/Baddies/penguinDead3.png");
		
		BufferedImage dr1 = ImageManipulator.horizontalFlip(dl1);
		BufferedImage dr2 = ImageManipulator.horizontalFlip(dl2);
		BufferedImage dr3 = ImageManipulator.horizontalFlip(dl3);
		
		BufferedImage fl1 = ImageManipulator.verticalFlip(wl1);
		BufferedImage fr1 = ImageManipulator.verticalFlip(wr1);
		final class DeadAfterAnimation extends Animation {
			public void endOfAnimationAction() {
				kill();
			}
		}
		
		walkLeft = new Animation(150).addFrame(wl1).addFrame(wl2).addFrame(wl3);
		walkRight = new Animation(150).addFrame(wr1).addFrame(wr2).addFrame(wr3);
		deadLeft = new DeadAfterAnimation().setDAL(100).addFrame(dl1).addFrame(dl2).addFrame(dl3);
		deadRight = new DeadAfterAnimation().setDAL(100).addFrame(dr1).addFrame(dr2).addFrame(dr3);
		flipLeft = new Animation().addFrame(fl1);
		flipRight = new Animation().addFrame(fr1);
		setAnimation(walkLeft);
		isLeft = true;
		Random random = new Random();
		dx = (random.nextInt(3) == 0) ? -.03f : .03f;
		if(dx == .03)
			isLeft = false;
		
	}
	
	public void wakeUp(){
		super.wakeUp();
		if(!isLeft){
			setAnimation(walkRight);
		}
		else{
			setAnimation(walkLeft);
		}
	}
	
	public void jumpedOn(){
		if(isLeft){
			setAnimation(deadLeft);
		}
		else{
			setAnimation(deadRight);
		}
		setIsCollidable(false);
		dx = 0;
	}
	
	public void flip(){
		if(isLeft){
			setAnimation(flipLeft);
		}
		else{
			setAnimation(flipRight);
		}
		setIsFlipped(true);
		setIsCollidable(false);
		dy = -.2f;
		dx = 0;
	}
}
