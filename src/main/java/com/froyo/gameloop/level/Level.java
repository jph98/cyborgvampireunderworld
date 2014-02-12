package com.froyo.gameloop.level;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.froyo.gameloop.entities.Entity;
import com.froyo.gameloop.gfx.Screen;
import com.froyo.gameloop.tiles.Tile;

public class Level {

    // Tile id's
    private byte[] tiles;
    public int width;
    public int height;

    public List<Entity> entities = new ArrayList<Entity>();
    
    private String levelImage;
    private BufferedImage image;

    public Level(String levelImagePath) {

	if (levelImagePath != null) {
	    this.levelImage = levelImagePath;
	    this.loadLevelFromFile();
	} else {
	    tiles = new byte[width * height];
	    this.width = width;
	    this.height = height;
	    this.generateLevel();
	}
    }

    private void loadLevelFromFile() {

	try {
	    this.image = ImageIO.read(this.getClass()
		    .getResource(this.levelImage));

	    this.width = image.getWidth();
	    this.height = image.getHeight();
	    tiles = new byte[width * height];
	    this.loadTiles();
	    
	} catch (IOException e) {
	    System.out.println("Error loading level from file " + this.levelImage);
	}
    }

    private void loadTiles() {
	
	int[] tileColours = this.image.getRGB(0, 0, width, height, null, 0, width);
	
	for (int y = 0; y < height; y++) {
	    
	    for (int x = 0; x < width; x++) {
		
		for (Tile t: Tile.tiles) {
		    
		    if (t != null && t.getLevelColour() == tileColours[x + y * width]) {
			this.tiles[x + y * width] = t.getId();
		    }
		}
	    }
	}
    }
    
    public void saveLevelToFile() {
	
	try {
	    ImageIO.write(image, "png", new File(Level.class.getResource(this.levelImage).getFile()));
	} catch (IOException e) {
	    System.out.println("Error loading level image " + this.levelImage);
	}
    }
    
    public void alterTile(int x, int y, Tile newTile) {
	
	this.tiles[x +y * width] = newTile.getId();
	image.setRGB(x, y, newTile.getLevelColour());
    }

    public void generateLevel() {

	for (int y = 0; y < height; y++) {
	    for (int x = 0; x < width; x++) {

		// Grass and stone background
		if (x * y % 10 < 7) {
		    tiles[x + y * width] = Tile.GRASS.getId();
		} else {
		    tiles[x + y * width] = Tile.STONE.getId();
		}

	    }
	}
    }

    // This is the camera
    public void renderTiles(Screen screen, int xOffset, int yOffset) {

	if (xOffset < 0)
	    xOffset = 0;
	if (xOffset > ((width << 3) - screen.width))
	    xOffset = ((width << 3) - screen.width);

	if (yOffset < 0)
	    yOffset = 0;
	if (yOffset > ((height << 3) - screen.height))
	    yOffset = ((height << 3) - screen.height);

	screen.setOffset(xOffset, yOffset);

	for (int y = 0; y < height; y++) {
	    for (int x = 0; x < width; x++) {
		getTile(x, y).render(screen, this, x << 3, y << 3);
	    }
	}
    }

    public void tick() {

	for (Entity e : entities) {
	    e.tick();
	}

	for (Tile t : Tile.tiles) {
	    if (t == null) {
		break;
	    }
	    t.tick();
	}
    }

    public void renderEntities(Screen screen) {

	for (Entity e : entities) {
	    e.render(screen);
	}
    }

    public Tile getTile(int x, int y) {

	if (x < 0 || x > width || y < 0 || y >= height)
	    return Tile.VOID;

	// get at byte value
	return Tile.tiles[tiles[x + y * width]];

    }

    public void addEntity(Entity entity) {

	entities.add(entity);
    }
}
