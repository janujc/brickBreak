package game;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * @author Januario Carreiro
 * Use this class to create new Brick objects in-game.
 * e.g. Brick brick = new Brick(0, 0, 50, 50) creates a sqaure Brick at (0, 0) where the
 * width and height of the brick are both 50 pixels.
 * Brick's color is determined by its health
 * Health of brick when using setHealth() should be between 1 and 5. May also use object.setFill() in
 * GameLoop class if necessary.
 * Permanent Brick objects are not permanent per-se, but should not count towards number of
 * bricks needed to be eliminated in a level as they have 1000 health points.
 */
public class Brick extends Rectangle {
    boolean destroyed;
    int health;

    /**
     * Default Constructor
     * Use when you want to remove a Brick object from game
     */
    public Brick() {
        super(0, 0, 0, 0);
        health = 0;
        destroyed = true;
    }

    /**
     * Constructor used to make new Bricks. Need to use setHealth() to set health.
     * @param x location x-axis
     * @param y location y-axis
     * @param width width of block
     * @param height height of block
     */
    public Brick(double x, double y, double width, double height) {
        super(x, y, width, height);
        health = 1;
        destroyed = false;
    }

    /**
     * Will reduce health by 1 point. Will also change color of brick to match health.
     */
    public void reduceHealth() {
        health--;
        switch(health) {
            case 6:
                super.setFill(Color.INDIGO);
                break;
            case 5:
                super.setFill(Color.BLUE);
                break;
            case 4:
                super.setFill(Color.GREEN);
                break;
            case 3:
                super.setFill(Color.YELLOW);
                break;
            case 2:
                super.setFill(Color.ORANGE);
                break;
            case 1:
                super.setFill(Color.RED);
                break;
            case 0:
                setDestroyed();
                break;
        }
    }

    /**
     * Sets health of a Brick object. This also determines the Brick object's color if health between 1 and 5.
     * @param value is the number of health points.
     */
    public void setHealth(int value) {
        health = value;
        switch(health) {
            case 5:
                super.setFill(Color.BLUE);
                break;
            case 4:
                super.setFill(Color.GREEN);
                break;
            case 3:
                super.setFill(Color.YELLOW);
                break;
            case 2:
                super.setFill(Color.ORANGE);
                break;
            case 1:
                super.setFill(Color.RED);
                break;
        }
    }

    /**
     * Sets instance variable destroyed to true.
     */
    public void setDestroyed() {
        destroyed = true;
    }

    /**
     * @return value of destroyed.
     */
    public boolean isDestroyed() {
        return destroyed;
    }

    /**
     * Makes a gray Brick Object with 1000 health points.
     */
    public void setPermanent() {
        health = 1000;
        super.setFill(Color.GRAY);
    }
}
