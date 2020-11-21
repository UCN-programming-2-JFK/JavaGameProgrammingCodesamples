package jfk.gameprogrammingsamples.graphics;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class SimpleAnimatedPanel extends JPanel {

	//Private variables
	static Image explosionSpriteSheet = null;			//the sprite sheet with the different stages of explosion
	int currentFrame = 0;
	static int tileSize = 128;							//size of the tiles in the sprite sheet in pixels
	int rows = 7;										//the number of rows in the sprite sheet
	int columns = 6;									//the number of columns in the sprite sheet
	Font font = new Font("Arial", Font.PLAIN, 24);		//the font used to write which frame we're in
	
	
	public static void main(String[] args) {
		
		SimpleAnimatedPanel examplePanel = new SimpleAnimatedPanel();		//create our panel
		
		JFrame frame = new JFrame("Simple animated sample");				//create a Frame (window)
		frame.setResizable(false);											//lock its size
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);				//set the X button to close the window
		//set the size based on the sprite sheet, with a bit added to compensate for the windows' titlebar and borders
		frame.setSize(explosionSpriteSheet.getWidth(null)+ 20, explosionSpriteSheet.getHeight(null) + tileSize + 40);									
		frame.getContentPane().add(examplePanel);							//add our panel
		frame.setVisible(true);												//show the window
		examplePanel.runGameLoop();											//start the game loop, which runs the animation and refreshes the screen
	}
	

	public SimpleAnimatedPanel() {
		explosionSpriteSheet = loadImage("/explosionspritesheet.png");		//load the png with the sprite sheet from the resources folder
	}

	public void paint(Graphics g) {
		
		drawBackground(g);
		drawSpriteSheetAndCurrentTile(g);
		drawCorrectFrameToScreen(g);
	}

	private void drawSpriteSheetAndCurrentTile(Graphics g) {
		
		//draw the entire sprite sheet
		g.drawImage(explosionSpriteSheet, 0,tileSize, null);

		//and a box around the current tile
		g.setColor(Color.cyan);
		int frameRow = currentFrame / columns;
		int frameColumn = currentFrame % columns;
		g.drawRect(frameColumn * tileSize, tileSize + frameRow * tileSize, tileSize, tileSize);
		
		//and the number of the current frame
		g.setColor(Color.white);
		g.setFont(font);
		g.drawString("Current frame: " + currentFrame, 4, 24);	
	}

	private void drawBackground(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0,0,getWidth(), getHeight());
	}

	//runs the game loop forever
	public void runGameLoop() {
		
		while (true) { // run as long as the window exists
			currentFrame ++;
			if(currentFrame >= rows * columns) {
				currentFrame = 0;
			}
			repaint(); // ask for the UI to be redrawn
			waitAShortInterval();
		}
	}
	
	private void waitAShortInterval() {
		try {
			Thread.sleep(30);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	

	private void drawCorrectFrameToScreen(Graphics g) {
		
		int frameRow = currentFrame / columns;
		int frameColumn = currentFrame % columns;
		int halfSize = tileSize/2;	
		
		//we find top left and bottom right corner of the destination rectangle we want to draw to on screen (on the JPanel)
		 Point drawDestinationTopLeftCorner = new Point(getWidth()/2 - halfSize, 0);
		 Point drawDestinationBottomRightCorner = new Point(drawDestinationTopLeftCorner.x + tileSize, drawDestinationTopLeftCorner.y + tileSize);

		//we find top left and bottom right corner of the source rectangle we want to get a bush image from (on the sprite sheet)
		Point imageSourceTopLeftCorner = new Point(frameColumn * tileSize, frameRow * tileSize);
		Point imageSourceBottomRightCorner = new Point(imageSourceTopLeftCorner.x + tileSize, imageSourceTopLeftCorner.y + tileSize);

		//we draw from the source rectangle in the sprite sheet to the destination rectangle on screen 
		g.drawImage(explosionSpriteSheet, 	drawDestinationTopLeftCorner.x, drawDestinationTopLeftCorner.y,
											drawDestinationBottomRightCorner.x, drawDestinationBottomRightCorner.y,		
											imageSourceTopLeftCorner.x, imageSourceTopLeftCorner.y, 
											imageSourceBottomRightCorner.x,	imageSourceBottomRightCorner.y, null);
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
}