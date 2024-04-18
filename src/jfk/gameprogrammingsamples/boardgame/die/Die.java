package jfk.gameprogrammingsamples.boardgame.die;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.Random;

public class Die {

	private Image dieFaces;
	private static Random random = new Random();
	private int value;
	private float msLeftRolling = 0;
	private DieRollerListenerIF listener;
	private float rollLengthInMs;
	private Point topLeft;
	
	public float getRollLengthInMs() {
		return rollLengthInMs;
	}

	public void setRollLengthInMs(float rollLengthInMs) {
		this.rollLengthInMs = rollLengthInMs;
	}

	public void update(float msPassedSinceLastUpdate){
		if(msLeftRolling <= 0 ) {return;}
		msLeftRolling -= msPassedSinceLastUpdate;
		//System.out.println("msPassedSinceLastUpdate " + msPassedSinceLastUpdate);
		setValue(random.nextInt(3)+1);
		if(msLeftRolling < 0) {
			listener.rollEnded(new DieRollEvent(this));
			//System.out.println("Roll ended");
		}
	}
	
	public int getSize() {
		return dieFaces.getHeight(null);
	}
	
	public Die(Image dieFaces) {
		this(dieFaces, new Point(), 700, null);
	}
	
	public Die(Image dieFaces, Point topLeft, float rollLengthInMs, DieRollerListenerIF listener) {
		super();
		setTopLeft(topLeft);
		this.dieFaces = dieFaces;
		this.setRollLengthInMs(rollLengthInMs);
		this.listener = listener;
	}

	public DieRollerListenerIF getListener() {
		return listener;
	}

	public void setListener(DieRollerListenerIF listener) {
		this.listener = listener;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	public void roll () {
		if(listener != null) {
			msLeftRolling = getRollLengthInMs();
//			System.out.println("Roll begun: "  + msLeftRolling);
			listener.rollBegun(new DieRollEvent(this));
		}
	}

	public Point getTopLeft() {
		return topLeft;
	}

	public void setTopLeft(Point topLeft) {
		this.topLeft = topLeft;
	}
	
	public void draw(Graphics g) {
		int size = getSize();
		
		int sourceTopLeftX = (getValue()-1)* size;
		int sourceTopLeftY = 0;
		int sourceBottomRightX = sourceTopLeftX + size;
		int sourceBottomRightY = size;
		
		int destinationTopLeftX = getTopLeft().x;
		int destinationTopLeftY = getTopLeft().y;
		int destinationBottomRightX = destinationTopLeftX + size;
		int destinationBottomRightY = destinationTopLeftY + size;
		
		g.drawImage(dieFaces,  destinationTopLeftX, destinationTopLeftY, destinationBottomRightX, destinationBottomRightY, sourceTopLeftX, sourceTopLeftY, sourceBottomRightX, sourceBottomRightY, null);
	}
	
	public boolean contains(Point point) {
		return point.x >= topLeft.x && point.x <= topLeft.x + getSize() && point.y >= topLeft.y && point.y <= topLeft.y + getSize();
	}
	
	public boolean isRolling() {return msLeftRolling > 0;}
}