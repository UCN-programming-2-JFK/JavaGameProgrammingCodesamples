package jfk.gameprogrammingsamples.scenemanagement.scenes;

import java.awt.Graphics;
import java.util.ArrayList;

public class SceneManager implements GameComponent {

	private ArrayList<BaseScene> scenes = new ArrayList<BaseScene>();
	private BaseScene currentScene;
	
	public BaseScene getCurrentScene() {
		return currentScene;
	}
	
	public void setCurrentScene(BaseScene currentScene) {
		this.currentScene = currentScene;
	}
	
	public void setCurrentScene(String sceneName) {
		for(BaseScene scene : scenes) {
			if(scene.getName() == sceneName) {
				setCurrentScene(scene);
				break;
			} 
		}
	}
	
	public void addScene(BaseScene scene) {
		scenes.add(scene);
		scene.setSceneManager(this);
	}
	
	@Override
	public void update() {
		
		if(getCurrentScene() != null){
			getCurrentScene().update();
		}
		
	}
	@Override
	public void draw(Graphics g) {
		if(getCurrentScene() != null){
			getCurrentScene().draw(g);
		}
	}
}