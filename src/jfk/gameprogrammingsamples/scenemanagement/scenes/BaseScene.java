package jfk.gameprogrammingsamples.scenemanagement.scenes;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import jfk.gameprogrammingsamples.scenemanagement.SceneManagerPanel;

public abstract class BaseScene implements GameComponent {
	
	private String name;
	private SceneManager sceneManager;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SceneManager getSceneManager() {
		return sceneManager;
	}

	public void setSceneManager(SceneManager sceneManager) {
		this.sceneManager = sceneManager;
	}
	
	public void draw(Graphics g) {
		
		fillBackgroundWithColor(g, Color.black);
		writeTextCenteredOnScreen(g, getName(), Color.white);
	}

	protected void writeTextCenteredOnScreen(Graphics g, String text, Color textColor) {
		g.setColor(textColor);
		FontMetrics fm = g.getFontMetrics(SceneManagerPanel.bigFont);
		Rectangle2D rect = fm.getStringBounds(text, g);
		g.drawString(text, (int)(g.getClipBounds().width-rect.getWidth())/2, (int)(g.getClipBounds().height-rect.getHeight())/2);
	}
	
	protected void fillBackgroundWithColor(Graphics g, Color backgroundColor) {
		g.setColor(backgroundColor);
		g.fillRect(0,0,g.getClipBounds().width, g.getClipBounds().height);
	}
	
	public void onFocus() {
		
	}
}