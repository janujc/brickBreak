package game;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.Random;

/**
 * @author Januario Carreiro
 * Use this class to create new PowerUp objects in-game.
 * e.g. PowerUp(5, 2) creates a PowerUp object with a radius of 5 with a 50% chance of being
 * Green and a 50% chance of being Yellow.
 * If we do PowerUp(5, 5), the PowerUp object has a 20% of being either Green, Yellow, Blue,
 * Orange, or Red.
 * DO NOT let the second parameter be equal to or greater than 6.
 * What each color will mean in GameLoop:
 * GREEN:   LIVES + 1
 * YELLOW:  EXTENDS PLATFORM
 * BLUE:    FASTER BALL
 * ORANGE:  REDUCES PLATFORM
 * RED:     GAME OVER
 */
public class PowerUp extends Circle {
    Color myColor;

    /**
     * Default constructor. Use this after removing PowerUp object from root in order to keep PowerUp
     * object from activating more than once.
     */
    public PowerUp() {
        super(0);
        myColor = Color.BLACK;
    }

    /**
     * Constructor used to make new PowerUp objects. Randomly determines which color it will be
     * based on the level given.
     * @param radius is the radius of the circle.
     * @param level is the current difficulty level. Should not be greater than 5.
     */
    public PowerUp(int radius, int level) {
        super(radius);
        Random rand = new Random();
        int generatedPowerUp = rand.nextInt(level);
        Color[] color = {Color.GREEN, Color.YELLOW, Color.BLUE, Color.ORANGE, Color.RED};
        myColor = color[generatedPowerUp];
        super.setFill(myColor);
    }
}
