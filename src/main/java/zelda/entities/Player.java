package zelda.entities;

import zelda.Game;
import zelda.entities.weapon.Arrow;
import zelda.entities.weapon.Bow;
import zelda.entities.weapon.Sword;
import zelda.entities.weapon.Weapon;
import zelda.graphics.SpriteSheet;
import zelda.window.Window;
import zelda.world.Camera;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import static zelda.entities.weapon.Arrow.*;
import static zelda.world.World.*;
import static zelda.world.tile.Tile.TILE_SIZE;

public class Player extends Entity {
    public static final int PLAYER_WIDTH = 20;
    public static final int PLAYER_HEIGHT = 30;

    private static final double WALKING_SPEED = 1.5;
    private static final double RUNNING_SPEED = 2.5;
    private boolean right, left, up, down, running;
    private boolean damaged;
    private boolean shooting;

    private boolean slashing;
    private int slashUpDownIndex = 0, slashRightLeftIndex = 0;
    private final int slashMaxIndex = 4;

    private int stamina = 100;
    private int maxStamina = 100;

    private int frames = 0;
    private final int maxFps = 5;
    private int upDownIndex = 0, rightLeftIndex = 0;
    private final int upDownMaxIndex = 7, rightLeftMaxIndex = 5;

    private int direction = downDirection;

    private Weapon[] weapons = new Weapon[2];
    private int currentWeapon = Weapon.sword;
    private int arrowAmmo = 20;
    private final int AMMUNITION_SIZE = 20;

    private final BufferedImage[] moveRight = new BufferedImage[6];
    private final BufferedImage[] moveLeft = new BufferedImage[6];
    private final BufferedImage[] moveDown = new BufferedImage[8];
    private final BufferedImage[] moveUp = new BufferedImage[8];
    private final BufferedImage[] stopMoveDirection = new BufferedImage[4];

    private final BufferedImage[] slashUp = new BufferedImage[5];
    private final BufferedImage[] slashDown = new BufferedImage[5];
    private final BufferedImage[] slashRight = new BufferedImage[5];
    private final BufferedImage[] slashLeft = new BufferedImage[5];

    private final BufferedImage bookCollectedMove;

    private boolean collectingBook = false;
    private int life = 100, maxLife = 100;
    private int dodgeRate = 90;

    public Player(int x, int y, int width, int height, SpriteSheet spriteSheet) {
        super(x, y, width, height, spriteSheet.getSprite(30, 0, PLAYER_WIDTH, PLAYER_HEIGHT));
        setSpeed(WALKING_SPEED);

        // Walking/Running moves
        for (int i = 0; i < moveRight.length; i++) {
            moveRight[i] = spriteSheet.getSprite(240 + (i * 30), 120, PLAYER_WIDTH, PLAYER_HEIGHT);
        }
        for (int i = 0; i < moveLeft.length; i++) {
            moveLeft[i] = spriteSheet.getSprite(240 + (i * 30), 30, PLAYER_WIDTH, PLAYER_HEIGHT);
        }
        for (int i = 0; i < moveUp.length; i++) {
            moveUp[i] = spriteSheet.getSprite(i * 30, 120, PLAYER_WIDTH, PLAYER_HEIGHT);
        }
        for (int i = 0; i < moveDown.length; i++) {
            moveDown[i] = spriteSheet.getSprite(i * 30, 30, PLAYER_WIDTH, PLAYER_HEIGHT);
        }

        stopMoveDirection[rightDirection] = spriteSheet.getSprite(120, 0, PLAYER_WIDTH, PLAYER_HEIGHT);
        stopMoveDirection[leftDirection] = spriteSheet.getSprite(150, 0, PLAYER_WIDTH, PLAYER_HEIGHT);
        stopMoveDirection[upDirection] = spriteSheet.getSprite(60, 0, PLAYER_WIDTH, PLAYER_HEIGHT);
        stopMoveDirection[downDirection] = spriteSheet.getSprite(30, 0, PLAYER_WIDTH, PLAYER_HEIGHT);

        // Slashing moves
        for (int i = 0; i < slashRight.length; i++) {
            slashRight[i] = spriteSheet.getSprite(240 + (i * 30), 180, PLAYER_WIDTH, PLAYER_HEIGHT);
        }
        for (int i = 0; i < slashLeft.length; i++) {
            slashLeft[i] = spriteSheet.getSprite(240 + (i * 30), 90, PLAYER_WIDTH, PLAYER_HEIGHT);
        }
        for (int i = 0; i < slashDown.length; i++) {
            slashDown[i] = spriteSheet.getSprite(i * 30, 90, PLAYER_WIDTH, PLAYER_HEIGHT);
        }
        for (int i = 0; i < slashUp.length; i++) {
            slashUp[i] = spriteSheet.getSprite(i * 30, 180, PLAYER_WIDTH, PLAYER_HEIGHT);
        }

        this.bookCollectedMove = spriteSheet.getSprite(420, 0, PLAYER_WIDTH, PLAYER_HEIGHT + 20);

        weapons[Weapon.sword] = new Sword(x, y);
        weapons[Weapon.bow] = null;
    }

    @Override
    public void tick() {
        // TODO set stamina recovering state
        if (running && stamina >= 0) {
            setSpeed(RUNNING_SPEED);
            stamina--;
        } else {
            setSpeed(WALKING_SPEED);
            if (stamina < 100)
                stamina++;
        }

        // TODO check collisions with enemies
        if (up && isFree(getX(), (int) (y - getSpeed()))) {
            y -= getSpeed();
            direction = upDirection;
        } else if (down && isFree(getX(), (int) (y + getSpeed()))) {
            y += getSpeed();
            direction = downDirection;
        }

        if (right && isFree((int) (x + getSpeed()), getY())) {
            x += getSpeed();
            direction = rightDirection;
        } else if (left && isFree((int) (x - getSpeed()), getY())) {
            x -= getSpeed();
            direction = leftDirection;
        }

        frames++;
        if (frames == maxFps) {
            frames = 0;

            if (slashing) {
                if (direction == rightDirection || direction == leftDirection) {
                    slashUpDownIndex = 0;
                    slashRightLeftIndex++;
                    if (slashRightLeftIndex > slashMaxIndex) {
                        slashRightLeftIndex = 0;
                        slashing = false;
                    }
                } else if (direction == upDirection  || direction == downDirection) {
                    slashRightLeftIndex = 0;
                    slashUpDownIndex++;
                    if (slashUpDownIndex > slashMaxIndex) {
                        slashUpDownIndex = 0;
                        slashing = false;
                    }
                }
            } else {
                if (right || left) {
                    upDownIndex = 0;
                    rightLeftIndex++;
                    if (rightLeftIndex > rightLeftMaxIndex)
                        rightLeftIndex = 0;

                } else if (up || down) {
                    rightLeftIndex = 0;
                    upDownIndex++;
                    if (upDownIndex > upDownMaxIndex)
                        upDownIndex = 0;
                }
            }

            if (damaged) {
                damaged = false;
            }
            if (collectingBook) {
                collectingBook = false;
            }
            if (shooting) {
                arrowAmmo--; // FIXME ammo when weapon get out of ammo
                shooting = false;
                // TODO add arrow Sprite
                final Arrow arrow = new Arrow(getX() + 8, getY() + 11, ARROW_WIDTH, ARROW_HEIGHT, null, direction); // TODO direction
                Game.addArrow(arrow);
            }

            collectItem();
        }

        Camera.x = Camera.clamp(getX() - (Window.WIDTH / 2), 0, (WIDTH * TILE_SIZE) - Window.WIDTH);
        Camera.y = Camera.clamp(getY() - (Window.HEIGHT / 2), 0, (HEIGHT * TILE_SIZE) - Window.HEIGHT);
    }

    @Override
    public void render(Graphics g) {
        drawPlayer(g);
        if (damaged) {
            g.setColor(new Color(255, 0, 0, 100));
            g.fillOval(getXCamera(), getYCamera(), getWidth(), getHeight());
        }
    }

    private void drawPlayer(final Graphics g) {
        if (collectingBook) {
            // TODO stop here and make it stays more time in the paint with enemies frozen around
            g.drawImage(bookCollectedMove, getXCamera(), getYCamera(), null);
        } else if (slashing) {
            drawSlashing(g, direction);
        } else {
            switch (direction) {
                case rightDirection:
                    g.drawImage(moveRight[rightLeftIndex], getXCamera(), getYCamera(), null);
                    drawWeapon(g, weapons[currentWeapon], rightDirection);
                    break;
                case leftDirection:
                    g.drawImage(moveLeft[rightLeftIndex], getXCamera(), getYCamera(), null);
                    drawWeapon(g, weapons[currentWeapon], leftDirection);
                    break;
                case upDirection:
                    drawWeapon(g, weapons[currentWeapon], upDirection);
                    g.drawImage(moveUp[upDownIndex], getXCamera(), getYCamera(), null);
                    break;
                case downDirection:
                    g.drawImage(moveDown[upDownIndex], getXCamera(), getYCamera(), null);
                    drawWeapon(g, weapons[currentWeapon], downDirection);
                    break;
                default:
                    g.drawImage(stopMoveDirection[direction], getXCamera(), getYCamera(), null);
                    break;
            }
        }

    }

    private void drawWeapon(final Graphics g, final Weapon weapon, final int direction) {
        if (weapon != null) {
            weapons[currentWeapon].render(g, getXCamera(), getYCamera(), direction);
        }
    }

    private void drawSlashing(final Graphics g, final int direction) {
        switch (direction) {
            case rightDirection:
                g.drawImage(slashRight[slashRightLeftIndex], getXCamera(), getYCamera(), null);
                break;
            case leftDirection:
                g.drawImage(slashLeft[slashRightLeftIndex], getXCamera(), getYCamera(), null);
                break;
            case upDirection:
                g.drawImage(slashUp[slashUpDownIndex], getXCamera(), getYCamera(), null);
                break;
            case downDirection:
                g.drawImage(slashDown[slashUpDownIndex], getXCamera(), getYCamera(), null);
                break;
            default:
                g.drawImage(stopMoveDirection[direction], getXCamera(), getYCamera(), null);
                break;
        }
    }

    public void move(final KeyEvent e) {
        if (isRightMoveKeyPressed(e)) {
            right = true;
            direction = rightDirection;
        } else if (isLeftMoveKeyPressed(e)) {
            left = true;
            direction = leftDirection;
        }

        if (isUpMoveKeyPressed(e)) {
            up = true;
            direction = upDirection;
        } else if (isDownMoveKeyPressed(e)) {
            down = true;
            direction = downDirection;
        }

        // TODO consider if player is moving
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            running = true;
        }
    }

    public void attack(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_F) {
            switch (currentWeapon) {
                case Weapon.sword:
                    slashing = true;
                    break;
                case Weapon.bow:
                    if (arrowAmmo > 0) {
                        shooting = true;
                    }
                    break;
            }
        }

    }

    public void changeWeapon(KeyEvent e) {
        // TODO apply constant to weapon here
        if (e.getKeyCode() == KeyEvent.VK_1 && currentWeapon != Weapon.sword) {
            currentWeapon = Weapon.sword;
        }
        if (e.getKeyCode() == KeyEvent.VK_2 && currentWeapon != Weapon.bow) {
            currentWeapon = Weapon.bow;
        }
    }

    public boolean isDead() {
        return life <= 0;
    }

    public void stop(final KeyEvent e) {
        if (isRightMoveKeyPressed(e)) right = false;
        if (isLeftMoveKeyPressed(e)) left = false;
        if (isUpMoveKeyPressed(e)) up = false;
        if (isDownMoveKeyPressed(e)) down = false;

        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            running = false;
        }
    }

    // TODO set a better name
    public void setHit(int hit) {
        if (Game.random.nextInt(100) > dodgeRate) {
            damaged = true;
            this.life -= hit;
            // TODO change hit probability here by power ups
            if (isDead()) {
                // TODO change this
            }
        }
    }

    private void collectItem() {
        for (int i = 0; i < Game.getEntities().size(); i++) {
            final Entity e = Game.getEntities().get(i);
            if (this.collidesWith(e)) {
                if (e instanceof LifeHeart) {
                    this.setLife(getMaxLife());
                    Game.removeEntity(e);
                    return;
                }
                if (e instanceof Book) {
                    this.setPower(this.getPower() + 20);
                    Game.removeEntity(e);
                    this.collectingBook = true;
                    return;
                }
                if (e instanceof Bow) {
                    if (weapons[Weapon.bow] == null) {
                        // TODO new Bow
                        weapons[Weapon.bow] = (Bow) e;
                    } else {
                        arrowAmmo += AMMUNITION_SIZE;
                    }
                    Game.removeEntity(e);
                    return;
                }
            }
        }
    }

    private boolean isRightMoveKeyPressed(final KeyEvent e) {
        return e.getKeyCode() == KeyEvent.VK_RIGHT ||
                e.getKeyCode() == KeyEvent.VK_D;
    }

    private boolean isLeftMoveKeyPressed(final KeyEvent e) {
        return e.getKeyCode() == KeyEvent.VK_LEFT ||
                e.getKeyCode() == KeyEvent.VK_A;
    }

    private boolean isUpMoveKeyPressed(final KeyEvent e) {
        return e.getKeyCode() == KeyEvent.VK_UP ||
                e.getKeyCode() == KeyEvent.VK_W;
    }

    private boolean isDownMoveKeyPressed(final KeyEvent e) {
        return e.getKeyCode() == KeyEvent.VK_DOWN ||
                e.getKeyCode() == KeyEvent.VK_S;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getMaxLife() {
        return maxLife;
    }

    public int getStamina() {
        return stamina;
    }

    public int getMaxStamina() {
        return maxStamina;
    }

    @Override
    public double getSpeed() {
        return super.getSpeed();
    }
}
