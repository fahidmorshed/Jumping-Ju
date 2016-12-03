package objects.creatures;

import java.awt.image.BufferedImage;

import objects.base.Creature;
import utility.ImageManipulator;
import utility.SpriteMap;
import core.animation.Animation;
import core.tile.TileMap;

public class Coin extends Creature {
	private static BufferedImage[] coinImages = (new SpriteMap("Data/Items/coin.png", 10, 1)).getSprites();
	public static Animation turn = new Animation(100).addFrame(coinImages[0]).addFrame(coinImages[1]).addFrame(coinImages[2]).addFrame(coinImages[3])
			.addFrame(coinImages[4]).addFrame(coinImages[5]).addFrame(coinImages[6]).addFrame(coinImages[7]).addFrame(coinImages[8])
			.addFrame(coinImages[9]);
	private Animation shoot;
	private static BufferedImage[] numbers = {ImageManipulator.loadImage("Data/HUD/num0.png"),ImageManipulator.loadImage("Data/HUD/num1.png"),
		ImageManipulator.loadImage("Data/HUD/num2.png"), ImageManipulator.loadImage("Data/HUD/num3.png"), ImageManipulator.loadImage("Data/HUD/num4.png"),
		ImageManipulator.loadImage("Data/HUD/num5.png"), ImageManipulator.loadImage("Data/HUD/num6.png"), ImageManipulator.loadImage("Data/HUD/num7.png"),
		ImageManipulator.loadImage("Data/HUD/num8.png"), ImageManipulator.loadImage("Data/HUD/num9.png")};
	
	public Coin(int pixelX, int pixelY) {
		super(pixelX, pixelY);
		setIsItem(true);
		
		final class DeadAfterAnimation extends Animation{
			public void endOfAnimationAction(){
				kill();
			}
		}
		
		shoot = new DeadAfterAnimation().setDAL(120).addFrame(coinImages[0]).addFrame(coinImages[1]).addFrame(coinImages[2]).addFrame(coinImages[3])
				.addFrame(coinImages[4]).addFrame(coinImages[5]).addFrame(coinImages[6]).addFrame(coinImages[7]).addFrame(coinImages[8])
				.addFrame(coinImages[9]);
		setAnimation(turn);
	}
	
	public void updateCreature(TileMap map, int time){
		if(currentAnimation()==shoot){
			super.update(time);
			y = y + dy * time;
			if(dy<0){
				dy = dy + .018f;
			}
		}
	}
	
	public void shoot(){
		setIsCollidable(true);
		setAnimation(shoot);
		dy = -.3f;
	}
	
	public static BufferedImage getNumberImage(int i){
		return numbers[i];
	}
}

