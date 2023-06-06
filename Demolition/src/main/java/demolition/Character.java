package demolition;

import processing.core.PApplet;
import processing.core.PImage;

public class Character extends Tile{

	private PImage[][] animations = new PImage[4][4];
	
	private int buffer = 0;
	private int animationStep = 0;
	private int direction = 3;

	public Character(int x, int y, PImage[][] animations){
		super(x, y, animations[3][0]);
		this.animations = animations;
	}

	public void tick(){
		//The sprite animation is handled by this tick function, while movement is handled by the GameManager class
		if(buffer == 0){
			animationStep++;
			if(animationStep > 3){
				animationStep = 0;
			}
			buffer = 12;
		}else{
			buffer--;
		}
		
		this.sprite = animations[direction][animationStep];
	}

	public void display(PApplet app){
		//Needs to offset the sprite as it is 16 pixels larger than the tiles.
		app.image(this.sprite, this.x, this.y - 16);
	}

	public void changeDirection(int direction){
		this.direction = direction;
		return;
	}

	public int getDirection(){
		return direction;
	}

}
