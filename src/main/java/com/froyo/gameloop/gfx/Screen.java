package com.froyo.gameloop.gfx;

public class Screen {

    public static final int MAP_WIDTH = 64;
    public static final int MAP_WIDTH_MASK = MAP_WIDTH - 1;
    
    public int[] pixels;
    
    public int xOffset = 0;
    public int yOffset = 0;
    
    public int width;
    public int height;
    
    public SpriteSheet sheet;
    
    // 00 - normal
    // 01 - flipped x
    // 02 - flipped y
    public static final int BIT_MIRROR_NONE = 0x00;
    public static final byte BIT_MIRROR_X = 0x01;
    public static final byte BIT_MIRROR_Y = 0x02;
    
    public static final int NORMAL_SCALE = 1;
    
    public Screen(int width, int height, SpriteSheet sheet) {
	
	this.width = width;
	this.height = height;
	this.sheet = sheet;
	
	initialiseColours();
    }

    private void initialiseColours() {
	
	pixels = new int[width * height];
	
    }
    
    // Number will be between 0 and 32 (x)
    // Number will be between 0 and 32 (y)
    // *   * *
    // 0,1 * *
   
    public void render(int xPos, int yPos, int tile, int colour, int mirrorDir, int scale) {
	
	xPos -= xOffset;
	yPos -= yOffset;
	
	// IF this is greater than zero then mirror
	boolean mirrorX = (mirrorDir & BIT_MIRROR_X) > 0;
	boolean mirrorY = (mirrorDir & BIT_MIRROR_Y) > 0;

	int scaleMap = scale - 1;
	
	// width
	int xTile = tile % 32;
	
	// height
	int yTile = tile / 32;
	
	int tileOffset = (xTile << 3) // 2 to the 3 is 8, (each tile is 8 pixels) 
			 + (yTile << 3)
			 * sheet.width;
	
	// write data inside each tile
	for (int y = 0; y < 8; y++) {
	    
	    if (y + yPos < 0 || y + yPos >= height) continue;
	    int ySheet = y;
	    
	    if (mirrorY) ySheet = 7 - y;

	    // << 3 means just multiply by 8
	    int yPixel = y + yPos + (y * scaleMap) - ((scaleMap << 3));
	    
	    for (int x = 0; x < 8; x++) {
		
		int xSheet = x;
		if (mirrorX) xSheet = 7 - x;
		
		int xPixel = x + xPos + (x * scaleMap) - ((scaleMap << 3) / 2);
		
		// Get the colour
		int actualColour = (colour >> ( sheet.pixels[xSheet + ySheet * sheet.width + tileOffset] * 8)) & 255;
		
		// Renders one pixels
		// Scale: 1 = 1 pixel
		// Scale: 2 = every pixel render 2 pixels
		if (actualColour < 255) {
		    
		    for (int yScale = 0; yScale < scale; yScale ++) {
			
			if (yPixel + yScale < 0 || yPixel + yScale >= height) continue;
			
			for (int xScale = 0; xScale < scale; xScale++) {
			    if (xPixel + xScale < 0 || xPixel + xScale >= width)  continue;
			    
			    pixels[(xPixel + xScale) + (yPixel + yScale) * width] = actualColour;
			}
		    }
		    
		}
		
	    }
	}
    }
    

    public void setOffset(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }
}
