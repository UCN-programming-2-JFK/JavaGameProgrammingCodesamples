package jfk.gameprogrammingsamples.graphics;

import java.awt.*;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;


public class ParallaxScrollingPanel extends JPanel {

	//Private variables
	Image galaxyBackground, transparentStarfield;									//the galaxy image and the starfield
	float[] starfieldOffsets = new float[3];
	Random rnd = new Random();
	
	public static void main(String[] args) {
		
		ParallaxScrollingPanel examplePanel = new ParallaxScrollingPanel();		//create our panel
	
		JFrame frame = new JFrame("Parallax scrolling");			//create a Frame (window)
		frame.setResizable(false);									//lock its size
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		//set the X button to close the window
		frame.setSize(1024, 800);									//set the size depending on the desired number of columns and rows
		frame.getContentPane().add(examplePanel);					//add our panel
		frame.setVisible(true);										//show the window
		examplePanel.run();
	}
	
	public ParallaxScrollingPanel() {
		galaxyBackground = loadImage("/galaxy.png");		//load the png with the sprite sheet from the resources folder
		transparentStarfield = loadImage("/starfield.png");		//load the png with the sprite sheet from the resources folder
	}

	private void run() {
		initializeStarfieldsPositionRandomly();
		while(true) {
			moveStarfields();
			repaint();
			waitAShortInterval();
		}
	}
	
	private void initializeStarfieldsPositionRandomly() {
		
		//initialize the position of all starfields to a random percentage of their entire width shifted to the left
		//Random.nextFloat() returns a value between 0.0 and 1.0 (0 to 100 % in other words) 
		//By multiplying the width of the image and offsetting to the left by this amount, we get a random offset from zero to the entire length of the image 
		for(int starfieldIndex = 0; starfieldIndex < starfieldOffsets.length; starfieldIndex++)
		{
			starfieldOffsets[starfieldIndex] = rnd.nextFloat() * transparentStarfield.getWidth(null) * -1;	// we multiply by negative one to make the value negative (move left on the x-axis)
		}	
	}

	private void moveStarfields() {
		
		//move all offsets by a bit, at different speeds
		starfieldOffsets[0] -= .25;		//move a quarter pixel left
		starfieldOffsets[1] -= .75;		//move three quarters of a pixel left
		starfieldOffsets[2] -= 1.5;		//move one and a half pixels left
		
		//check up on all layers. 
		//if the entire image is off the screen to the left, move it one image-width to the right, 
		//to bring it seamlessly back for another go  
		for(int starfieldIndex = 0; starfieldIndex < starfieldOffsets.length; starfieldIndex++)
		{
			if(starfieldOffsets[starfieldIndex] <= -transparentStarfield.getWidth(null)) {
				starfieldOffsets[starfieldIndex] += transparentStarfield.getWidth(null);
			}
		}
	}

	private void waitAShortInterval() {
		try {
			Thread.sleep(30);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void paint(Graphics g) {
		drawBackgroundAndStarfields(g);
	}

	private void drawBackgroundAndStarfields(Graphics g) {

		g.setColor(Color.black);
		g.fillRect(0,0,getWidth(), getHeight());
		g.drawImage(galaxyBackground, 0, 0, getWidth(), getHeight(), null);			//draw the galaxy as background	

		//we draw all three starfields twice beside each other, to ensure that even though the offset for drawing slowly moves left off the screen, the entire screen is covered in stars
		for(int starfieldIndex = 0; starfieldIndex < starfieldOffsets.length; starfieldIndex++)
		{
			g.drawImage(transparentStarfield, (int)starfieldOffsets[starfieldIndex], 0, (int)(transparentStarfield.getWidth(null)), (int)(transparentStarfield.getHeight(null)), null);
			g.drawImage(transparentStarfield, (int)starfieldOffsets[starfieldIndex] + transparentStarfield.getWidth(null), 0, (int)(transparentStarfield.getWidth(null)), (int)(transparentStarfield.getHeight(null)), null);
		}
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