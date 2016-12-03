//OK, Probably
package core.animation;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * An object used to bridge the gap between keyboard input and a basic sprite
 * Attach this object to the panel and pass a sprite in to call the sprites
 * keyboard methods. Each sprite which extends BasicSprite gains a set of methods
 * such that this object is compatible with any sprite that is a BasicSprite.
 * 
 * Aslo, while creating a sprite, keyEvents should be overridden.
 */

public class SpriteListener extends KeyAdapter {
	private Sprite sprite;
	
	public SpriteListener(Sprite sprite){
		this.sprite = sprite;
	}
	
	public void keyReleased(KeyEvent e){
		sprite.keyReleased(e);
	}
	
	public void keyPressed(KeyEvent e){
		sprite.keyPressed(e);
	}
	
	public void keyTyped(KeyEvent e){
		sprite.keyTyped(e);
	}
}
