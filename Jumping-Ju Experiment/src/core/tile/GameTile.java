package core.tile;

import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import objects.base.Creature;
import core.animation.Animation;

/**
 *This object is more or less responsible for a complete tiles, their colliding issue with creatures
 *including Ju and other objects.
 */
public class GameTile extends Tile {
	private boolean isCollidable = true;
	private boolean isSloped = false;
	private List<Creature> collidingCreatures;
	//LIST OF CREATURES, CAREFULL, it's a LINKEDLIST not an array List.
	
	public GameTile(int pixelX, int pixelY, Animation anim, BufferedImage img){
		super(pixelX, pixelY, anim, img);
		collidingCreatures = new LinkedList<Creature>();
		//create a linked list of creatures.
	}
	
	public GameTile(int pixelX, int pixelY, BufferedImage img){
		this(pixelX, pixelY, null, img);
	}
	
	//For overriding purpose.
	public void doAction(){ }
	
	public boolean isCollidable(){
		return isCollidable;
	}
	
	public boolean isSloped(){
		return isSloped;
	}
	
	public void setIsCollidable(boolean isCollidable){
		this.isCollidable = isCollidable;
	}
	
	public void setIsSloped(boolean isSloped){
		this.isSloped = isSloped;
	}
	
	public List<Creature> collidingCreatures() {
		return collidingCreatures;
	}
	
	//Beware, no method for adding creature in the list. It's derrived class should remember that.
}
