package jfk.gameprogrammingsamples.scenemanagement.scenes;

import java.awt.Color;
import jfk.gameprogrammingsamples.scenemanagement.*;
import java.awt.Graphics;

import jfk.gameprogrammingsamples.scenemanagement.SceneManagerPanel;

public class MenuScene extends BaseScene {

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
		fillBackgroundWithColor(g, Color.blue);
		writeTextCenteredOnScreen(g, "MENU: G for Game, Q to Quit", Color.white);
	}
	
	public MenuScene() {
		setName("MenuScene");
	}
}