package com.froyo.gameloop;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class InputHandler implements KeyListener {

    public List<Key> keys = new ArrayList<Key>();
    
    public Key up = new Key();
    public Key down = new Key();
    public Key left = new Key();
    public Key right = new Key();
    
    public InputHandler(Game game) {
	
	// Add the input handler to the game
	game.addKeyListener(this);
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
	// do nothing
    }

    @Override
    public void keyPressed(KeyEvent e) {
	
	toggleKey(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
	
	toggleKey(e.getKeyCode(), false);
    }

    public void toggleKey(int keyCode, boolean isPressed){
	
	if (keyCode == KeyEvent.VK_Q || keyCode == KeyEvent.VK_UP) up.toggle(isPressed);
	if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_DOWN) down.toggle(isPressed);
	if (keyCode == KeyEvent.VK_O || keyCode == KeyEvent.VK_LEFT) left.toggle(isPressed);
	if (keyCode == KeyEvent.VK_P || keyCode == KeyEvent.VK_RIGHT) right.toggle(isPressed);
    }
    
    /**
     * Handles key pressed events
     */
    public class Key {
    
	public boolean isPressed = false;
	
	// Used for double jumping?
	private int numTimesPressed = 0;
	
	
	public boolean isPressed() {
	    return isPressed;
	}
	
	public void toggle(boolean isPressed) {
	    this.isPressed = isPressed;
	    if (isPressed) numTimesPressed++;
	}
    }
}
