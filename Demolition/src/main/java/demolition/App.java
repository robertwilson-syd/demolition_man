package demolition;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PFont;
import processing.data.JSONObject;
import processing.data.JSONArray;
import java.util.concurrent.TimeUnit;

public class App extends PApplet {

    public static final int WIDTH = 480;
    public static final int HEIGHT = 480;
    public static final int FPS = 60;
    private int playerDirection;
    private int lives;
    public Map map;

    public GameManager gameManager;

    private PImage wall;
	private PImage brokenWall;
	private PImage goalImage;

    public PImage[][] playerAnimations = new PImage[4][4];
    public PImage[][] redEnemyAnimations = new PImage[4][4];
    public PImage[][] yellowEnemyAnimations = new PImage[4][4];

    public PImage[] bombAnimation = new PImage[8];
    public PImage[] explosionSprites = new PImage[3];

    private UI ui;
    
    private String configPath = "config.json";

    private JSONObject config;

    private JSONArray levels;

    public App() {
    }

    public void settings() {
        size(WIDTH, HEIGHT);
    }

    public void setConfig(String path){
        this.configPath = path;
    }

    public void setup() {
        frameRate(FPS);

        // Load images during setup
        config = loadJSONObject(configPath);
        lives = config.getInt("lives");
        levels = config.getJSONArray("levels");

        wall = this.loadImage("src/main/resources/wall/solid.png");
        brokenWall = this.loadImage("src/main/resources/broken/broken.png");
        goalImage = this.loadImage("src/main/resources/goal/goal.png");
        playerAnimations = loadCharacterAnimations("player");
        redEnemyAnimations = loadCharacterAnimations("red_enemy");
        yellowEnemyAnimations = loadCharacterAnimations("yellow_enemy");

        for(int i = 0; i < bombAnimation.length; i++){
            bombAnimation[i] = this.loadImage("src/main/resources/bomb/bomb" + (i + 1) + ".png");
        }

        explosionSprites[0] = this.loadImage("src/main/resources/explosion/centre.png");
        explosionSprites[1] = this.loadImage("src/main/resources/explosion/horizontal.png");
        explosionSprites[2] = this.loadImage("src/main/resources/explosion/vertical.png");

        //Create ui, map and game manager
        this.ui = new UI(lives);
        this.ui.loadImages(this);

        this.map = new Map(wall, brokenWall, goalImage, redEnemyAnimations, yellowEnemyAnimations, playerAnimations);

        this.gameManager = new GameManager(map, ui, levels);
        this.gameManager.setBombAnimation(bombAnimation);
        this.gameManager.setExplosionSprites(explosionSprites);        
    }
    
    //Loading the 2D array for character animations
    private PImage[][] loadCharacterAnimations(String characterName){
        String characterType;
        PImage[][] tempAnimations = new PImage[4][4];

        if(characterName == "yellow_enemy"){
            characterType = "yellow";
        }else if(characterName == "red_enemy"){
            characterType = "red";
        }else{
            characterType = "player";
        }

        for(int i = 0; i < 4; i++){
            tempAnimations[0][i] = this.loadImage("src/main/resources/" + characterName + "/" + characterType + "_left" + (i+1) + ".png");
        }

        for(int i = 0; i < 4; i++){
            tempAnimations[1][i] = this.loadImage("src/main/resources/" + characterName + "/" + characterType + "_up" + (i+1) + ".png");        
        }

        for(int i = 0; i < 4; i++){
            tempAnimations[2][i] = this.loadImage("src/main/resources/" + characterName + "/" + characterType + "_right" + (i+1) + ".png");
        }


        if(characterName == "player"){
            for(int i = 0; i < 4; i++){
            tempAnimations[3][i] = this.loadImage("src/main/resources/" + characterName + "/" + characterType + (i+1) + ".png");        
            }

        }else{
            for(int i = 0; i < 4; i++){
                tempAnimations[3][i] = this.loadImage("src/main/resources/" + characterName + "/" + characterType + "_down" + (i+1) + ".png");        
            }
        }

        return tempAnimations;
    }

    public void draw() {
        //Ticks the game manager which ticks all the other objects, and draws all the objects 
        background(238, 129, 0);
        fill(75, 105, 45);
        rect(0, 64, 480, 416);

        playerDirection = keyCode;
        this.gameManager.tick(playerDirection, keyPressed);

        for(BrokenWall brokenWall : this.map.getBrokenWallList()){
            brokenWall.display(this);
        }

        for(Wall wall : this.map.getWallList()){
            wall.display(this);
        }

        for(Bomb bomb : this.gameManager.getBombList()){
            bomb.display(this);
        }

        for(RedEnemy redEnemy: this.map.getRedEnemies()){
            redEnemy.display(this);
        }

        for(YellowEnemy yellowEnemy: this.map.getYellowEnemies()){
            yellowEnemy.display(this);
        }

        for(Explosion explosion : this.gameManager.getExplosions()){
            explosion.display(this);
        }
        
        this.map.getGoal().display(this);
        this.map.getBomberMan().display(this);

        textAlign(CENTER, TOP);
        this.gameManager.getUI().display(this);
    }
    
    public UI getUI(){
        return ui;
    }

    public static void main(String[] args) {
        PApplet.main("demolition.App");
    }
}
