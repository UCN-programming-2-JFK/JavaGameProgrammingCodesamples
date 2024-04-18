package jfk.gameprogrammingsamples.boardgame;

import java.awt.Graphics;
import java.awt.Image;

public class PlayingPiece {

	private Image image;
	private int width, height;
	private String playersName;
	private int indexOfCurrentTile;
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
		setWidth(image.getWidth(null));
		setHeight(image.getHeight(null));
	}
	public String getPlayersName() {
		return playersName;
	}
	public void setPlayersName(String playersName) {
		this.playersName = playersName;
	}
	public PlayingPiece(Image image, String playersName) {
		super();
		this.setImage(image);
		this.playersName = playersName;
	}
	
	public void draw(Graphics g, int x, int y) {
		g.drawImage(image, x, y, null);
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getIndexOfCurrentTile() {
		return indexOfCurrentTile;
	}
	public void setIndexOfCurrentTile(int indexOfCurrentTile) {
		this.indexOfCurrentTile = indexOfCurrentTile;
	}
	
}