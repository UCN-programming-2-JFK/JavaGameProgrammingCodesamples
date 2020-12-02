package jfk.gameprogrammingsamples.scenemanagement.scenes;

import java.awt.Color;
import java.awt.Graphics;

public class TitleScene extends BaseScene {

	long firstUpdate;
	boolean isFirstUpdate = true;
	long millisecondsShown;

	@Override
	public void update() {

		if (isFirstUpdate) {
			isFirstUpdate = false;
			firstUpdate = System.currentTimeMillis();
		}

		millisecondsShown = System.currentTimeMillis() - firstUpdate;
		if ( millisecondsShown > 3000) {
			getSceneManager().setCurrentScene("MenuScene");
		}
	}
	
	@Override 
	public void draw(Graphics g) {
		fillBackgroundWithColor(g, Color.yellow);
		g.setColor(Color.black);
		writeTextCenteredOnScreen(g, "TitleScene (" + (3-millisecondsShown/1000) + ")", Color.black);
	}

	public TitleScene() {
		setName("TitleScene");
	}
}