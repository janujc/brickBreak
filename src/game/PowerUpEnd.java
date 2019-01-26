package game;

import javafx.scene.paint.Color;

/**
 * @author Januario Carreiro
 * This is a subclass of PowerUp. Please read the documentation for the PowerUp abstract class.
 * <p>
 * In the current implimentation of game, there are five unique PowerUps. PowerUpEnd objects have a chance of being
 * created only in the last level. If PowerUpEnd.intersect(myPlatform), the game ends immediately.
 */
public class PowerUpEnd extends PowerUp {
    /**
     * To create a PowerUpEnd object in the GameLoop class, the radius, x-position, and y-position of the PowerUp
     * object must be specified. The color cannot be specified as the color of a PowerUp is unique to the kind of
     * PowerUp it is. PowerUpLife objects are always Color.RED.
     * @param radius is the radius of the PowerUp
     * @param x is the x-position where the object should be created
     * @param y is the y-position where the object should be created
     */
    public PowerUpEnd(int radius, double x, double y) {
        super(radius, Color.RED, x, y);
    }
}
