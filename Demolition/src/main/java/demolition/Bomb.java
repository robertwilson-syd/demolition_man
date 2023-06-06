package demolition;

import processing.core.PApplet;
import processing.core.PImage;

public class Bomb extends Tile{

	private PImage[] animation;
	private int animationBuffer = 0;
	private int animationStep;	
	private boolean isExploded = false;
	
	public Bomb(int x, int y, PImage[] animation){
		super(x, y, animation[0]);
		this.animation = animation;
	}

	public void tick(){
		//Bomb explodes after 2 seconds
		if(animationBuffer == 0){
			animationStep++;
			if(animationStep > 7){
				isExploded  = true;
				animationStep = 0;
			}
			animationBuffer = 15;
		}else{
			animationBuffer--;
		}
		this.sprite = animation[animationStep];
	}

	public boolean getIsExploded(){
		return isExploded;
	}
}