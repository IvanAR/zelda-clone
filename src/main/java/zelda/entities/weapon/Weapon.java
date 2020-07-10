package zelda.entities.weapon;

import zelda.entities.Entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Weapon extends Entity {
    public static final int sword = 0, bow = 1;

    public Weapon(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
    }

    public void render(Graphics g, final int x, final int y, final int direction) {
        super.render(g);
    }
}
