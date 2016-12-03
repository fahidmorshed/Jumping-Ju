package core;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import objects.base.Creature;
import objects.creatures.Coin;
import objects.ju.Ju;
import utility.ImageManipulator;
import core.animation.SpriteListener;
import core.tile.GameTile;
import core.tile.TileMap;

public class JuPanel extends JPanel implements Runnable{
	private Thread animatorThread;		//An animation Thread to control the game loop.
	private Graphics dbGraphics;		//Importing n painting related graphics object.
	private Image dbImage = null;		//Importing n painting related Image object.
	private TileMap map;
	private TileMap backgroundMap;
	private TileMap foregroundMap;
	private JuLoader loader;
	private JuRenderer renderer;
	private Ju player;
	
	Font f=new Font("sanserrif",20,20);
	private int panelWidth;
	private int panelHeight;
	private boolean running =  false;
	private boolean gameFreeze = false;
	private boolean gameOver = false;
	private int period = 30;			//Thread Sleeping time.
	private int skipLoop = 0;
	public static boolean gameWin = false;
	public static int stage = 1;
	private int health;
	private int point;
	private int k=0,start=1,gangutai=0;
	public static BufferedImage pauseImage = ImageManipulator.loadImage("Data/Background/gamePause.png");
	public static BufferedImage menuMessage = ImageManipulator.loadImage("Data/Background/messagefkdlsf.png");
	public static BufferedImage menu = ImageManipulator.loadImage("Data/Background/message.png");
	public JuPanel(int w, int h){
		this.panelWidth = w;
		this.panelHeight = h;
		loader = new JuLoader();
		stage = 1;
		renderer = new JuRenderer();
		player = new Ju();
		health = player.getHealth();
		try{
			renderer.setBackground(ImageIO.read(new File("Data/Background/juBackground1.png")));	// NEW
			map = loader.loadMap("Data/Map/stage1.txt");
			backgroundMap = loader.loadMap("Data/Map/backgroundStage1.txt");
			foregroundMap = loader.loadMap("Data/Map/foregroundStage1.txt");
			map.setPlayer(player);
		}catch (IOException e) {System.out.println("Map not found error: " + e);}
		this.addKeyListener(new GameListener());
		this.addKeyListener(new SpriteListener(player));
		this.setFocusable(true);	
	}
	
	public void init(){
		skipLoop = 0;
		player = new Ju();
		stage = 1;
		k=0;
		start=1;
		try{
			renderer.setBackground(ImageIO.read(new File("Data/Background/juBackground1.png")));	// NEW
			map = loader.loadMap("Data/Map/stage1.txt");
			backgroundMap = loader.loadMap("Data/Map/backgroundStage1.txt");
			foregroundMap = loader.loadMap("Data/Map/foregroundStage1.txt");
			map.setPlayer(player);
		}catch (IOException e) {System.out.println("Map not found error: " + e);}
		gameFreeze = false;
		this.addKeyListener(new SpriteListener(player));
		gameOver = false;
		gameFreeze = false;
		gameWin = false;
	}
	
	public void init(int stage){
		skipLoop = 0;
		player = new Ju();
		player.setHealth(health);
		player.newPoint(point);
		try{
			switch (stage) {
			case 1:
				renderer.setBackground(ImageIO.read(new File("Data/Background/juBackground1.png")));	// NEW
				map = loader.loadMap("Data/Map/stage1.txt");
				backgroundMap = loader.loadMap("Data/Map/backgroundStage1.txt");
				foregroundMap = loader.loadMap("Data/Map/foregroundStage1.txt");
				break;
			case 2:
				renderer.setBackground(ImageIO.read(new File("Data/Background/juBackground2.png")));	// NEW
				map = loader.loadMap("Data/Map/stage2.txt");
				backgroundMap = loader.loadMap("Data/Map/backgroundStage2.txt");
				foregroundMap = loader.loadMap("Data/Map/foregroundStage2.txt");
				break;
			case 3:
				renderer.setBackground(ImageIO.read(new File("Data/Background/juBackground3.png")));	// NEW
				map = loader.loadMap("Data/Map/stage3.txt");
				backgroundMap = loader.loadMap("Data/Map/backgroundStage3.txt");
				foregroundMap = loader.loadMap("Data/Map/foregroundStage3.txt");
				break;
			default:
				renderer.setBackground(ImageIO.read(new File("Data/Background/juBackground.png")));	// NEW
				map = loader.loadMap("Data/Map/stage1.txt");
				backgroundMap = loader.loadMap("Data/Map/backgroundStage1.txt");
				foregroundMap = loader.loadMap("Data/Map/foregroundStage1.txt");
				break;
			}
		}catch(IOException e) {System.out.println("Map not found error: " + e);}
		map.setPlayer(player);
		gameFreeze = false;
		this.addKeyListener(new SpriteListener(player));
		gameOver = false;
		gameFreeze = false;
		gameWin = false;
	}
	
	//Starting point of the Thread. Implicitely called. 
	public void addNotify(){
		super.addNotify();
		startGame();
	}
	
	private void startGame(){
		if(animatorThread == null || !running){
			animatorThread = new Thread(this, "Animator v 1.0");
			animatorThread.start();	
		}
	}
	
	public void stopGame(){
		running = false;
	}
	
	public void gameAction(){
		gameUpdate();
		gameRender();
		paintScreen();
	}
	
	public void run(){
		running = true;
		while(running){
			if(!gameFreeze){
				gameAction();
			}
			else{
				paintScreen();
			}
			try{
				Thread.sleep(period);
			}catch(InterruptedException e) {System.out.println("Thread Interrupted error: " + e);}
			
		}
		System.exit(0);
	}
	
	
	private void gameUpdate(){
		if(!gameOver && !gameWin){
			// Update all relevant Creatures.
			if(player.isStageComplete()){
				skipLoop++;
				if(player.currentAnimation() != player.stageWin && skipLoop >70)
					player.setAnimation(player.stageWin);
				if(skipLoop > 110){
					gameWin = true;
				}
			}
			if(player.getHealth() < 0){
				gameOver = true;
			}
			if(player.isBlinking() ==  true){
				skipLoop++;
				if(skipLoop == 50){
					skipLoop = 0;
					player.setIsBlinking(false);
					player.setIsInvisible(false);
				}
			}
			for(int i = 0; i < map.relevantCreatures().size(); i++){
				Creature c = map.relevantCreatures().get(i);
				if(!(c instanceof Coin)){
					c.updateCreature(map, period);
					player.playerCollision(map, c);
					for(Creature other : map.relevantCreatures()){
						c.creatureCollision(other);
					}
				}
				else{
					c.updateCreature(map, period);
					player.playerCollision(map, c);
				}
			}
			// clear the colliding sprites on the tile
			for(GameTile tile : map.animatedTiles()){
				tile.collidingCreatures().clear();
				tile.update(20);
			}
			// Add creatures that need to be created.
			for(Creature c : map.creaturesToAdd()){
				map.creatures().add(c);
			}
			
			map.creaturesToAdd().clear();
			player.update(map, period);
			Coin.turn.update(period);
			map.relevantCreatures().clear();
			map.platforms().clear();
			if(player.getHealth() < 0){
				gameOver = true;
			}
		}
		else if(gameOver){
			player.setAnimation(player.deadRight);
			player.update(map, period);
			point = player.getPoint();
			
			////////////// add the point file by maloy
			
			
			String name=null,score;
			score=point+"";
			if( k == 0){
			name=JOptionPane.showInputDialog("ENTER YOUR NAME");
			JOptionPane.showMessageDialog(null,"OK"+JOptionPane.PLAIN_MESSAGE);
			k=1;
			}
			if( point > 0)
		    new fileWork(name,score);
			
//			try{
//					
//				animatorThread.sleep(5000);
//			}catch(InterruptedException e){};
			start=0;
			gangutai=0;
		    gameFreeze=true;
		
//			if(skipLoop >60){
//				try{
//					animatorThread.sleep(1000);
//				}catch(InterruptedException e){};
//				
//				init();
//			}
//			skipLoop++;
		}
		else if(gameWin){
			health = player.getHealth();
			stage++;
			point = player.getPoint();
			try{
				animatorThread.sleep(5000);
			}catch(InterruptedException e){};
			if(stage < 4){
				init(stage);
			}
			else{
				
				
				// add the file by maloy
				String name=null,score;
				score=point+"";
				if( k == 0){
				name=JOptionPane.showInputDialog("ENTER YOUR NAME");
				JOptionPane.showMessageDialog(null,"OK"+JOptionPane.PLAIN_MESSAGE);
				k=1;
				}
				if( point > 0 )
		        new fileWork(name,score);
				
				
				start=0;
				gangutai=0;
				gameFreeze=true;
			}
		}
	}
	private void gameRender(){
		if(dbImage==null){
			dbImage = createImage(this.panelWidth, this.panelHeight);
			return;
		}
		dbGraphics = dbImage.getGraphics();
		renderer.draw((Graphics2D) dbGraphics, map, backgroundMap, foregroundMap, panelWidth, panelHeight);	//NEW
	}
	
	private void paintScreen(){
		Graphics graphics;
		try{
			graphics = this.getGraphics();
			if(graphics!=null && (dbImage!=null) && !gameFreeze){
				graphics.drawImage(dbImage, 0, 0, null);
				graphics.dispose();
			}
			else{
				if( start == 1){
				graphics.drawImage(pauseImage, 230, 100, null);
				graphics.drawImage(menuMessage, 260, 200, null);
				start=0;
				}
				
				if(  gangutai == 0)
				graphics.drawImage(menu, 260, 320, null);
			}
		}catch (Exception e) { System.out.println("Graphics context error: " + e);}
	}
	
	
	
	
	
	/**
	 * Adds debugging features so it is possible to single step a game loop one by one.
	 * 'Z' pauses the game.
	 * 'X' resumes the game.
	 * '1' runs a single game loop if the game if paused.
	 * 'L' runs a single game loop if pressed and continously runs the game loop if held.
	 */
	class GameListener extends KeyAdapter {
		
		public void keyReleased(KeyEvent e) {
			int key = e.getKeyCode();
			
			// 'Z' is pressed.
			if (key == KeyEvent.VK_Z) { // pause
				if(JuPanel.this.gameFreeze == false) {
					JuPanel.this.gameFreeze = true;
				}
			}
			
			// 'X' is pressed.
			if (key == KeyEvent.VK_X) { // resume
				if(JuPanel.this.gameFreeze == true) {
					JuPanel.this.gameFreeze = false;
				}
			}
			
			// '1' is pressed.
			if (key == KeyEvent.VK_1) {
				if(JuPanel.this.gameFreeze == true) {
					System.out.println();
					System.out.println("Game Update (1) Starting...");
					JuPanel.this.gameAction();
					System.out.println();
					System.out.println("Game Update (1) Completed.");
				}
			}
			if(key == KeyEvent.VK_ESCAPE){
				start=1;
				gangutai=1;
				if(gameFreeze){
					gameFreeze = false;
				}
				else {
					gameFreeze = true;
				}
			}
			if(key == KeyEvent.VK_R){
			
				init();
			}
			
		}
		
		
	


		//Read more: http://mrbool.com/showing-images-in-a-swing-jframe-in-java/24594#ixzz2xALxI4Rh
		
		// 'L' is pressed or held.
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			if (key == KeyEvent.VK_L) {
				JuPanel.this.gameAction();
			}
			if(key == KeyEvent.VK_Q){
				running = false;
			}
		}
	}
}
