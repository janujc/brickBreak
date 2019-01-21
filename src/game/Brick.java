package game;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Brick extends Rectangle {
    boolean destroyed;
    int health;

    public Brick() {
        super(0, 0, 0, 0);
        health = 0;
        destroyed = true;
    }

    public Brick(double x, double y, double width, double height) {
        super(x, y, width, height);
        health = 1;
        destroyed = false;
    }

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

    public void setDestroyed() {
        destroyed = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setPermanent() {
        health = 1000;
        super.setFill(Color.GRAY);
    }
}
