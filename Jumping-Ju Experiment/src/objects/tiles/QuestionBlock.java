package objects.tiles;

import java.awt.image.BufferedImage;
import java.util.Random;

import objects.creatures.Mushroom;
import objects.creatures.Score;
import objects.ju.Ju;
import utility.ImageManipulator;
import core.animation.Animation;
import core.tile.GameTile;
import core.tile.TileMap;

public class QuestionBlock extends GameTile {
	// Sound obj #check
	private TileMap map;
	private Animation active;
	private Animation dead;
	private boolean isActive;
	private boolean hasCoin;
	private boolean hasMushroom;
	private int randomCoins;
	private int counter;
	
	public QuestionBlock(int pixelX, int pixelY, TileMap map) {
		super(pixelX, pixelY, null, null);
		setIsSloped(false);
		isActive = true;
		Random random = new Random();
		boolean a = random.nextBoolean();
		hasCoin = a;
		hasMushroom = !a;
		//sound obj #check
		this.map = map;
		randomCoins = random.nextInt(20)+1;
		counter = 0;
		BufferedImage q[] = {ImageManipulator.loadImage("Data/Tile/qusBlock1.png"), ImageManipulator.loadImage("Data/Tile/qusBlock2.png"),
				ImageManipulator.loadImage("Data/Tile/qusBlock3.png"), ImageManipulator.loadImage("Data/Tile/qusBlock4.png"),
				ImageManipulator.loadImage("Data/Tile/qusBlockDead.png")
		};
		
		Random r  = new Random();
		active = new Animation(r.nextInt(20) + 140).addFrame(q[0]).addFrame(q[1]).addFrame(q[2]).addFrame(q[3]);
		dead = new Animation(2000).addFrame(q[4]);
		setAnimation(active);
	}
	
	public void update(int time){
		super.update(time);
		if(getOffsetY() != 0) { setOffsetY(getOffsetY() + 2); }
	}
	
	public void doAction() {
		if(isActive) {
			if(hasCoin) {
				setOffsetY(-20);
				Score score = new Score(getPixelX(), getPixelY());
				map.creaturesToAdd().add(score);
				Ju.setPoint(1);
				counter++;
				if(counter>=randomCoins){
					isActive = false;
					setAnimation(dead);
				}
			} else if(hasMushroom) {
				setOffsetY(-10);
				Mushroom shroom = new Mushroom(getPixelX(), getPixelY()-26);
				map.creaturesToAdd().add(shroom);
				isActive = false;
				setAnimation(dead);
			}
		}
	}
}
