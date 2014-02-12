package com.froyo.gameloop.tiles;

import com.froyo.gameloop.gfx.Screen;
import com.froyo.gameloop.level.Level;

public class AnimatedTile extends BasicTile {

    private int[][] animTileCoords;
    private int animFrameIndex;
    private long animLastIterTime;
    private int animDelay;
    
    /**
     * Use a 2D array for the animated tile.
     * 1. x, y
     * 2. x, y
     * 3. x, y
     */
    public AnimatedTile(int id, int[][] animTileCoords, int tileColour, int levelColour, int animDelay) {
	
	super(id, animTileCoords[0][0], animTileCoords[0][1], tileColour, levelColour);
	this.animTileCoords = animTileCoords;
	this.animFrameIndex = 0;
	this.animLastIterTime = System.currentTimeMillis();
	this.animDelay = animDelay;
    }
    
    @Override
    public void tick() {
	
	if ( (System.currentTimeMillis() - animLastIterTime) >= animDelay) {
	    animLastIterTime = System.currentTimeMillis();
	    animFrameIndex = (animFrameIndex + 1) % animTileCoords.length;
	    tileCoordinate = (animTileCoords[animFrameIndex][0] + animTileCoords[animFrameIndex][1] * 32);
	}
    }
    
    public void render(Screen screen, Level level, int x, int y) {
	screen.render(x, y, super.tileCoordinate, tileColour, Screen.BIT_MIRROR_NONE, Screen.NORMAL_SCALE);
    }

    
}