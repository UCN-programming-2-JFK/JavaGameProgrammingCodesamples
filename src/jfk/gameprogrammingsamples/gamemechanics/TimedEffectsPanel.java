package jfk.gameprogrammingsamples.gamemechanics;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;

@SuppressWarnings("serial")
public class TimedEffectsPanel extends JPanel implements KeyListener {

	//Private variables
	static int windowWidth = 800, windowHeight = 600;
	int totalDamageBonus, totalDefenseBonus;
	Image attackEffect, defenseEffect	 = null;								//the sprite sheet with the different stages of explosion
	public static final Font FONT = new Font("Courier New", Font.BOLD, 20);				//font for writing
	public static final Font BIGFONT = new Font("Courier New", Font.BOLD, 64);				//font for writing
	long lastUpdate;												//the last time an update was performed. Used to calculate time since last update, to smooth animations
	ArrayList<TimedEffect> effects = new ArrayList<TimedEffect>();	//the list to hold all currently running explosions
	Random rnd = new Random();
	public static void main(String[] args) {
		
		TimedEffectsPanel examplePanel = new TimedEffectsPanel();//create our panel
		
		JFrame frame = new JFrame("Timed effect sample");			//create a Frame (window)
		frame.setResizable(false); 									// lock its size
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 		// set the X button click to close the window
		frame.setSize(windowWidth, windowHeight); 					// set the size
		frame.getContentPane().add(examplePanel); 					// add our  panel
		frame.setVisible(true); 									// show the window>>
		examplePanel.setFocusable(true);
		examplePanel.grabFocus();									// !Important! - for the KeyListener to get key presses	
		examplePanel.runGameLoop();
	}
	
	public TimedEffectsPanel() {
		attackEffect = loadImage("/gamemechanics/attack.png");		//load the png with the sprite sheet from the resources folder
		defenseEffect = loadImage("/gamemechanics/defense.png");		//load the png with the sprite sheet from the resources folder
		addKeyListener(this);										//let the panel listen to its own mouse movements
	}

	public void paint(Graphics g) {
		
		drawBackground(g);
		drawTimedEffects(g);
		writeHelpAndNumberOfEffects(g);
	}

	private void writeHelpAndNumberOfEffects(Graphics g) {
		
		g.setColor(Color.white);
		g.setFont(FONT);
		g.drawString(effects.size() + " bonuses active", 6, 30);
		g.drawString("[A] add random Attack bonus ", 6, 55);

		g.drawString("[D] add random Defense bonus ", 6, 80);
		g.setFont(BIGFONT);
		g.drawImage(attackEffect, getWidth()/2, 24, null);
		g.drawString("+" + totalDamageBonus,getWidth()/2+ 74, 74);
		g.drawImage(defenseEffect, getWidth()/2+ 200, 24, null);
		
		g.drawString("+" + totalDefenseBonus,getWidth()/2 + 274, 74);
	}

	private void drawTimedEffects(Graphics g) {
		for (int i = 0; i < effects.size(); i++) {
			effects.get(i).draw(g, new Point(getWidth()/4, getHeight()- 64 - attackEffect.getHeight(null)*i));	
		}		
	}

	private void drawBackground(Graphics g) {
		g.setColor(Color.blue);
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
		
		System.out.println(timePassedSinceLastUpdate);
		System.out.println(effects.size());
		totalDamageBonus = 0;
		totalDefenseBonus = 0;
		//iterate backwards through all the sprites - update them - and remove any that have reached the final frame (they return true from isDone())
		for(int effectCounter = effects.size()-1; effectCounter >= 0; effectCounter--) {
			
			//get the current sprite from the ArrayList, to make it easier to update and check for isDone
			TimedEffect currentSprite = effects.get(effectCounter);	 
			
			currentSprite.update(timePassedSinceLastUpdate);

			if(currentSprite.isDone()) {
				effects.remove(effectCounter);
			}
			else {
				totalDamageBonus += currentSprite.getAttackBonus();
				totalDefenseBonus += currentSprite.getDefenseBonus();
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
			
	private Image loadImage(String imagePathOrUrl)
	    {
		 Image image = null;
		 try {
			 image = ImageIO.read(this.getClass().getResource(imagePathOrUrl));
			} catch (IOException e) {System.out.println(e.getMessage());}
		 return image;
	    }

		@Override
		public void keyPressed(KeyEvent arg0) {

			switch (arg0.getKeyCode()) {
			case KeyEvent.VK_A:
				effects.add(new TimedEffect(attackEffect, rnd.nextInt(5)+1, 0, 10000));
				break;

			case KeyEvent.VK_D:
				effects.add(new TimedEffect(defenseEffect, 0, rnd.nextInt(5)+1, 10000));
				break;
			}
			
		}

		@Override
		public void keyReleased(KeyEvent arg0) {}
		@Override
		public void keyTyped(KeyEvent arg0) {}
}