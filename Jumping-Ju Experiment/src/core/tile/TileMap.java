package core.tile;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import objects.base.Creature;
import objects.creatures.Platform;
import objects.ju.Ju;
import objects.tiles.SlopedTile;

public class TileMap {
	private GameTile[][] tiles;
	private List<GameTile> animatedTiles;
	private Ju player;
	private List<SlopedTile> slopedTiles;
	private List<Platform> platforms;
	private List<Creature> creatures;
	private List<Creature> relevantCreatures; // List of relevant Creatures to the current frame.
											// This is a subset of creatures.
	private List<Creature> creaturesToAdd; // List of Creatures to be added inbetween frames.
	
	public TileMap(int width, int height){
		tiles = new GameTile[width][height]; //NOT INITIALIZED.
		creatures = new LinkedList<Creature>();
		animatedTiles = new ArrayList<GameTile>(); //NOT INITIALIZED.
		slopedTiles = new ArrayList<SlopedTile>();
		platforms = new ArrayList<Platform>();
		relevantCreatures = new ArrayList<Creature>();
		creaturesToAdd = new ArrayList<Creature>();
	}
	
	public GameTile[][] getTiles(){
		return tiles;
	}
	
	public int getWidth(){
		return tiles.length;
	}
	
	public int getHeight(){
		return tiles[0].length;
	}
	
	public void setPlayer(Ju player){
		this.player = player;
	}
	
	public Ju getPlayer(){
		return player;
	}
	
	//return the GameTiles at tiles[x][y]. If x or y is out of bounds
	//or if tiles[x][y] == null, null is returned.
	public GameTile getTile(int x, int y){
		if(x<0 || x>=getWidth() || y<0 || y>=getHeight()){
			return null;
		}
		else {
			if(tiles[x][y] != null){
				return tiles[x][y];
			}
			else {
				return null;
			}
		}
	}
	
	//return the image of the GameTiles at tiles[x][y]. If x or y is out of bounds
	//or if tiles[x][y] == null, null is returned.
	public BufferedImage getImage(int x, int y){
		if(x<0 || x>=getWidth() || y<0 || y>=getHeight()){
			return null;
		}
		else{
			if(tiles[x][y]!=null){
				return tiles[x][y].getImage();
			}
			else{
				return null;
			}
		}
	}
	
	//Sets tiles[x][y] equal to parameter tile.
	//This is used to set animated GameTiles.
	public void setTile(int x, int y, GameTile tile) {
		tiles[x][y] = tile;
	}
	
	//Sets tiles[x][y] equal to a new Tile with no animation and the constant Image img.
	//This is used to set non-animated GameTiles.
	public void setTile(int x, int y, BufferedImage img) {
		tiles[x][y] = new GameTile(x, y, null, img);
	}
	
	public List<SlopedTile> slopedTiles() {
		return slopedTiles;
	}
	
	public List<GameTile> animatedTiles() {
		return animatedTiles;
	}
	
	public List<Platform> platforms() {
		return platforms;
	}
	
	public List<Creature> creatures() {
		return creatures;
	}
	
	public List<Creature> creaturesToAdd() {
		return creaturesToAdd;
	}
	
	public List<Creature> relevantCreatures() {
		return relevantCreatures;
	}
}
