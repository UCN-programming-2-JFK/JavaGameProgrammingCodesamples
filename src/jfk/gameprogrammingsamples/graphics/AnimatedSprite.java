package jfk.gameprogrammingsamples.graphics;

import java.awt.*;


//A sprite class, which supports animation

public class AnimatedSprite {
	
	private int currentFrameIndex = 0;			//which frame is currently shown
	private Image spriteSheet;					//the sprite sheet used for the source of animation frames
	private int tileHeight, tileWidth;			//height and width of the tiles in pixels
	private int numberOfColumnsInSpriteSheet;	//number of columns in the sprite sheet
	private int numberOfRowsInSpriteSheet;		//number of rows in the sprite sheet
	private boolean loop;						//whether to begin the animation over again, when it has reached the last frame
	private int msBetweenFrames;				//milliseconds between frame changes
	private int msBeforeNextFrame;				//how long time is left before the next frame should be shown
	private Point position;						//where should this animation be drawn. The current frame is drawn centered on this Point.
	
	public int getCurrentFrameIndex() {
		return currentFrameIndex;
	}

	public void setCurrentFrameIndex(int frameIndex) {
		this.currentFrameIndex = frameIndex;
	}

	public Image getSpriteSheet() {
		return spriteSheet;
	}

	public void setSpriteSheet(Image spriteSheet) {
		this.spriteSheet = spriteSheet;
	}
	
	public int getNumberOfColumnsInSpriteSheet() {
		return numberOfColumnsInSpriteSheet;
	}
	

	private void setNumberOfColumnsInSpriteSheet(int numberOfColumnsInSpriteSheet) {
		this.numberOfColumnsInSpriteSheet = numberOfColumnsInSpriteSheet;
	}
	
	public int getNumberOfRowsInSpriteSheet() {
		return numberOfRowsInSpriteSheet;
	}

	private void setNumberOfRowsInSpriteSheet(int numberOfRowsInSpriteSheet) {
		this.numberOfRowsInSpriteSheet = numberOfRowsInSpriteSheet;
	}

	public boolean isLoop() {
		return loop;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	public int getMsBetweenFrames() {
		return msBetweenFrames;
	}

	public void setMsBetweenFrames(int msBetweenFrames) {
		this.msBetweenFrames = msBetweenFrames;
	}	

	public int getTileHeight() {
		return tileHeight;
	}

	public void setTileHeight(int tileHeight) {
		this.tileHeight = tileHeight;
		setNumberOfRowsInSpriteSheet(getSpriteSheet().getHeight(null)/getTileHeight());
	}

	public int getTileWidth() {
		return tileWidth;
	}

	public void setTileWidth(int tileWidth) {
		this.tileWidth = tileWidth;
		setNumberOfColumnsInSpriteSheet ( getSpriteSheet().getWidth(null)/getTileWidth());
	}

	public int getTotalNumberOfFrames() {
		return getNumberOfColumnsInSpriteSheet() * getNumberOfRowsInSpriteSheet();
	}
	
		public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}
	
	//returns true
	public boolean isDone() {
		return (!isLoop() && getCurrentFrameIndex() >= getTotalNumberOfFrames()-1 );
	}
	
	
public AnimatedSprite(Image spriteSheet, Point position, int msBetweenFrames, int tileHeight, int tileWidth, boolean loop) {
		
		setSpriteSheet(spriteSheet);
		setPosition(position);
		setMsBetweenFrames(msBetweenFrames);
		msBeforeNextFrame = msBetweenFrames;
		setTileHeight(tileHeight);
		setTileWidth(tileWidth);
		setLoop(loop);
	}
	
	public void update(float timePassedSinceLastUpdate){
		msBeforeNextFrame -= timePassedSinceLastUpdate;
		
		while(msBeforeNextFrame <= 0) {
			moveToNextFrame();
			msBeforeNextFrame += msBetweenFrames;
			//System.out.println(msBeforeNextFrame);
		}
	}
	
	private void moveToNextFrame() {

		//increase the frame counter, and loop to the beginning if this is a looping animation
		int nextFrame = getCurrentFrameIndex() + 1;
		if(nextFrame >= getTotalNumberOfFrames() && isLoop()) {
			nextFrame = 0;
		}
		setCurrentFrameIndex(nextFrame);
	}

	public void draw(Graphics g) {
		drawCorrectFrameToScreen(g);
	}

	//this method calculates where on the sprite sheet the source rectangle is for the current frame of the animation
	//then draws that frame centered on the sprite's position
	private void drawCorrectFrameToScreen(Graphics g) {
		
		int frameRow = getCurrentFrameIndex() / getNumberOfColumnsInSpriteSheet();		//dividing the counter by number of columns will get us the current row 
		int frameColumn = getCurrentFrameIndex() % getNumberOfColumnsInSpriteSheet();	//using the modulo operator (returns remainder after integer division) gives us the current column
		//get half the width and height of the frame, to be able to draw the current tile centered on the sprite's position
		int halfWidth = getTileWidth()/2;												 
		int halfHeight = getTileHeight()/2;
		
		//we find top left and bottom right corner of the destination rectangle we want to draw to on screen (on the JPanel)
		 Point drawDestinationTopLeftCorner = new Point(getPosition().x - halfWidth, getPosition().y - halfHeight);
		 Point drawDestinationBottomRightCorner = new Point(drawDestinationTopLeftCorner.x + getTileWidth(), drawDestinationTopLeftCorner.y + getTileHeight());

		//we find top left and bottom right corner of the source rectangle we want to get a bush image from (on the sprite sheet)
		Point imageSourceTopLeftCorner = new Point(frameColumn * getTileWidth(), frameRow * getTileHeight());
		Point imageSourceBottomRightCorner = new Point(imageSourceTopLeftCorner.x + getTileWidth(), imageSourceTopLeftCorner.y + getTileHeight());

		//we draw from the source rectangle in the sprite sheet to the destination rectangle on screen 
		g.drawImage(getSpriteSheet(), 	drawDestinationTopLeftCorner.x, drawDestinationTopLeftCorner.y,
										drawDestinationBottomRightCorner.x, drawDestinationBottomRightCorner.y,		
										imageSourceTopLeftCorner.x, imageSourceTopLeftCorner.y, 
										imageSourceBottomRightCorner.x,	imageSourceBottomRightCorner.y, null);
	}
}