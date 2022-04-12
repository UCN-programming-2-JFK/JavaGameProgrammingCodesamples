package jfk.gameprogrammingsamples.maps;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;


public class DraggableMapPanel extends JPanel implements MouseListener, MouseMotionListener {

	Random rnd = new Random();
	Color greenColor = new Color(0,210, 0);
	static int tileSize = 64;							//size of the tiles in the sprite sheet in pixels
	int rows = 16;										//the number of rows in the map
	int columns = 24;									//the number of columns in the map
	Font font = new Font("Arial", Font.PLAIN, 16);		//the font used to write which frame we're in
	int currentXoffset, currentYoffset;					//stores how much the map is scrolled left and up
	int[][] map = createRandomMap(columns, rows);
	Point lastMousePosition = null;
	static String windowTitle = "Draggable map sample";
	
	
	public static void main(String[] args) {
		
		DraggableMapPanel examplePanel = new DraggableMapPanel();		//create our panel
		examplePanel.addMouseListener(examplePanel);
		examplePanel.addMouseMotionListener(examplePanel);
		
		JFrame frame = new JFrame(windowTitle);				//create a Frame (window)
		frame.setResizable(false);											//lock its size
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);				//set the X button to close the window
		frame.setSize(800, 600);
		//set the size based on the sprite sheet, with a bit added to compensate for the windows' titlebar and borders									
		frame.getContentPane().add(examplePanel);							//add our panel
		frame.setVisible(true);												//show the window
	}
	
	private int[][] createRandomMap(int columns, int rows) {
		
		int[][] map = new int[columns][rows];
		for(int columnCounter = 0; columnCounter < columns; columnCounter++ ) {
			for(int rowCounter = 0; rowCounter < rows; rowCounter++ ) {
				if(rnd.nextInt(4) == 0) {map[columnCounter][rowCounter] = 1;}
			}	
		}
		return map;
	}

	public void paint(Graphics g) {
		g.setFont(font);
		drawMap(g);
		((JFrame) SwingUtilities.getWindowAncestor(this)).setTitle(windowTitle + " ** Offset  = x: " + currentXoffset + ", y: " + currentYoffset);
	}

	private void drawMap(Graphics g) {
		int firstVisibleColumn = currentXoffset / tileSize;
		int firstVisibleRow =  currentYoffset / tileSize;
		int columnsToDraw = getWidth() / tileSize;
		int rowsToDraw = getHeight() / tileSize;
		int lastVisibleColumn = firstVisibleColumn + columnsToDraw + 1;
		int lastVisibleRow = firstVisibleRow + rowsToDraw + 1;
		//in case we're one over the number of rows/columns, limit ourselves to the map's size
		lastVisibleColumn = Math.min(lastVisibleColumn,columns-1);
		lastVisibleRow = Math.min(lastVisibleRow,rows-1);
		
		int xPositionForTile, yPositionForTile; 
		
		for(int columnCounter = firstVisibleColumn; columnCounter <= lastVisibleColumn; columnCounter++ ) {
			for(int rowCounter = firstVisibleRow; rowCounter <= lastVisibleRow; rowCounter++ ) {
				g.setColor(Color.blue);
				if(map[columnCounter][rowCounter] == 1) {
					g.setColor(greenColor);
				}
				xPositionForTile = columnCounter * tileSize - currentXoffset ;
				yPositionForTile = rowCounter * tileSize - currentYoffset;
				g.fillRect(xPositionForTile, yPositionForTile, tileSize, tileSize);
				
				//write tile column and row with a shadow to make it easier to see
				g.setColor(Color.black);
				g.drawString("(" + columnCounter+ "," + rowCounter+ ")", xPositionForTile, yPositionForTile + 16);
				g.setColor(Color.white);
				g.drawString("(" + columnCounter+ "," + rowCounter+ ")", xPositionForTile +1, yPositionForTile + 15);
			}	
		}
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		lastMousePosition = arg0.getPoint();
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		currentXoffset += lastMousePosition.x - arg0.getPoint().x;
		if(currentXoffset <= 0) { currentXoffset = 0;}
		if(currentXoffset >= columns*tileSize - getWidth()) { currentXoffset = columns*tileSize - getWidth();}
		currentYoffset +=  lastMousePosition.y - arg0.getPoint().y;
		if(currentYoffset <= 0) { currentYoffset = 0;}
		if(currentYoffset >= rows*tileSize - getHeight()) { currentYoffset = rows*tileSize - getHeight();}
		lastMousePosition = arg0.getPoint();
		
		repaint();
	}

	//empty implementations of interfaces
	@Override
	public void mouseMoved(MouseEvent arg0) {}
	@Override
	public void mouseClicked(MouseEvent arg0) {}
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mouseReleased(MouseEvent arg0) {}
}