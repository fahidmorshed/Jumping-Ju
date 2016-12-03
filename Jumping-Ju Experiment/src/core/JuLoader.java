package core;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import objects.creatures.Coin;
import objects.creatures.Penguin;
import objects.creatures.Platform;
import objects.creatures.TypeOfPlatform;
import objects.tiles.QuestionBlock;
import utility.ImageManipulator;
import core.tile.GameTile;
import core.tile.TileMap;

public class JuLoader {
	private ArrayList<BufferedImage> planeTileImages;
	private ArrayList<BufferedImage> backgroundImages;
	private ArrayList<BufferedImage> foregroundImages;
	
	public JuLoader(){
		planeTileImages = new ArrayList<BufferedImage>();
		backgroundImages = new ArrayList<BufferedImage>();
		foregroundImages = new ArrayList<BufferedImage>();
		//Try block has been used inside ImageManupulator.loadImage()
		planeTileImages.add(ImageManipulator.loadImage("Data/Tile/groundTile.png"));
		planeTileImages.add(ImageManipulator.loadImage("Data/Tile/groundMudTile.png"));
		planeTileImages.add(ImageManipulator.loadImage("Data/Tile/brickTile.png"));
		planeTileImages.add(ImageManipulator.loadImage("Data/Tile/qusTile.png"));
		
		backgroundImages.add(ImageManipulator.loadImage("Data/Background/tree1.png"));
		backgroundImages.add(ImageManipulator.loadImage("Data/Background/tree2.png"));
		backgroundImages.add(ImageManipulator.loadImage("Data/Background/tree3.png"));
		backgroundImages.add(ImageManipulator.loadImage("Data/Background/tree4.png"));
		backgroundImages.add(ImageManipulator.loadImage("Data/Background/castle.png"));
		
		foregroundImages.add(ImageManipulator.loadImage("Data/Background/grass.png"));
		foregroundImages.add(ImageManipulator.loadImage("Data/Background/fence1.png"));
		foregroundImages.add(ImageManipulator.loadImage("Data/Background/fence2.png"));
	}
	
	//BufferedImage -> Image
	public static Image toImage(BufferedImage bufferedImage){
		return Toolkit.getDefaultToolkit().createImage(bufferedImage.getSource());
	}
	
	public TileMap loadMap(String filename) throws IOException{ //A sound object should be included. #check
		ArrayList<String> lines = new ArrayList<String>();
		int widthOfMap = 0; //In tiles
		int heightOfMap = 0; //In tiles
		Scanner mapReader = new Scanner(new File(filename));
		
		while(mapReader.hasNextLine()){
			String line = mapReader.nextLine();
			if(!line.startsWith("#")){				//For comment purpose and debugging
				lines.add(line);
				widthOfMap = Math.max(widthOfMap, line.length());
			}
		}
		heightOfMap = lines.size();
		mapReader.close();
		TileMap newTileMap = new TileMap(widthOfMap, heightOfMap);
		
		for(int y=0; y<heightOfMap; y++){
			String line = lines.get(y);
			for(int x=0; x<line.length(); x++){
				char ch = line.charAt(x);
				
				int pixelX = JuRenderer.tilesToPixels(x);
				int pixelY = JuRenderer.tilesToPixels(y);
				if(ch == '!'){
					GameTile treeDead = new GameTile(pixelX, pixelY, backgroundImages.get(0));
					newTileMap.setTile(x, y, treeDead);
				}
				if(ch == '@'){
					GameTile treeDead = new GameTile(pixelX, pixelY, backgroundImages.get(1));
					newTileMap.setTile(x, y, treeDead);
				}
				if(ch == '%'){
					GameTile treeDead = new GameTile(pixelX, pixelY, backgroundImages.get(2));
					newTileMap.setTile(x, y, treeDead);
				}
				if(ch == '^'){
					GameTile treeDead = new GameTile(pixelX, pixelY, backgroundImages.get(3));
					newTileMap.setTile(x, y, treeDead);
				}
				if(ch == '('){
					GameTile treeDead = new GameTile(pixelX, pixelY, foregroundImages.get(0));
					newTileMap.setTile(x, y, treeDead);
				}
				if(ch == ')'){
					GameTile treeDead = new GameTile(pixelX, pixelY, foregroundImages.get(1));
					newTileMap.setTile(x, y, treeDead);
				}
				if(ch == '{'){
					GameTile treeDead = new GameTile(pixelX, pixelY, foregroundImages.get(2));
					newTileMap.setTile(x, y, treeDead);
				}
				if(ch == 'C'){
					GameTile castle = new GameTile(pixelX, pixelY, backgroundImages.get(4));
					newTileMap.setTile(x, y, castle);
				}
				if(ch == '0'){		//Mud
					GameTile mudTile = new GameTile(pixelX, pixelY, planeTileImages.get(0));
					newTileMap.setTile(x, y, mudTile);
				}
				else if(ch == '1'){		//Ground
					GameTile groundTile = new GameTile(pixelX, pixelY, planeTileImages.get(1));
					newTileMap.setTile(x, y, groundTile);
				}
				else if(ch == '2'){		//Brick
					GameTile brickTile = new GameTile(pixelX, pixelY, planeTileImages.get(2));
					newTileMap.setTile(x, y, brickTile);
				}
				else if(ch == '3'){
					QuestionBlock q = new QuestionBlock(pixelX, pixelY, newTileMap);
					newTileMap.setTile(x, y, q);
					newTileMap.animatedTiles().add(q);
				}
				else if(ch == 'P'){		//Platform //both //double size
					Platform platform = new Platform(pixelX, pixelY, 2, TypeOfPlatform.BOTH);
					newTileMap.creatures().add(platform);
				}
				else if(ch ==  'p'){	//Platform //both //single size
					Platform platform = new Platform(pixelX, pixelY, 1, TypeOfPlatform.BOTH);
					newTileMap.creatures().add(platform);
				}
				else if(ch == 'Q'){		//Platform //Horizontal //double size
					Platform platform = new Platform(pixelX, pixelY, 2, TypeOfPlatform.HORIZONTAL);
					newTileMap.creatures().add(platform);
				}
				else if(ch == 'q'){		//Platform //Horizontal //single size
					Platform platform = new Platform(pixelX, pixelY, 1, TypeOfPlatform.HORIZONTAL);
					newTileMap.creatures().add(platform);
				}
				else if(ch == 'R'){		//Platform //Vertical //double size
					Platform platform = new Platform(pixelX, pixelY, 2, TypeOfPlatform.VERTICAL);
					newTileMap.creatures().add(platform);
				}
				else if(ch == 'r'){		//Platform //Vertical //single size
					Platform platform = new Platform(pixelX, pixelY, 1, TypeOfPlatform.VERTICAL);
					newTileMap.creatures().add(platform);
				}
				else if(ch == 'S'){ //Platform // Both invert// double size
					Platform platform = new Platform(pixelX, pixelY, 2, TypeOfPlatform.BOTHi);
					newTileMap.creatures().add(platform);
				}
				else if(ch == 's'){	//Platform // both invert // double size
					Platform platform = new Platform(pixelX, pixelY, 1, TypeOfPlatform.BOTHi);
					newTileMap.creatures().add(platform);
				}
				else if(ch ==  '$'){ //coin
					newTileMap.creatures().add(new Coin(pixelX, pixelY));
				}
				else if(ch == 'G'){ //PenguinEnemy
					newTileMap.creatures().add(new Penguin(pixelX, pixelY));
				}
				
			}
		}
		return newTileMap;
	}
}
