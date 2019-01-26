package game;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * @author Januario Carreiro
 * Use this class to create new PowerUp objects in-game.
 * e.g. PowerUp(5, 2) creates a PowerUp object with a radius of 5 with a 50% chance of being
 * Green and a 50% chance of being Yellow.
 * If we do PowerUp(5, 5), the PowerUp object has a 20% of being either Green, Yellow, Blue,
 * Orange, or Red.
 * DO NOT let the second parameter be equal to or greater than 6.
 * What each color will mean in GameLoop:
 */
public abstract class PowerUp extends Circle {
    Color myColor;

    /**
     * Constructor used to make new PowerUp objects. Randomly determines which color it will be
     * based on the level given.
     * @param radius is the radius of the circle.
     */
    public PowerUp(int radius, Color color) {
        super(radius);
        myColor = color;
        super.setFill(myColor);
    }

//    public PowerUp generatePowerUp(int x, int y) {
//        Random rand = new Random();
//        double chance = rand.nextDouble();
//        if (chance <= 0.5) {
//            myPower[myPowerNumber] = new PowerUp(POWERUP_RADIUS, myLevel);
//            myPower[myPowerNumber].relocate(myBrickConfig[x][y].getX(), myBrickConfig[x][y].getY());
//            root.getChildren().add(myPower[myPowerNumber]);
//            myPowerNumber++;
//        }
//    }

    public abstract boolean powerUpIntersect(Rectangle platform);
}
