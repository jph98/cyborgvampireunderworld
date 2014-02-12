package com.froyo.gameloop.tiles;

import com.froyo.gameloop.gfx.Colours;
import com.froyo.gameloop.gfx.Screen;
import com.froyo.gameloop.level.Level;

public abstract class Tile {

    private static final int DELAY_MS = 1000;

    // Max number of tiles given the board
    public static final Tile[] tiles = new Tile[256];
    
    // alpha channel is first (FF), colours specified in Photoshop
    private static final int BLACK = 0xFF000000;
    private static final int GREY = 0xFF555555;
    private static final int GREEN = 0xFF45a424;
    private static final int BLUE = 0xFF3682cf;
   
    public static final Tile VOID = new BasicSolidTile(0, 0, 0, Colours.get(000, -1, -1, -1), BLACK);
    public static final Tile STONE = new BasicSolidTile(1, 1, 0, Colours.get(-1, 333, -1, -1), GREY);
    public static final Tile GRASS = new BasicTile(2, 2, 0, Colours.get(-1, 131, 143, -1), GREEN);
    
    public static final Tile WATER = new AnimatedTile(3, 
	    					      new int[][] {{0,5},{1,5},{2,5},{1,5}}, 
    						      Colours.get(-1, 004, 115, -1), 
    						      BLUE,
    						      DELAY_MS);

    protected byte id;
    protected boolean solid;
    protected boolean emitter;
    protected int levelColour;
    
    public Tile(int id, boolean solid, boolean emitter) {
	
	this.id = (byte) id;
	if (tiles[id] != null) throw new IllegalArgumentException("Duplicate tile id found" + id);
	this.solid = solid;
	this.emitter = emitter;
	tiles[id] = this;
	this.levelColour = levelColour;
    }

    public byte getId() {
        return id;
    }

    public void setId(byte id) {
        this.id = id;
    }

    public boolean isSolid() {
        return solid;
    }

    public void setSolid(boolean solid) {
        this.solid = solid;
    }

    public boolean isEmitter() {
        return emitter;
    }

    public void setEmitter(boolean emitter) {
        this.emitter = emitter;
    }

    public abstract void render(Screen screen, Level level, int x, int y);

    public abstract void tick();

    public void setColour(int colour) {
	this.levelColour = colour;
    }

    public int getLevelColour() {
	return levelColour;
    }
}
