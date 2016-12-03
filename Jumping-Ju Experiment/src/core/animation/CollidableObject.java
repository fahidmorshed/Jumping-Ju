//WORK TO DO, Implement the sound object
package core.animation;

//IMPORT SOUND PACKAGE #check

/**
 *This object is full functionating animation for an object with sound and colliding details.
 */

public class CollidableObject extends Sprite { //#check
	//Proceted Instance of a Sound Object.
	private boolean isCollidable;
	private boolean isOnScreen;
	
	public CollidableObject(int pixelX, int pixelY){
		super(pixelX, pixelY);
		this.isCollidable = true;
		setIsOnScreen(true);
	}
	//Override the constructor for a sound obj. #check
	
	public boolean isCollidable(){
		return isCollidable;
	}
	public void setIsCollidable(boolean c){
		isCollidable = c;
	}
	public boolean isOnScreen(){
		return isOnScreen;
	}
	public void setIsOnScreen(boolean o){
		isOnScreen = o;
	}
}
