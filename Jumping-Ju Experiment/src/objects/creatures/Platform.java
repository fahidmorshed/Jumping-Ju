package objects.creatures;

import java.awt.image.BufferedImage;

import objects.base.Creature;
import utility.ImageManipulator;
import core.animation.Animation;
import core.tile.TileMap;

public class Platform extends Creature {
	protected Animation move;
	protected int turn;
	protected boolean isVertical = false;
	protected boolean isHorizontal = false;
	protected boolean switchedVertical = false;
	protected boolean switchedHorizontal = false;
	protected boolean canJumpThrough = false;
	protected float oldX;
	protected float oldY;
	protected float oldDx;
	protected float oldDy;
	private TypeOfPlatform type;
	private int turns;
	
	public Platform(int pixelX, int pixelY){
		this(pixelX, pixelY, 1, TypeOfPlatform.BOTH, 801);
	}
	public Platform(int pixelX, int pixelY, int size){
		this(pixelX, pixelY, size, TypeOfPlatform.BOTH, 801);
	}
	
	public Platform(int pixelX, int pixelY, int size, TypeOfPlatform type){
		this(pixelX, pixelY, size, type, 801);
	}
	public Platform(int pixelX, int pixelY, int size, TypeOfPlatform type, int turns){
		super(pixelX, pixelY);
		setIsAlwaysRelevant(true);
		setIsPlatform(true);
		turn = 1;
		this.turns = turns;
		dx = 0;
		dy = 0;
		setType(type);
		BufferedImage woodenPlatform;
		if(size == 1){
			woodenPlatform = ImageManipulator.loadImage("Data/Platform/woodenTile.png");
		}
		else if(size == 2){
			woodenPlatform = ImageManipulator.loadImage("Data/Platform/woodenTile2.png");
		}
		else{
			woodenPlatform = ImageManipulator.loadImage("Data/Platform/woodenTile.png");
		}
		move = new Animation(100).addFrame(woodenPlatform);
		setAnimation(move);
	}
	
	public void setTurns(int turns){
		this.turns = turns;
	}
	
	public int getTurns(){
		return turns;
	}
	
	public void setType(TypeOfPlatform type){
		this.type = type;
	}
	
	public TypeOfPlatform getType(){
		return type;
	}
	
	public float getOldX() {
		return oldX;
	}
	
	public float getOldY() {
		return oldY;
	}
	
	public void setOldX(float oldX) {
		this.oldX = oldX;
	}
	
	public void setOldY(float oldY) {
		this.oldY = oldY;
	}
	
	public boolean canJumpThrough() {
		return canJumpThrough;
	}
	
	public boolean isHorizontal() {
		return isHorizontal;
	}
	
	public boolean isVertical() {
		return isVertical;
	}
	
	public float getLastdX() {
		return oldDx;
	}
	
	public float getLastdY() {
		return oldDy;
	}
	
	public boolean switchedVertical() {
		return switchedVertical;
	}
	
	public boolean switchedHorizontal() {
		return switchedHorizontal;
	}
	
	public void updateCreature(TileMap map, int time){
		if(getType()==TypeOfPlatform.BOTH){
			if(dx != 0) {
				isHorizontal = true;
			}
			if(dy != 0) {
				isVertical = true;
			}
			
			if(turn == turns) {
				turn = 1;
			}
			
			if(turn <= turns/2) {
				oldDx = dx;
				oldDy = dy;
				dx = .07f;
				dy = .05f;
			} else if(turn > turns/2) {
				oldDy = dy;
				oldDx = dx;
				dx = -.07f;
				dy = -.05f;
			} 
			turn += 2;
			oldX = x;
			oldY = y;
			x = x + time*dx;
			y = y + time*dy;
			
			if((oldDx > 0 && dx <0) || (oldDx < 0 && dx > 0)){
				this.switchedHorizontal = true;
			}
			else{
				this.switchedHorizontal = false;
			}
			
			if((oldDy > 0 && dy < 0) || (oldDy < 0 && dy > 0)) {
				this.switchedVertical = true;
			} else {
				this.switchedVertical = false;
			}
		}
		else if(getType()==TypeOfPlatform.BOTHi){
			if(dx != 0) {
				isHorizontal = true;
			}
			if(dy != 0) {
				isVertical = true;
			}
			
			if(turn == turns) {
				turn = 1;
			}
			
			if(turn <= turns/2) {
				oldDx = dx;
				oldDy = dy;
				dx = -.07f;
				dy = .05f;
			} else if(turn > turns/2) {
				oldDy = dy;
				oldDx = dx;
				dx = .07f;
				dy = -.05f;
			} 
			turn += 2;
			oldX = x;
			oldY = y;
			x = x + time*dx;
			y = y + time*dy;
			
			if((oldDx > 0 && dx <0) || (oldDx < 0 && dx > 0)){
				this.switchedHorizontal = true;
			}
			else{
				this.switchedHorizontal = false;
			}
			
			if((oldDy > 0 && dy < 0) || (oldDy < 0 && dy > 0)) {
				this.switchedVertical = true;
			} else {
				this.switchedVertical = false;
			}
		}
		else if(getType()==TypeOfPlatform.VERTICAL){
			if(dx != 0) {
				isHorizontal = true;
			}
			if(dy != 0) {
				isVertical = true;
			}
			
			if(turn == turns) {
				turn = 1;
			}
			
			if(turn <= turns/2) {
				oldDx = dx;
				oldDy = dy;
				dx = .07f;
				//dy = .025f;
			} else if(turn > turns/2) {
				oldDy = dy;
				oldDx = dx;
				dx = -.07f;
				//dy = -.025f;
			} 
			turn += 2;
			oldX = x;
			oldY = y;
			x = x + time*dx;
			//y = y + time*dy;
			
			if((oldDx > 0 && dx <0) || (oldDx < 0 && dx > 0)){
				this.switchedHorizontal = true;
			}
			else{
				this.switchedHorizontal = false;
			}
			
			if((oldDy > 0 && dy < 0) || (oldDy < 0 && dy > 0)) {
				this.switchedVertical = true;
			} else {
				this.switchedVertical = false;
			}
		}
		else{	//HORIZONTAL
			if(dx != 0) {
				isHorizontal = true;
			}
			if(dy != 0) {
				isVertical = true;
			}
			
			if(turn == turns) {
				turn = 1;
			}
			
			if(turn <= turns/2) {
				oldDx = dx;
				oldDy = dy;
			//	dx = .05f;
				dy = .05f;
			} else if(turn > turns/2) {
				oldDy = dy;
				oldDx = dx;
			//	dx = -.05f;
				dy = -.05f;
			} 
			turn += 2;
			oldX = x;
			oldY = y;
			//x = x + time*dx;
			y = y + time*dy;
			
			if((oldDx > 0 && dx <0) || (oldDx < 0 && dx > 0)){
				this.switchedHorizontal = true;
			}
			else{
				this.switchedHorizontal = false;
			}
			
			if((oldDy > 0 && dy < 0) || (oldDy < 0 && dy > 0)) {
				this.switchedVertical = true;
			} else {
				this.switchedVertical = false;
			}
		}
	}
}
