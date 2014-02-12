package com.froyo.gameloop.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {

    public String path;
    public int width;
    public int height;
    
    public int[] pixels;
    
    public SpriteSheet(String path) {
	
	BufferedImage image = null;
	try {
	    image = ImageIO.read(this.getClass().getResourceAsStream(path));
	} catch (IOException e) {
	    System.out.println("could not load iamge at path " + path);
	}
	
	// Eh?
	if (image == null) {
	    return;
	}
	
	this.path = path;
	this.width = image.getWidth();
	this.height = image.getHeight();
	
	pixels = image.getRGB(0, 0, width, height, null, 0, width);
	
	for (int i = 0; i < pixels.length; i++) {
	    
	    // Remove the alpha channel, we only want 4 colours also
	    int removeAlpha = pixels[i] & 0xff;
	    pixels[i] = removeAlpha / 64;
	}
    }
}
