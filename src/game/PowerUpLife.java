package game;

import javafx.scene.paint.Color;

public class PowerUpLife extends PowerUp {
    public Color myColor;

    public PowerUpLife(int radius) {
        super(radius);
        myColor = Color.GREEN;
    }
}
