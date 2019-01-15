package game;

import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;

public class Brick extends Region {
    Rectangle myBrick;
    boolean myState;

    public Brick(double x, double y, double width, double height) {
        myBrick = new Rectangle(x, y, width, height);
        myState = false;
        getChildren().add(myBrick);
    }

    public void setMyState(boolean value) {
        myState = value;
    }

    public boolean getMyState() {
        return myState;
    }
}
