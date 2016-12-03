//OK probably
package core.animation;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *This Object is used to store a single animation. Every frame is stored inside another
 *private object called AnimFrame. The update() method is used to create animation by 
 *changing every single frame inside a single animation one at a time.
 *
 *endOfAnimationAction() is a free to use function. Can be overridded if needed.
 */
public class Animation {
	private ArrayList<AnimFrame> frames;
	private int currFrameIndex;	//running frame number.
	private long animTime;			//current time of the running animation.
	private long totalDuration;		//for a single animation.
	private long defaultAnimLength; //for each frame.
	
	//constructors doesn't add any frame. Just initialize the object.
	public Animation(){
		this(0);
	}
	
	public Animation(long defaultAnimLength) {
		frames = new ArrayList<AnimFrame>();
		totalDuration = 0;
		this.defaultAnimLength = defaultAnimLength;
		start();
	}
	
	//Set Default Animation Length. If needed to change.
	public Animation setDAL(long defaultAnimLength) {
		this.defaultAnimLength = defaultAnimLength;
		return this;
	}
	
	//This function explictly adds frames to animation(AnimFrame)
	//if a duration is given for a specific frame, it's added to the totalDuration of the
	//animation. Otherwise defaultAnimLength is used.
	public Animation addFrame(BufferedImage image) {
		return this.addFrame(image, defaultAnimLength);
	}
	
	public Animation addFrame(BufferedImage image, long duration) {
		totalDuration += duration;
		frames.add(new AnimFrame(image, totalDuration));
		defaultAnimLength = duration;
		return this;
	}
	
	public void start() {
		animTime = 0;
		currFrameIndex = 0;
	}
	
	public int getHeight() {
		return getFrame(currFrameIndex).image.getHeight(null); //in pixels
	}
	
	public int getWidth() {
		return getFrame(currFrameIndex).image.getWidth(null); //in pixels
	}
	
	//Updates the animation by a give amount of time.
	//A single frame is updated. not the whole animation.
	public void update(long elapsedTime) {
		if (frames.size() > 1) { // must have at least 2 frames to animate
			animTime += elapsedTime; // animation time increase here

			if (animTime >= totalDuration) { //reset animation
				animTime = 0; 
				currFrameIndex = 0; // back to first frame
				endOfAnimationAction();
				
			}

			if(animTime > getFrame(currFrameIndex).endTime) {
				currFrameIndex++;
			}
		}
	}
	
	//Can be overridded if needed.
	public void endOfAnimationAction(){
		
	}
	
	//Minor change, no need to use getFrames()
	
	//If needed. Although, i dont think it's useful at all. still being safe for now. #check
	//Returns the current animation frame/image.
	public BufferedImage getImage() {
		if (frames.size() == 0) {
			return null;
		}
		else {
			return getFrame(currFrameIndex).image;
		}
	}
	
	//Retruns a single AnimFrame, so the image and the endTime is also returned.
	private AnimFrame getFrame(int i) {
		return frames.get(i);
	}
	/**
	 *This Class is the core of animation frames. Each instance holds a single frame.
	 *It's a private class, thus other side of the program should not concern with the
	 *individual frames.
	 *long endTime is the indicator for a frame to switch with respect to the total time of an
	 *animation.
	 */
	private class AnimFrame{
		BufferedImage image;
		long endTime;
		
		public AnimFrame(BufferedImage image, long endTime) {
			this.image = image;
			this.endTime = endTime;
		}
	}
}
