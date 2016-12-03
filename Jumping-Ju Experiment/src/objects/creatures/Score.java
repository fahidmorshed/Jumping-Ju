package objects.creatures;

import java.awt.image.BufferedImage;

import objects.base.Creature;
import utility.ImageManipulator;
import core.animation.Animation;
import core.tile.TileMap;

public class Score extends Creature {
	public Animation onePoint;
	
	public Score(int x, int y){
		super(x,y);
		setIsItem(true);
		
		dy = -.45f;
		
		BufferedImage[] oneImage = {ImageManipulator.loadImage("Data/Items/point1.png"),
				ImageManipulator.loadImage("Data/Items/point2.png"), ImageManipulator.loadImage("Data/Items/point3.png")
		};
		
		final class DeadAfterAnimation extends Animation {
			public void endOfAnimationAction() {
				kill();
			}
		}
		
		onePoint = new DeadAfterAnimation();
		for(BufferedImage b: oneImage){
			onePoint.addFrame(b, 150);
		}
		
		setAnimation(onePoint);
	}
	
	public void updateCreature(TileMap map, int time) {
		this.update((int) time);
		y = y + dy * time;
		if(dy < 0) {
			dy = dy + .031f;
		} else {
			dy = 0;
		}
	}
}
