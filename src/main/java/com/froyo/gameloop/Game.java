package com.froyo.gameloop;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable {

    public static final int WIDTH = 160;
    public static final int HEIGHT = WIDTH / 12 * 9;
    public static final int SCALE = 3;
    public static final String NAME = "Game";
   
    private JFrame frame;
    private Thread thread;
    
    public volatile boolean running = false;
    
    public int tickCount = 0;
    
    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    
    // Update pixels in the image
    private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	
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
    
    public void run() {
	
	long lastTime = System.nanoTime();
	// How many ns in a tick
	double nsPerTick = 1000000000D / 60D;
	
	int ticks = 0;
	int frames = 0;
	
	long lastTimer = System.currentTimeMillis();
	
	// How many ns have gone by so far
	double delta = 0;
	
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
		System.out.println("Frames: " + frames + ", ticks: " + ticks);
		frames = 0;
		ticks = 0;
	    }
	}
    }

    private void tick() {
	
	tickCount++;
	
	for (int i = 0; i < pixels.length; i++) {
	    pixels[i] = i + tickCount;
	}
    }
    
    private void render() {
	
	BufferStrategy bs = getBufferStrategy();
	
	// Triple buffering, more processing power required, but lets pixelating of images
	if (bs == null) {
	    createBufferStrategy(3);
	    return;
	}
	
	Graphics g = bs.getDrawGraphics();	
	g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
	g.dispose();
	bs.show();
	
    }

   

    private void update() {
	// TODO Auto-generated method stub
	
    }
    
    public static void main(String[] args) {
	new Game().start();
    }
    
}
