package jfk.gameprogrammingsamples.scenemanagement.scenes;

import java.awt.Color;
import java.awt.Graphics;

import jfk.gameprogrammingsamples.scenemanagement.SceneManagerPanel;

public class GameOverScene extends BaseScene {

	@Override
	public void update() {
		if(SceneManagerPanel.STARTGAMEPRESSED) {
			getSceneManager().setCurrentScene("GameScene");
		}
		if(SceneManagerPanel.QUITGAMEPRESSED) {
			System.exit(0);
		}
	}

	@Override
	public void draw(Graphics g) {
		fillBackgroundWithColor(g, Color.red);
		writeTextCenteredOnScreen(g, "You rolled a 1. G for new Game, Q to Quit game", Color.white);
	}

	public GameOverScene() {
		setName("GameOverScene");
	}
}