package com.froyo.gameloop.gfx;

public class Font {

    // Chars, plus six spaces plus numbers and symbols
    public static String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ      "
			 + "0123456789.,:;'\"!?$%()-=+/";
    
    public static void render(String msg, Screen screen, int x, int y, int colour, int scale) {
	
	msg = msg.toUpperCase();
	
	for (int i = 0; i < msg.length(); i++) {
	    
	    int charIndex = chars.indexOf(msg.charAt(i));
	    
	    // Check it exists in the char array
	    if (charIndex >= 0) screen.render(x + (i*8), // Each character is 8 pixels long 
		    y,  
		    charIndex + 30 * 32, // 30th row down for the characters starting in the grid
		    colour,
		    Screen.BIT_MIRROR_NONE,
		    scale);
	}
    }
    
}
