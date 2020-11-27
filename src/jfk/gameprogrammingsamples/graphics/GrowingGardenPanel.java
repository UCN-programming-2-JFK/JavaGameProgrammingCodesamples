package jfk.gameprogrammingsamples.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import jfk.tools.Toolbox;

public class GrowingGardenPanel extends JPanel implements MouseMotionListener {

	//Private variables
	static int rows = 18, columns = 24;								//set number of bushes as rows and columns
	static int bushWidthInPixels = 32, bushHeightInPixels = 40;		//store the size of the bush-images
	static int windowWidthInPixels = columns * bushWidthInPixels, 	//calculate the total window size
			windowHeightInPixels = rows * bushHeightInPixels;		
	Point currentMouseSquare = new Point(0,0);						//stores which bush (column/row) the mouse is over currently 
	Point previousMouseSquare = new Point(-1, -1);					//stores which bush (column/row) the mouse was over previously 
																	//(ensures we only grow bushes when the mouse moves to a new bush)		
	int[][] bushStates = new int[columns][rows];					//double array of integers to store which state all the bushes are in. From sprout (0) to fully grown bush with fruits (4)
	Color dirtBrown = new Color(118, 73, 48);						//a brown color used to fill the background, to look like dirt for the bushes
	BufferedImage bushSpriteSheet = null;							//the sprite sheet with all five states of bush growth (0-4)

	public static void main(String[] args) {
		
		GrowingGardenPanel examplePanel = new GrowingGardenPanel();	//create our panel
		examplePanel.addMouseMotionListener(examplePanel);			//let the panel listen to its own mouse movements
		
		JFrame frame = new JFrame("State visualization sample");	//create a Frame (window)
		frame.setResizable(false);									//lock its size
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		//set the X button to close the window
		frame.setSize(windowWidthInPixels, windowHeightInPixels);	//set the size depending on the desired number of columns and rows
		frame.getContentPane().add(examplePanel);					//add our panel
		frame.setVisible(true);										//show the window
	}
	

	public GrowingGardenPanel() {
		bushSpriteSheet = loadImage("/graphics/bush.png");					//load the png with the sprite sheet from the resources folder
	}

	public void paint(Graphics g) {
		g.setColor(dirtBrown);						//set the color to dirt brown
		g.fillRect(0, 0, getWidth(), getHeight());	//fill the screen with dirt

		//for each column and row
		for (int columnCounter = 0; columnCounter < bushStates.length; columnCounter++) {
			for (int rowCounter = 0; rowCounter < bushStates[0].length; rowCounter++) {
				
				//look up the current bush size in that square
				int bushSize = bushStates[columnCounter][rowCounter];

				//and draw the correct part of the bush sprite sheet to the screen at the correct location for that square
				drawCorrectBushPictureToScreen(g, bushSize, columnCounter, rowCounter);
			}
		}
		g.setColor(Color.white);
		g.drawString("Square (" + currentMouseSquare.x + "," + currentMouseSquare.y + "), bush size " + bushStates[currentMouseSquare.x][currentMouseSquare.y], 5, 15);
	}

	private void drawCorrectBushPictureToScreen(Graphics g, int bushSize, int column, int row) {
		
		//we find top left and bottom right corner of the destination rectangle we want to draw to on screen (on the JPanel)
		 Point drawDestinationTopLeftCorner = new Point(column * bushWidthInPixels, row * bushHeightInPixels);
		 Point drawDestinationBottomRightCorner = new Point(drawDestinationTopLeftCorner.x + bushWidthInPixels, drawDestinationTopLeftCorner.y + bushHeightInPixels);

		//we find top left and bottom right corner of the source rectangle we want to get a bush image from (on the sprite sheet)
		Point imageSourceTopLeftCorner = new Point(bushSize * bushWidthInPixels, 0);
		Point imageSourceBottomRightCorner = new Point(imageSourceTopLeftCorner.x + bushWidthInPixels, imageSourceTopLeftCorner.y + bushHeightInPixels);

		//we draw from the source rectangle in the sprite sheet to the destination rectangle on screen 
		g.drawImage(bushSpriteSheet, 	drawDestinationTopLeftCorner.x, drawDestinationTopLeftCorner.y,
										drawDestinationBottomRightCorner.x, drawDestinationBottomRightCorner.y,		
										imageSourceTopLeftCorner.x, imageSourceTopLeftCorner.y, 
										imageSourceBottomRightCorner.x,	imageSourceBottomRightCorner.y, null);
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		
		calculateCurrentMouseSquare(arg0.getPoint());
		if (mouseIsOverNewSquare()) {
			growPlantInSquareByOneSize(currentMouseSquare.x, currentMouseSquare.y);
			previousMouseSquare = currentMouseSquare;
			repaint();
		}
	}

	private void calculateCurrentMouseSquare(Point mousePoint) {
		Point currentMousePosition = mousePoint;
		currentMouseSquare = new Point((int) (currentMousePosition.getX() / bushWidthInPixels), (int) (currentMousePosition.getY() / bushHeightInPixels));
	}

	private boolean mouseIsOverNewSquare() {
		return !currentMouseSquare.equals(previousMouseSquare);
	}

	private void growPlantInSquareByOneSize(int x, int y) {
		//grow current square one step, but ensure we stop at the final state (4)
		if (bushStates[x][y] < 4) {	
			bushStates[x][y]++;
			System.out.println("Value is now: " + bushStates[x][y] + " in square [" + x + "][" + y + "]");
		}
	}

	private BufferedImage loadImage(String imagePathOrUrl) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(this.getClass().getResource(imagePathOrUrl));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return image;
	}

	// empty interface implementation
	@Override
	public void mouseDragged(MouseEvent arg0) {}
}