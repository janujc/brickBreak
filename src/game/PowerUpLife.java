package game;

import javafx.scene.Group;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class PowerUpLife extends PowerUp {
    public PowerUpLife(int radius, double x, double y) {
        super(radius, Color.GREEN.brighter(), x, y);
    }
}
