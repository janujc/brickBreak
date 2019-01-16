package game;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Brick extends Rectangle {
    boolean destroyed;

    public Brick() {
        super(0, 0, 0, 0);
        destroyed = true;
    }

    public Brick(double x, double y, double width, double height, Paint color) {
        super(x, y, width, height);
        super.setFill(color);
        destroyed = false;
    }

    public void setDestroyed() {
        destroyed = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }
}
