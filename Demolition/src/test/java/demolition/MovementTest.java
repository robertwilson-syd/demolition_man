package demolition;

import processing.core.PApplet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MovementTest {

    //Testing that coordinates are correctly updated by movement and ensuring movement is stopped by walls
    @Test 
    public void basicTest() {
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
		int initialX = app.map.getBomberMan().getX();
		int initialY = app.map.getBomberMan().getY();

		app.gameManager.tick(39, true);
        // Call draw again to move onto the next frame
        app.draw();

        // Verify the new state of the program with assertions
		if(app.map.isDestinationPassable(app.map.getBomberMan().getX(), app.map.getBomberMan().getY())){
			assertEquals(app.map.getBomberMan().getX(), initialX + 32);
			assertEquals(app.map.getBomberMan().getY(), initialY);
		}else{
			assertEquals(app.map.getBomberMan().getX(), initialX);
			assertEquals(app.map.getBomberMan().getY(), initialY);
		}
		assertEquals(app.map.getBomberMan().getDirection(), 2);
    }
    
}
