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
	Point currentMousePosition = new Point(); 						// stores the mouse position from last movement
	Point tractorPosition = new Point(200,200);
	int currentTractorImage = 0;
	int tractorWidth = 128, tractorHeight = 128; 						// half width and -height, used to draw the balloon centered on the position
	int tractorImageIndex = 0;
	int speed = 2;
	Font font = new Font("Arial", 0, 25);							//a bigger font, to make it easier to read


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

	
	//Constructor to load the balloon image and calculate half width/height
	public MouseChaserPanel() {
		tractorImage = loadImage("/movement/tractor_spritesheet.png"); 				// load the grass texture
		// Transparent 16 x 16 pixel cursor image.
		Image cursorImg = loadImage("/movement/x.png");

		// Create a new blank cursor.
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "green 'X' cursor");

		// Set the blank cursor to the JFrame.
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
		
		
		int distanceToMouseX = currentMousePosition.x - tractorPosition.x ;
		int distanceToMouseY = currentMousePosition.y - tractorPosition.y ;
		
		//if(Math.abs(distanceToMouseX) > Math.abs(distanceToMouseY)*2) distanceToMouseY = 0;
		
		int directionXToMouse = (int)Math.signum( distanceToMouseX );
		int directionYToMouse = (int)Math.signum( distanceToMouseY );
		if(directionXToMouse == 0 && directionYToMouse == -1) { tractorImageIndex = 0;}
		if(directionXToMouse == 1 && directionYToMouse == -1) { tractorImageIndex = 1;}
		if(directionXToMouse == 1 && directionYToMouse == 0) { tractorImageIndex = 2;}
		if(directionXToMouse == 1 && directionYToMouse == 1) { tractorImageIndex = 3;}
		if(directionXToMouse == 0 && directionYToMouse == 1) { tractorImageIndex = 4;}
		if(directionXToMouse == -1 && directionYToMouse == 1) { tractorImageIndex = 5;}
		if(directionXToMouse == -1 && directionYToMouse == 0) { tractorImageIndex = 6;}
		if(directionXToMouse == -1 && directionYToMouse == -1) { tractorImageIndex = 7;}
		Point newTractorPosition = new Point(tractorPosition.x + directionXToMouse*speed, tractorPosition.y + directionYToMouse*speed);
		if(Math.abs(newTractorPosition.x - currentMousePosition.x) <= speed) {newTractorPosition.x = currentMousePosition.x;}
		if(Math.abs(newTractorPosition.y - currentMousePosition.y) <= speed) {newTractorPosition.y = currentMousePosition.y;}
		tractorPosition = newTractorPosition;
		
	}
	

	private void waitAShortInterval() {
		try {
			Thread.sleep(30);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void paint(Graphics g) {
		drawBackground(g);
		drawTractorAt(tractorPosition, g);
		g.setColor(Color.white);
		g.setFont(font);
		//g.drawString("Distance to mouse: ");// + tractorPositions.size(), 10, 24);
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