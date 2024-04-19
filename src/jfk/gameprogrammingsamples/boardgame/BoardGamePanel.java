package jfk.gameprogrammingsamples.boardgame;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

import jfk.gameprogrammingsamples.boardgame.die.Die;

public class BoardGamePanel extends JPanel implements MouseListener {
	long lastUpdate; // the last time an update was performed. Used to calculate time since last
						// update, to smooth animations
	public static final Font FONT = new Font("Courier New", Font.BOLD, 42);
	static BoardGame game;

	public BoardGamePanel() {
		super();
		java.util.List<PlayingPiece> pieces = new ArrayList<>();
		pieces.add(new PlayingPiece(loadImage("/boardgame/playingpiece_64px_red.png"), "Anna"));
		pieces.add(new PlayingPiece(loadImage("/boardgame/playingpiece_64px_blue.png"), "Bob"));
		Image spaceImage = loadImage("/boardgame/space_128px.png");
		Image startSpaceImage = loadImage("/boardgame/startspace_128px.png");
		Image dieFacesImage = loadImage("/boardgame/diefaces.png");
		game = new BoardGame(spaceImage, startSpaceImage, dieFacesImage, pieces);
		this.addMouseListener(game);
	}

	public static void main(String[] args) {

		BoardGamePanel examplePanel = new BoardGamePanel();// create our panel
		
		examplePanel.addMouseListener(examplePanel); // let the panel listen to its own mouse clicks

		JFrame frame = new JFrame("BoardGamePanel sample"); // create a Frame (window)
		frame.setResizable(false); // lock its size
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // set the X button to close the window
		int boardWidth = game.getBoard().getWidth();
		int boardHeight = game.getBoard().getHeight();
		frame.setSize(boardWidth, boardHeight ); // set the size depending on the desired number of columns and rows
		frame.getContentPane().add(examplePanel); // add our panel
		frame.setVisible(true); // show the window
		examplePanel.runGameLoop(); // start the game loop, which runs the animation and refreshes the screen
	}

	public void paint(Graphics g) {
		drawBackground(g);
		if(!game.getDie().isRolling() && !game.isMoving()) {
			drawCurrentPlayer(g);
		}
		game.draw(g);
	}

	private void drawCurrentPlayer(Graphics g) {
		g.setColor(Color.white);
		g.setFont(FONT);
		g.drawString("Roll the die, "+ game.getCurrentPlayer().getPlayersName(), 120, 90);	
	}

	private void drawBackground(Graphics g) {
		g.setColor(new Color(39, 130, 9));
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	// runs the game loop forever
	public void runGameLoop() {

		long timePassedSinceLastUpdate = 0;
		lastUpdate = System.currentTimeMillis();

		// run as long as the window exists
		while (true) {
			timePassedSinceLastUpdate = System.currentTimeMillis() - lastUpdate;
			lastUpdate = System.currentTimeMillis();
			update(timePassedSinceLastUpdate);
			repaint(); // ask for the UI to be redrawn
			waitAShortInterval();
		}
	}

	private void update(long timePassedSinceLastUpdate) {
		game.update(timePassedSinceLastUpdate);
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

	}

	private Image loadImage(String imagePathOrUrl) {
		Image image = null;
		try {
			image = ImageIO.read(this.getClass().getResource(imagePathOrUrl));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return image;
	}
	
	
	

	// mouse events with empty implementation
	@Override
	public void mouseClicked(MouseEvent arg0) {}
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mouseReleased(MouseEvent arg0) {}
}