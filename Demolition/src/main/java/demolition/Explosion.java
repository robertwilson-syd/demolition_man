package demolition;

import processing.core.PApplet;
import processing.core.PImage;

public class Explosion extends Tile{

	private int timer = 30;
	private boolean explosionEnded = false;

	public Explosion(int x, int y, PImage sprite){
		super(x, y, sprite);
	}

	public void tick(){
		timer--;
		if(timer == 0){
			explosionEnded = true;
		}
	}

	public boolean getExplosionEnded(){
		return explosionEnded;
	}
}