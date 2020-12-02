package jfk.gameprogrammingsamples.scenemanagement;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import jfk.gameprogrammingsamples.scenemanagement.scenes.GameOverScene;
import jfk.gameprogrammingsamples.scenemanagement.scenes.GameScene;
import jfk.gameprogrammingsamples.scenemanagement.scenes.MenuScene;
import jfk.gameprogrammingsamples.scenemanagement.scenes.SceneManager;
import jfk.gameprogrammingsamples.scenemanagement.scenes.SplashScene;


public class SceneManagerPanel extends JPanel implements KeyListener{

	
	SceneManager sceneManager = new SceneManager();
	public static boolean QUITGAMEPRESSED, STARTGAMEPRESSED, ROLLDICEPRESSED;
	public static Font bigFont = new Font("Verdana", Font.PLAIN, 18);
	
	//main method, to instantiate and add the JPanel to a window (JFrame) 
		public static void main(String[] args) {
			SceneManagerPanel examplePanel= null;
			examplePanel = new SceneManagerPanel();
			
			// create our panel
			examplePanel.addKeyListener(examplePanel); 				// let the panel listen to mouse clicks

			JFrame frame = new JFrame("Scene management sample using scenemanagement"); 	// create a Frame (window)
			frame.setResizable(false); 									// lock its size
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 		// set the X button click to close the window
			frame.getContentPane().setPreferredSize(new Dimension(800, 600));
			frame.getContentPane().add(examplePanel); 					// add our panel
			frame.pack();
			frame.setVisible(true); 									// show the window
			examplePanel.grabFocus();									// !Important! - for the KeyListener to get key presses	
			examplePanel.runGameLoop(); 								// start the game loop
		}
		
		public SceneManagerPanel(){
			this.setFocusable(true);
			sceneManager.addScene(new SplashScene());
			sceneManager.addScene(new MenuScene());
			sceneManager.addScene(new GameScene());
			sceneManager.addScene(new GameOverScene());
			sceneManager.setCurrentScene("SplashScene");
		}
		
		public void runGameLoop() {

			while (true) { // run as long as the window exists
				update();
				repaint(); // ask for the UI to be redrawn
				waitAShortInterval();
			}
		}
		
		
		public void update() {
			sceneManager.update();
		}
		
		public void paint (Graphics g) {
			g.setFont(bigFont);
			sceneManager.draw(g);
		}

		private void waitAShortInterval() {
			try {Thread.sleep(30);} 
			catch (InterruptedException e) {e.printStackTrace();}
		}

		// find out which direction to move the warrior based on the arrow pressed
		@Override
		public void keyPressed(KeyEvent arg0) {
			switch (arg0.getKeyCode()) {
			case KeyEvent.VK_G: STARTGAMEPRESSED = true; break;
			case KeyEvent.VK_Q: QUITGAMEPRESSED = true; break;
			case KeyEvent.VK_SPACE: ROLLDICEPRESSED = true; break;
			default:break;
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			switch (arg0.getKeyCode()) {
			case KeyEvent.VK_G: STARTGAMEPRESSED = false; break;
			case KeyEvent.VK_Q: QUITGAMEPRESSED = false; break;
			case KeyEvent.VK_SPACE: ROLLDICEPRESSED = false; break;
			default:break;
			}
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
}