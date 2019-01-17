package game;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Brick extends Rectangle {
    boolean destroyed;
    int health;
    Color shade;

    public Brick() {
        super(0, 0, 0, 0);
        health = 0;
        destroyed = true;
    }

    public Brick(double x, double y, double width, double height, Color color) {
        super(x, y, width, height);
        shade = color;
        super.setFill(shade);
        health = 3;
        destroyed = false;
    }

    public void reduceHealth() {
        health--;
        switch(health) {
            case 2:
                super.setFill(shade.darker());
                break;
            case 1:
                super.setFill(shade.grayscale());
                break;
            case 0:
                setDestroyed();
                break;
        }
    }

    public void setDestroyed() {
        destroyed = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }
}
