package jfk.gameprogrammingsamples.gamemechanics;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Dimension2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;

@SuppressWarnings("serial")
public class FiniteStateMachinePanel extends JPanel {

	//Private variables
	
	static int windowWidth = 800, windowHeight = 600;
	public static final Font FONT = new Font("Courier New", Font.BOLD, 20);				//font for writing
	public static final Font BIGFONT = new Font("Courier New", Font.BOLD, 64);				//font for writing
	FiniteStateCreature creature;
	long lastUpdate;							//the last time an update was performed. Used to calculate time since last update, to smooth animations
	ArrayList<Point.Float> food = new ArrayList<>();		//the list to hold all positions of food
	Image creatureImage, creatureChewImage, creatureDeadImage;
	Random rnd = new Random();
	public static void main(String[] args) {
		
		FiniteStateMachinePanel examplePanel = new FiniteStateMachinePanel();//create our panel
		
		JFrame frame = new JFrame("Finite State Machine sample");			//create a Frame (window)
		frame.setResizable(false); 									// lock its size
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 		// set the X button click to close the window
		frame.setSize(windowWidth, windowHeight); 					// set the size
		frame.getContentPane().add(examplePanel); 					// add our  panel
		frame.setVisible(true); 									// show the window>>
		
		examplePanel.setPreferredSize( new Dimension(windowWidth, windowHeight));
		examplePanel.setFocusable(true);
		examplePanel.setSize(windowWidth, windowHeight);
		frame.pack();
		examplePanel.grabFocus();									// !Important! - for the KeyListener to get key presses	
		examplePanel.runGameLoop();
	}
	
	public FiniteStateMachinePanel() {
		createCreature();
	}

	private void createCreature() {
		creatureImage = loadImage("/gamemechanics/finitestatecreature.png");
		creatureChewImage = loadImage("/gamemechanics/finitestatecreature_chewimage.png");
		creatureDeadImage = loadImage("/gamemechanics/finitestatecreature_deadimage.png");
		creature = new FiniteStateCreature(new Point.Float(windowWidth/2, windowHeight/2), new Point.Float(), creatureImage, creatureChewImage, creatureDeadImage, food, FiniteStateCreature.CreatureState.WANDERING, new Dimension(windowWidth, windowHeight));
	}

	private void addFoodAtRandomPositions() {
		for (int i = 0; i < 20; i++) {	
			food.add(new Point.Float(rnd.nextInt(windowWidth), rnd.nextInt(windowHeight)));
		}
	}

	public void paint(Graphics g) {
		
		drawBackground(g);
		drawFood(g);
		creature.draw(g);
	}

	private void drawFood(Graphics g) {
		int size = 6;
		g.setColor(Color.green);
	for (int i = 0; i < food.size(); i++) {
		g.fillRect((int)food.get(i).x-size/2, (int)food.get(i).y-size/2, size, size);
	}
		
	}

	private void drawBackground(Graphics g) {
		Color background = new Color(153, 74, 0);
		g.setColor(background);
		g.fillRect(0,0,getWidth(), getHeight());

	}

	//runs the game loop forever
	public void runGameLoop() {

		addFoodAtRandomPositions();
		
		long timePassedSinceLastUpdate = 0;
		lastUpdate = System.currentTimeMillis();
		
		// run as long as the window exists
		while (true) { 
			timePassedSinceLastUpdate = System.currentTimeMillis() - lastUpdate;;
			lastUpdate = System.currentTimeMillis();
			update(timePassedSinceLastUpdate);
			repaint(); // ask for the UI to be redrawn
			waitAShortInterval();
		}
	}
	
	private void update(long timePassedSinceLastUpdate) {
			creature.update(timePassedSinceLastUpdate);
	}

	private void waitAShortInterval() {
		try {
			Thread.sleep(30);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
			
	private Image loadImage(String imagePathOrUrl)
	    {
		 Image image = null;
		 try {
			 image = ImageIO.read(this.getClass().getResource(imagePathOrUrl));
			} catch (IOException e) {System.out.println(e.getMessage());}
		 return image;
	    }


}