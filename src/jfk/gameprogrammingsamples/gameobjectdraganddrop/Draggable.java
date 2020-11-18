package jfk.gameprogrammingsamples.gameobjectdraganddrop;

import java.awt.Point;

// interface which defines the minimum requirements for anything to be draggable
public interface Draggable {
	boolean contains(Point point);
	void move(int xMovement, int yMovement);	
}
