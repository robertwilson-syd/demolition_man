package demolition;

import processing.core.PApplet;
import processing.core.PImage;

public class Tile{
	protected int x;
	protected int y;
	protected PImage sprite;

	public Tile(int x, int y, PImage sprite){
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}

	public void display(PApplet app){
		app.image(this.sprite, this.x, this.y);
	}

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

	public PImage getSprite(){
		return sprite;
	}

	public void adjustX(int adj){
		this.x += adj;
	}

	public void adjustY(int adj){
		this.y += adj;
	}

	public void setX(int x){
		this.x = x;
	}

	public void setY(int y){
		this.y = y;
	}

}