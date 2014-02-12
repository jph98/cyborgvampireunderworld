package com.froyo.gameloop.tiles;

import com.froyo.gameloop.gfx.Screen;
import com.froyo.gameloop.level.Level;

public class BasicTile extends Tile {

    protected int tileCoordinate;
    protected int tileColour;
    
    public BasicTile(int id, int x, int y, int tileColour, int levelColour) {
	
	super(id, false, false);
	this.tileCoordinate = x + y * 32;
	this.tileColour = tileColour;
	this.levelColour = levelColour;
    }
    
    @Override
    public void render(Screen screen, Level level, int x, int y) {
	
	screen.render(x, y, tileCoordinate, tileColour, Screen.BIT_MIRROR_NONE, Screen.NORMAL_SCALE);
	
    }

    @Override
    public void tick() {
    }
}