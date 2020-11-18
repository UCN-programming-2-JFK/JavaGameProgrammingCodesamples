package jfk.gameprogrammingsamples.movement;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

public class MouseBalloonPanel extends JPanel implements MouseMotionListener, MouseListener {

	
	//private variables 
	Image balloonImage = null; 										// the balloon image loaded from PNG
	ArrayList<Point> balloonPositions = new ArrayList<Point>(); 	// a list of all the balloons' positions
	Point currentMousePosition = new Point(); 						// stores the mouse position from last movement
	int halfBalloonWidth, halfBalloonHeight; 						// half width and -height, used to draw the balloon centered on the position
	Font font = new Font("Arial", 0, 25);							//a bigger font, to make it easier to read


	//main method, to instantiate and add the JPanel to a window (JFrame) 
	public static void main(String[] args) {
		MouseBalloonPanel examplePanel = new MouseBalloonPanel(); 	// create our panel
		examplePanel.addMouseListener(examplePanel); 				// let the panel listen to mouse clicks
		examplePanel.addMouseMotionListener(examplePanel); 			// let the panel listen to mouse movement

		JFrame frame = new JFrame("Simple game loop animation"); 	// create a Frame (window)
		frame.setResizable(false); 									// lock its size
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 		// set the X button click to close the window
		frame.setSize(800, 600); 									// set the size
		frame.getContentPane().add(examplePanel); 					// add our panel
		frame.setVisible(true); 									// show the window
		examplePanel.runGameLoop(); 								// start the game loop
	}

	
	//Constructor to load the balloon image and calculate half width/height
	public MouseBalloonPanel() {
		balloonImage = loadImage("/balloon.png"); 				// load the grass texture
		//calculate half of a balloon, to be able to center it on the desired coordinate
		halfBalloonWidth = balloonImage.getWidth(null) / 2;		
		halfBalloonHeight = balloonImage.getHeight(null) / 2;
	}

	
	//runs the game loop forever
	public void runGameLoop() {

		while (true) { // run as long as the window exists
			
			moveBalloonsUpward();
			removeBalloonsThatTouchTheTopOfTheWindow();
			waitAShortInterval();
			repaint(); // ask for the UI to be redrawn
		}
	}
	
	private void moveBalloonsUpward() {
		for (Point balloonPosition : balloonPositions) {
			balloonPosition.translate(0, -1);
		}
	}
	
	private void removeBalloonsThatTouchTheTopOfTheWindow() {
		for (int balloonCounter = balloonPositions.size() - 1; balloonCounter >= 0; balloonCounter--) {
			if (balloonPositions.get(balloonCounter).y < halfBalloonHeight) {
				balloonPositions.remove(balloonCounter);
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

	@Override
	public void paint(Graphics g) {
		drawBackground(g);
		drawAllBalloons(g);
		
		g.setColor(Color.white);
		g.setFont(font);
		g.drawString("Number of balloons alive: " + balloonPositions.size(), 10, 24);
	}

	private void drawAllBalloons(Graphics g) {
		drawBalloonAt(currentMousePosition, g);
		for (Point balloonPosition : balloonPositions) {
			drawBalloonAt(balloonPosition, g);
		}
	}

	private void drawBalloonAt(Point balloonPosition, Graphics g) {
		g.drawImage(balloonImage, balloonPosition.x - halfBalloonWidth, balloonPosition.y - halfBalloonHeight, null);
	}

	private void drawBackground(Graphics g) {

		g.setColor(Color.blue);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	
	@Override
	public void mousePressed(MouseEvent arg0) {
		addBalloonAt(arg0.getPoint());
	}

	private void addBalloonAt(Point point) {
		balloonPositions.add(point);
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		currentMousePosition = arg0.getPoint();
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		currentMousePosition = arg0.getPoint();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

	//helper method to load an image from resources
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
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}
}