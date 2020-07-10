package zelda.entities.weapon;

import zelda.graphics.SpriteSheet;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Sword extends Weapon {
    public static final int WIDTH = 6;
    public static final int HEIGHT = 14;
    private static final int UP_SWORD = 0, DOWN_SWORD = 1, RIGHT_SWORD = 2, LEFT_SWORD = 3;
    public static final BufferedImage[] SWORD_SPRITES = new BufferedImage[4];

    static {
        SWORD_SPRITES[RIGHT_SWORD] = SpriteSheet.objectSpriteSheet.getSprite(486, 27, HEIGHT, WIDTH);
        SWORD_SPRITES[LEFT_SWORD] = SpriteSheet.objectSpriteSheet.getSprite(504, 27, HEIGHT, WIDTH);
        SWORD_SPRITES[DOWN_SWORD] = SpriteSheet.objectSpriteSheet.getSprite(503, 10, WIDTH, HEIGHT);
        SWORD_SPRITES[UP_SWORD] = SpriteSheet.objectSpriteSheet.getSprite(511, 10, WIDTH, HEIGHT);
    }

    public Sword(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
    }

    public Sword(int x, int y) {
        super(x, y, WIDTH, HEIGHT, SWORD_SPRITES[DOWN_SWORD]);
    }

    @Override
    public void render(Graphics g, int x, int y, int direction) {
        switch (direction) {
            case rightDirection:
                g.drawImage(getRightSprite(), x + 8, y + 13, null);
                break;
            case leftDirection:
                g.drawImage(getLeftSprite(), x, y + 13 , null);
                break;
            case upDirection:
                g.drawImage(getUpSprite(), x, y, null);
                break;
            case downDirection:
                g.drawImage(getDownSprite(), x + 14, y + 12, null);
                break;
        }
    }

    public BufferedImage getRightSprite() {
        return SWORD_SPRITES[RIGHT_SWORD];
    }

    public BufferedImage getLeftSprite() {
        return SWORD_SPRITES[LEFT_SWORD];
    }

    public BufferedImage getUpSprite() {
        return SWORD_SPRITES[UP_SWORD];
    }

    public BufferedImage getDownSprite() {
        return SWORD_SPRITES[DOWN_SWORD];
    }
}
