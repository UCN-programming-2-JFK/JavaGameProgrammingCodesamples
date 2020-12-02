package jfk.gameprogrammingsamples.scenemanagement.scenes;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import jfk.gameprogrammingsamples.scenemanagement.SceneManagerPanel;

public class GameScene extends BaseScene {

	Random rnd = new Random();
	int lastRoll = 0;
	boolean rollDiceWasReleasedLastUpdate = true;

	@Override
	public void update() {
		
		if (SceneManagerPanel.ROLLDICEPRESSED) {
			if (rollDiceWasReleasedLastUpdate) {
				lastRoll = rnd.nextInt(6) + 1;
				if (lastRoll == 1) {
					getSceneManager().setCurrentScene("GameOverScene");
				}
			} 
			rollDiceWasReleasedLastUpdate = false;
		}
		else {
			rollDiceWasReleasedLastUpdate = true;
		}
	}

	@Override
	public void draw(Graphics g) {
		fillBackgroundWithColor(g, Color.green);
		writeTextCenteredOnScreen(g, "GAME: SPACE to roll a die. Rolling a 1 means game over! Last roll: " + lastRoll, Color.black);
	}

	public GameScene() {
		setName("GameScene");
	}

	@Override
	public void onFocus() {
		lastRoll = 0;
	}

}