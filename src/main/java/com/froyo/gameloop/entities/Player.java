package com.froyo.gameloop.entities;

import com.froyo.gameloop.InputHandler;
import com.froyo.gameloop.gfx.Colours;
import com.froyo.gameloop.gfx.Screen;
import com.froyo.gameloop.level.Level;
import com.froyo.gameloop.tiles.Tile;

public class Player extends Mob {

    private static final int COLOUR = Colours.get(-1, 111, 145, 543);
    private InputHandler input;
    private int scale = 1;
    private boolean isSwimming;
    private int tickCount = 0;

    public Player(Level level, int x, int y, InputHandler input) {
	super(level, "Player", x, y, Screen.NORMAL_SCALE);
	this.input = input;
    }

    public void tick() {
	int xa = 0;
	int ya = 0;

	if (input.up.isPressed())
	    ya--;
	if (input.down.isPressed())
	    ya++;
	if (input.left.isPressed())
	    xa--;
	if (input.right.isPressed())
	    xa++;

	if (xa != 0 || ya != 0) {
	    move(xa, ya);
	    isMoving = true;
	} else {
	    isMoving = false;
	}

	// Scale up the player image (twice as big)
	this.scale = 1;

	if (level.getTile(this.x >> 3, this.y >> 3).getId() == Tile.WATER
		.getId()) {
	    isSwimming = true;
	}

	if (isSwimming
		&& level.getTile(this.x >> 3, this.y >> 3).getId() != Tile.WATER
			.getId()) {
	    isSwimming = false;
	}
	// How many ticks have gone by since we've updated
	tickCount++;
    }

    public void render(Screen screen) {

	// Where the player is found on the spritesheet
	int xTile = 0;
	int yTile = 28;
	int walkingSpeed = 3;

	// Number between 0 and 1

	// top part of the body
	int flipTop = (numSteps >> walkingSpeed) & 1;

	// bottom part of the body
	int flipBottom = (numSteps >> walkingSpeed) & 1;

	if (movingDir == 1) {
	    xTile += 2;
	} else if (movingDir > 1) {
	    // Third player tile start
	    xTile += 4 + ((numSteps >> walkingSpeed) & 1) * 2;
	    flipTop = (movingDir - 1) % 2;
	}

	// Size of the player (8x8)
	int modifier = 8 * scale;
	int xOffset = x - modifier / 2;
	int yOffset = y - modifier / 2 - 4;
	
	if (isSwimming) {
	    int waterColour = 0;
	    yOffset += 4;
	    
	    // 1/4 of this
	    if (tickCount % 60 < 15) {
		waterColour = Colours.get(-1, -1, 225, -1);
	    } else if (15 <= tickCount % 60 && tickCount % 60 < 30) {
		yOffset -= 1;
		waterColour = Colours.get(-1, 225, 115, -1);
	    } else if (30 <= tickCount % 60 && tickCount % 60 < 45) {
		waterColour = Colours.get(-1, 115, -1, 225);
	    } else {
		yOffset -= 1;
		waterColour = Colours.get(-1, 225, 115, -1);
	    }
	    
	    int tile = 0 + 27 * 32;
	    screen.render(xOffset, 
		    	  yOffset + 3, 
		    	  tile,
		    	  waterColour,
		    	  Screen.BIT_MIRROR_NONE,
		    	  Screen.NORMAL_SCALE);
	    
	    // Flip the image displayed above on the x axis
	    screen.render(xOffset + 8, 
		    	  yOffset + 3, 
		    	  tile,
		    	  waterColour,
		    	  Screen.BIT_MIRROR_X,
		    	  Screen.NORMAL_SCALE);
	    
	}

	// Render the first top left part of the player image (and others)
	// This is for future modification (e.g. swimming etc...)
	screen.render(xOffset + (modifier * flipTop), yOffset, xTile + yTile
		* 32, // to get the tile number
		COLOUR, flipTop, scale);

	screen.render(xOffset + modifier - (modifier * flipTop), yOffset,
		(xTile + 1) + yTile * 32, COLOUR, flipTop, scale);

	if (!isSwimming) {
	    
	    screen.render(xOffset + (modifier * flipBottom),
		    yOffset + modifier, xTile + (yTile + 1) * 32, COLOUR,
		    flipBottom, scale);

	    screen.render(xOffset + modifier - (modifier * flipBottom), yOffset
		    + modifier, (xTile + 1) + (yTile + 1) * 32, COLOUR,
		    flipBottom, scale);
	}
    }

    @Override
    public boolean hasCollided(int xa, int ya) {

	// 8 pixels wide, bounding box is lower half of the body box (because
	// head is off the ground)
	int xMin = 0;
	int xMax = 7;
	int yMin = 3;
	int yMax = 7;

	for (int x = xMin; x < xMax; x++) {
	    if (isSolidTile(xa, ya, x, yMin)) {
		return true;
	    }
	}

	for (int x = xMin; x < xMax; x++) {
	    if (isSolidTile(xa, ya, x, yMax)) {
		return true;
	    }
	}

	for (int y = yMin; y < yMax; y++) {
	    if (isSolidTile(xa, ya, xMin, y)) {
		return true;
	    }
	}

	for (int y = yMin; y < yMax; y++) {
	    if (isSolidTile(xa, ya, xMax, y)) {
		return true;
	    }
	}

	return false;
    }

}
