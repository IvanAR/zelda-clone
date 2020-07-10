package zelda.entities;

import zelda.world.Camera;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Entity {
    protected double x, y, width, height;
    protected final int rightDirection = 0, leftDirection = 1, upDirection = 2, downDirection = 3;
    private final BufferedImage sprite;

    private double speed = 0.8;
    private int power = 5;

    public Entity(int x, int y, int width, int height, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = sprite;
    }

    public void render(final Graphics g) {
        g.drawImage(sprite, getXCamera(), getYCamera(), null);
    }

    public void tick(){}

    public boolean collidesWith(final Entity ce) {
        final Rectangle e1Mask = new Rectangle(this.getX() + this.getMaskX(), this.getY() + this.getMaskY(), this.getMaskW(), this.getMaskH());
        final Rectangle e2Mask = new Rectangle(ce.getX() + ce.getMaskX(), ce.getY() + ce.getMaskY(), ce.getMaskW(), ce.getMaskH());
        return e1Mask.intersects(e2Mask);
    }

    public int getX() {
        return (int) x;
    }

    public int getXPlusSpeed() {
        return (int) (x + getSpeed());
    }

    public int getXMinusSpeed() {
        return (int) (x - getSpeed());
    }

    public int getXCamera() {
        return (int) x - Camera.x;
    }

    public int getY() {
        return (int) y;
    }

    public int getYPlusSpeed() {
        return (int) (y + getSpeed());
    }

    public int getYMinusSpeed() {
        return (int) (y - getSpeed());
    }

    public int getYCamera() {
        return (int) y - Camera.y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getWidth() {
        return (int) width;
    }

    public int getHeight() {
        return (int) height;
    }

    public int getMaskX() {
        return 0;
    }

    public int getMaskY() {
        return 0;
    }

    public int getMaskW() {
        return getWidth();
    }

    public int getMaskH() {
        return getHeight();
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }
}
