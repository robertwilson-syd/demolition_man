package demolition;

import processing.core.PApplet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EnemyDeathTest {

    //Testing that when the player and enemies are on the same tile, the player loses a life and the level is reset.
    @Test 
    public void enemyDeathTest() {
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
		app.getUI().setLives(3);
		app.map.getBomberMan().setX(256);
		app.map.getBomberMan().setY(224);

        // Call draw again to move onto the next frame
		app.draw();
		assertEquals(2, app.getUI().getLives());
		app.draw();
		app.map.getBomberMan().setX(160);
		app.map.getBomberMan().setY(352);
		app.draw();
		assertEquals(1, app.getUI().getLives());
		
		app.draw();
        // Verify the new state of the program with assertions
		assertEquals(32, app.map.getBomberMan().getX());
		assertEquals(96, app.map.getBomberMan().getY());

    }
    
}
