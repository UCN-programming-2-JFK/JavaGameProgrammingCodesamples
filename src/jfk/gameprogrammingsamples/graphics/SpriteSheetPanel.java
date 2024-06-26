package jfk.gameprogrammingsamples.graphics;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class SpriteSheetPanel extends JPanel implements MouseMotionListener, MouseListener {

	//Private variables
	static int tileSize = 128;										//the size of the sprite sheet images		
	Color grassGreen = new Color(39, 174, 96);						//a green color used to fill the background
	Image rpgSpriteSheet = null;									//the sprite sheet with 8 different images
	int currentSpriteIndex = -1;									//which index is the mouse currently over (-1 means "none")
	int scale = 1;													//the current scale factor, when drawing selected image to center of screen
	Font font = new Font("Courier New", Font.BOLD, 16);				//font for writing
	Font bigFont = new Font("Courier New", Font.BOLD, 20);			//bigger font for writing resulting code at the bottom of the window

	public static void main(String[] args) {
		
		SpriteSheetPanel examplePanel = new SpriteSheetPanel();		//create our panel
		examplePanel.addMouseMotionListener(examplePanel);			//let the panel listen to its own mouse movements
		examplePanel.addMouseListener(examplePanel);				//let the panel listen to its own mouse clicks
		
		JFrame frame = new JFrame("Sprite sheet sample");			//create a Frame (window)
		frame.setResizable(false);									//lock its size
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		//set the X button to close the window
		frame.setSize(1024, 800);									//set the size depending on the desired number of columns and rows
		frame.getContentPane().add(examplePanel);					//add our panel
		frame.setVisible(true);										//show the window
	}
	

	public SpriteSheetPanel() {
		rpgSpriteSheet = loadImage("/graphics/rpg_spritesheet.png");		//load the png with the sprite sheet from the resources folder
	}

	public void paint(Graphics g) {
		
		drawBackground(g);
		drawCurrentSpriteToScreen(g);
	}

	private void drawBackground(Graphics g) {

		g.setFont(font);
		g.setColor(Color.pink);
		g.fillRect(0, 0, getWidth(), getHeight());			//fill the screen with pink, to show the transparency of the PNG
		
		g.setColor(grassGreen);								//set the color to grass green
		g.fillRect(0, tileSize, getWidth(), getHeight());	//fill the bottom three quarters of the screen with grass, so the sprite sheet images appear to be drawn on grass
		
		g.setColor(Color.white);								
		g.drawString("tileSize = "  + tileSize, 16, 20);	//write tile size and scale
		g.drawString("scale = "  + scale, 16, tileSize -8);
		g.drawImage(rpgSpriteSheet, 0, 0, null);			//draw the sprite sheet

	}


	private void drawCurrentSpriteToScreen(Graphics g) {
		
		if(currentSpriteIndex < 0) {return;}		//if no sprite is selected (by hovering the mouse over it), then we don't draw but return.
		
		//we find top left and bottom right corner of the destination rectangle we want to draw to on screen (on the JPanel) based on the current scale factor
		 Point drawDestinationTopLeftCorner = new Point((getWidth()-tileSize*scale)/2, (getHeight()-tileSize*scale)/2 + 40);
		 Point drawDestinationBottomRightCorner = new Point(drawDestinationTopLeftCorner.x + tileSize*scale, drawDestinationTopLeftCorner.y + tileSize*scale);

		//we find top left and bottom right corner of the source rectangle we want to get a bush image from (on the sprite sheet)
		Point imageSourceTopLeftCorner = new Point(currentSpriteIndex * tileSize, 0);
		Point imageSourceBottomRightCorner = new Point(imageSourceTopLeftCorner.x + tileSize, imageSourceTopLeftCorner.y + tileSize);
		
		//draw rectangles around source and destination rectangles
		g.setColor(Color.white);
		g.drawRect(imageSourceTopLeftCorner.x, imageSourceTopLeftCorner.y, tileSize, tileSize);
		g.drawRect(drawDestinationTopLeftCorner.x, drawDestinationTopLeftCorner.y, tileSize * scale, tileSize * scale);
		
		//draw circles around the top-left and bottom-right corners of the source- and destination rectangle, with lines joining them
		g.fillOval(drawDestinationTopLeftCorner.x - 8, drawDestinationTopLeftCorner.y-8, 16, 16);
		g.fillOval(drawDestinationBottomRightCorner.x - 8, drawDestinationBottomRightCorner.y-8, 16, 16);
		
		g.fillOval(imageSourceTopLeftCorner.x - 4, imageSourceTopLeftCorner.y-4, 8, 8);
		g.fillOval(imageSourceBottomRightCorner.x - 4, imageSourceBottomRightCorner.y-4, 8, 8);
		
		g.drawLine(imageSourceTopLeftCorner.x, imageSourceTopLeftCorner.y, drawDestinationTopLeftCorner.x, drawDestinationTopLeftCorner.y);
		g.drawLine(imageSourceBottomRightCorner.x , imageSourceBottomRightCorner.y, drawDestinationBottomRightCorner.x, drawDestinationBottomRightCorner.y);


		//we draw from the currently wanted source rectangle in the sprite sheet to the destination rectangle on screen 
		g.drawImage(rpgSpriteSheet, 	drawDestinationTopLeftCorner.x, drawDestinationTopLeftCorner.y,
										drawDestinationBottomRightCorner.x, drawDestinationBottomRightCorner.y,		
										imageSourceTopLeftCorner.x, imageSourceTopLeftCorner.y, 
										imageSourceBottomRightCorner.x,	imageSourceBottomRightCorner.y, null);

		//write information about the coordinates
		g.setColor(Color.white);
		
		g.drawString("imageSourceTopLeftCorner is "  + pointToString(imageSourceTopLeftCorner), 16, tileSize + 20);
		g.drawString("imageSourceBottomRightCorner is " + pointToString(imageSourceBottomRightCorner), 16, tileSize + 40);
		
		
		g.drawString("drawDestinationTopLeftCorner is "  + pointToString(drawDestinationTopLeftCorner), drawDestinationTopLeftCorner.x+16, drawDestinationTopLeftCorner.y-16);

		g.drawString("drawDestinationBottomRightCorner is "+ pointToString(drawDestinationBottomRightCorner), drawDestinationBottomRightCorner.x - 256, drawDestinationBottomRightCorner.y + 24);
		
		//and the resulting Java code at the bottom of the screen
		g.setFont(bigFont);
		g.drawString("Code: 'g.drawImage(rpgSpriteSheet, " + drawDestinationTopLeftCorner.x + ", " + drawDestinationTopLeftCorner.y + ", " + 
				drawDestinationBottomRightCorner.x + ", "  + drawDestinationBottomRightCorner.y + ", " + imageSourceTopLeftCorner.x + ", "  + imageSourceTopLeftCorner.y + ", " + 
				imageSourceBottomRightCorner.x + ", "+ imageSourceBottomRightCorner.y + ", null);'", 16,getHeight()-30);
		
	}

	private String pointToString(Point pointToConvert) {
		return "(" + pointToConvert.x + "," + pointToConvert.y + ")";
	}
	

	private void calculateSpriteIndexFromMouseCoordinate(Point mousePoint) {
		if(mousePoint.y < tileSize) {
			currentSpriteIndex = mousePoint.x / tileSize;
		}
		else {
			currentSpriteIndex = -1;	//-1 signals "none"
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		calculateSpriteIndexFromMouseCoordinate(arg0.getPoint());
		repaint();
	}
	
	@Override
	public void mousePressed(MouseEvent arg0) {
		scale ++;
		if(scale > 3) {scale = 1;}
		repaint();
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
		public void mouseClicked(MouseEvent arg0) {}
		@Override
		public void mouseEntered(MouseEvent arg0) {}
		@Override
		public void mouseExited(MouseEvent arg0) {}
		@Override
		public void mouseReleased(MouseEvent arg0) {}
}