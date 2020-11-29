package jfk.gameprogrammingsamples.collisiondetection;

import java.awt.*;
import java.awt.image.BufferedImage;

//simple sprite class for storing an image at a location and drawing it centered, when asked to
public class MovingSprite  {
	
	private Point position, movement;
	private Image image;
	
	public int getWidth() {
		return getImage().getWidth(null);
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

	public Point getMovement() {
		return movement;
	}

	public void setMovement(Point movement) {
		this.movement = movement;
	}
	
	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public MovingSprite(Point position, Point movement, Image image) {
		this.setPosition(position);
		this.setMovement(movement);
		this.setImage(image);
		
	}
	
	public void update() {
		Point newPosition = new Point(getPosition().x + getMovement().x, getPosition().y + getMovement().y);
		this.setPosition(newPosition);
	}
	
	public void draw(Graphics g){
		g.drawImage(getImage(), getPosition().x - getWidth()/2, getPosition().y- getHeight()/2, null);
	}

}