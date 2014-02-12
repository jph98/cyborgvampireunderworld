package com.froyo.gameloop.gfx;

public class Colours {

    // Combine four colours into one long int
    // black, dgrey, lgrey, 
    public static int get(int colour1, int colour2, int colour3, int colour4) {
	
	// 2 to the 8 bits for each colour
	return (get(colour4) << 24)
	       + (get(colour3) << 16)
	       + (get(colour2) << 8)
	       + get(colour1);
    }
    
    public static int get(int colour) {
	
	if (colour < 0 ) return 255;
	
	// We want the first part of the colur
	int r = colour / 100 % 10;
	int g = colour / 10 % 10;
	int b = colour % 10;
	return r * 36 + g * 6 + b;
    }
    
    static {
	Colours.get(555, 543, 542, 123);
    }
}