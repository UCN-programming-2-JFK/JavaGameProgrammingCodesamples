package jfk.gameprogrammingsamples.gameobjectdraganddrop;

import java.awt.Point;

public interface Draggable {
	boolean contains(Point point);
	void move(int xMovement, int yMovement);	
}
