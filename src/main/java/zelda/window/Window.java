package zelda.window;

import javax.swing.JFrame;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

public class Window {
    private final Canvas canvas;
    public static final int WIDTH = 240;
    public static final int HEIGHT = 240;
    public static final int SCALE = 3;
    public static final int SCALED_WIDTH = WIDTH * SCALE;
    public static final int SCALED_HEIGHT = HEIGHT * SCALE;
    private static final BufferedImage layer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

    public Window(final Canvas canvas) {
        this.canvas = canvas;
    }

    public void create() {
        final JFrame frame = new JFrame("Zelda");
        frame.add(canvas);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static Dimension getDimension() {
        return new Dimension(Window.SCALED_WIDTH, Window.SCALED_HEIGHT);
    }

    public BufferedImage getLayer() {
        return layer;
    }

}
