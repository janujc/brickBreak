package game;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class PowerUpLife extends PowerUp {
    public PowerUpLife(int radius) {
        super(radius, Color.GREEN.brighter());
    }
}
