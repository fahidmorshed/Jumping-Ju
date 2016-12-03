package utility;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *This class is responsible for cutting a BufferedImage into pieces and returning the hole
 *pieces into a BufferedImage type arrayList.
 */
public class SpriteMap {
	private BufferedImage spriteMap;
	private BufferedImage[] sprites;
	
	public SpriteMap(String filename, int c, int r){
		this.loadSprites(filename, c, r);
	}
	
	public BufferedImage[] getSprites(){
		return sprites;
	}
	
	private void loadSprites(String filename, int c, int r){
		spriteMap = ImageManipulator.loadImage(filename); // #check
		sprites = splitSprites(c, r);
	}
	
	private BufferedImage[] splitSprites(int c, int r){
		int pWidth = spriteMap.getWidth() / c;
		int pHeight = spriteMap.getHeight() / r;
		BufferedImage[] sprites = new BufferedImage[c*r];
		int n = 0;
		
		for(int y=0; y<r; y++){
			for(int x=0; x<c; x++){
				sprites[n] = new BufferedImage(pWidth, pHeight, 2);
				Graphics2D g2d = sprites[n].createGraphics();
				g2d.drawImage(spriteMap, 0, 0, pWidth, pHeight, pWidth*x, pHeight*y,
						pWidth*x+pWidth, pHeight*y+pHeight, null);
				n++;
				g2d.dispose();
			}
		}
		return sprites;
	}
}
