package jfk.gameprogrammingsamples.boardgame;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Tile {
	private java.awt.Point topLeft;
	private int size = 64;
	private Image image;
	private List<PlayingPiece> piecesOnTile = new ArrayList<>();
	public int getSize() {	return size;}

	public Tile(Point topLeft, int size, Image image) {
		super();
		this.setTopLeft(topLeft);
		this.size = size;
		this.image = image;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public java.awt.Point getTopLeft() {
		return topLeft;
	}

	public void setTopLeft(java.awt.Point topLeft) {
		this.topLeft = topLeft;
	}	
	
	public void draw (Graphics g) {
		drawWithOffset(g, 0,0);
	}
	
 public void drawWithOffset (Graphics g, int xOffset, int yOffset) {
		
		g.drawImage(getImage(), getTopLeft().x + xOffset, getTopLeft().y + yOffset, null);
		
		if(getPiecesOnTile().size() > 0) {
			int pieceSpacing = getSize() / (getPiecesOnTile().size()+1);
			
			List<PlayingPiece> piecesOnTile = getPiecesOnTile();
			for (int i = 0; i < piecesOnTile.size(); i++) {
				PlayingPiece piece = piecesOnTile.get(i);
				int xPositionToDraw =getTopLeft().x + pieceSpacing * (i+1) + xOffset;
				int yPositionToDraw = getTopLeft().y + pieceSpacing * (i+1) + yOffset; 
				
				piece.draw(g,xPositionToDraw - piece.getWidth()/2 , yPositionToDraw- piece.getHeight()/2);
			}
		}
	}

	public List<PlayingPiece> getPiecesOnTile() {
		return piecesOnTile;
	}

	public void setPiecesOnTile(List<PlayingPiece> piecesOnTile) {
		this.piecesOnTile = piecesOnTile;
	}
}