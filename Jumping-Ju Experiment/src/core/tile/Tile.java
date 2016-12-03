//OK, Probably
package core.tile;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import core.JuRenderer;
import core.animation.Animatible;
import core.animation.Animation;


/**
 *Fundamental characterstics of a tile. It gets converted from pixel to tile using a method in
 *JuRenderer. This object just stores those information and overrides the draw method to draw
 *images on screen. Also, it can store animation.
 */
public class Tile extends Animatible {
	private int pixelX;
	private int pixelY;
	private int tileX;
	private int tileY;
	protected BufferedImage img;
	
	public Tile(int pixelX, int pixelY, Animation anim, BufferedImage img){
		this.pixelX = pixelX;
		this.pixelY = pixelY;
		tileX = JuRenderer.pixelsToTiles(pixelX);
		tileY = JuRenderer.pixelsToTiles(pixelY);
		this.img = img;
		setAnimation(anim);
	}
	
	public Tile(int pixelX, int pixelY, BufferedImage img){
		this(pixelX, pixelY, null, img);
	}
	
	public void draw(Graphics g, int pixelX, int pixelY){
		g.drawImage(getImage(), pixelX, pixelY, null);
	}
	
	public void draw(Graphics g, int pixelX, int pixelY, int offsetX, int offsetY){
		this.draw(g, pixelX + offsetX, pixelY + offsetY);
	}
	
	public BufferedImage getImage(){
		if(currentAnimation()==null){
			return img;
		}
		else{
			return currentAnimation().getImage();
		}
	}
	
	public int getPixelX(){
		return pixelX;
	}
	
	public int getPixelY(){
		return pixelY;
	}
	
	public int getWidth(){
		return getImage().getWidth();
	}
	
	public int getHeight(){
		return getImage().getHeight();
	}
}
