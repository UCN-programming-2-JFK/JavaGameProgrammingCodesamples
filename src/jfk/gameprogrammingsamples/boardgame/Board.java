package jfk.gameprogrammingsamples.boardgame;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Board {
	private Point topLeft = new Point();
	private List<Tile> track = new ArrayList<Tile>();
	private int spaceSize = 128, margin = 128;
	private List<PlayingPiece> playingPieces = new ArrayList<>();

	public List<Tile> getTrack() {
		return track;
	}

	public void setTrack(List<Tile> track) {
		this.track = track;
	}

	public int getTileSize() {
		return spaceSize;
	}

	public void setTileSize(int spaceSize) {
		this.spaceSize = spaceSize;
	}

	public int getMargin() {
		return margin;
	}

	public void setMargin(int margin) {
		this.margin = margin;
	}

	public List<PlayingPiece> getPlayingPieces() {
		return playingPieces;
	}

	public void setPlayingPieces(List<PlayingPiece> playingPieces) {
		this.playingPieces = playingPieces;
	}

	public Board(int spaceSize, int margin) {
		super();
		this.spaceSize = spaceSize;
		this.margin = margin;
	}

	private int getWidthOfTrack() {
		int leftSide = getLeftOfTrack();
		int rightSide = getRightOfTrack();
		return rightSide - leftSide;
	}

	private int getLeftOfTrack() {
		if (getTrack().isEmpty()) {
			return -1; // Or any other appropriate value indicating no spaces in the track
		}
		int leftMost = getTrack().get(0).getTopLeft().x; // Initialize leftMost with the x-coordinate of the first space
		for (Tile currentTile : getTrack()) {
			int currentTileLeft = currentTile.getTopLeft().x; // Get the x-coordinate of the current space's top-left
																// corner
			if (currentTileLeft < leftMost) {
				leftMost = currentTileLeft; // Update leftMost if the current space's left side is more left than the
												// current leftMost
			}
		}
		return leftMost;
	}

	private int getRightOfTrack() {
		if (getTrack().isEmpty()) {
			return -1; // Or any other appropriate value indicating no spaces in the track
		}
		Tile firstTile = getTrack().get(0);
		int rightMost = firstTile.getTopLeft().x; // Initialize rightMost with the x-coordinate of the first space's
													// top-right corner
		for (Tile currentTile : getTrack()) {
			int currentTileRight = currentTile.getTopLeft().x; // Get the x-coordinate of the current space's
																	// top-right corner
			if (currentTileRight > rightMost) {
				rightMost = currentTileRight; // Update rightMost if the current space's right side is more right than
												// the current rightMost
			}
		}
		return rightMost + firstTile.getSize();
	}

	private int getHeightOfTrack() {
		return getBottomOfTrack() - getTopOfTrack();
	}

	private int getTopOfTrack() {
		if (getTrack().size() == 0) {
			return -1;
		}
		int topMost = getTrack().get(0).getTopLeft().y;
		for (Tile currentTile : getTrack()) {
			int currentTilesTop = currentTile.getTopLeft().y;
			if (currentTilesTop < topMost) {
				topMost = currentTilesTop;
			}
		}
		return topMost;
	}

	private int getBottomOfTrack() {
		if (getTrack().size() == 0) {
			return -1;
		}
		Tile firstTile = getTrack().get(0);
		int bottomMost = firstTile.getTopLeft().y;
		for (Tile currentTile : getTrack()) {
			int currentTilesBottom = currentTile.getTopLeft().y;
			if (currentTilesBottom > bottomMost) {
				bottomMost = currentTilesBottom;
			}
		}
		return bottomMost + firstTile.getSize();
	}

	public Point getTopLeft() {
		return topLeft;
	}

	public void setTopLeft(Point topLeft) {
		this.topLeft = topLeft;
	}

	public void draw(Graphics g) {
		drawTrack(g);
	}

	private void drawTrack(Graphics g) {
		for (Tile space : track) {
			space.drawWithOffset(g, getTopLeft().x + getMargin(), getTopLeft().y + getMargin());
		}
	}
	
	public int getWidth() {
		return getWidthOfTrack() + 2 * getMargin();
	}
	public int getHeight() {
		return getHeightOfTrack() + 2 * getMargin();
	}
	
	public void movePlayingPieceOneTileForward(PlayingPiece piece) {
		int currentTileIndex = piece.getIndexOfCurrentTile();
		getTrack().get(currentTileIndex).getPiecesOnTile().remove(piece);
		
		int nextTileIndex = currentTileIndex +1;
		nextTileIndex %= getTrack().size();
		getTrack().get(nextTileIndex).getPiecesOnTile().add(piece);
		
		piece.setIndexOfCurrentTile(nextTileIndex);

	}
}