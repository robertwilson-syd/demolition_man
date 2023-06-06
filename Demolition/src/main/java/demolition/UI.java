package demolition;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PFont;

public class UI{
	private int levelTime;
	private int timer = 60;
	private int lives;

	private PImage clock;
    private PImage livesImage;

	private String gameFinishMessage = "GAME OVER";

	private boolean gameOver = false;

	public UI(int lives){
		this.lives = lives;
	}

	public void tick(){
		if(timer == 0){
			levelTime--;
			if(levelTime <= 0){
				this.gameOver = true;
			}
			timer = 60;
		}
		timer--;

	}

	public void setLevelTime(int levelTime){
		this.levelTime = levelTime;
	}

	public void setLives(int lives){
		this.lives = lives;
	}


	public void display(PApplet app){
		if(gameOver){
			gameFinish(app);
		}else{
			PFont font = app.createFont("src/main/resources/PressStart2P-Regular.ttf", 16);
			app.textFont(font);
			app.fill(0, 0, 0);
			app.text(levelTime, 320, 24);
			app.fill(0, 0, 0);
			app.text(lives, 176, 24);
			app.image(livesImage, 128, 16);
			app.image(clock, 256, 16);
		}
	}

	public void loadImages(PApplet app){
		clock = app.loadImage("src/main/resources/icons/clock.png");
        livesImage = app.loadImage("src/main/resources/icons/player.png");
	}

	public void loseLife(){
		lives--;
		if(lives <= 0){
			this.gameOver = true;
		}
	}

	public int getLives(){
		return lives;
	}

	public int getLevelTime(){
		return levelTime;
	}

	public boolean getGameOver(){
		return gameOver;
	}

	public String getGameFinishMessage(){
		return gameFinishMessage;
	}

	public void setGameOver(boolean gameOver){
		this.gameOver = gameOver; 
	}

	public void setGameFinishMessage(String gameFinishMessage){
		this.gameFinishMessage = gameFinishMessage; 
	}

	public void gameFinish(PApplet app){
		app.fill(238, 129, 0);
		app.rect(0, 0, 480, 480);
		app.fill(0, 0, 0);
		app.text(gameFinishMessage, 240, 240);
		app.noLoop();
	}

}