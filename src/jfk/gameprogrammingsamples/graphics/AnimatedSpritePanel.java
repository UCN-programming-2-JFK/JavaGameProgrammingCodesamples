package jfk.gameprogrammingsamples.graphics;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

public class AnimatedSpritePanel extends JPanel implements MouseMotionListener, MouseListener {

	//Private variables
	Image explosionSpriteSheet = null;								//the sprite sheet with the different stages of explosion
	Font font = new Font("Courier New", Font.BOLD, 24);				//font for writing
	long lastUpdate;												//the last time an update was performed. Used to calculate time since last update, to smooth animations
	ArrayList<AnimatedSprite> sprites = new ArrayList<AnimatedSprite>();	//the list to hold all currently running explosions
	
	public static void main(String[] args) {
		
		AnimatedSpritePanel examplePanel = new AnimatedSpritePanel();//create our panel
		examplePanel.addMouseMotionListener(examplePanel);			//let the panel listen to its own mouse movements
		examplePanel.addMouseListener(examplePanel);				//let the panel listen to its own mouse clicks
		
		JFrame frame = new JFrame("AnimatedSprite sample");			//create a Frame (window)
		frame.setResizable(false);									//lock its size
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		//set the X button to close the window
		frame.setSize(800, 600);									//set the size depending on the desired number of columns and rows
		frame.getContentPane().add(examplePanel);					//add our panel
		frame.setVisible(true);										//show the window
		examplePanel.runGameLoop();									//start the game loop, which runs the animation and refreshes the screen
	}
	
	public AnimatedSpritePanel() {
		explosionSpriteSheet = loadImage("/explosionspritesheet.png");		//load the png with the sprite sheet from the resources folder
	}

	public void paint(Graphics g) {
		
		drawBackground(g);
		drawExplosions(g);
		writeNumberOfExplosions(g);
	}

	private void writeNumberOfExplosions(Graphics g) {
		g.setColor(Color.white);
		g.setFont(font);
		g.drawString(sprites.size() + " explosions on screen", 6, 24);
	}

	private void drawExplosions(Graphics g) {
		for	(AnimatedSprite sprite : sprites) {
			sprite.draw(g);
		}
	}

	private void drawBackground(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0,0,getWidth(), getHeight());

	}

	//runs the game loop forever
	public void runGameLoop() {

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
		
		//iterate backwards through all the sprites - update them - and remove any that have reached the final frame (they return true from isDone())
		for(int spriteCounter = sprites.size()-1; spriteCounter >= 0; spriteCounter--) {
			
			//get the current sprite from the ArrayList, to make it easier to update and check for isDone
			AnimatedSprite currentSprite = sprites.get(spriteCounter);	 
			
			currentSprite.update(timePassedSinceLastUpdate);

			if(currentSprite.isDone()) {
				sprites.remove(spriteCounter);
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
	public void mousePressed(MouseEvent arg0) {
		createNewExplosionAt(arg0.getPoint());	//add explosions wher the user clicks
	}

	private void createNewExplosionAt(Point position) {
		
		int msBetweenFramesInAnimation = 32;
		int tileSize = 128;
		boolean loop = false;
		
		sprites.add(new AnimatedSprite(explosionSpriteSheet, position, msBetweenFramesInAnimation, tileSize, tileSize, loop));
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
		@Override
		public void mouseMoved(MouseEvent arg0) {}
}