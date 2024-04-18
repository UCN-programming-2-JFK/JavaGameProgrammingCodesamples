package jfk.gameprogrammingsamples.boardgame.die;

public interface DieRollerListenerIF {
	void rollBegun(DieRollEvent event);
	void rollEnded(DieRollEvent event);
}
