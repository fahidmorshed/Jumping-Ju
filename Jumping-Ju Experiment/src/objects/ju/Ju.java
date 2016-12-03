package objects.ju;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import objects.base.Collision;
import objects.base.Creature;
import objects.creatures.Coin;
import objects.creatures.Mushroom;
import objects.creatures.Penguin;
import objects.creatures.Platform;
import objects.creatures.Score;
import utility.ImageManipulator;
import core.JuRenderer;
import core.animation.Animation;
import core.animation.CollidableObject;
import core.tile.GameTile;
import core.tile.TileMap;

public class Ju extends CollidableObject{
	//A sound object is needed.
	
	/* Static Constant Fields.
	 * Gravity:   Effects the amount of pull objects feel toward the ground. pixels/ms
	 * Friction:  Effects the amount of sliding an object displays before coming to a stop.
	 * S_X:       Starting X position of Ju.
	 * S_Y:       Starting Y position of Ju.
	 * S_DY:      Starting Dy of Ju.
	 * S_JH:      Effects the height of Ju's first jump.
	 * Anim_Time: The time between each of Ju's Animations. 
	 * 
	 * Terminal_Walking_Dx:  Max speed when Ju is walking.
	 * Terminal_R3unning_Dx:  Max speed when Ju is running.
	 * Terminal_Fall_Dy:     Max speed Ju can fall at.
	 * Walking_Dx_Inc:       The increase in speed per update when walking until terminal runnning is reached.
	 * Running_Dx_Inc:       The increase in speed per update when running until terminal walking is reached.
	 * Start_Run_Anim_Thres: The speed where Ju switches to the running animation.
	 */
	public static final float GRAVITY = 0.0008f;
	public static final float FRICTION = 0.0008f;
	private static final int STARTING_X = 32;
	private static final int STARTING_Y = 640;
	private static final float STARTING_DY = .03f;
	private static final float INITIAL_JUMP_HEIGHT = -.34f;
	private static final float JUMP_MULTIPLIER = .55f;
	private static final float TERMINAL_WALKING_DX = .20f;
	private static final float WALKING_DX_INC = .01f;
	private static final float TERMINAL_RUNNING_DX = .35f;
	private static final float START_RUN_ANIM_THRESHOLD = .2f;
	private static final float RUNNING_DX_INC = .006f;
	private static final float TERMINAL_FALL_DY = .22f;
	private static final int STARTING_LIFE = 1;
	private static final int ANIM_TIME = 100;
	
	/* INITIAL_JUMP_HEIGHT + dx*JUMP_MULTIPLIER */
	private float jumpHeight;
	
	/* Boolean variables used to identify which keys are pressed. */ //#check isUpHeld
	private boolean isDownHeld, isRightHeld, isLeftHeld, isUpHeld, isShiftHeld, isSpaceHeld;
	
	/* Boolean variables used to identify the state of Ju. */
	private boolean isJumping, frictionLock, isInvisible;
	private boolean isUpSlope, isDownSlope, onSlopedTile;
	/* Boolean variables used to identify where Ju is with respect to Platforms. */
	private boolean isRightOfPlatform, isLeftOfPlatform, isBelowPlatform, isAbovePlatform;
	private Animation walkLeft, runLeft, stillLeft, stillUpLeft, stillJumpLeft, runningJumpLeft, sitLeft, changeLeft, currLeftAnim;
	private Animation walkRight, runRight, stillRight, stillUpRight, stillJumpRight, runningJumpRight, sitRight, changeRight, currRightAnim;
	public Animation stageWin, deadRight, deadLeft;
	private int health;
	private static int point;

	//A Platform var is to be decleared. #check #uncheck
	private Platform platform;
	//A Sound var is to be decleared. #check
	private boolean isBlinking = false;
	private boolean isStageComplete;
	
	//Should have been initialized with a sound obj both in the constructor 
	//and it's super.
	public Ju(){
		super(STARTING_X, STARTING_Y);
		setIsJumping(true);
		dy = STARTING_DY;
		jumpHeight = INITIAL_JUMP_HEIGHT;
		health = STARTING_LIFE;
		isStageComplete = false;
		point = 0;
		//A Sound obj
		
		//LOAD THE IMAGES AND CREATE ANIMATION. #check
		BufferedImage[] rightImages = {ImageManipulator.loadImage("Data/Ju/stillRight.png"), ImageManipulator.loadImage("Data/Ju/runRight1.png"),
					ImageManipulator.loadImage("Data/Ju/runRight2.png"), ImageManipulator.loadImage("Data/Ju/runRight3.png"),
					ImageManipulator.loadImage("Data/Ju/runRight4.png"), ImageManipulator.loadImage("Data/Ju/stillJumpRight1.png"),
					ImageManipulator.loadImage("Data/Ju/stillJumpRight2.png"), ImageManipulator.loadImage("Data/Ju/runningJumpRight1.png"),
					ImageManipulator.loadImage("Data/Ju/runningJumpRight2.png"), ImageManipulator.loadImage("Data/Ju/runningJumpRight3.png"),
					ImageManipulator.loadImage("Data/Ju/stillSitRight.png"), ImageManipulator.loadImage("Data/Ju/stillUpRight.png"),
					ImageManipulator.loadImage("Data/Ju/deadRight1.png"), ImageManipulator.loadImage("Data/Ju/deadRight2.png"), 
					ImageManipulator.loadImage("Data/Ju/deadRight3.png"), ImageManipulator.loadImage("Data/Ju/runRight5.png"),
					ImageManipulator.loadImage("Data/Ju/runRight6.png"), ImageManipulator.loadImage("Data/Ju/runRight7.png"),
					ImageManipulator.loadImage("Data/Ju/runRight8.png"), ImageManipulator.loadImage("Data/Ju/runRightNew.png"),
					ImageManipulator.loadImage("Data/Ju/stageWin1.png"), ImageManipulator.loadImage("Data/Ju/stageWin2.png"),
					ImageManipulator.loadImage("Data/Ju/stageWin3.png"), ImageManipulator.loadImage("Data/Ju/stageWin4.png"),
					ImageManipulator.loadImage("Data/Ju/stageWin5.png")};
		//0 == still right
		//1-4 == walk & run right
		//5-6 == still jump right
		//7-9 == running jump right
		//10 == sit right
		//11 == still up right
		//12-14 == dead right
		//15-16 17 18 == walk & run right new
		// 19-20 == walk right new
		//21-24 == win
		BufferedImage[] leftImages = {null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
		for(int i=0; i<rightImages.length; i++){
			leftImages[i] = ImageManipulator.horizontalFlip(rightImages[i]);
		}
		
		stillLeft = new Animation(ANIM_TIME).addFrame(leftImages[0]);
		walkLeft = new Animation(ANIM_TIME+30).addFrame(leftImages[1]).addFrame(leftImages[4]).addFrame(leftImages[15]).addFrame(leftImages[19]).addFrame(leftImages[18]);
		runLeft = new Animation(ANIM_TIME).addFrame(leftImages[1]).addFrame(leftImages[2]).addFrame(leftImages[3]).addFrame(leftImages[4]).addFrame(leftImages[15]).addFrame(leftImages[16]).addFrame(leftImages[17]).addFrame(leftImages[18]);
		sitLeft = new Animation(ANIM_TIME).addFrame(leftImages[10]);
		stillJumpLeft = new Animation(ANIM_TIME).addFrame(leftImages[5], 300).addFrame(leftImages[6], 4000);
		runningJumpLeft = new Animation(ANIM_TIME).addFrame(leftImages[7]).addFrame(leftImages[8], 400).addFrame(leftImages[9], 4000);
		changeLeft = new Animation(ANIM_TIME).addFrame(leftImages[1]);
		stillUpLeft = new Animation(ANIM_TIME).addFrame(leftImages[11]);
		deadLeft = new Animation(ANIM_TIME-30).addFrame(leftImages[12], 500).addFrame(leftImages[13]).addFrame(leftImages[14], 3000);
		
		stillRight = new Animation(ANIM_TIME).addFrame(rightImages[0]);
		walkRight = new Animation(ANIM_TIME+30).addFrame(rightImages[1]).addFrame(rightImages[4]).addFrame(rightImages[15]).addFrame(rightImages[19]).addFrame(rightImages[18]);
		runRight = new Animation(ANIM_TIME).addFrame(rightImages[1]).addFrame(rightImages[2]).addFrame(rightImages[3]).addFrame(rightImages[4]).addFrame(rightImages[15]).addFrame(rightImages[16]).addFrame(rightImages[17]).addFrame(rightImages[18]);
		sitRight = new Animation(ANIM_TIME).addFrame(rightImages[10]);
		stillJumpRight = new Animation(ANIM_TIME).addFrame(rightImages[5], 300).addFrame(rightImages[6], 4000);
		runningJumpRight = new Animation(ANIM_TIME).addFrame(rightImages[7]).addFrame(rightImages[8], 400).addFrame(rightImages[9], 4000);
		changeRight = new Animation(ANIM_TIME).addFrame(rightImages[1]);
		stillUpRight = new Animation(ANIM_TIME).addFrame(rightImages[11]);
		deadRight = new Animation(ANIM_TIME-60).addFrame(rightImages[12], 500).addFrame(rightImages[13]).addFrame(rightImages[14], 3000);
		
		stageWin = new Animation(ANIM_TIME+60).addFrame(rightImages[0]).addFrame(rightImages[20]).addFrame(rightImages[21]).addFrame(rightImages[22]).addFrame(rightImages[23]).addFrame(rightImages[24]);
		
		setAnimation(stillRight);
		currLeftAnim = walkLeft;
		currRightAnim = walkRight;
	}
	public static void newPoint(int a){
		point = a;
	}
	public static void setPoint(int a){
		point += a;
	}
	public static int getPoint(){
		return point;
	}
	public boolean isStageComplete(){
		return isStageComplete;
	}
	
	public int getHealth(){
		return health;
	}
	
	public void setHealth(int x){
		health = x;
	}
	
	public boolean isInvisible(){
		return isInvisible;
	}
	public void setIsInvisible(boolean b){
		isInvisible = b;
	}
	
	// A function for isSlopedTile().
	public boolean isOnSlopedTile() { return onSlopedTile; }
	
	public void setIsJumping(boolean isJumping){
		this.isJumping = isJumping;
	}
	
	public boolean isJumping(){
		return isJumping;
	}
	
	private void slowSpeed(int factor){
		setdX(getdX()/factor);
	}
	
	private void accelerateFall(){
		setdY(-getdY()/3);
	}
	
	//A function for isAbovePlatform()
	public boolean isAbovePlatform() { return isAbovePlatform; }
	
	
	/**
	 * Fixes Y movement on tiles and platforms where animation height changes by setting the Ju's y
	 * value to the difference between animation heights. 
	 */
	//overrides the setAnimation in Animatiable. Adjusts the animation for jumps as
	//well as regular cases.
	public void setAnimation(Animation newAnim){
		if(currentAnimation() != null){
			Animation currAnim = currentAnimation();
			int oldHeight = currAnim.getHeight();
			int newHeight = newAnim.getHeight();
			if(newHeight > oldHeight){
				setY(getY() - (newHeight - oldHeight));
			}
			else{
				setY(getY() + (newHeight - oldHeight));
			}
			//if newHeight == oldHeight, nothing happens.
		}
		super.setAnimation(newAnim);
	}
	
	
	private Platform getPlatformCollisionX(TileMap map, float oldX, float oldY, float newX, float newY) {
		for(Platform platform : map.platforms()) {
			float width = getWidth();
			float height = getHeight();
			float pX = platform.getX();
			float pY = platform.getY();
			float oldpX = platform.getOldX();
			float pWidth = platform.getWidth();
			float pHeight = platform.getHeight();
		
			if(oldX + width <= oldpX && // This is needed to make transparant platforms work
					!platform.canJumpThrough() &&
					newX + width >= pX && newX + width <= pX + pWidth &&
					pY + pHeight > oldY && pY < oldY + height
					){
				this.isLeftOfPlatform = true;
				this.isRightOfPlatform = false;
				return platform;
			} else if (
					oldX >= oldpX + pWidth && // This is needed to make transparant platforms work
					!platform.canJumpThrough() &&
					newX <= pX + pWidth && newX >= pX &&
					pY + pHeight > oldY && pY < oldY + height
					){
				this.isRightOfPlatform = true;
				this.isLeftOfPlatform = false;
				return platform;
			}
		}
		this.isRightOfPlatform = false;
		this.isLeftOfPlatform = false;
		return null;
	}
	
	private Platform getPlatformCollisionY(TileMap map, float oldX, float oldY, float newX, float newY) {
		for(Platform platform : map.platforms()) {
	    	float width = getWidth();
	    	float height = getHeight();
	    	float pX = platform.getX();
	    	float pY = platform.getY();
	    	float oldpY = platform.getOldY();
	    	float pWidth = platform.getWidth();
	    	float pHeight = platform.getHeight(); 
	    	
	    	if(
	    	   newY + height >= pY &&
	    	   newY + height <= pY + pHeight &&
	    	   oldX + width >= pX &&
	    	   oldX <= pX + pWidth &&
	    	   oldY + height <= oldpY) { 
		    	this.isAbovePlatform = true;
		    	this.isBelowPlatform = false;
		    	return platform;
	    	} else if(!platform.canJumpThrough()) {
	    	   if (oldY >= oldpY + pHeight && 
	    		   newY <= pY + pHeight &&
	    		   newY >= pY &&
		           oldX + width >= pX &&
		           oldX <= pX + pWidth) {
		        	   this.isBelowPlatform = true;
		        	   this.isAbovePlatform = false;
		        	   return platform;
		           }
	    	}
	    }
	    this.isBelowPlatform = false;
	    this.isAbovePlatform = false;
	    return null;
	}
	
	
	public void update(TileMap map, float time){
		jumpHeight = INITIAL_JUMP_HEIGHT - Math.abs(dx)*JUMP_MULTIPLIER; //making the jump larger
		
		if(!frictionLock && isLeftHeld && !isShiftHeld){ //only left arrow
			toggleMovement(1); //Making sure that the current anim is set to walk
			if(dx < -TERMINAL_WALKING_DX){
				dx = dx + TERMINAL_WALKING_DX; //slowing the speed. from run to walk.
			}
			else if(dx > - TERMINAL_WALKING_DX){
				dx = dx - TERMINAL_WALKING_DX; //speeding up.
			}
			//when dx == -TERMINAL_WALKING_DX no change is made, max of walking speed.
		}
		else if(!frictionLock && isRightHeld && !isShiftHeld){
			toggleMovement(1);
			if(dx < TERMINAL_WALKING_DX){
				dx = dx + TERMINAL_WALKING_DX; // speeding up.
			}
			else if(dx > TERMINAL_WALKING_DX){
				dx = dx - TERMINAL_WALKING_DX; // slowing
			}
			//when dx == TERMINAL_WALKING_DX no change is made, max of walking speed.
		}
		else if(!frictionLock && isLeftHeld && isShiftHeld){
			if(dx > -TERMINAL_WALKING_DX){
				dx = dx - WALKING_DX_INC; //Initially speeding up as walking speed. //walking anim
			}
			else if(dx > -TERMINAL_RUNNING_DX){
				if(dx == -START_RUN_ANIM_THRESHOLD){
					toggleMovement(2); //setting up Running animation.
				}
				dx -= RUNNING_DX_INC;
				//No slowing condition. As it's the top speed.
			}
		}
		else if(!frictionLock && isRightHeld && isShiftHeld){
			if(dx < TERMINAL_WALKING_DX){
				dx = dx + WALKING_DX_INC;
			}
			else if(dx < TERMINAL_RUNNING_DX){
				if(dx == START_RUN_ANIM_THRESHOLD){
					toggleMovement(2);
				}
				dx += RUNNING_DX_INC;
			}
		}
		else if(!isRightHeld && !isLeftHeld && isShiftHeld){
			if(currentAnimation() == currRightAnim){
				currRightAnim = runRight;
			}
			else if (currentAnimation() == currLeftAnim) {
				currLeftAnim = runLeft;
			}
			if(dx!=0){
				frictionLock = true;
				if(dx > -.05f && dx < .05f){ //When speed almost zero. Making a stop.
					dx = 0;
					frictionLock = false;
				}
				//Slowing due to friction.
				else if(dx >= .05){
					dx =  dx - FRICTION*time;
				}
				else if(dx <= -.05){
					dx = dx + FRICTION*time;
				}
			}
		}
		else{
			toggleMovement(3);	//Either right/left arrow is not held or frictionLock is true.
			if(dx != 0){
				frictionLock = true;
				if(dx > -.05f && dx < .05f){ //When speed almost zero. Making a stop.
					dx = 0;
					frictionLock = false;
				}
				//Slowing due to friction.
				else if(dx >= .05){
					dx =  dx - FRICTION*time;
				}
				else if(dx <= -.05){
					dx = dx + FRICTION*time;
				}
			}
		}
		
		
		// Apply gravity.
		if(getdY() < TERMINAL_FALL_DY) { 
			setdY(getdY() + GRAVITY * time);
		}
		
		//A slope function. 
		if(getOffsetX() != 0) { setOffsetX(getOffsetX() - 1);} //still not sure of it's purpose 
		
		float oldX = getX();
		float newXCalc = oldX + getdX()*time;
		//A platform related calculation. 
		if(platform != null) { newXCalc += platform.getdX() * time; } 
		float oldY = getY();
		float newYCalc = oldY + getdY()*time;
		
		//calculate and save the tile collisions
		ArrayList<Point> xTile = JuRenderer.getTileCollisionAll(map, this, oldX, oldY, newXCalc, oldY);
		ArrayList<Point> yTile = JuRenderer.getTileCollisionAll(map, this, oldX, oldY, oldX, newYCalc);
		
		int numOfXTiles = xTile.size();
		int numOfYTiles = yTile.size();
		
		Platform platformX = getPlatformCollisionX(map, oldX, oldY, newXCalc, newYCalc);
		Platform platformY = getPlatformCollisionY(map, oldX, oldY, newXCalc, newYCalc);
		if(isAbovePlatform) {
			platform = platformY;
		} else {
			platform = null;
		}
		// Manage collision in the X direction.
		if(oldX<0){		//collision with the left border of the map.
			setX(JuRenderer.tilesToPixels(0));
			slowSpeed(20);
		}
		
		else if(oldX > JuRenderer.tilesToPixels(map.getWidth()) - 450){//collision with the left border of the map.
															// 64 is the width of the sprite
			slowSpeed(20);
			isStageComplete = true;
		}
		else{
			if(numOfXTiles == 0){	//No tile collision in X direction
				setX(newXCalc);
			}
			else{
				Point xtp = xTile.get(0); // First tile.
				Collision c = Creature.tileCollisionX(map.getTile(xtp.x, xtp.y), this);
				toggleMovement(1); //Still animation
				frictionLock = false;
				if(c == Collision.EAST){ //Left of a tile
					setX(JuRenderer.tilesToPixels(xtp.x) - getWidth());
				}
				else if(c == Collision.WEST){
					setX(JuRenderer.tilesToPixels(xtp.x + 1));
				}
				if(!isAbovePlatform) { setdX(0); } // Velocity 0, making a stop. 
				//setdX(0) is called inside a platform obj.
			}
			if (platformX != null) { 
				slowSpeed(2);
				if(isLeftOfPlatform) {
					setX(platformX.getX() - getWidth() - 1);
				} else if(isRightOfPlatform) {
					setX(platformX.getX() + platformX.getWidth() + 1);
				}
			}
		}
		
		super.update((int)time); // update animation.
		
		// Manage collision in the Y direction.
		boolean upperCollision = false; // will check if Ju is above a tile
		
		if(oldY > JuRenderer.tilesToPixels(map.getHeight()) - getHeight()) { // Off the bottom of the map.
			health = -999;
		}
		else{	// No Y collision, allow Y position to update uninterrupted.
			if(numOfYTiles == 0){
				setY(newYCalc);
				setIsJumping(true);
				jump();
			}
			else{
				Point ytp = yTile.get(0); // First Tile
				Collision c = Creature.tileCollisionY(map.getTile(ytp.x, ytp.y), this);
				fixJumping();
				if(c == Collision.NORTH) { // Downward collision with tile.
					upperCollision = true;
					setIsJumping(false);
					setY(JuRenderer.tilesToPixels(ytp.y) - getHeight());
				}
				else if (c == Collision.SOUTH) { // Upward collision with tile.
					for(Point p : yTile){
						GameTile tile = map.getTile(p.x, p.y);
						if(tile!=null){
							tile.doAction();
						}
					}
					setY(JuRenderer.tilesToPixels(ytp.y + 1));
					accelerateFall(); 
				}
			}
			if (platformY != null && !upperCollision) { 
				fixJumping();
				if(isAbovePlatform) { // Downward collision with platform.
					setIsJumping(false);
					setY(platformY.getY() - getHeight());
				} else if (isBelowPlatform) { // Upward collision with platform.
					setY(platformY.getY() + platformY.getHeight() + 1);
					accelerateFall(); 
				}
			}
		}
	}
	
	public void juToTileToBaddieCollide(GameTile tile) { //works in a different way. skipped.
		List<Creature> toRemove = new LinkedList<Creature>();
		for(Creature c : tile.collidingCreatures()) {
			if(c instanceof Penguin){
				c.flip();
				toRemove.add(c);
			}  
		}
		for(Creature c : toRemove) { tile.collidingCreatures().remove(c); }
	}
	
	// Determines what happens when Ju collides with a creature.
	public void playerCollision(TileMap map, Creature creature){
		// only check collision of creatures with this that are not sleeping, are on the screen, and are collidable
		if(!creature.isPlatform() && creature.isCollidable()){
			boolean collision = isCollision(this, creature);
			if(collision && !(creature instanceof Score)){ //#check
				//A lot of work Creature related.
				if(creature instanceof Coin){
					creature.kill();
					point++;
					map.creaturesToAdd().add(new Score(Math.round(creature.getX()), Math.round(creature.getY() - 16)));
				}else if(creature instanceof Mushroom) {
					creature.kill();
					if(health > 2) {
					map.creaturesToAdd().add(new Score(Math.round(creature.getX()), Math.round(creature.getY()+13)));
					point += 10;
					} else {
						health++;
					}
				}
				else if(creature instanceof Penguin && isJumping() && getdY() > 0) {
					System.out.println("OK");
					((Penguin) creature).jumpedOn(); // kill
					this.creatureHop();
				}
				else{
					getDamaged();
				}
			}
		}
	}
	
	public void setIsBlinking(boolean b){
		isBlinking = b;
	}
	public boolean isBlinking(){
		return isBlinking;
	}
	public void getDamaged(){
		if(!isBlinking){
			health--;
			if(health>=0)
				isBlinking = true;
			
		}
	}
	
	public void creatureHop() {
		isJumping = true;
		setY(y -5);
		if(!isShiftHeld) {
			setdY(jumpHeight/2f); // jump
		} else {
			setdY(jumpHeight/1.4f);
		}
	}
	//for changing the current animation.
	//1 == walk
	//2 == run
	//3 == still
	public void toggleMovement(int type){
		if(type == 1){
			currLeftAnim = walkLeft;
			currRightAnim = walkRight;
		}
		else if(type == 2){
			currLeftAnim = runLeft;
			currRightAnim = runRight;
		}
		else if(type == 3){
			currLeftAnim = stillLeft;
			currRightAnim = stillRight;
		}
	}
	
	
	public void fixJumping() {
		if(!isRightHeld && !isLeftHeld) {
			if(currentAnimation() == runningJumpLeft || currentAnimation() == stillJumpLeft) {
				setAnimation(stillLeft);
			}
			if(currentAnimation() == runningJumpRight || currentAnimation() == stillJumpRight) {
				setAnimation(stillRight);
			}
		} else {
			if(!this.frictionLock) {
				if(isRightHeld) {
					setAnimation(currRightAnim);
				} else if (isLeftHeld) {
					setAnimation(currLeftAnim);
				}
			} else {
				if(isRightHeld) {
					setAnimation(currRightAnim);
				} else if (isLeftHeld) {
					setAnimation(currLeftAnim);
				}
			}
		}
	}
	
	public void jump() {
		setIsJumping(true);
		if(currentAnimation() == currLeftAnim || currentAnimation() == stillLeft || currentAnimation() == changeRight) {
    		if(currLeftAnim == runLeft || currLeftAnim == walkLeft){
    			setAnimation(runningJumpLeft);
    		}
    		else {
				setAnimation(stillJumpLeft);
    		}
    	}
    	if(currentAnimation() == currRightAnim || currentAnimation() == stillRight || currentAnimation() == changeLeft) {
    		if(currRightAnim == runRight || currRightAnim == walkRight){
    			setAnimation(runningJumpRight);
    		}
    		else{
    			setAnimation(stillJumpRight);
    		}
    	}
	}
	
	public void noKeyListeners(){
		isUpHeld = false;
		isDownHeld = false;
		isLeftHeld = false;
		isRightHeld = false;
		isShiftHeld = false;
		isSpaceHeld = false;
	}
	
	//For KeyListener in JPanel
	public void keyPressed(KeyEvent e){
		int key = e.getKeyCode();
		
		if(!isStageComplete && health >= 0){
			if(key == KeyEvent.VK_UP){
				isUpHeld = true;
				if(currentAnimation() == currLeftAnim || currentAnimation() == stillLeft || currentAnimation() == sitLeft) {
					setAnimation(stillUpLeft);
				}
				if(currentAnimation() == currRightAnim || currentAnimation() == stillRight || currentAnimation() == sitRight) {
					setAnimation(stillUpRight);
				}
			}
			
			if(key == KeyEvent.VK_LEFT){
				isLeftHeld = true;
				if(!isDownHeld){
					setAnimation(currLeftAnim);
				}
			}
			
			if(key == KeyEvent.VK_RIGHT){
				isRightHeld = true;
				if(!isDownHeld){
					setAnimation(currRightAnim);
				}
			}
			
			if(key == KeyEvent.VK_SHIFT) {
				this.isShiftHeld = true;
			}
			
			if(key == KeyEvent.VK_DOWN) {
				isDownHeld = true;
				if(currentAnimation() == currLeftAnim || currentAnimation() == stillLeft || currentAnimation() == sitLeft) {
					setAnimation(sitLeft);
				}
				if(currentAnimation() == currRightAnim || currentAnimation() == stillRight || currentAnimation() == sitRight) {
					setAnimation(sitRight);
				}
			}
			
			if(key == KeyEvent.VK_SPACE){
				if(!isJumping){
					isSpaceHeld = true;
					isJumping = true;
					dy = jumpHeight;
					//A call to a sound function for jump
				}
			}
		}
		else {
			noKeyListeners();
		}
	}
	
	public void keyReleased(KeyEvent e){
		int key = e.getKeyCode();
		if(health>=0){
			if(key == KeyEvent.VK_LEFT){
				isLeftHeld = false;
				if(!isJumping){
					setAnimation(stillLeft);
				}
			}
			
			if(key == KeyEvent.VK_RIGHT){
				isRightHeld = false;
				if(!isJumping){
					setAnimation(stillRight);
				}
			}
			
			if(key == KeyEvent.VK_UP){
				isUpHeld = false;
				if (currentAnimation() == stillUpLeft || currentAnimation() == currLeftAnim || currentAnimation() == changeLeft) {
					setAnimation(stillLeft);
				}
				if (currentAnimation() == stillUpRight || currentAnimation() == currRightAnim || currentAnimation() == changeRight) {
					setAnimation(stillRight);
				}
			}
			
			if(key == KeyEvent.VK_SHIFT){
				this.isShiftHeld = false;
			}
			
			if(key == KeyEvent.VK_SPACE){
				isShiftHeld = false;
				dy = this.getdY()/2.5f;		//JUMPS OF DIFFERENT HEIGHT
			}
			
			if(key == KeyEvent.VK_DOWN){
				isDownHeld = false;
				if (currentAnimation() == sitLeft || currentAnimation() == currLeftAnim || currentAnimation() == changeLeft) {
					setAnimation(stillLeft);
				}
				if (currentAnimation() == sitRight || currentAnimation() == currRightAnim || currentAnimation() == changeRight) {
					setAnimation(stillRight);
				}
			}
		}
		else {
			noKeyListeners();
		}
	}
}
