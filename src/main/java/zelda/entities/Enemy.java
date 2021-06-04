package zelda.entities;

import zelda.Game;
import zelda.graphics.SpriteSheet;

import java.awt.*;
import java.awt.image.BufferedImage;

import static zelda.world.World.isFree;

public class Enemy extends Entity {
    public static final BufferedImage ENEMY_SPRITE = SpriteSheet.enemySpriteSheet.getSprite(0, 7, 16, 22);

    private final Player player;

    private final int maxFps = 4;
    private int upDownIndex = 0, rightLeftIndex = 0;
    private final int upDownMaxIndex = 3, rightLeftMaxIndex = 3;

    private int direction = downDirection;
    private int frames = 0;

    private int life  = 100, maxLife = 100;
    private int dodgeRate = 10;
    private boolean damaged;

    private final BufferedImage[] rightMovement = new BufferedImage[4];
    private final BufferedImage[] leftMovement = new BufferedImage[4];
    private final BufferedImage[] downMovement = new BufferedImage[4];
    private final BufferedImage[] upMovement = new BufferedImage[4];

    public Enemy(int x, int y, int width, int height, SpriteSheet spriteSheet, final Player player) {
        // FIXME we could set Player as static and apply to a singleton
        super(x, y, width, height, ENEMY_SPRITE); // TODO fix sizes here
        this.player = player;

        for (int i = 0; i < downMovement.length; i++) {
            downMovement[i] = spriteSheet.getSprite(i * 16, 7, 16, 22);
        }
        for (int i = 0; i < rightMovement.length; i++) {
            rightMovement[i] = spriteSheet.getSprite(i * 16, 39, 16, 22);
        }
        for (int i = 0; i < upMovement.length; i++) {
            upMovement[i] = spriteSheet.getSprite(i * 16, 70, 16, 22);
        }
        for (int i = 0; i < leftMovement.length; i++) {
            leftMovement[i] = spriteSheet.getSprite(i * 16, 103, 16, 22);
        }
    }

    @Override
    public void tick() {
//        if (Game.random.nextInt(100) < 60) { // Randomizes Enemy movement
        if (collidesWith(player)) {
            attack();
        } else {
            if (getX() < player.getX() && isFree(getXPlusSpeed(), getY()) && !isSelfColliding(getXPlusSpeed(), getY())) {
                x += getSpeed();
                direction = rightDirection;
            } else if (getX() > player.getX() && isFree(getXMinusSpeed(), getY()) && !isSelfColliding(getXMinusSpeed(), getY())) {
                x -= getSpeed();
                direction = leftDirection;
            }
            if (getY() < player.getY() && isFree(getX(), getYPlusSpeed()) && !isSelfColliding(getX(), getYPlusSpeed())) {
                y += getSpeed();
                direction = downDirection;
            } else if (getY() > player.getY() && isFree(getX(), getYMinusSpeed()) && !isSelfColliding(getX(), getYMinusSpeed())) {
                y -= getSpeed();
                direction = upDirection;
            }
        }

        frames++;
        if (frames == maxFps) {
            frames = 0;
            if (direction == rightDirection || direction == leftDirection) {
                upDownIndex = 0;
                rightLeftIndex++;
                if (rightLeftIndex > rightLeftMaxIndex)
                    rightLeftIndex = 0;
            } else if (direction == upDirection || direction == downDirection) {
                upDownIndex++;
                rightLeftIndex = 0;
                if (upDownIndex > upDownMaxIndex)
                    upDownIndex = 0;
            }
            if (damaged) {
                damaged = false;
            }
        }

        if (isDead()) {
            Game.removeEnemy(this);
        }
    }

    public boolean isSelfColliding(int nextX, int nextY) {
        // TODO we could use the collidesWith in Entity
        getMask().setLocation(nextX, nextY);
        for (Enemy enemy : Game.getEnemies()) {
            if (enemy == this) continue;
            if (getMask().intersects(enemy.getMask())) {
                return true;
            }
        }
        return false;
    }

    // TODO set a better name
    public void hit(int hit) {
        if (Game.random.nextInt(100) > dodgeRate) {
            damaged = true;
            this.life -= hit;
        } else {
            System.out.println("miss");
        }
    }

    public boolean isDead() {
        return life <= 0;
    }


    @Override
    public void render(Graphics g) {
//        super.render(g);;
        if (direction == rightDirection) {
            g.drawImage(rightMovement[rightLeftIndex], getXCamera(), getYCamera(), null);
        } else if (direction == leftDirection) {
            g.drawImage(leftMovement[rightLeftIndex], getXCamera(), getYCamera(), null);
        } else if (direction == upDirection) {
            g.drawImage(upMovement[upDownIndex], getXCamera(), getYCamera(), null);
        } else if (direction == downDirection) {
            g.drawImage(downMovement[upDownIndex], getXCamera(), getYCamera(), null);
        }
        if (damaged) {
            g.setColor(new Color(255, 0, 0, 100));
            g.fillOval(getXCamera(), getYCamera(), getWidth(), getHeight());
        }
    }

    private void attack() {
        player.setHit(getPower());
    }
}
