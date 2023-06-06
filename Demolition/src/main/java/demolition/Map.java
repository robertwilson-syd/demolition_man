package demolition;

import processing.core.PApplet;
import processing.core.PImage;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Arrays;

public class Map{

	private int offset = 64;

	private Goal goal;

	private BomberMan bomberMan;

	private char[][] mapList = new char[13][15];

	private List<BrokenWall> brokenWalls = new ArrayList<BrokenWall>();
	private List<Wall> walls = new ArrayList<Wall>();
	private List<RedEnemy> redEnemies = new ArrayList<RedEnemy>();
	private List<YellowEnemy> yellowEnemies = new ArrayList<YellowEnemy>();

	private PImage wall;
	private PImage brokenWall;
	private PImage goalImage;

	private PImage[][] redEnemyAnimations;
	private PImage[][] yellowEnemyAnimations;
    public PImage[][] playerAnimations;

	public Map(PImage wall, PImage brokenWall, PImage goalImage, PImage[][] redEnemyAnimations, PImage[][] yellowEnemyAnimations, PImage[][] playerAnimations){
		this.wall = wall;
		this.brokenWall = brokenWall;
		this.goalImage = goalImage;
		this.redEnemyAnimations = redEnemyAnimations;
		this.yellowEnemyAnimations = yellowEnemyAnimations;
		this.playerAnimations = playerAnimations;
	}

	public void loadMap(String path){
		//Loads tiles from a text map, and copies each character into a 2D array stored within the class.
		yellowEnemies.clear();
		redEnemies.clear();
		brokenWalls.clear();
		walls.clear();
		for(int i = 0; i < 13; i++){
			for(int j = 0; j < 15; j++){
				mapList[i][j] = '\0';
			}
		}

		int counterX = 0;
		int counterY = offset;
		File file;
		String line;
		Scanner scan;

		try{
			file = new File(path);
		}catch(java.lang.NullPointerException e){
			return;
		}
		try{
			scan = new Scanner(file);
		}catch(FileNotFoundException e){
			System.out.println("ERROR");
			return;
		}

		while(scan.hasNextLine()){
			line = scan.nextLine();
			for(int i = 0; i < line.length(); i++){
				char c = line.charAt(i);
				switch(c) {
					case 'B':
						BrokenWall tempBrokenWall = new BrokenWall(counterX, counterY, brokenWall);
						brokenWalls.add(tempBrokenWall); 
						mapList[(counterY -64) / 32][counterX/32] = 'B';
						break;
					case 'W':
						Wall tempWall = new Wall(counterX, counterY, wall);
						walls.add(tempWall); 
						mapList[(counterY -64)/32][counterX/32] = 'W';
						break;
					case 'G':
						goal = new Goal(counterX, counterY, goalImage);
						mapList[(counterY -64)/32][counterX/32] = 'G';
						break;
					case 'P':
					//Why is this different?
						bomberMan = new BomberMan(counterX, counterY, playerAnimations);
						mapList[(counterY -64)/32][counterX/32] = 'E';
						break;
					case 'R':
						RedEnemy redEnemy = new RedEnemy(counterX, counterY, redEnemyAnimations);
						redEnemies.add(redEnemy);
						mapList[(counterY -64)/32][counterX/32] = 'E';
						break;
					case 'Y':
						YellowEnemy yellowEnemy = new YellowEnemy(counterX, counterY, yellowEnemyAnimations);
						yellowEnemies.add(yellowEnemy);
						mapList[(counterY -64)/32][counterX/32] = 'E';
						break;
					case ' ':
						mapList[(counterY -64)/32][counterX/32] = 'E';
						break;
				}
				counterX += 32;

			}
			counterX = 0;
			counterY += 32;
		}
	}

	public List<BrokenWall> getBrokenWallList(){
		return brokenWalls;
	}

	public List<Wall> getWallList(){
		return walls;
	}
	
	public Goal getGoal(){
		return goal;
	}

	public List<RedEnemy> getRedEnemies(){
		return redEnemies;
	}

	public List<YellowEnemy> getYellowEnemies(){
		return yellowEnemies;
	}

	public boolean isDestinationPassable(int x, int y){
		if(mapList[(y-64)/32][x/32] == 'E' || mapList[(y-64)/32][x/32] == 'G'){
			return true;
		}else{
			return false;
		}
	}

	public boolean isDestinationBrokenWall(int x, int y){
		if(mapList[(y-64)/32][x/32] == 'B'){
			return true;
		}else{
			return false;
		}
	}

	public void breakWall(int x, int y){
		mapList[(y-64)/32][x/32] = 'E';
		for(Iterator<BrokenWall> it = brokenWalls.iterator(); it.hasNext();){
			BrokenWall brokenWall = it.next();
			if(brokenWall.getX() == x && brokenWall.getY() == y){
				it.remove();
			}
		}
	}

	public BomberMan getBomberMan(){
		return bomberMan;
	}

}