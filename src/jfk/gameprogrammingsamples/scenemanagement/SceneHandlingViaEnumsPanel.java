package jfk.gameprogrammingsamples.scenemanagement;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.Random;
import javax.swing.*;

public class SceneHandlingViaEnumsPanel extends JPanel implements KeyListener {

	public enum GameState {
		SplashScreen, MenuScreen, GameRunning, GameOver
	}

	Random rnd = new Random();
	int lastRoll = 0;
	long gameStartTime;
	long splashScreenAgeInMilliseconds;
	private GameState currentState = GameState.SplashScreen;

	public GameState getCurrentState() {
		return currentState;
	}

	public void setCurrentState(GameState newState) {
		this.currentState = newState;
	}

	Font bigFont = new Font("Verdana", Font.PLAIN, 18);

	// main method, to instantiate and add the JPanel to a window (JFrame)
	public static void main(String[] args) {
		
		SceneHandlingViaEnumsPanel examplePanel = new SceneHandlingViaEnumsPanel();

		JFrame frame = new JFrame("Scene management sample using enums"); // create a Frame (window)
		frame.setResizable(false); // lock its size
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // set the X button click to close the window
		frame.getContentPane().setPreferredSize(new Dimension(800, 600));
		frame.getContentPane().add(examplePanel); // add our panel
		frame.pack();
		frame.setVisible(true); // show the window
		examplePanel.grabFocus(); // !Important! - for the KeyListener to get key presses

		examplePanel.runGameLoop(); // start the game loop
	}

	public SceneHandlingViaEnumsPanel() {
		gameStartTime = System.currentTimeMillis();
		addKeyListener(this);
	}

	public void runGameLoop() {

		while (true) { 				// run as long as the window exists
			update();				//perform any game updates
			repaint(); 				// ask for the UI to be redrawn
			waitAShortInterval();	
		}
	}

	public void update() {
		
		switch (currentState) {
		case GameOver: break;
		case GameRunning: break;
		case MenuScreen: break;
		case SplashScreen:
			splashScreenAgeInMilliseconds = System.currentTimeMillis() - gameStartTime;
			if (splashScreenAgeInMilliseconds > 3000) { setCurrentState(GameState.MenuScreen);}
			break;
		default: break;
		}
	}

	public void paint(Graphics g) {
		
		g.setFont(bigFont);

		switch (getCurrentState()) {
		case GameRunning:
			fillBackgroundWithColor(g, Color.green);
			writeTextCenteredOnScreen(g,
					"GAME: SPACE to roll a die. Rolling a 1 means game over! Last roll: " + lastRoll, Color.black);
			break;
		case GameOver:
			fillBackgroundWithColor(g, Color.red);
			writeTextCenteredOnScreen(g, "You rolled a 1. G for new Game, Q to Quit game", Color.white);
			break;
		case MenuScreen:
			fillBackgroundWithColor(g, Color.blue);
			writeTextCenteredOnScreen(g, "MENU: G for Game, Q to Quit", Color.white);
			break;
		case SplashScreen:
			fillBackgroundWithColor(g, Color.yellow);
			g.setColor(Color.black);
			writeTextCenteredOnScreen(g, "SplashScene (" + (3 - splashScreenAgeInMilliseconds / 1000) + ")", Color.black);
			break;
		default:
			break;
		}
	}

	private void waitAShortInterval() {
		try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
	}

	// find out which direction to move the warrior based on the arrow pressed

	public void keyPressed(KeyEvent arg0) {
		switch (arg0.getKeyCode()) {
		case KeyEvent.VK_G:
			if (getCurrentState() == GameState.MenuScreen || getCurrentState() == GameState.GameOver) {
				setCurrentState(GameState.GameRunning);
			}
			break;
		case KeyEvent.VK_Q:
			if (getCurrentState() == GameState.MenuScreen || getCurrentState() == GameState.GameOver) {
				System.exit(0);
			}
			break;
		case KeyEvent.VK_SPACE:

			lastRoll = rnd.nextInt(6) + 1;
			if (lastRoll == 1) {
				setCurrentState(GameState.GameOver);
			}
			break;
		default:
			break;
		}
	}

	public void keyReleased(KeyEvent arg0) {}
	public void keyTyped(KeyEvent arg0) {}

	protected void writeTextCenteredOnScreen(Graphics g, String text, Color textColor) {
		g.setColor(textColor);
		FontMetrics fm = g.getFontMetrics(SceneManagerPanel.bigFont);
		Rectangle2D rect = fm.getStringBounds(text, g);
		g.drawString(text, (int) (g.getClipBounds().width - rect.getWidth()) / 2,
				(int) (g.getClipBounds().height - rect.getHeight()) / 2);
	}

	protected void fillBackgroundWithColor(Graphics g, Color backgroundColor) {
		g.setColor(backgroundColor);
		g.fillRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);
	}
}