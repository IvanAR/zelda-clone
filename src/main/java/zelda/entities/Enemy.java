package zelda.entities;

import zelda.Game;
import zelda.graphics.SpriteSheet;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import static zelda.world.World.isFree;

public class Enemy extends Entity {
    public static final BufferedImage ENEMY_SPRITE = SpriteSheet.enemySpriteSheet.getSprite(1, 6, 16, 20);

    private final Player player;

    private final int maxFps = 5;
    private int upDownIndex = 0, rightLeftIndex = 0;
    private final int upDownMaxIndex = 3, rightLeftMaxIndex = 3;

    private final int rightDirection = 0, leftDirection = 1, upDirection = 2, downDirection = 3;
    private int direction = downDirection;
    private int frames = 0;

    private final BufferedImage[] rightMovement = new BufferedImage[4];
    private final BufferedImage[] leftMovement = new BufferedImage[4];
    private final BufferedImage[] downMovement = new BufferedImage[4];
    private final BufferedImage[] upMovement = new BufferedImage[4];


    public Enemy(int x, int y, int width, int height, SpriteSheet spriteSheet, final Player player) {
        // FIXME we could set Player as static and apply to a singleton
        super(x, y, width, height, spriteSheet.getSprite(0, 7, 16, 22)); // TODO fix sizes here
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
        if (!collidesWith(player)) {
            if (getX() < player.getX() && isFree(getXPlusSpeed(), getY()) && !isSelfCollinding(getXPlusSpeed(), getY())) {
                x += getSpeed();
                direction = rightDirection;
            } else if (getX() > player.getX() && isFree(getXMinusSpeed(), getY()) && !isSelfCollinding(getXMinusSpeed(), getY())) {
                x -= getSpeed();
                direction = leftDirection;
            }
            if (getY() < player.getY() && isFree(getX(), getYPlusSpeed()) && !isSelfCollinding(getX(), getYPlusSpeed())) {
                y += getSpeed();
                direction = downDirection;
            } else if (getY() > player.getY() && isFree(getX(), getYMinusSpeed()) && !isSelfCollinding(getX(), getYMinusSpeed())) {
                y -= getSpeed();
                direction = upDirection;
            }
        } else {
            // TODO hits are too fast, change it.
            hit();
        }
//        }

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
        }
    }

    public boolean isSelfCollinding(int nextX, int nextY) {
        final Rectangle currentEnemy = getMask(nextX, nextY);
        for (Enemy enemy : Game.enemies) {
            if (enemy == this) continue;
            final Rectangle collidingEnemy = new Rectangle(enemy.getX() + getMaskX(), enemy.getY() + getMaskY(), getMaskW(), getMaskH());
            if (currentEnemy.intersects(collidingEnemy)) {
                return true;
            }
        }
        return false;
    }

    private Rectangle getMask(int nextX, int nextY) {
        return new Rectangle(nextX + getMaskX(), nextY + getMaskY(), getMaskW(), getMaskH());
    }

    @Override
    public void render(Graphics g) {
//        super.render(g);;
        //g.setColor(BLUE); Testing mask here
//        g.fillRect(getX() + maskX - Camera.x, getY() + maskY - Camera.y, maskW, maskH);
        if (direction == rightDirection) {
            g.drawImage(rightMovement[rightLeftIndex], getXCamera(), getYCamera(), null);
        } else if (direction == leftDirection) {
            g.drawImage(leftMovement[rightLeftIndex], getXCamera(), getYCamera(), null);
        } else if (direction == upDirection) {
            g.drawImage(upMovement[upDownIndex], getXCamera(), getYCamera(), null);
        } else if (direction == downDirection) {
            g.drawImage(downMovement[upDownIndex], getXCamera(), getYCamera(), null);
        }
    }

    private void hit() {
        player.setHit(getPower());
    }

}