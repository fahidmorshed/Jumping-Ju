package objects.tiles;

import java.awt.Point;
import java.awt.image.BufferedImage;

import core.tile.GameTile;

public class SlopedTile extends GameTile {
	private Point startingPoint;
	private Point endingPoint;
	private boolean hasPositiveSlope;
	
	public SlopedTile(int pixelX, int pixelY, BufferedImage img, boolean hasPositiveSlope){
		super(pixelX, pixelY, null, img);
		setIsCollidable(true);
		setIsSloped(true);
		this.hasPositiveSlope = hasPositiveSlope;
		
		if(hasPositiveSlope){
			startingPoint = new Point(pixelX, pixelY + 32);
			endingPoint = new Point(pixelX + 32, pixelY);
		}
		else{
			startingPoint = new Point(pixelX, pixelY);
			endingPoint = new Point(pixelX + 32, pixelY + 32);
		}
	}
	
	public Point getStartingPoint() {
		return startingPoint;
	}
	
	public Point getEndingPoint() {
		return endingPoint;
	}
	
	public boolean hasPositiveSlope() {
		return hasPositiveSlope;
	}
	
}
