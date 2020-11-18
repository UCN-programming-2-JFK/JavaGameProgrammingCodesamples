package jfk.gameprogrammingsamples.movement;

import java.awt.*;
import java.awt.image.BufferedImage;

//simple sprite class for storing an image at a location and drawing it centered, when asked to
public class Sprite  {
	
	private Point position;
	private Image image;
	
	public int getWidth() {
		return getImage().getHeight(null);
	}

	public int getHeight() {
		return getImage().getHeight(null);
	}
	
	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public Sprite(Point position, Image image) {
		this.setPosition(position);
		this.setImage(image);
		
	}
	
	public void draw(Graphics g){
		g.drawImage(getImage(), getPosition().x - getWidth()/2, getPosition().y- getHeight()/2, null);
	}
}