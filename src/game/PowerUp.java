package game;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

/**
 * @author Januario Carreiro
 * This abstract class is the base for all the other PowerUp classes in our game. It contains all the methods that you
 * should need to be able to interact with PowerUp objects in-game and to create new PowerUp classes.
 * Things class is relatively simple, and should not fail. With that being said, unwanted results may occur if, when
 * creating new PowerUp classes and subsequent objects, the GameLoop class is not updated with the correct color
 * information and object initializations.
 * This class depends on the GameLoop class for correct information.
 * <p>
 * Example of how to use one of the subclasses:
 * PowerUp powerUp = new PowerUp(5, 100, 100);
 * This creates a PowerUp object with a radius of 5 at xPos 100, yPos 100.
 * powerUp(step, delay);
 * This updates the position of the PowerUp object by one frame.
 * powerUp.remove(root, 500);
 * This removes a PowerUp object from Group root and relocates the object to yPos 500. The reason why we relocate the
 * object is because root.getChildren.remove(Object) only makes the object invisible---it still interacts with other
 * objects in the group. In our case, if we did not relocate it to the bottom of the scene, the PowerUp would be
 * interacting with GameLoop.myPlatform for a few more frames, causing the player to receive multiple instances of the
 * same power-up (e.g. one PowerUpLife object could give 5 extra lives instead of one).
 */
public abstract class PowerUp extends Circle {
    private Color myColor;

    /**
     * Constructor used to make new PowerUp objects. All the PowerUp subclasses should make a call super(...) to create
     * a PowerUp object. In GameLoop, when we determine what action to do if PowerUp.intersect(shape), we depend on the
     * color of the PowerUp. Thus, each PowerUp class should have a unique color. Again, be sure to update the constant
     * POWERUP_COLORS[] in GameLoop if the color associated with a PowerUp object is changed or if new PowerUp classes
     * are created.
     * @param radius is the radius of the PowerUp
     * @param color is the color of the PowerUp --- this is determined the type of PowerUp i.e. colors should be unique
     * @param xPos is the x-position where the object should be created
     * @param yPos is the y-position where the object should be created
     */
    public PowerUp(int radius, Color color, double xPos, double yPos) {
        super(radius);
        myColor = color;
        super.setFill(myColor);
        super.relocate(xPos, yPos);
    }

    /**
     * This method returns myColor since myColor is private. We do not want to make myColor public and let it be
     * modified by other classes as a color uniquely determines a PowerUp object.
     * @return Color myColor
     */
    public Color getMyColor() {
        return myColor;
    }

    /**
     * This method checks whether the PowerUp is intersecting with another shape within the scene. In GameLoop, we only
     * use this to check whether the PowerUp is intersecting GameLoop.myPlatform.
     * @param shape is the Shape PowerUp could be intersecting. Note that Shape can be a Rectangle, Circle, or even
     *              a PowerUp as PowerUp extends Circle which extends Shape.
     * @return true if intersecting, false if not.
     */
    public boolean intersect(Shape shape) {
        var powerUpHit = Shape.intersect(this, shape);
        if (powerUpHit.getBoundsInLocal().getWidth() != -1) {
            return true;
        }
        return false;
    }

    /**
     * This method removes a PowerUp object from a Group and then changes the PowerUp object's y-position. It is
     * __important__ that @param yPos be a value past the position of another object with which we might be checking if
     * PowerUp intersects. If we do not do this, then our PowerUp object might interact with another object
     * multiple times. This is usually not an intended scenario. To be safe, make yPos be the size of the window of the
     * game. Any other value risks PowerUp interacting with other objects multiple times.
     * @param group is the Parent of this PowerUp object
     * @param yPos is where we want to relocate this PowerUp object (Usually bottom of screen)
     */
    public void remove(Group group, int yPos) {
        group.getChildren().remove(this);
        super.setLayoutY(yPos);
    }

    /**
     * This method updates the position of this PowerUp object. Since PowerUp objects fall down the screen in a
     * straight line, this method only updates the y-position.
     * @param speed is how fast the PowerUp falls
     * @param delay is the delay between frames
     */
    public void setPos(double speed, double delay) {
        super.setLayoutY(super.getLayoutY() + speed * delay);
    }
}
