package com.froyo.gameloop.entities;

import com.froyo.gameloop.gfx.Screen;
import com.froyo.gameloop.level.Level;
import com.froyo.gameloop.tiles.Tile;

/**
 * Represents all things that move.
 */
public abstract class Mob extends Entity {

    protected String name;
    protected int speed;
    protected int numSteps = 0;
    protected boolean isMoving;
    
    // facing camera
    protected int movingDir = 1;
    protected int scale = 1;
    
    public Mob(Level level, String name, int x, int y, int speed) {
	super(level);
	this.name = name;
	this.x = x;
	this.y = y;
	this.speed = speed;
    }

    @Override
    public void tick() {
    }

    @Override
    public void render(Screen screen) {
    }
    
    // number to move in that direction
    public void move(int xa, int ya) {
	
	if (xa != 0 && ya != 0) {
	    // So that we don't move two steps diagonally
	    move(xa, 0);
	    move(0, ya);
	    numSteps--;
	    return;
	}
	numSteps++;
	
	if (!hasCollided(xa, ya)) {
	    if (ya < 0) movingDir = 0;
	    if (ya > 0) movingDir = 1;
	    if (xa < 0) movingDir = 2;
	    if (xa > 0) movingDir = 3;
	    x += xa * speed;
	    y += ya * speed;
	}
    }
    
    public abstract boolean hasCollided(int xa, int ya);
    
    // Relative movement to see if solid (xa and ya - how much we're moving)
    protected boolean isSolidTile(int xa, int ya, int x, int y) {
	
	if (level == null) return false;
	
	Tile lastTile = level.getTile((this.x + x) >> 3, (this.y + y) >> 3);
	Tile newTile = level.getTile((this.x + x + xa) >> 3, (this.y + y + ya) >> 3);
	
	if (!lastTile.equals(newTile) && newTile.isSolid()) return true;
	
	return false;
    }
    
    public String getName() {
	return this.name;
    }
}
