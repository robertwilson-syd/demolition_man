package demolition;

import processing.core.PApplet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameOverTest {

    //Testing whether the game ends once the player runs out of lives
    @Test 
    public void bombDeathTest() {
        // Create an instance of your application
        App app = new App();

        // Set the program to not loop automatically
        app.noLoop();

        // Set the path of the config file to use
        app.setConfig("src/test/resources/config.json");

        // Tell PApplet to create the worker threads for the program
        PApplet.runSketch(new String[] {"App"}, app);

        // Call App.setup() to load in sprites
        app.setup();

        // Set a 1 second delay to ensure all resources are loaded
        app.delay(1000);

        // Call draw to update the program.
        app.draw();

        // Call keyPressed() or similar
		//Create bomb
		app.getUI().setLives(1);
		app.map.getBomberMan().setX(32);
		app.map.getBomberMan().setY(96);
		app.gameManager.tick(32, true);

        // Call draw again to move onto the next frame
		int i = 120;
        while(i > 0){
			app.draw();
			i--;
		}

        // Verify the new state of the program with assertions
		assertTrue(app.getUI().getGameOver());

    }


	//Testing whether the game ends once the level timer runs out
	@Test 
    public void TimerTest() {
        // Create an instance of your application
        App app = new App();

        // Set the program to not loop automatically
        app.noLoop();

        // Set the path of the config file to use
        app.setConfig("src/test/resources/config.json");

        // Tell PApplet to create the worker threads for the program
        PApplet.runSketch(new String[] {"App"}, app);

        // Call App.setup() to load in sprites
        app.setup();

        // Set a 1 second delay to ensure all resources are loaded
        app.delay(1000);

        // Call draw to update the program.
        app.draw();

        // Call keyPressed() or similar
		//Create bomb
		app.getUI().setLevelTime(1);

        // Call draw again to move onto the next frame
		int i = 120;
        while(i > 0){
			app.draw();
			i--;
		}

        // Verify the new state of the program with assertions
		assertTrue(app.getUI().getGameOver());

    }
    
}
