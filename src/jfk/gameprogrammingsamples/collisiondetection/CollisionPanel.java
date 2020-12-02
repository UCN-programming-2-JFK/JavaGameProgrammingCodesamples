package jfk.gameprogrammingsamples.collisiondetection;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;

public class CollisionPanel extends JPanel implements KeyListener {

	MovingSprite spaceship = null;
	static int windowWidth = 800, windowHeight = 600;
	ArrayList<MovingSprite> asteroids = new ArrayList<MovingSprite>();
	Image asteroidImage, spaceshipImage;
	boolean rightIsDown, leftIsDown, upIsDown, downIsDown;
	Point currentDirection = new Point();
	Random rnd = new Random();
	int asteroidsHit = 0;
	Font font = new Font("Arial", 0, 24);

	public static void main(String[] args) {

		CollisionPanel examplePanel = new CollisionPanel(); // create our panel
		examplePanel.addKeyListener(examplePanel); // let the panel listen to its own key presses

		JFrame frame = new JFrame("Collision detection - use arrow keys and space to shoot"); // create a Frame (window)
		frame.setResizable(false); // lock its size
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // set the X button click to close the window
		frame.setSize(windowWidth, windowHeight); // set the size
		frame.getContentPane().add(examplePanel); // add our panel
		frame.setVisible(true); // show the window
		examplePanel.grabFocus(); // !Important! - for the KeyListener to get key presses
		examplePanel.runGameLoop();
	}

	public CollisionPanel() {
		asteroidImage = loadImage("/collisiondetection/asteroid.png");
		spaceshipImage = loadImage("/collisiondetection/spaceship.png");
		spaceship = new MovingSprite(new Point(windowWidth/2, (int)(windowHeight * 0.7f)), new Point(), spaceshipImage);
		addAsteroids();
	}

	private void addAsteroids() {
	
		for(int asteroidCounter = 0;asteroidCounter < 16; asteroidCounter++) {
			MovingSprite asteroid = new MovingSprite(getRandomPointForAsteroidAboveScreen(), new Point(0,4), asteroidImage);
			asteroids.add(asteroid);
		}
	}
	
	private Point getRandomPointForAsteroidAboveScreen() {
		return new Point(rnd.nextInt(windowWidth), -rnd.nextInt(windowHeight*2));
	}

	// runs the game loop forever
	public void runGameLoop() {

		while (true) { // run as long as the window exists
			update();
			repaint(); // ask for the UI to be redrawn
			waitAShortInterval();
		}
	}

	public void update() {
		setMovementBasedOnKeysPressed();
		moveEverything();
		performCollisionDetection();
	}

	private void performCollisionDetection() {
		for (int asteroidCounter = asteroids.size() - 1; asteroidCounter >= 0; asteroidCounter--) {

			MovingSprite currentAsteroid = asteroids.get(asteroidCounter);
			if (currentAsteroid.getPosition().distance(spaceship.getPosition()) < spaceship.getWidth() / 2
					+ currentAsteroid.getWidth() / 2) {
				currentAsteroid.setPosition(getRandomPointForAsteroidAboveScreen());
				asteroidsHit++;
			}
		}
	}

	private void moveEverything() {
		for (MovingSprite asteroid : asteroids) {
			asteroid.update();
			if(asteroid.getPosition().y > windowHeight + 150) {
				asteroid.setPosition(getRandomPointForAsteroidAboveScreen());
			}
		}
		spaceship.update();
	}

	private void setMovementBasedOnKeysPressed() {
		int speed = 5;
		int directionX = 0, directionY = 0;

		if (upIsDown) {directionY = -speed;}
		if (downIsDown) {directionY = speed;}
		if (rightIsDown) {directionX = speed;}
		if (leftIsDown) {directionX = -speed;}

		Point movement = new Point(directionX, directionY);
		spaceship.setMovement(movement);
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		spaceship.draw(g);
		Point spaceshipPosition = spaceship.getPosition();
		int spaceshipWidth = spaceship.getWidth();
		int spaceshipHeight= spaceship.getHeight();
		if(System.currentTimeMillis()% 1000 < 200) {
			g.setColor(Color.red);
			g.drawOval(spaceshipPosition.x - spaceshipWidth/2, spaceshipPosition.y - spaceshipWidth/2 , spaceshipWidth, spaceshipWidth);	
		}
		

		for (MovingSprite asteroid : asteroids) {
			asteroid.draw(g);
			Point asteroidPosition = asteroid.getPosition();
			int asteroidWidth = asteroid.getWidth();
			int asteroidHeight= asteroid.getHeight();
			if(System.currentTimeMillis()% 1000 < 200) {
				g.setColor(Color.yellow);
				g.drawOval(asteroidPosition.x - asteroidWidth/2, asteroidPosition.y - asteroidWidth/2 , asteroidWidth, asteroidWidth);
			}
		}
		g.setColor(Color.white);
		g.setFont(font);
		g.drawString("Asteroids hit: " + asteroidsHit, 10, 26);
	}

	private void waitAShortInterval() {
		try {Thread.sleep(30);} 
		catch (InterruptedException e) {e.printStackTrace();}
	}

	// find out which direction to move the warrior based on the arrow pressed
	@Override
	public void keyPressed(KeyEvent arg0) {
		switch (arg0.getKeyCode()) {
		case KeyEvent.VK_UP: upIsDown = true; break;
		case KeyEvent.VK_DOWN: downIsDown = true; break;
		case KeyEvent.VK_LEFT: leftIsDown = true; break;
		case KeyEvent.VK_RIGHT: rightIsDown = true; break;
		default:break;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		switch (arg0.getKeyCode()) {
		case KeyEvent.VK_UP: upIsDown = false; break;
		case KeyEvent.VK_DOWN: downIsDown = false; break;
		case KeyEvent.VK_LEFT: leftIsDown = false; break;
		case KeyEvent.VK_RIGHT: rightIsDown = false; break;
		default:break;
		}
	}

	private Image loadImage(String imagePathOrUrl) {
		Image image = null;
		try {image = ImageIO.read(this.getClass().getResource(imagePathOrUrl));}
		catch (IOException e) {System.out.println(e.getMessage());}
		return image;
	}

	@Override
	public void keyTyped(KeyEvent arg0) {}
}