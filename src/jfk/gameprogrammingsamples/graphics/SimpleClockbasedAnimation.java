package jfk.gameprogrammingsamples.graphics;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

public class SimpleClockbasedAnimation extends JPanel {

	//Private variables
	Image rotatingCoinSpriteSheet = null;								//the sprite sheet with the different stages of explosion
	final int numberOfFramesInAnimation = 6;
	int tileSize = 128;
	
	public static void main(String[] args) {
		
		SimpleClockbasedAnimation examplePanel = new SimpleClockbasedAnimation();	//create our panel
		
		JFrame frame = new JFrame("AnimatedSprite sample");			//create a Frame (window)
		frame.setResizable(false);									//lock its size
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		//set the X button to close the window
		frame.setSize(800, 300);									//set the size depending on the desired number of columns and rows
		frame.getContentPane().add(examplePanel);					//add our panel
		frame.setVisible(true);										//show the window
	}
	
	public SimpleClockbasedAnimation() {
		rotatingCoinSpriteSheet = loadImage("/graphics/starcoinrotate_768.png");		//load the png with the sprite sheet from the resources folder
	}

	public void paint(Graphics g) {
		drawBackground(g);
		drawCoins(g);
		repaint();
	}

	private void drawCoins(Graphics g) {
		for	(int coinCounter = 0; coinCounter < 8; coinCounter++) {
			
			//get only the millisecond part of the time, by using the modulus (%) operator which returns the remainder of an integer division
			long millisecond = System.currentTimeMillis() % 1000;
			
			//divide the second into sixths, by dividing by a bit more than a sixth of 1000 (which would be 166,666)
			//this gives us a value from 0 to 5 over and over again, which is perfect for looking up the current animation frame.
			int currentFrame = (int)((float)millisecond / (float)167);
			

			//find where to start the drawing, so there's room for 8 coins
			int yOffset = getHeight()/4;
			int xSpacing = getWidth()/7;

			//draw the part of the image we want to the panel
			g.drawImage(rotatingCoinSpriteSheet, xSpacing * coinCounter, yOffset, xSpacing * coinCounter + tileSize,  yOffset + tileSize, tileSize * currentFrame, 0, tileSize * currentFrame + tileSize, tileSize, null);
		}
	}

	private void drawBackground(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0,0,getWidth(), getHeight());
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