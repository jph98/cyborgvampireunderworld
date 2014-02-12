package com.froyo.gameloop;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.froyo.gameloop.entities.Player;
import com.froyo.gameloop.gfx.Colours;
import com.froyo.gameloop.gfx.Font;
import com.froyo.gameloop.gfx.Screen;
import com.froyo.gameloop.gfx.SpriteSheet;
import com.froyo.gameloop.level.Level;

public class Game extends Canvas implements Runnable {

    public static final int WIDTH = 160;
    public static final int HEIGHT = WIDTH / 12 * 9;
    public static final int SCALE = 3;
    public static final String NAME = "Game";

    private JFrame frame;
    private Thread thread;

    public volatile boolean running = false;

    public int countTick = 0;

    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT,
	    BufferedImage.TYPE_INT_RGB);

    // Update pixels in the image
    private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer())
	    .getData();

    // Six different shades of each colour, not to overload things
    private int[] colours = new int[6 * 6 * 6];

    private Screen screen;
    public InputHandler input;

    public Level level;
    public Player player;

    /**
     * Game constructor.
     */
    public Game() {

	setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
	setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
	setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

	frame = new JFrame(NAME);

	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setLayout(new BorderLayout());

	frame.add(this, BorderLayout.CENTER);
	frame.pack();

	frame.setResizable(false);
	frame.setLocationRelativeTo(null);
	frame.setVisible(true);
    }

    public synchronized void start() {

	running = true;
	thread = new Thread(this, NAME + "_main");
	thread.start();
    }

    public synchronized void stop() {

	running = false;
	if (thread != null) {
	    try {
		thread.join();
	    } catch (InterruptedException e) {
	    }
	}
    }

    public void init() {

	int index = 0;

	for (int r = 0; r < 6; r++) {

	    for (int g = 0; g < 6; g++) {

		for (int b = 0; b < 6; b++) {

		    // red, red
		    int rr = (r * 255) / 5;
		    int gg = (g * 255) / 5;
		    int bb = (b * 255) / 5;

		    // 2 to the 8 bits for each one, colours
		    int bytes = rr << 16 | gg << 8 | bb;
		    colours[index++] = bytes;

		    // If we want a colour not to be rendered (the white) we set
		    // it to 255
		}
	    }
	}

	SpriteSheet spriteMap = new SpriteSheet("sprite_sheet.png");
	screen = new Screen(WIDTH, HEIGHT, spriteMap);
	input = new InputHandler(this);
	level = new Level("water_test_level.png");
	player = new Player(level, 0, 0, input);
	level.addEntity(player);
    }

    public void run() {

	long lastTime = System.nanoTime();
	// How many ns in a tick
	double nsPerTick = 1000000000D / 60D;

	int ticks = 0;
	int frames = 0;

	long lastTimer = System.currentTimeMillis();

	// How many ns have gone by so far
	double delta = 0;

	// screen
	init();

	while (running) {
	    long now = System.nanoTime();
	    delta += (now - lastTime) / nsPerTick;
	    lastTime = now;

	    boolean shouldRender = true;

	    while (delta >= 1) {
		ticks++;
		tick();
		delta -= 1;
		shouldRender = true;
	    }

	    // Otherwise we end up with huge frame rate
	    try {
		Thread.sleep(2);
	    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }

	    if (shouldRender) {
		frames++;
		render();
	    }

	    if (System.currentTimeMillis() - lastTimer >= 1000) {
		lastTimer += 1000;
		System.out.println("ticks: " + ticks + ", frames: " + frames);
		frames = 0;
		ticks = 0;
	    }
	}
    }    

    private void tick() {

	countTick++;
	level.tick();
    }

    private void render() {

	BufferStrategy bs = getBufferStrategy();

	// Triple buffering, more processing power required, but lets pixelating
	// of images
	if (bs == null) {
	    createBufferStrategy(3);
	    return;
	}

	int xOffset = player.x - (screen.width / 2);
	int yOffset = player.y - (screen.height / 2);

	// Tiles
	level.renderTiles(screen, xOffset, yOffset);

	// Entities
	level.renderEntities(screen);

	for (int y = 0; y < screen.height; y++) {

	    for (int x = 0; x < screen.width; x++) {

		int colourCode = screen.pixels[x + y * screen.width];

		if (colourCode < 255)
		    pixels[x + y * WIDTH] = colours[colourCode];
	    }
	}

	Graphics g = bs.getDrawGraphics();
	g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
	g.dispose();
	bs.show();

    }

    public static void main(String[] args) {
	new Game().start();
    }

}
