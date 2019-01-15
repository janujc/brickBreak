package game;

import javafx.scene.shape.Rectangle;

public class Brick {
    private Rectangle myBrick;
    private boolean myState;

    public Brick(double x, double y, double width, double height) {
        myBrick = new Rectangle(x, y, width, height);
        myState = false;
    }

    public void setMyState(boolean value) {
        myState = value;
    }

    public boolean getMyState() {
        return myState;
    }
}