package jfk.gameprogrammingsamples.gameobjectdraganddrop;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class DragAndDropPanel extends JPanel implements MouseMotionListener, MouseListener{

	BufferedImage cardTileSheet = null;
	ArrayList<Draggable> draggableObjects = new ArrayList<Draggable>();
	static int windowWidth = 800, windowHeight = 600;
	Draggable currentlySelectedItem = null;
	Point lastMousePosition = new Point();
	Color background = new Color(0,96, 0);
	Font font = new Font("Arial",0, 24);
	
	public DragAndDropPanel() {
		cardTileSheet = loadImage("/cards.png"); // load the png with the card sprite sheet from the resources folder
		createAndArrangeCards();
	}

	private void createAndArrangeCards() {
		for(Card.Suit suit : Card.Suit.values()) {
			for (int i = 1; i <= 13; i++) {
				Card card = new Card(suit, i, cardTileSheet);
				card.setPosition(new Point((int)(i * card.getWidth()*.7), 32 + suit.getValue() * (int)(card.getHeight()*1.35)));
				draggableObjects.add(card);
			}
		}
	}

	public static void main(String[] args) {

		DragAndDropPanel examplePanel = new DragAndDropPanel(); 	// create our panel
		examplePanel.addMouseMotionListener(examplePanel); 			// let the panel listen to its own mouse movements
		examplePanel.addMouseListener(examplePanel); 				// let the panel listen to its own mouse clicks and drags

		JFrame frame = new JFrame("Drag and drop"); 				// create a Frame (window)
		frame.setResizable(false); 									// lock its size
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 		// set the X button click to close the window
		frame.setSize(windowWidth, windowHeight); 					// set the size depending on the desired number of columns and rows
		frame.getContentPane().add(examplePanel); 					// add our border tiling panel
		frame.setVisible(true); 									// show the window
	}
	
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;	//cast Graphics to Graphics2D, to be able to set antialiasing, etc.
	    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	    //fill the background
		g.setColor(background);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		//draw all cards
		for (Draggable item : draggableObjects) {
			((Card)item).draw(g);
		}
		Draggable itemUnderMouse = findItemAt(lastMousePosition);
		if(itemUnderMouse != null) {
			g.setFont(font);
			g.setColor(Color.black);
			g.drawString(itemUnderMouse.toString(), 10,getHeight() -20);
			g.setColor(Color.white);
			g.drawString(itemUnderMouse.toString(), 8, getHeight() - 18);
		}
			
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		//remember that this was the last place the mouse was clicked
		lastMousePosition = arg0.getPoint();
		
		Draggable foundItem = findItemAt(arg0.getPoint());	
		if(foundItem != null)
		{
			//we found one! Move it to the top of the list, so it appears to have been picked up
			selectItemAndMoveToTop(foundItem);
			//as the GUI to repaint
			repaint();
		}
	}

	private Draggable findItemAt(Point point) {
		//look through all items (from top to bottom) and see if we hit one
		for (int i = draggableObjects.size() -1; i >= 0 ; i--) {
			if(draggableObjects.get(i).contains(lastMousePosition)) {			
				return draggableObjects.get(i);
			}
		}
		return null;
	}
	
	private void selectItemAndMoveToTop(Draggable item){
		draggableObjects.remove(item);	//remove the item from the list
		draggableObjects.add(item);		//move it to the top
		currentlySelectedItem = item;	//remember that this is the item we've got selected now
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		currentlySelectedItem = null;
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {

		//find out how far we've dragged on both axes
		int mouseMovementX = arg0.getX() - lastMousePosition.x;
		int mouseMovementY = arg0.getY() - lastMousePosition.y;

		//if there is something currently selected - move it
		if(currentlySelectedItem != null) {
			currentlySelectedItem.move(mouseMovementX, mouseMovementY);
			repaint();
		}	
		lastMousePosition = arg0.getPoint();	//remember where the mouse is at
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
	
	@Override
	public void mouseMoved(MouseEvent arg0) {
		lastMousePosition = arg0.getPoint();
		repaint();}
	@Override
	public void mouseClicked(MouseEvent arg0) {}
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}	
}