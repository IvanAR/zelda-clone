package zelda.entities.weapon;

import zelda.Game;
import zelda.entities.Enemy;
import zelda.entities.Entity;

import java.awt.*;
import java.awt.image.BufferedImage;

import static zelda.world.World.isFree;

public class Arrow extends Entity {
    private final int dx, dy;
    private final double speed = 4;
    private final int life = 150;
    private int currentLife = 0;
    private int damage = 50;

    public static final int ARROW_WIDTH = 10;
    public static final int ARROW_HEIGHT = 2;

    public Arrow(int x, int y, int width, int height, BufferedImage sprite, int dx, int dy) {
        // spriteSheet.getSprite(30, 0, PLAYER_WIDTH, PLAYER_HEIGHT); // TODO add sprite
        super(x, y, width, height, sprite);
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public void tick() {
        x += dx * speed;
        y += dy * speed;
        currentLife++;
        if (currentLife == life) {
            Game.removeArrow(this);
        }

        // TODO fix isFree dimensions for arrow
        if (!isFree(getX(), (int)(y - getSpeed()))) {
            Game.removeArrow(this);
            return;
        }

        for (int i = 0; i < Game.getEnemies().size(); i++) {
            final Enemy enemy = Game.getEnemies().get(i);
            if (this.collidesWith(enemy)) {
                enemy.hit(this.getPower() + damage);
                Game.removeArrow(this);
                return;
            }
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillRect(getXCamera(), getYCamera(), ARROW_WIDTH, ARROW_HEIGHT);
    }
}
