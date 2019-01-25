package game;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class PowerUpEnd extends PowerUp {
    public Color myColor;

    public PowerUpEnd() {
        super(0);
    }

    public PowerUpEnd(int radius) {
        super(radius);
        myColor = Color.RED;
    }

    @Override
    public boolean powerUpIntersect(Rectangle platform) {
        var powerUpHit = Shape.intersect(this, platform);
        if (powerUpHit.getBoundsInLocal().getWidth() != -1) {
            return true;
        }
        return false;
    }
}
