package jfk.gameprogrammingsamples.maps;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;


public class DynamicMapVisualizationPanel extends JPanel implements MouseMotionListener, MouseListener {

	// Private variables
	static int rows = 8, columns = 12; 							// set map size as number of rows and columns
	static int tileSize = 64; 									// store the size of tiles in pixels
	static int windowWidth = columns * tileSize + 16,			 // calculate the total window size and add pixels for top bar and Window border
			windowHeight = rows * tileSize + 40;
	
	static Color waterColor = new Color(43, 158, 232);			//store the water color for filling the frame when painting islands
	static Color grassColor = new Color(139, 181, 74);			//store the grass color for filling the frame when painting roads
	
	static int[][] map = new int[columns][rows];				//the map (stores 0 for empty and 1 for content)
	static int[][] mapTileIndex = new int[columns][rows];		//the value of the sprite sheet index to use for each square in the map, based on the four neighbor tiles 
	
	Image landSpriteSheet, roadSpriteSheet; 						// the two sprite sheets with all 16 different combinations of land or roads in the four compass directions
	Image currentSpriteSheet; 									//stores which of the two sprite sheets to use when drawing 
	
	//stores state
	Point currentMouseSquare = new Point();						//stores the column and row of the tile that the mouse is currently over
	static int mouseButtonCurrentlyPressed = 0;					//stores what mouse button is pressed (0 = none, 1 = left, 3 = right)

	
	
	public static void main(String[] args) {

		DynamicMapVisualizationPanel examplePanel = new DynamicMapVisualizationPanel(); 	// create our panel
		examplePanel.addMouseMotionListener(examplePanel); 			// let the panel listen to its own mouse movements
		examplePanel.addMouseListener(examplePanel); 				// let the panel listen to its own mouse clicks and drags

		createWindowWithPanel(examplePanel);
	}

	private static void createWindowWithPanel(DynamicMapVisualizationPanel examplePanel) {
		JFrame frame = new JFrame("Dynamic Map Visualization codesample"); 	// create a Frame (window)
		frame.setResizable(false); 									// lock its size
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 		// set the X button click to close the window
		frame.setSize(windowWidth, windowHeight); 					// set the size depending on the desired number of columns and rows
		frame.getContentPane().add(examplePanel); 					// add our panel
		frame.setVisible(true); 									// show the window
	}

	public DynamicMapVisualizationPanel() {
		// load the PNGs with the sprite sheets from the resources folder
		landSpriteSheet = loadImage("/maps/landwatertiles.png"); 		
		roadSpriteSheet = loadImage("/maps/roadTileMap_striped.png"); 
		currentSpriteSheet = landSpriteSheet;	//set up for initially drawing land/sea 
	}

	public void paint(Graphics g) {

		if(currentSpriteSheet == landSpriteSheet) {
			g.setColor(waterColor);		//fill the entire panel with water
		}
		else {
			g.setColor(grassColor);		//fill the entire panel with grass
		}
		
		g.fillRect(0, 0, getWidth(), getHeight());	//fill the background

		// for each column and row
		for (int columnCounter = 0; columnCounter < map.length; columnCounter++) {
			for (int rowCounter = 0; rowCounter < map[0].length; rowCounter++) {
				if (map[columnCounter][rowCounter] > 0) {
					//if we need something else than background (i.e. the square has a value above zero), we draw from the sprite sheet
					drawCorrectTileSheetAreaToScreen(g, columnCounter, rowCounter);
				}
			}
		}
		g.setColor(Color.gray);
		g.drawRect(currentMouseSquare.x * tileSize, currentMouseSquare.y * tileSize, tileSize, tileSize);
		g.setColor(Color.black);
		g.drawString("Left click draws. Right click erases. Middle mouse click toggles sprite sheet", 9, 21);
		g.setColor(Color.white);
		g.drawString("Left click draws. Right click erases. Middle mouse click toggles sprite sheet", 10, 20);
	}

	//this function calculates where to draw on the screen 
	//and where to find the area to draw from on the sprite sheet, based on the column, row, tileSize and the tile's map value
	private void drawCorrectTileSheetAreaToScreen(Graphics g, int column, int row) {

		// we find top left and bottom right corner of the destination rectangle we want
		// to draw to on screen (on the JPanel)
		Point drawDestinationTopLeftCorner = new Point(column * tileSize, row * tileSize);
		Point drawDestinationBottomRightCorner = new Point(drawDestinationTopLeftCorner.x + tileSize,
				drawDestinationTopLeftCorner.y + tileSize);

		// we find top left and bottom right corner of the source rectangle we want to
		// get a bush image from (on the sprite sheet)
		Point imageSourceTopLeftCorner = new Point(mapTileIndex[column][row] * tileSize, 0);
		Point imageSourceBottomRightCorner = new Point(imageSourceTopLeftCorner.x + tileSize,
				imageSourceTopLeftCorner.y + tileSize);

		// we draw from the source rectangle in the sprite sheet to the destination rectangle on screen
		g.drawImage(currentSpriteSheet, drawDestinationTopLeftCorner.x, drawDestinationTopLeftCorner.y,
				drawDestinationBottomRightCorner.x, drawDestinationBottomRightCorner.y, imageSourceTopLeftCorner.x,
				imageSourceTopLeftCorner.y, imageSourceBottomRightCorner.x, imageSourceBottomRightCorner.y, null);
	}
	
	//calculates what map square the mouse is in, by dividing the mouse coordinates with the tile size
	private Point calculateMapTileFromPoint(Point mousePoint) {
		Point currentMousePosition = mousePoint;
		return new Point((int) (currentMousePosition.getX() / tileSize), (int) (currentMousePosition.getY() / tileSize));
	}

	private void updateMapTile(Point mapSquare) {
		
		//don't do anything, if this square is off the map
		if(!isWithinMap(mapSquare)) {return;}
		
		if(mouseButtonCurrentlyPressed == MouseEvent.BUTTON1) {
			map[mapSquare.x][mapSquare.y] = 1;
			updateTileAndNeighbors(mapSquare);
		}
		else if(mouseButtonCurrentlyPressed == MouseEvent.BUTTON3) {
			map[mapSquare.x][mapSquare.y] = 0;
			updateTileAndNeighbors(mapSquare);
		}
		repaint();
	}
	
	private void updateTileAndNeighbors(Point mapSquare) {
		
		//don't do anything, if this square is off the map
		if(!isWithinMap(mapSquare)) {return;}
		
		Point topNeighbor = new Point(mapSquare.x, mapSquare.y-1); updateMapTileIndex(topNeighbor);
		Point rightNeighbor = new Point(mapSquare.x+1, mapSquare.y);updateMapTileIndex(rightNeighbor);
		Point bottomNeighbor = new Point(mapSquare.x, mapSquare.y+1);updateMapTileIndex(bottomNeighbor);
		Point leftNeighbor = new Point(mapSquare.x-1, mapSquare.y);updateMapTileIndex(leftNeighbor);
	}

	private void updateMapTileIndex(Point mapSquare) {
		
		//don't do anything, if this square is off the map
		if(!isWithinMap(mapSquare)) {return;}
		
		int tileSumBasedOnNeighbors = 0;
		
		Point topNeighbor = new Point(mapSquare.x, mapSquare.y-1);
		Point rightNeighbor = new Point(mapSquare.x+1, mapSquare.y);
		Point bottomNeighbor = new Point(mapSquare.x, mapSquare.y+1);
		Point leftNeighbor = new Point(mapSquare.x-1, mapSquare.y);
		
		if(isWithinMap(topNeighbor) && map[topNeighbor.x][topNeighbor.y] == 1) { tileSumBasedOnNeighbors +=1;}
		if(isWithinMap(rightNeighbor) && map[rightNeighbor.x][rightNeighbor.y] == 1) { tileSumBasedOnNeighbors +=2;}
		if(isWithinMap(bottomNeighbor) && map[bottomNeighbor.x][bottomNeighbor.y] == 1) { tileSumBasedOnNeighbors +=4;}
		if(isWithinMap(leftNeighbor) && map[leftNeighbor.x][leftNeighbor.y] == 1) { tileSumBasedOnNeighbors +=8;}
		
		mapTileIndex[mapSquare.x][mapSquare.y] = tileSumBasedOnNeighbors;
		
		repaint();
	}
	
	private boolean isWithinMap(int column, int row)
	{
		return (column >= 0 && column < map.length && row >= 0 && row < map[0].length);
	}

	private boolean isWithinMap(Point square)
	{
		return isWithinMap(square.x, square.y);
	}
	
	
	//if the mouse button is pressed on a tile, update the values in the neighbor tiles
	@Override
	public void mousePressed(MouseEvent arg0) {
		mouseButtonCurrentlyPressed = arg0.getButton();
		Point mapTile = calculateMapTileFromPoint(arg0.getPoint());
		updateMapTile(mapTile);
		if(arg0.getButton() == MouseEvent.BUTTON2) {
			toggleTilesheet();
		}
	}
	
	private void toggleTilesheet() {
		if(currentSpriteSheet == landSpriteSheet) { currentSpriteSheet = roadSpriteSheet; }
		else { currentSpriteSheet = landSpriteSheet; }
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		mouseButtonCurrentlyPressed = 0;
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		currentMouseSquare = calculateMapTileFromPoint(arg0.getPoint());
		updateMapTile(currentMouseSquare);
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		currentMouseSquare = calculateMapTileFromPoint(arg0.getPoint());
		updateMapTile(currentMouseSquare);
		}	
	
	//empty interface implementations
	@Override
	public void mouseClicked(MouseEvent arg0) {}
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	

	private BufferedImage loadImage(String imagePathOrUrl) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(this.getClass().getResource(imagePathOrUrl));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return image;
	}
	
	
}