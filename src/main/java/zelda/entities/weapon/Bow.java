package zelda.entities.weapon;

import zelda.graphics.SpriteSheet;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Bow extends Weapon {
    public static final int WIDTH = 7;
    public static final int HEIGHT = 16;
    public static final int UP_BOW = 0, DOWN_BOW = 1, RIGHT_BOW = 2, LEFT_BOW = 3;
    public static final BufferedImage[] BOW_SPRITES = new BufferedImage[4];

    static {
        BOW_SPRITES[RIGHT_BOW] = SpriteSheet.objectSpriteSheet.getSprite(450, 8, WIDTH, HEIGHT);
        BOW_SPRITES[LEFT_BOW] = SpriteSheet.objectSpriteSheet.getSprite(460, 8, WIDTH, HEIGHT);
        BOW_SPRITES[DOWN_BOW] = SpriteSheet.objectSpriteSheet.getSprite(468, 14, HEIGHT, WIDTH);
        BOW_SPRITES[UP_BOW] = SpriteSheet.objectSpriteSheet.getSprite(485, 14, HEIGHT, WIDTH);
    }

    public Bow(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
    }

    @Override
    public void render(Graphics g, int x, int y, int direction) {
        switch (direction) {
            case rightDirection:
                g.drawImage(getRightSprite(), x + 14, y + 5, null);
                break;
            case leftDirection:
                g.drawImage(getLeftSprite(), x, y + 5, null);
                break;
            case upDirection:
                g.drawImage(getUpSprite(), x, y, null);
                break;
            case downDirection:
                g.drawImage(getDownSprite(), x + 5, y + 15, null);
                break;
        }
    }

    private BufferedImage getRightSprite() {
        return  BOW_SPRITES[RIGHT_BOW];
    }

    private BufferedImage getLeftSprite() {
        return  BOW_SPRITES[LEFT_BOW];
    }

    private BufferedImage getUpSprite() {
        return  BOW_SPRITES[UP_BOW];
    }

    private BufferedImage getDownSprite() {
        return  BOW_SPRITES[DOWN_BOW];
    }
}
