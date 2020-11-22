package jfk.gameprogrammingsamples.graphics;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

public class NineSlicePanel extends JPanel implements MouseMotionListener {

	//Private variables
	Image nineSliceSpriteSheet, threeSliceSpriteSheet;				//the sprite sheets with the nine- and three-slices
	Font font = new Font("Courier New", Font.BOLD, 24);				//font for writing
	long lastUpdate;												//the last time an update was performed. Used to calculate time since last update, to smooth animations
	ArrayList<AnimatedSprite> sprites = new ArrayList<AnimatedSprite>();	//the list to hold all currently running explosions
	Point currentMousePosition = new Point(0,0);

	public static void main(String[] args) {
		
		NineSlicePanel examplePanel = new NineSlicePanel();//create our panel
		examplePanel.addMouseMotionListener(examplePanel);			//let the panel listen to its own mouse movements
		
		JFrame frame = new JFrame("9-slice and 3-slice sample");			//create a Frame (window)
		frame.setResizable(false);									//lock its size
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		//set the X button to close the window
		frame.setSize(800, 600);									//set the size depending on the desired number of columns and rows
		frame.getContentPane().add(examplePanel);					//add our panel
		frame.setVisible(true);										//show the window
	}
	
	public NineSlicePanel() {
		threeSliceSpriteSheet = loadImage("/threeslice.png");			//load the png with the sprite sheet from the resources folder
		nineSliceSpriteSheet = loadImage("/nineslice.png");			
	}

	public void paint(Graphics g) {
		drawBackground(g);
	}

	private void drawBackground(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0,0,getWidth(), getHeight());
		drawNineSlice(g);
		drawThreeSlice(g);
	}

	private void drawThreeSlice(Graphics g) {
		
		
		int topLeftDestinationX = 100;
		int topLeftDestinationY = 50;
		
		int minimumWidth = threeSliceSpriteSheet.getWidth(null);
		int height = 36;
		
		int bottom = topLeftDestinationY + height;
		int right = topLeftDestinationX + minimumWidth;
		
		right = Math.max(right, currentMousePosition.x);
		
		int third = minimumWidth / 3;

		// left third
		g.drawImage(threeSliceSpriteSheet, topLeftDestinationX, topLeftDestinationY, topLeftDestinationX + third, bottom, 0, 0, third, height, null);

		// middle third
		g.drawImage(threeSliceSpriteSheet, topLeftDestinationX + third, topLeftDestinationY, right - third, bottom,	third, 0, third * 2, height, null);

		// right third
		g.drawImage(threeSliceSpriteSheet, right - third, topLeftDestinationY, right, bottom, third * 2, 0, third * 3, height, null);

	}

	private void drawNineSlice(Graphics g) {
		
		int topLeftDestinationX = 100;
		int topLeftDestinationY = 100;
		
		int minimumWidth = 92;
		int minimumHeight = 92;
		
		int bottom = topLeftDestinationY + minimumHeight;
		int right = topLeftDestinationX + minimumWidth;
		
		bottom = Math.max(bottom, currentMousePosition.y);
		right = Math.max(right, currentMousePosition.x);
		
		int cornerSize = 32;
		
		//left column from top to bottom
		g.drawImage(nineSliceSpriteSheet, topLeftDestinationX, topLeftDestinationY, topLeftDestinationX + cornerSize, topLeftDestinationY + cornerSize, 0,0,cornerSize,cornerSize, null);
		g.drawImage(nineSliceSpriteSheet, topLeftDestinationX, topLeftDestinationY + cornerSize, topLeftDestinationX + cornerSize, bottom - cornerSize, 0, cornerSize, cornerSize, cornerSize*2, null);
		g.drawImage(nineSliceSpriteSheet, topLeftDestinationX, bottom - cornerSize, topLeftDestinationX + cornerSize, bottom , 0, cornerSize*2, cornerSize, cornerSize*3, null);

		//middle column from top to bottom
		g.drawImage(nineSliceSpriteSheet, topLeftDestinationX + cornerSize, topLeftDestinationY, right - cornerSize, topLeftDestinationY + cornerSize, cornerSize,0,cornerSize*2,cornerSize, null);
		g.drawImage(nineSliceSpriteSheet, topLeftDestinationX + cornerSize, topLeftDestinationY + cornerSize, right - cornerSize, bottom - cornerSize, cornerSize,cornerSize,cornerSize*2,cornerSize*2, null);
		g.drawImage(nineSliceSpriteSheet, topLeftDestinationX + cornerSize, bottom - cornerSize, right - cornerSize, bottom, cornerSize,cornerSize*2,cornerSize*2,cornerSize*3, null);
		
		//right column from top to bottom
		g.drawImage(nineSliceSpriteSheet, right - cornerSize, topLeftDestinationY, right, topLeftDestinationY + cornerSize, cornerSize*2,0,cornerSize*3, cornerSize, null);
		g.drawImage(nineSliceSpriteSheet, right - cornerSize, topLeftDestinationY + cornerSize, right, bottom - cornerSize, cornerSize*2,cornerSize,cornerSize*3, cornerSize*2, null);		
		g.drawImage(nineSliceSpriteSheet, right - cornerSize, bottom - cornerSize, right, bottom, cornerSize*2, cornerSize*2, cornerSize*3, cornerSize*3, null);
		
	}
		
	 private Image loadImage(String imagePathOrUrl)
	    {
		 Image image = null;
		 try {
			 image = ImageIO.read(this.getClass().getResource(imagePathOrUrl));
			} catch (IOException e) {System.out.println(e.getMessage());}
		 return image;
	    }

		// mouse events with empty implementation
		@Override
		public void mouseDragged(MouseEvent arg0) {}
		@Override
		public void mouseMoved(MouseEvent arg0) {currentMousePosition = arg0.getPoint(); repaint();}
}