package game;

import javafx.scene.paint.Color;

public class PowerUpEnd extends PowerUp {
    public Color myColor;

    public PowerUpEnd(int radius) {
        super(radius);
        myColor = Color.RED;
    }
}
