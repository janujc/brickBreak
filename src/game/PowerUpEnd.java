package game;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class PowerUpEnd extends PowerUp {
    public PowerUpEnd(int radius) {
        super(radius, Color.RED);
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
