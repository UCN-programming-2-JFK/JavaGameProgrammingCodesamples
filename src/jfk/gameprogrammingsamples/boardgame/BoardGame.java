package jfk.gameprogrammingsamples.boardgame;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import jfk.gameprogrammingsamples.boardgame.die.Die;
import jfk.gameprogrammingsamples.boardgame.die.DieRollEvent;
import jfk.gameprogrammingsamples.boardgame.die.DieRollerListenerIF;

public class BoardGame implements MouseListener, DieRollerListenerIF {

	private Board board;
	private List<PlayingPiece> playingPieces = new ArrayList<>();
	private Image spaceImage, startTileImage;
	private int currentPlayerIndex = 0;
	private int movesLeftBeforeNextPlayer;
	private float msLeftBeforeNextMove, msBetweenPieceMovements = 400;
	private Die die;

	public Die getDie() {
		return die;
	}

	public void setDie(Die die) {
		this.die = die;
	}

	public BoardGame(Image spaceImage, Image startTileImage, Image dieImage, List<PlayingPiece> playingPieces) {
		super();
		this.spaceImage = spaceImage;
		this.startTileImage = startTileImage;
		this.setPlayingPieces(playingPieces);
		setBoard(createBoard());
		int diePositionX = getBoard().getTopLeft().x + getBoard().getWidth() - 236;
		int diePositionY = getBoard().getTopLeft().y + 64;
		die = new Die(dieImage,
				new Point(diePositionX, diePositionY), 700,
				this);
		newGame();
	}

	public void newGame() {
		die.setValue(6);
	}

	private Board createBoard() {
		Board newBoard = new Board(128, 120);
		newBoard.setTrack(createTrack());
		return newBoard;
	}

	private List<Tile> createTrack() {

		List<Tile> track = new ArrayList<Tile>();
		int tileSize = spaceImage.getWidth(null);
		Tile startTile = new Tile(new Point(tileSize * 4, tileSize * 3), tileSize, startTileImage);
		track.add(startTile);
		track.add(new Tile(new Point(tileSize * 4, tileSize * 2), tileSize, spaceImage));
		track.add(new Tile(new Point(tileSize * 4, tileSize * 1), tileSize, spaceImage));
		track.add(new Tile(new Point(tileSize * 3, tileSize * 1), tileSize, spaceImage));
		track.add(new Tile(new Point(tileSize * 2, tileSize * 1), tileSize, spaceImage));
		track.add(new Tile(new Point(tileSize * 2, 0), tileSize, spaceImage));
		track.add(new Tile(new Point(tileSize, 0), tileSize, spaceImage));
		track.add(new Tile(new Point(0, 0), tileSize, spaceImage));
		track.add(new Tile(new Point(0, tileSize), tileSize, spaceImage));
		track.add(new Tile(new Point(0, tileSize * 2), tileSize, spaceImage));
		track.add(new Tile(new Point(0, tileSize * 3), tileSize, spaceImage));
		track.add(new Tile(new Point(tileSize, tileSize * 3), tileSize, spaceImage));
		track.add(new Tile(new Point(tileSize * 2, tileSize * 3), tileSize, spaceImage));
		track.add(new Tile(new Point(tileSize * 3, tileSize * 3), tileSize, spaceImage));

		for (PlayingPiece piece : playingPieces) {
			startTile.getPiecesOnTile().add(piece);
		}
		
		return track;

	}

	public void draw(Graphics g) {
		getBoard().draw(g);
		die.draw(g);
	}

	public List<PlayingPiece> getPlayingPieces() {
		return playingPieces;
	}

	public void setPlayingPieces(List<PlayingPiece> playingPieces) {
		this.playingPieces = playingPieces;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public void nextPlayer() {
		currentPlayerIndex++;
		currentPlayerIndex %= getPlayingPieces().size();
	}

	public PlayingPiece getCurrentPlayer() {
		return getPlayingPieces().get(currentPlayerIndex);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void movePiece() {
		movesLeftBeforeNextPlayer--;
		board.movePlayingPieceOneTileForward(getPlayingPieces().get(currentPlayerIndex));
		if(movesLeftBeforeNextPlayer <= 0) {
			nextPlayer();
		}
	}

	public boolean isMoving() {
		return movesLeftBeforeNextPlayer > 0;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println("BoardGame.mousePressed()");
		if (!isMoving() && die.contains(e.getPoint())) {
			die.roll();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void rollBegun(DieRollEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void rollEnded(DieRollEvent event) {
		movesLeftBeforeNextPlayer = die.getValue();

	}

	public void update(long timePassedSinceLastUpdate) {
		if (isMoving()) {
			msLeftBeforeNextMove -= timePassedSinceLastUpdate;
			if(msLeftBeforeNextMove <= 0) {
				movePiece();
				msLeftBeforeNextMove = msBetweenPieceMovements;
			}
		}
		die.update(timePassedSinceLastUpdate);

	}
}