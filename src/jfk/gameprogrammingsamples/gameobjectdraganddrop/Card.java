package jfk.gameprogrammingsamples.gameobjectdraganddrop;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Card implements Draggable {

	public enum Suit {Hearts(0), Diamonds(1), Spades(2), Clubs(3);
		
		private int value; private Suit(int value) { this.value = value; }
		public int getValue() {return value;}
	}
	
private int width = 72;
private int height = 96;
	private int value;
	private Suit suit;
	private Point position;
	private BufferedImage spriteMap;
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Suit getSuit() {
		return suit;
	}

	public void setSuit(Suit suit) {
		this.suit = suit;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public BufferedImage getImage() {
		return spriteMap;
	}

	public void setImage(BufferedImage spriteMap) {
		this.spriteMap = spriteMap;
	}

	public Card(Suit suit, int value, BufferedImage spriteMap) {
		super();
		this.setSuit(suit);
		this.setValue(value);
		this.setImage(spriteMap);
	}

	//find out whether the card contains a given point (for click and drag)
	public boolean contains(Point point) {
		return (getPosition().getX() <= point.getX() && point.getX()  <= getPosition().getX() + getWidth() &&
				getPosition().getY() <= point.getY() && point.getY() <= getPosition().getY() + getHeight());
	}
	
	//move the card (update position) by a certain amount
	public void move(int xMovement, int yMovement) {
		this.setPosition(new Point(getPosition().x + xMovement, getPosition().y + yMovement));
	}
	
	
	
	public void draw(Graphics g){
		//draw the card from the sprite sheet based on the suit (determines where to read from on the y-axis) and the value (determines the x-axis)
		g.drawImage(getImage(),
				getPosition().x, getPosition().y, 
				getPosition().x + getWidth(), getPosition().y  + getHeight(),
				(getValue()-1) * getWidth(), getSuit().getValue()*getHeight(), 
				(getValue()-1) * getWidth() + getWidth(), getSuit().getValue()*getHeight() + getHeight(), null);
	}
	
	@Override
	public String toString() {
		String valueName = null;
		switch(getValue()) {
		case 13 : valueName = "King"; break;
		case 12 : valueName = "Queen"; break;
		case 11 : valueName = "Jack"; break;
		case 1 : valueName = "Ace"; break;
		default: valueName = "" + getValue();
		}
		return valueName + " of " + getSuit();
	}
}