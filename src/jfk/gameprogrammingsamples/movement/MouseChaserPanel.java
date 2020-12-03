package jfk.gameprogrammingsamples.movement;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

public class MouseChaserPanel extends JPanel implements MouseMotionListener {

	
	//private variables 
	Image tractorImage = null; 										
	Point currentMousePosition = new Point(); 		// stores the mouse position from last detected mouse move
	Point tractorPosition = new Point(200,200);		// sets the tractor's starting position
	int tractorWidth = 128, tractorHeight = 128; 	// height and width of the image of the tractor 
	int tractorImageIndex = 0;						// which image in the sprite sheet to use based on current direction
	int speed = 2;									//how many pixels to move on each axis per update
	Font font = new Font("Arial", 0, 25);		 	//a bigger font, to make it easier to read


	//main method, to instantiate and add the JPanel to a window (JFrame) 
	public static void main(String[] args) {
		MouseChaserPanel examplePanel = new MouseChaserPanel(); 	// create our panel
		examplePanel.addMouseMotionListener(examplePanel); 			// let the panel listen to mouse movement
		
		JFrame frame = new JFrame("Movement towards a target"); 	// create a Frame (window)
		frame.setResizable(false); 									// lock its size
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 		// set the X button click to close the window
		frame.setSize(800, 600); 									// set the size
		frame.getContentPane().add(examplePanel); 					// add our panel
		frame.setVisible(true); 									// show the window
		examplePanel.runGameLoop(); 								// start the game loop
		
	}

	public MouseChaserPanel() {
		tractorImage = loadImage("/movement/tractor_spritesheet.png"); 		// load tractor spritesheet with tractor pointing in all 8 directions
		
		//load the "x" to use for mouse cursor
		Image cursorImg = loadImage("/movement/x.png");		

		// Create a new cursor from the 'x' image
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "green 'X' cursor");

		// Set the new cursor on the JFrame.
		setCursor(blankCursor);
	}

	
	//runs the game loop forever
	public void runGameLoop() {

		while (true) { // run as long as the window exists
			moveTractorTowardMouse();
			repaint(); // ask for the UI to be redrawn
			waitAShortInterval();
		}
	}
	
private void moveTractorTowardMouse() {
	
	//find out what direction to move on the two axes
	int distanceToMouseX = currentMousePosition.x - tractorPosition.x ;
	int distanceToMouseY = currentMousePosition.y - tractorPosition.y ;
	
	//find out whether that is positive, negative or zero (mathematical 'sign' operation)
	int directionXToMouse = (int)Math.signum( distanceToMouseX );
	int directionYToMouse = (int)Math.signum( distanceToMouseY );
	
	//find out which image to use, by looking at the direction on both axes
	if(directionXToMouse == 0 && directionYToMouse == -1) { tractorImageIndex = 0;}
	else if(directionXToMouse == 1 && directionYToMouse == -1) { tractorImageIndex = 1;}
	else if(directionXToMouse == 1 && directionYToMouse == 0) { tractorImageIndex = 2;}
	else if(directionXToMouse == 1 && directionYToMouse == 1) { tractorImageIndex = 3;}
	else if(directionXToMouse == 0 && directionYToMouse == 1) { tractorImageIndex = 4;}
	else if(directionXToMouse == -1 && directionYToMouse == 1) { tractorImageIndex = 5;}
	else if(directionXToMouse == -1 && directionYToMouse == 0) { tractorImageIndex = 6;}
	else if(directionXToMouse == -1 && directionYToMouse == -1) { tractorImageIndex = 7;}
	
	//add the direction to the current position, to find the tractor's new position
	Point newTractorPosition = new Point(tractorPosition.x + directionXToMouse*speed, tractorPosition.y + directionYToMouse*speed);
	
	//ensure the tractor doesn't "overshoot" 
	//(e.g. if it's 2 pixels away, and the speed is 3, then it would overshoot the target by 1 pixel).
	if(Math.abs(newTractorPosition.x - currentMousePosition.x) <= speed) {newTractorPosition.x = currentMousePosition.x;}
	if(Math.abs(newTractorPosition.y - currentMousePosition.y) <= speed) {newTractorPosition.y = currentMousePosition.y;}
	
	//update the tractor's position
	tractorPosition = newTractorPosition;
}
	

	private void waitAShortInterval() {
		try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
	}

	@Override
	public void paint(Graphics g) {
		drawBackground(g);
		drawTractorAt(tractorPosition, g);
		g.setColor(Color.white);
		g.setFont(font);
		g.drawString("Mouse at: (" + currentMousePosition.x + "," + currentMousePosition.y + ")", 10,30);
		g.drawString("Tractor at: (" + tractorPosition.x + "," + tractorPosition.y + ")", 10,60);
		int distance = (int)currentMousePosition.distance(tractorPosition);
		g.drawString("Distance is: " + distance, 10,90);
	}


	private void drawTractorAt(Point tractorPosition, Graphics g) {
		Point topLeftDestination = new Point( tractorPosition.x - tractorWidth/2, tractorPosition.y - tractorHeight/2);
		Point bottomRightDestination = new Point( tractorPosition.x + tractorWidth, tractorPosition.y + tractorHeight);
		
		Point topLeftSource = new Point(tractorImageIndex * tractorWidth, 0);
		Point bottomRightSource = new Point(tractorImageIndex * tractorWidth + tractorWidth, tractorHeight);
		
		g.drawImage(tractorImage, topLeftDestination.x, topLeftDestination.y, bottomRightDestination.x, bottomRightDestination.y, topLeftSource.x, topLeftSource.y, bottomRightSource.x, bottomRightSource.y, null);
	}

	private void drawBackground(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		currentMousePosition = arg0.getPoint();
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		currentMousePosition = arg0.getPoint();
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