package jfk.gameprogrammingsamples.gamemechanics;

import java.awt.*;
import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.Random;

//simple sprite class for storing an image at a location and drawing it centered, when asked to
public class FiniteStateCreature  {
	
	Random rnd = new Random();
	private float speed = .1f;
	//monster image from https://omagerio.itch.io/1200-tiny-monsters-sprites-16x16
	public enum CreatureState {WANDERING, EATING, DEAD}
	private CreatureState currentState;
	private Point.Float position, movement;
	private Image image, chewImage, deadImage;
	private Dimension2D terrainBorder;
	private float directionInRadians;
	private ArrayList<Point.Float> foodPlacements;
	private long msLeftOfEating = 0;
	private long msLeftOfSleeping = 0;
	private float foodInBelly = 100;

	public CreatureState getCurrentState() {
		return currentState;
	}

	public void setCurrentState(CreatureState currentState) {
		this.currentState = currentState; 
		if(this.getCurrentState()==CreatureState.EATING) {
			msLeftOfEating = 2000;
		}
	}
	
	public int getWidth() {
		return getImage().getWidth(null);
	}

	public int getHeight() {
		return getImage().getHeight(null);
	}
	
	public Point.Float getPosition() {
		return position;
	}

	public void setPosition(Point.Float position) {
		this.position = position;
	}

	public Point.Float getMovement() {
		return movement;
	}

	public void setMovement(Point.Float movement) {
		this.movement = movement;
	}
	
	public Image getImage() {
		switch (getCurrentState()) {
		case WANDERING: return image;
		case EATING: return ((System.currentTimeMillis()%1000)/250 %2)==0 ? chewImage : image;
		case DEAD : return deadImage;
		default: return null;
		}
	}

	public void setImage(Image image) {
		this.image = image;
	}
	
	public Dimension2D getTerrainBorder() {
		return terrainBorder;
	}

	public void setTerrainBorder(Dimension2D terrainBorder) {
		this.terrainBorder = terrainBorder;
	}
	
	public FiniteStateCreature(Point.Float position, Point.Float movement, Image image, Image chewImage, Image deadImage, ArrayList<Point.Float> foodPlacements, CreatureState currenState, Dimension2D terrainBorder) {
		this.setPosition(position);
		this.setMovement(movement);
		this.image = image;
		this.chewImage = chewImage;
		this.deadImage = deadImage;
		this.foodPlacements = foodPlacements;
		this.setCurrentState(currenState);
		this.setTerrainBorder(terrainBorder);
	}

	public void update(double msElapsedSinceLastUpdate) {
		
		switch (getCurrentState()) {
		case WANDERING:
			wander(msElapsedSinceLastUpdate);	
			break;
		case EATING:
			eat(msElapsedSinceLastUpdate);
			break;
		default:
			break;
		}
	}

	private void eat(double msElapsedSinceLastUpdate) {
		msLeftOfEating -= msElapsedSinceLastUpdate;
		if(msLeftOfEating <= 0) {
			this.setFoodInBelly(getFoodInBelly()+50);
			setCurrentState(CreatureState.WANDERING);
		}
	}

	private void wander(double msElapsedSinceLastUpdate) {
		turnSlightlyRightOrLeftAtRandom();
		Point.Float newPosition = new Point.Float(getPosition().x + getMovement().x * (float)msElapsedSinceLastUpdate, getPosition().y + getMovement().y* (float)msElapsedSinceLastUpdate);
		if(newPosition.x - getWidth()/2 >= 0 && getTerrainBorder().getWidth() >= newPosition.x + getWidth()/2 &&
			newPosition.y - getHeight()/2 >= 0 && getTerrainBorder().getHeight()  >= newPosition.y + getHeight()/2) {
			this.setPosition(newPosition);	
		}
		else{
			findNewRandomDirection();
		}
		eatFoodIfCloseEnough();
		this.setFoodInBelly((float) (getFoodInBelly()-0.005f * msElapsedSinceLastUpdate));
	}

	private void eatFoodIfCloseEnough() {
		Point.Float closestFoodPlacement = getClosestFoodPlacement();
		if(closestFoodPlacement != null) {
			Point.Float position = getPosition();
			double distanceToClosestFood = closestFoodPlacement.distance(position);
			//System.out.println(distanceToClosestFood);
			if(distanceToClosestFood < this.getWidth()/2) {
				this.setCurrentState(FiniteStateCreature.CreatureState.EATING);
				foodPlacements.remove(closestFoodPlacement);
			}
		}
	}
	
	public void draw(Graphics g){				
		g.drawImage(getImage(), (int)getPosition().x - getWidth()/2, (int)getPosition().y- getHeight()/2, null);
		g.setFont(FiniteStateMachinePanel.FONT);
		g.drawString(getCurrentState().toString(), (int)getPosition().x - 45, (int)getPosition().y-20);
		drawLifebar(g);
	}
	
	private void drawLifebar(Graphics g) {
		int width = 50;
		int left = (int)getPosition().x - width/2;
		int top = (int)getPosition().y + 30;
		
		if(this.getFoodInBelly()> 70) {g.setColor(Color.green);}
		else if(this.getFoodInBelly() > 50) {g.setColor(Color.yellow);}
		else if(this.getFoodInBelly() > 30) {g.setColor(Color.orange);}
		else {g.setColor(Color.red);}
		g.fillRect(left, top, (int) (width * getFoodInBelly()/100), 10);
		g.drawRect(left, top, width, 10);
		
	}

	private void findNewRandomDirection() {
		float randomDirectionInRadians = rnd.nextFloat() * (float)Math.PI * 2;
		this.setDirectionInRadians(randomDirectionInRadians);
	}
	
	private void turnSlightlyRightOrLeftAtRandom() {
		float randomDirectionChangeInRadians = (rnd.nextFloat() * (float)Math.PI / 8) -  (float)Math.PI / 16;
		this.setDirectionInRadians(this.getDirectionInRadians()+randomDirectionChangeInRadians);
	}

	public float getDirectionInRadians() {
		return directionInRadians;
	}

	public void setDirectionInRadians(float directionInRadians) {
		this.directionInRadians = directionInRadians;
		this.getMovement().x = (float)Math.cos(getDirectionInRadians())*speed;
		this.getMovement().y = (float)Math.sin(getDirectionInRadians())*speed;
	}
	
	private Point.Float getClosestFoodPlacement(){
		
		double smallestDistanceSoFar = Float.MAX_VALUE;
		Point.Float closestFoodPlacementSoFar = null;
		
		for (int i = 0; i < foodPlacements.size(); i++) {
			Point.Float currentPlacement = foodPlacements.get(i);
			double distanceToCurrentPlacement = currentPlacement.distance(getPosition());
			if(distanceToCurrentPlacement < smallestDistanceSoFar) {
				closestFoodPlacementSoFar = currentPlacement;
				smallestDistanceSoFar = distanceToCurrentPlacement;
			}
		}
		return closestFoodPlacementSoFar;
	}

	public float getFoodInBelly() {
		return foodInBelly;
	}

	public void setFoodInBelly(float foodInBelly) {
		this.foodInBelly = foodInBelly;
		if(this.getFoodInBelly() > 100) {
			setFoodInBelly(100);
		}
		else if (getFoodInBelly()<= 0) {
			setCurrentState(CreatureState.DEAD);
		}
	}	
}