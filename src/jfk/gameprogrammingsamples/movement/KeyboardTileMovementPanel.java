package jfk.gameprogrammingsamples.movement;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class KeyboardTileMovementPanel extends JPanel implements KeyListener {

	static int tileSize = 64;
	static int columns = 12, rows = 8;
	static int windowWidth = columns * tileSize, windowHeight = rows * tileSize;
	Sprite warrior = null;
	Point currentTile = new Point(0,0);
	Image grassTile = null;
	
	public static void main(String[] args) {

		KeyboardTileMovementPanel examplePanel = new KeyboardTileMovementPanel(); 	// create our panel
		examplePanel.addKeyListener(examplePanel); 									// let the panel listen to its own key presses

		JFrame frame = new JFrame("Tilebased keyboard movement"); 	// create a Frame (window)
		frame.setResizable(false); 									// lock its size
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 		// set the X button click to close the window
		frame.setSize(windowWidth, windowHeight); 					// set the size
		frame.getContentPane().add(examplePanel); 					// add our  panel
		frame.getContentPane().setPreferredSize(new Dimension(windowWidth, windowHeight));
		frame.pack();
		frame.setVisible(true); 									// show the window
		examplePanel.grabFocus();									// !Important! - for the KeyListener to get key presses	
	}
	
	public KeyboardTileMovementPanel() {
		grassTile = loadImage("/grasstile.png"); 					// load the grass texture
		Image warriorTexture = loadImage("/warrior.png"); 			// load the warrior texture
		warrior = new Sprite( new Point(96, 96), warriorTexture);	//create the warrior sprite object
		moveWarriorToTile(currentTile.x, currentTile.y);			//move the warrior to the beginning tile
	}
	
	private void moveWarriorToTile(int x, int y) {
		Point positionToMoveTo = new Point(x * tileSize + tileSize/2, y * tileSize + tileSize/2);
		warrior.setPosition(positionToMoveTo);
	}

	@Override
	public void paint(Graphics g) {
		drawBackground(g);
		warrior.draw(g);
	}	

	private void drawBackground(Graphics g) {
		// for each column and row - draw the grass tile
		for (int columnCounter = 0; columnCounter < columns; columnCounter++) {
			for (int rowCounter = 0; rowCounter < rows; rowCounter++) {
				g.drawImage(grassTile, columnCounter * tileSize, rowCounter * tileSize, null);
			}
		}
	}

	//find out which direction to move the warrior based on the arrow pressed
	@Override
	public void keyPressed(KeyEvent arg0) {
		int directionX = 0, directionY = 0;
		switch(arg0.getKeyCode()) {
			case KeyEvent.VK_UP : 		directionY = -1; 	break;
			case KeyEvent.VK_DOWN: 		directionY = 1; 	break;
			case KeyEvent.VK_LEFT : 	directionX = -1; 	break;
			case KeyEvent.VK_RIGHT : 	directionX = 1; 	break;
			default : break;
		}
		
		Point newSquare = new Point(currentTile.x + directionX, currentTile.y + directionY);

		//only move if the desired new destination is on the map
		if(isWithinMap(newSquare)) {
			currentTile = newSquare;
			//calculate where the warrior is on the screen using the column and row multiplied by the tileSize
			//Remember to add half a tile to center the warrior on the tile
			Point pixelPosition = new Point(newSquare.x * tileSize + tileSize/2,newSquare.y * tileSize + tileSize/2);
			warrior.setPosition(pixelPosition);
			repaint();
		}
	}
	
	private boolean isWithinMap(int column, int row) {
		return (column >= 0 && column < columns && row >= 0 && row < rows);
	}

	private boolean isWithinMap(Point tile)	{
		return isWithinMap(tile.x, tile.y);
	}
	
	private Image loadImage(String imagePathOrUrl) {
		Image image = null;
		try {
			image = ImageIO.read(this.getClass().getResource(imagePathOrUrl));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return image;
	}	
	
	//empty interface implementations
	@Override
	public void keyReleased(KeyEvent arg0) {}
	@Override
	public void keyTyped(KeyEvent arg0) {}	
}