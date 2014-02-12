package com.froyo.gameloop.tiles;

import com.froyo.gameloop.gfx.Screen;
import com.froyo.gameloop.level.Level;

public class BasicSolidTile extends BasicTile {

    public BasicSolidTile(int id, int x, int y, int tileColour, int levelColour) {
	super(id, x, y, tileColour, levelColour);
	this.solid = true;
	this.levelColour = levelColour;
	
    }
    
    public void render(Screen screen, Level level, int x, int y) {
	screen.render(x, y, tileCoordinate, tileColour, Screen.BIT_MIRROR_NONE, Screen.NORMAL_SCALE);
    }
}