package com.froyo.gameloop;

public class Main {

    private Game game;
    
    public Main() {
	this.game = new Game();
	game.start();
    }
    
    public static void main(String[] args) {
	
	Main main = new Main();
    }
}
