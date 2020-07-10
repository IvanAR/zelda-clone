package zelda;

import zelda.entities.Enemy;
import zelda.entities.Entity;
import zelda.entities.Player;
import zelda.graphics.SpriteSheet;
import zelda.graphics.UI;
import zelda.window.Window;
import zelda.world.World;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game extends Canvas implements Runnable, KeyListener {
    private Thread thread;
    private boolean isRunning;

    public static List<Entity> entities;
    public static List<Enemy> enemies;
    private Player player;
    private World world;
    private Window window;
    private UI ui;

    public static Random random;

    public Game() {
        setPreferredSize(Window.getDimension());
        addKeyListener(this);
        init();
    }

    private void init() {
        random = new Random();
        entities = new ArrayList<>();
        enemies = new ArrayList<>();

        window = new Window(this);
        player = new Player(30,30,22,22, SpriteSheet.playerSpriteSheet);
        ui = new UI(player);
        world = new World("/map.png", player, entities, enemies);

        entities.add(player);
    }

    public void open() {
        window.create();
    }

    public synchronized void start() {
        thread = new Thread(this);
        isRunning = true;
        thread.start();
    }

    public synchronized void stop() {
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void tick() {
        if (player.isDead()) {
            // TODO could be better
            init();
        } else {
            for (int i = 0; i < entities.size(); i++) {
                entities.get(i).tick();
            }
        }

    }

    public void render() {
        final BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        final Graphics g = window.getLayer().getGraphics();
        g.setColor(new Color(0, 0, 0));
        g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);

        world.render(g);
        for (int i = 0; i < entities.size(); i++) {
            entities.get(i).render(g);
        }
        ui.render(g);

        g.dispose();
        final Graphics bsGraphics = bs.getDrawGraphics();
        bsGraphics.drawImage(window.getLayer(), 0, 0, Window.SCALED_WIDTH, Window.SCALED_HEIGHT, null);
        bs.show();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();

        double fps = 60.0;
        double threshold = 1000000000 / fps;
        double delta = 0;
        int frames = 0;
        double timer = System.currentTimeMillis();

        requestFocus();
        while (isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / threshold;
            lastTime = now;
            if (delta >= 1) {
                tick();
                render();
                frames++;
                delta--;
            }
            if (System.currentTimeMillis() - timer >= 1000) {
                System.out.println("FPS: " + frames);
                frames = 0;
                timer += 1000;
            }
        }

        stop();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        player.move(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        player.stop(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

}
