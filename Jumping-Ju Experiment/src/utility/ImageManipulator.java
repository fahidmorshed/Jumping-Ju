//OK
package utility;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 *This class is to manipulate Images. Importing, fliping and constructing images.
 *all the functions are static, thus can be implemented without an instance.
 *
 *every function @returns a BufferedImage.
 */
public class ImageManipulator {
	/** Reads in a BufferedImage using the standard ImageIO.read() */
	public static BufferedImage loadImage(String filename){
		BufferedImage bufferedImage = null;
		try{
			bufferedImage = ImageIO.read(new File(filename));
		}catch(IOException e) {System.out.println("Faild to load image error: " + e);}
		return bufferedImage;
	}
	
	/** Flip the image horizontally and return it*/
	public static BufferedImage horizontalFlip(BufferedImage bufferedImage){
		int w = bufferedImage.getWidth();
		int h = bufferedImage.getHeight();
		
		BufferedImage dImage = new BufferedImage(w, h, bufferedImage.getColorModel().getTransparency());
		Graphics2D g2d = dImage.createGraphics();
		g2d.drawImage(bufferedImage, 0, 0, w, h, w, 0, 0, h, null);
		g2d.dispose();
		return dImage;
	}
	
	/** Flip the image vertically and return it*/
	public static BufferedImage verticalFlip(BufferedImage bufferedImage){
		int w = bufferedImage.getWidth();
		int h = bufferedImage.getHeight();
		
		BufferedImage dImage = new BufferedImage(w, h, bufferedImage.getColorModel().getTransparency());
		Graphics2D g2d = dImage.createGraphics();
		g2d.drawImage(bufferedImage, 0, 0, w, h, 0, h, w, 0, null);
		g2d.dispose();
		return dImage;
	}
}
