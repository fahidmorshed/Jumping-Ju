package core;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import objects.base.Creature;
import objects.creatures.Coin;
import objects.creatures.Platform;
import objects.ju.Ju;
import utility.ImageManipulator;
import core.animation.Sprite;
import core.tile.GameTile;
import core.tile.Tile;
import core.tile.TileMap;


public class JuRenderer {
	private int AdjustScroll = 0;	//For keeping the Ju's height constant while no significant
									//change has been made. 
	//arraylist for a tilemap
	private ArrayList<TileMap> maps = new ArrayList<TileMap>();
	private int lastLife = -5;
	
	private static final int TILE_SIZE = 32; //Probably no use.
	private static final int TILE_SIZE_BITS = 5; //used as a power. can be repleced with 16 if directly used
	
	private Image background;
	private int point;
	private String pointString;
	private BufferedImage gameOverImage = ImageManipulator.loadImage("Data/Background/gameOver.png");
	private BufferedImage lifeImage = ImageManipulator.loadImage("Data/HUD/life.png");
	private BufferedImage stageCompleteImage = ImageManipulator.loadImage("Data/Background/stageComplete.png");
	private BufferedImage gameWinImage = ImageManipulator.loadImage("Data/Background/win.png");
	//EVERY TILE IS 32x32, remember.
	public static int pixelsToTiles(float pixels){
		return pixelsToTiles(Math.round(pixels));
	}
	// Converts a pixel position to a tile position.
	//Dividing every pixles with 16 to get the tile position.
	public static int pixelsToTiles(int pixels){
		return pixels >> TILE_SIZE_BITS;
	}
	// Converts a tile position to a pixel position.
	public static int tilesToPixels(int tiles){
		return tiles << TILE_SIZE_BITS;
	}
	
	// Sets the background to draw.
	public void setBackground(BufferedImage background){
		this.background = background;
	}
	
	// Returns the tile that a Sprite has collided with. Returns null if no 
	// collision was detected. The last parameter, right, is used to check if multiple blocks
	// are hit when a sprite jumps.
	public static Point getTileCollision(TileMap map, Sprite sprite, float currX, float currY, float newX, float newY){
		
		float fromX = Math.min(currX, newX);
		float fromY = Math.min(currY, newY);
		float toX = Math.max(currX, newX);
		float toY = Math.max(currY, newY);
		
		// get the tile locations
		int fromTileX = JuRenderer.pixelsToTiles(fromX);
		int fromTileY = JuRenderer.pixelsToTiles(fromY);
		int toTileX = JuRenderer.pixelsToTiles(toX + sprite.getWidth() - 1);
		int toTileY = JuRenderer.pixelsToTiles(toY + sprite.getHeight() - 1);
		
		// check each tile for a collision
		for (int x=fromTileX; x<=toTileX; x++){
			for(int y=fromTileY; y<=toTileY; y++){
				if(x < 0 || x>=map.getWidth() || map.getImage(x, y)!=null){ //for x outside map
					Tile tile = map.getTile(x, y);
					if(tile!=null && map.getTile(x, y).isCollidable()){
						// collision found and the tile is collidable, return the tile
						return new Point(x, y);
					}
				}
			}
		}
		//NO collusion. 
		return null;
	}
	
	public static ArrayList<Point> getTileCollisionAll(TileMap map, Sprite sprite, float currX, float currY, float newX, float newY) {
		
		ArrayList<Point> collisionPoints = new ArrayList<Point>(); 
		float fromX = Math.min(currX, newX);
		float fromY = Math.min(currY, newY);
		float toX = Math.max(currX, newX);
		float toY = Math.max(currY, newY);
	
		// get the tile locations
		int fromTileX = JuRenderer.pixelsToTiles(fromX);
		int fromTileY = JuRenderer.pixelsToTiles(fromY);
		int toTileX = JuRenderer.pixelsToTiles(toX + sprite.getWidth() - 1);
		int toTileY = JuRenderer.pixelsToTiles(toY + sprite.getHeight() - 1);
	
		// check each tile for a collision
		for (int x=fromTileX; x<=toTileX; x++) {
			for (int y=fromTileY; y<=toTileY; y++) {
				if (x < 0 || x >= map.getWidth() || map.getImage(x, y) != null) {
					Tile tile = map.getTile(x,y);
					if(tile != null && map.getTile(x, y).isCollidable()) {
					// collision found and the tile is collidable, return the tile
						collisionPoints.add(new Point(x,y));
					}
				}
			}
		}
	    // no collision found, return null
	    return collisionPoints;
	}
	
	//Draw a hole map // NEW
	public void draw(Graphics2D g, TileMap mainMap, TileMap backgroundMap, TileMap foregroundMap, int screenWidth, int screenHeight){
		maps.add(backgroundMap);
		maps.add(mainMap);
		maps.add(foregroundMap);
		Ju player = mainMap.getPlayer();
		int mapWidth = tilesToPixels(mainMap.getWidth());
		int mapHeight = tilesToPixels(mainMap.getHeight());
		
		// get the scrolling position of the map based on player's position...
		int offsetX = screenWidth/2 - Math.round(player.getX()) - TILE_SIZE;
		offsetX = Math.min(offsetX, 0);// if this gets set to 0, player is within a screen width
		offsetX = Math.max(offsetX, screenWidth - mapWidth);
		
		int round = Math.round(player.getY());
		
		// initialize AdjustYScroll
		if(AdjustScroll==0){
			AdjustScroll = round;
		}
		
		// if the player is jumping, change the level at which the screen is drawn.
		if(player.isJumping() || player.isOnSlopedTile() || player.isAbovePlatform()){
			AdjustScroll = round;
		}
		
		int offsetY = screenHeight/2 - AdjustScroll -TILE_SIZE;
		offsetY = Math.min(offsetY, 0);
		offsetY = Math.max(offsetY, screenHeight - mapHeight - 25);
		
		if(background!=null){
			// x and y are responsible for fitting the background image to the size of the map
			int x = offsetX * (screenWidth - background.getWidth(null))/(screenWidth - mapWidth);
			int y = offsetY * (screenHeight - background.getHeight(null))/(screenHeight - mapHeight);
			g.drawImage(background, x, y, null);
		}
		
		int firstTileX = pixelsToTiles(-offsetX) - 15;
		int lastTileX = firstTileX + pixelsToTiles(screenWidth) + 16;
		int firstTileY = pixelsToTiles(-offsetY);
		int lastTileY = firstTileY + pixelsToTiles(screenHeight) + 1;
		
		for(TileMap map: maps){
			if(map!=null){
				for(int y=firstTileY; y<=lastTileY; y++){
					for(int x=firstTileX; x<=lastTileX; x++){
						GameTile tile = map.getTile(x, y);
						if(tile!=null){
							tile.draw(g, tilesToPixels(x), tilesToPixels(y), 
									tile.getOffsetX() + offsetX, tile.getOffsetY() + offsetY);
						}
					}
				}
			}
			if(map == mainMap){
				for(int i=0; i<map.creatures().size(); i++){
					Creature creature = map.creatures().get(i);
					
					int x = Math.round(creature.getX()) + offsetX;
					int y = Math.round(creature.getY()) + offsetY;
					int tileX = pixelsToTiles(x);
					int tileY = pixelsToTiles(y);
					
					if(!creature.isAlive()){
						map.creatures().remove(i);
						i--;
					}
					else{
						if(Creature.WAKE_UP_VALUE_UP_LEFT <= tileX && Creature.WAKE_UP_VALUE_DOWN_RIGHT >= tileX &&
								Creature.WAKE_UP_VALUE_UP_LEFT <= tileY && Creature.WAKE_UP_VALUE_DOWN_RIGHT >= tileY){
							if(creature instanceof Platform){
								map.platforms().add((Platform) creature);
							}
							if(creature.isSleeping()){
								creature.wakeUp();
							}
							creature.setIsOnScreen(true);
							if(!creature.isInvisible()){
								creature.draw(g, x, y);
							}
							map.relevantCreatures().add(creature);
						}
						else{
							if(creature.isAlwaysRelevant()){
								map.relevantCreatures().add(creature);
							}
							creature.setIsOnScreen(false);
						}
					}
				}
				if(player.isBlinking()){
					if(player.isInvisible()){
						player.setIsInvisible(false);
					}
					else{
						player.setIsInvisible(true);
					}
				}
				if(!(((Ju)player).isInvisible())){
					player.draw(g, Math.round(player.getX()) + offsetX, 
							Math.round(player.getY()) + offsetY, player.getOffsetX(), player.getOffsetY());
					
				}
			}
		}
		
		lastLife = player.getHealth();
		for(int i=0; i<lastLife; i++){
			g.drawImage(lifeImage, i*48, 0, null);
		}
		if(player.isStageComplete() && JuPanel.stage != 3){
			g.drawImage(stageCompleteImage, 205, 150, null);
		}
		else if(player.isStageComplete() && JuPanel.stage == 3){
			g.drawImage(gameWinImage, 235, 150, null);
		}
		pointString = Integer.toString(player.getPoint());
		int j = 0;
		for(int i=pointString.length()-1; i>=0; i--){
			int x = pointString.charAt(i) - '0';
			BufferedImage numberImage = Coin.getNumberImage(x);
			g.drawImage(numberImage, 755 - j*32, 0, null);
			j++;
		}
		if(player.getHealth()<0){
			g.drawImage(gameOverImage, 200, 150, 400, 200, null);
		}
		
		maps.clear();
	}
}