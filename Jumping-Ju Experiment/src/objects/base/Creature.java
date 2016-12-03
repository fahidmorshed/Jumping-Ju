package objects.base;

import java.awt.Point;

import core.JuRenderer;
import core.animation.CollidableObject;
import core.animation.Sprite;
import core.tile.GameTile;
import core.tile.TileMap;

public class Creature extends CollidableObject {
	protected static int xCollideOffset = 2;
	protected static int  offMapOffset = 15;
	protected static float GRAVITY = .0008f;
	protected static float gravityEffect = .22f;
	
	// Wake up values are constants based on the number of tiles on the screen
	// that are used to determine when Ju comes within range of a creature.
	// Used exclusively within GameRender.java.
	public static int WAKE_UP_VALUE_DOWN_RIGHT = 24;
	public static int WAKE_UP_VALUE_UP_LEFT = -3;
	
	/* 
	 * Creature Attributes:
	 * 
	 * Relevant:  A creature that is always relevant must be updated every frame. By default, no creature
	 * 			  is always relevant. 
	 * Alive:     A creature that is on the map is alive. All creatures start alive and can be killed using
	 * 			  the kill() method.
	 * Sleeping:  A creature that is sleeping has yet to be seen by the player. All creatures start out
	 * 			  sleeping, and can be woken up using wakeUp(). They cannot be put back to sleep.
	 * Flipped:   isFlipped is a flag used to determine when to change the animation of a creature to death.
	 * 			  For example, a goomba that is hopped on is 'flipped', then removed from the game.
	 * Item:      A creature that is an item represents an item the player can interact with.
	 * Platform:  A creature is a platform if it is a non-aligned moving object the player
	 * 			  and creatures can interact with. 
	 * Invisible: When a creature is invisible, it isn't drawn.
	 */
	
	private boolean isAlwaysRelevant;
	private boolean isAlive;
	private boolean isSleeping;
	private boolean isFlipped;
	private boolean isItem;
	private boolean isPlatform;
	private boolean isInvisible;
	
	
	//MAKE THE CONSTRUCTORS FOR A SOUND OBJECT
	public Creature(){
		this(0, 0);
	}
	
	public Creature(int pixelX, int pixelY){
		super(pixelX, pixelY);
		setIsCollidable(true);
		isAlive = true;
		isSleeping = true;
		isFlipped = false;
		setIsOnScreen(false);
		isItem = false;
		isPlatform = false;
		isAlwaysRelevant = false;
		//isInvisible is not initialized.
	}
	
	public boolean isPlatform() {
		return isPlatform;
	}
	
	public void setIsPlatform(boolean isPlatform) {
		this.isPlatform = isPlatform;
	}
	
	public boolean isItem() {
		return isItem;
	}
	
	public void setIsItem(boolean isItem) {
		this.isItem = isItem;
	}
	
	public boolean isFlipped() {
		return isFlipped;
	}
	
	public void setIsFlipped(boolean isFlipped) {
		this.isFlipped = isFlipped;
	}
	
	public boolean isSleeping() {
		return isSleeping;
	}
	
	//Change the state of isSleeping to false. No function for setIsSleeping()
	public void wakeUp() { 
		isSleeping = false;
	}
	
	public boolean isAlive() {
		return isAlive;
	}
	
	//Change the state of isAlive to false. No function for setIsAlive()
	public void kill() {
		isAlive = false;
	}
	
	public boolean isAlwaysRelevant() {
		return isAlwaysRelevant;
	}
	
	public void setIsAlwaysRelevant(boolean isAlwaysRelevant) {
		this.isAlwaysRelevant = isAlwaysRelevant;
	}
	
	public boolean isInvisible() {
		return isInvisible;
	}
	
	public void setIsInvisible(boolean isInvisible) {
		this.isInvisible = isInvisible;
	}
	
	//for overriding purpose.
	public void jumpedOn() { }
	public void flip() { }
	
	//for Tile collision
	public void xCollide(Point point){
		if(dx > 0){	//dx is an int in Sprite object. Probably deals with the velocity on x axis.
			x = x - xCollideOffset; // staring from a slightly different position.
		}
		else{
			x = x + xCollideOffset; // starting from a slightly different position.
		}
		dx = -dx; //changing direction of the velocity. Bounce
	}
	
	//for creature collision, same as before.
	public void creatureXCollide(){
		if(dx > 0){
			x = x - xCollideOffset;
		}
		else {
			x = x + xCollideOffset;
		}
		
		dx = -dx;
	}
	
	/**
	 * Calculates the type of collision in the X direction between a Tile 
	 * and a Sprite given the Sprite is currentely colliding with the tile. 
	 * This method relies on the general heuristic that if two 
	 * rectangular objects are colliding, then one object is not completely
	 * contained in the other. Because the colliding objects stick out, we
	 * know the direction of the collision. 
	 * 
	 * pre-condition: sprite is colliding with tile.
	 * @return Collision.WEST if sprite is colliding with the tile from the west or
	 * Collision.EAST if sprite is colliding with the tile from the east.
	 */
	public static Collision tileCollisionX(GameTile tile, Sprite sprite){
		if(sprite.getX() > tile.getPixelX()){
			return Collision.WEST;
		}
		else {
			return Collision.EAST;
		}
	}
	
	//It's same as before.
	public static Collision tileCollisionY(GameTile tile, Sprite sprite){
		if(sprite.getY() < tile.getPixelY()){
			return Collision.NORTH;
		}
		else{
			return Collision.SOUTH;
		}
	}
	
	public void updateCreature(TileMap map, int time){
		if(dy < gravityEffect){
			dy = dy + GRAVITY*time; //applying gravity before calculating.
		}
		
		float dx =  this.dx;
		float oldX = this.x;
		float newX = oldX + dx*time;
		
		float dy = this.dy;
		float oldY = this.y;
		float newY = oldY + dy*time;
		
		if(!isFlipped){
			Point xTile = JuRenderer.getTileCollision(map, this, x, y, newX, y);
			Point yTile = JuRenderer.getTileCollision(map, this, x, y, x, newY);
			
			this.update(time); //Function of Animation. Updates frame.
			
			// Update collisions in the x-direction.
			if(oldX < -offMapOffset || oldX > JuRenderer.tilesToPixels(map.getWidth() + offMapOffset)){ //Offscreen
				kill();
			}
			else{
				if(xTile == null){
					x = newX;
				} else if(!xTile.equals(yTile)){
					this.xCollide(xTile);
					if(dx > 0){
						x = JuRenderer.tilesToPixels(xTile.x) - this.getWidth();
					} else if(dx < 0){
						x = JuRenderer.tilesToPixels(xTile.x + 1);
					}
				}
			}
			
			// Update collisions in the y-direction.
			if(oldY > JuRenderer.tilesToPixels(map.getHeight()) + offMapOffset) { // offscreen
				kill();
			}else {
				if(yTile == null) {
					y = newY;
				} else {
					if(dy > 0) {
						// mark this creature as colliding with a tile
						//adds this creature into the list of collidingCreature in GameTile object.
						map.getTile(yTile.x, yTile.y).collidingCreatures().add(this); // #check #uncheck 
						GameTile tileRight = map.getTile(((int) yTile.getX()) + 1, ((int) yTile.getY()));
						if(tileRight != null) {
							tileRight.collidingCreatures().add(this); 
						}
						y = JuRenderer.tilesToPixels(yTile.y) - this.getHeight();
					} else if (dy < 0) {
						y = JuRenderer.tilesToPixels(yTile.y + 1);
						this.dy = -dy/4; // fall faster if a collision occurs
					}
				}
			}
		}
		else{ //flipped
			x = newX;
			y = newY;
			this.update(time);
		}
	}
	
	// Determines what happens when two different creatures collide.
	public void creatureCollision(Creature creature){
		if(!this.isItem && !creature.isItem && !this.isPlatform && !creature.isPlatform &&
				this != creature && this.isCollidable() && creature.isCollidable()){
			boolean collision = isCollision(this, creature);
			if(collision){
				//do nothing, might be useful in future
			}
			else{
				this.creatureXCollide();
				creature.creatureXCollide();
			}
		}
	}
}
