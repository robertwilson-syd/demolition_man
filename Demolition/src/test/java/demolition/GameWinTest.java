package demolition;

import processing.core.PApplet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameWinTest {

    //Testing that the game ends when the player reaches the final goal and that the right message is displayed ("YOU WIN")
    @Test 
    public void gameWinTest() {
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
		app.map.getBomberMan().setX(416);
		app.map.getBomberMan().setY(416);

        // Call draw again to move onto the next frame
		app.draw();
		app.draw();
		app.map.getBomberMan().setX(416);
		app.map.getBomberMan().setY(256);
		
		app.draw();
        // Verify the new state of the program with assertions
		assertEquals("YOU WIN", app.getUI().getGameFinishMessage());
		assertTrue(app.getUI().getGameOver());

    }
    
}
