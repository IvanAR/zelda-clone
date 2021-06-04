package zelda.entities.weapon;

import zelda.Game;
import zelda.entities.Enemy;
import zelda.entities.Entity;

import java.awt.*;
import java.awt.image.BufferedImage;

import static zelda.world.World.isFree;

public class Arrow extends Entity {
    private final double speed = 4;
    private final int life = 150;
    private int currentLife = 0;
    private int damage = 50;
    private final int direction;

    public static final int ARROW_WIDTH = 10;
    public static final int ARROW_HEIGHT = 2;

    public Arrow(int x, int y, int width, int height, BufferedImage sprite, int direction) {
        super(x, y, width, height, sprite);
        this.direction = direction;
    }

    @Override
    public void tick() {
        // TODO this logic can be extracted
        if (direction == rightDirection) {
            x += 1 * speed;;
        } else if (direction == leftDirection){
            x += -1 * speed;;
        } else if (direction == upDirection) {
            y += -1 * speed;
        } else if (direction == downDirection) {
            y += 1 * speed;
        }

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
        g.setColor(Color.WHITE);
        if (direction == rightDirection) {
            g.fillRect(getXCamera(), getYCamera(), ARROW_WIDTH, ARROW_HEIGHT);
        } else if (direction == leftDirection) {
            g.fillRect(getXCamera(), getYCamera(), ARROW_WIDTH, ARROW_HEIGHT);
        } else if (direction == upDirection) {
            g.fillRect(getXCamera(), getYCamera() - 15, ARROW_HEIGHT, ARROW_WIDTH);
        } else if (direction == downDirection) {
            g.fillRect(getXCamera() + 4, getYCamera(), ARROW_HEIGHT, ARROW_WIDTH);
        }
    }
}
