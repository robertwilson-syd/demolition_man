package demolition;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import processing.data.JSONObject;
import processing.data.JSONArray;

public class GameManager{
	private Map map;

	private BomberMan bomberMan;

	private boolean inputBuffer = false;
	private boolean spaceBarReleased = true;
	private boolean levelSetup = false;

	private List<Bomb> bombList = new ArrayList<Bomb>();

	private List<Explosion> explosions = new ArrayList<Explosion>();

	private PImage[] bombAnimation;
	private PImage[] explosionSprites;

	private int enemyMovementTimer = 60;
	private int currentLevel = 0;
	private int currentLevelTime;
	private Random randInt = new Random();

	private JSONArray levels;
	
	private JSONObject currentLevelConfig;

	private String currentLevelPath;

	private UI ui;

	public GameManager(Map map, UI ui, JSONArray levels) {
        this.map = map;
		this.ui = ui;
		this.levels = levels;
	}

	public Map getMap(){
		return map;
	}

	public void tick(int playerDirection, boolean keyPressed){
		//The tick function ticks for all the animated Objects (player, enemies, bombs, explosions)
		if(!levelSetup){
			//Run once for each level to load in the map
			setupLevel();
			levelSetup = true;
		}
		bomberMan = this.map.getBomberMan();
		
		//This deals with user input. If the player presses space, it ensures only one bomb is created
		if(!keyPressed){
			inputBuffer = false;
		}
		if(playerDirection != 32){
			spaceBarReleased = true;
		}
		if(playerDirection == 32 && spaceBarReleased){
			spaceBarReleased = false;
			createBomb(bomberMan.getX(), bomberMan.getY());
		}
		//Then only the arrow keys are accepted as input into the move function for the player character
		else if(keyPressed && !inputBuffer && playerDirection < 41 && playerDirection > 36){
			int[] temp = move(bomberMan.getX(), bomberMan.getY(), playerDirection - 37);
			bomberMan.adjustX(temp[0]);
			bomberMan.adjustY(temp[1]);
			inputBuffer = true;
			bomberMan.changeDirection(playerDirection - 37);
		}

		bomberMan.tick();

		//This for loop calls the tick function for each bomb once, and checks if any of them have finished their animation cycle.
		//If any of them have, the bomb is removed and an explosion is created in its place.
		for(Iterator<Bomb> it = bombList.iterator(); it.hasNext();){
			Bomb bomb = it.next();
			bomb.tick();
			if(bomb.getIsExploded()){
				createExplosion(bomb.getX(), bomb.getY());
				it.remove();
			}
		}

		collision();
		//This calls the tick function for each explosion once, after half a second each explosion is removed.
		for(Iterator<Explosion> it = explosions.iterator(); it.hasNext();){
			Explosion explosion = it.next();
			explosion.tick();
			if(explosion.getExplosionEnded()){
				it.remove();
			}
		}
		
		//This loop calls the tick function for each red enemy once, which animates their sprite.
		//It also dictates the red enemy movement logic.
		ArrayList<RedEnemy> redEnemiesCopy = new ArrayList<RedEnemy>(this.map.getRedEnemies());
		for(RedEnemy redEnemy : redEnemiesCopy){
			int[] temp = new int[2];
			if(enemyMovementTimer == 0){
				while(temp[0] == 0 && temp[1] == 0){
					temp = move(redEnemy.getX(), redEnemy.getY(), redEnemy.getDirection());
					if(temp[0] == 0 && temp[1] == 0){
						redEnemy.changeDirection(randInt.nextInt(4));
					}
				}
				redEnemy.adjustX(temp[0]);
				redEnemy.adjustY(temp[1]);
			}
			if(redEnemy.getX() == bomberMan.getX() && redEnemy.getY() == bomberMan.getY()){
				resetMap();
			}
			redEnemy.tick();
		}
		redEnemiesCopy.clear();
		
		//This loop calls the tick function for each yellow enemy once, which animates their sprite.
		//It also dictates the yellow enemy movement logic.
		ArrayList<YellowEnemy> yellowEnemiesCopy = new ArrayList<YellowEnemy>(this.map.getYellowEnemies());
		for(YellowEnemy yellowEnemy: yellowEnemiesCopy){
			int[] temp = new int[2];
			if(enemyMovementTimer == 0){
				
				while(temp[0] == 0 && temp[1] == 0){
					temp = move(yellowEnemy.getX(), yellowEnemy.getY(), yellowEnemy.getDirection());
					if(temp[0] == 0 && temp[1] == 0){
						int newDirection;
						if(yellowEnemy.getDirection() == 3){
							newDirection = 0;
						}else{
							newDirection = yellowEnemy.getDirection() + 1; 
						}
						yellowEnemy.changeDirection(newDirection);
					}
				}

				yellowEnemy.adjustX(temp[0]);
				yellowEnemy.adjustY(temp[1]);
			}
			if(yellowEnemy.getX() == bomberMan.getX() && yellowEnemy.getY() == bomberMan.getY()){
				resetMap();
			}
			yellowEnemy.tick();
		}
		yellowEnemiesCopy.clear();

		if(enemyMovementTimer == 0){
			enemyMovementTimer = 60;
		}
		enemyMovementTimer--;

		ui.tick();

		if(bomberMan.getX() == map.getGoal().getX() && bomberMan.getY() == map.getGoal().getY()){
			levelWin();
		}
			
	}


	public int[] move(int initX, int initY, int direction){
		//The move function accepts the coordinates of the character to be moved, and the direction they are trying to go.
		//It checks that the destination is passable, and if so it returns a value to adjust the character's coordinates.
		//Otherwise, it does nothing.
		int[] out = new int[2];
		switch(direction){
			case 0:
				if(map.isDestinationPassable(initX - 32, initY)){
					out[0] = -32;
				}
				break;
			case 1:
				if(map.isDestinationPassable(initX, initY - 32)){
					out[1] = -32;
				}
				break;
			case 2:
				if(map.isDestinationPassable(initX + 32, initY)){
					out[0] = 32;
				}
				break;
			case 3:
				if(map.isDestinationPassable(initX, initY + 32)){
					out[1] = 32;
				}
				break;
			default:
				break;
		}
		return out;
	}

	public void setBombAnimation(PImage[] bombAnimation){
		this.bombAnimation = bombAnimation;
	}

	public void setExplosionSprites(PImage[] explosionSprites){
		this.explosionSprites = explosionSprites;
	}

	public void createBomb(int x, int y){
		//Checks that there is no bomb already at the given coordinates and if there is not, then a bomb is created.
		for(Bomb bomb : bombList){
			if(bomb.getX() == x && bomb.getY() == y){
				return;
			}
		}
		Bomb tempBomb = new Bomb(x, y, bombAnimation);
		bombList.add(tempBomb);
	}

	public List<Bomb> getBombList(){
		return bombList;
	}

	public List<Explosion> getExplosions(){
		return explosions;
	}

	public void createExplosion(int x, int y){
		/*Creates a centre explosion at the given coordinates (no validation is required for the centre explosion as that is already checked when a bomb is created).
		It then checks each cardinal direction: 
		If the first tile is empty, an explosion is created and the second tile in that direction is checked.
		If the second tile is empty, an explosion is created.
		If the second tile is a broken wall the broken wall is destroyed and an explosion is created.
		If the second tile is a wall nothing happens.
		If the first tile is a broken wall, the broken wall is destroyed and an explosion is created, but the second tile in that direction is not checked.
		If the first tile is a wall do nothing in that direction.*/
		Explosion centreExplosion = new Explosion(x, y, explosionSprites[0]);
		explosions.add(centreExplosion);

		if(map.isDestinationPassable(x - 32, y) || map.isDestinationBrokenWall(x - 32, y)){
			Explosion firstLeftExplosion = new Explosion(x - 32, y, explosionSprites[1]);
			explosions.add(firstLeftExplosion);
			if(map.isDestinationBrokenWall(x - 32, y)){
				map.breakWall(x - 32, y);
			}
			else if(map.isDestinationPassable(x - 64, y)){
				Explosion secondLeftExplosion = new Explosion(x - 64, y, explosionSprites[1]);
				explosions.add(secondLeftExplosion);
				map.breakWall(x - 64, y);
			}
		}

		if(map.isDestinationPassable(x + 32, y) || map.isDestinationBrokenWall(x + 32, y)){
			Explosion firstRightExplosion = new Explosion(x + 32, y, explosionSprites[1]);
			explosions.add(firstRightExplosion);
			if(map.isDestinationBrokenWall(x + 32, y)){
				map.breakWall(x + 32, y);
			}else if(map.isDestinationPassable(x + 64, y) || map.isDestinationBrokenWall(x + 64, y)){
				Explosion secondRightExplosion = new Explosion(x + 64, y, explosionSprites[1]);
				explosions.add(secondRightExplosion);
				map.breakWall(x + 64, y);
			}
		}

		if(map.isDestinationPassable(x, y - 32) || map.isDestinationBrokenWall(x, y - 32)){
			Explosion firstUpExplosion = new Explosion(x, y - 32, explosionSprites[2]);
			explosions.add(firstUpExplosion);
			if(map.isDestinationBrokenWall(x, y - 32)){
				map.breakWall(x, y - 32);
			}else if(map.isDestinationPassable(x, y - 64) || map.isDestinationBrokenWall(x, y - 64)){
				Explosion secondUpExplosion = new Explosion(x, y - 64, explosionSprites[2]);
				explosions.add(secondUpExplosion);
				map.breakWall(x, y - 64);
			}
		}

		if(map.isDestinationPassable(x, y + 32) || map.isDestinationBrokenWall(x, y + 32)){
			Explosion firstDownExplosion = new Explosion(x, y + 32, explosionSprites[2]);
			explosions.add(firstDownExplosion);
			if(map.isDestinationBrokenWall(x, y + 32)){
				map.breakWall(x, y + 32);
			}else if(map.isDestinationPassable(x, y + 64) || map.isDestinationBrokenWall(x, y + 64)){
				Explosion secondDownExplosion = new Explosion(x, y + 64, explosionSprites[2]);
				explosions.add(secondDownExplosion);
				map.breakWall(x, y + 64);
			}
		}
	}

	public void collision(){
		//This checks for character collisions with explosions, if the character is an enemy the enemy is removed. 
		//If the character is the player the level is reset and a life is lost.
		ArrayList<Explosion> explosionsCopy = new ArrayList<Explosion>(explosions);
		for(Explosion explosion : explosionsCopy){
			for(Iterator<RedEnemy> it = this.map.getRedEnemies().iterator(); it.hasNext();){
				RedEnemy redEnemy = it.next();
				if(explosion.getX() == redEnemy.getX() && explosion.getY() == redEnemy.getY()){
					it.remove();
				}
			}

			for(Iterator<YellowEnemy> it = this.map.getYellowEnemies().iterator(); it.hasNext();){
				YellowEnemy yellowEnemy = it.next();
				if(explosion.getX() == yellowEnemy.getX() && explosion.getY() == yellowEnemy.getY()){
					it.remove();
				}
			}
			
			if(explosion.getX() == bomberMan.getX() && explosion.getY() == bomberMan.getY()){
				resetMap();
				break;
			}
		}
	}

	public void resetMap(){
		this.map.loadMap(currentLevelPath);
		explosions.clear();
		bombList.clear();
		ui.loseLife();
	}
	
	public UI getUI(){
		return ui;
	}

	public void setupLevel(){
		currentLevelConfig = levels.getJSONObject(currentLevel);
		currentLevelPath = currentLevelConfig.getString("path");
		currentLevelTime = currentLevelConfig.getInt("time"); 
		this.map.loadMap(currentLevelPath);
		this.ui.setLevelTime(currentLevelTime);
	}

	public void levelWin(){
		currentLevel++;
		if(currentLevel >= levels.size()){
			gameWin();
		}
		levelSetup = false;
	}

	public void gameWin(){
		this.ui.setGameOver(true);
		this.ui.setGameFinishMessage("YOU WIN");
	}

}