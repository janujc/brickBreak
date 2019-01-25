package game;

import javafx.scene.paint.Color;

public class PowerUpFast extends PowerUp {
    public Color myColor;

    public PowerUpFast(int radius) {
        super(radius);
        myColor = Color.BLUE;
    }
}
