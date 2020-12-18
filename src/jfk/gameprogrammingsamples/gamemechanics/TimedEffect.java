package jfk.gameprogrammingsamples.gamemechanics;

import java.awt.*;

//A class which stores attack- and defense bonuses with a limited lifespan

public class TimedEffect {

	private Image image; 			// the image for the effect
	private float msLeftOfEffect; 	// how long is left of this effect
	private int attackBonus, defenseBonus;	//the bonuses

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public boolean isDone() {
		return getMsLeftOfEffect() <= 0;
	}

	public float getMsLeftOfEffect() {
		return msLeftOfEffect;
	}

	public void setMsLeftOfEffect(float msLeftOfEffect) {
		this.msLeftOfEffect = msLeftOfEffect;
	}

	public int getDefenseBonus() {
		return defenseBonus;
	}

	public void setDefenseBonus(int defenseBonus) {
		if (defenseBonus > 0) {
			this.defenseBonus = defenseBonus;
		}
	}

	public int getAttackBonus() {
		return attackBonus;
	}

	public void setAttackBonus(int attackBonus) {
		if (attackBonus > 0) {
			this.attackBonus = attackBonus;
		}
	}

	public TimedEffect(Image spriteSheet, int attackBonus, int defenseBonus, float msLeftOfEffect) {

		setImage(spriteSheet);
		setAttackBonus(attackBonus);
		setDefenseBonus(defenseBonus);
		setMsLeftOfEffect(msLeftOfEffect);
	}

	public void update(float timePassedSinceLastUpdate) {
		setMsLeftOfEffect(getMsLeftOfEffect() - timePassedSinceLastUpdate);
	}

	public void draw(Graphics g, Point position) {

		int left = position.x - getImage().getWidth(null) / 2;
		int top = position.y - getImage().getHeight(null) / 2;

		g.drawImage(getImage(), left, top, null);
		g.setFont(TimedEffectsPanel.FONT);
		String text = "";
		if (getAttackBonus() > 0)
			text += "+" + getAttackBonus() + " attack ";
		if (getDefenseBonus() > 0)
			text += "+" + getDefenseBonus() + " defense";
		text += "(" + (int) Math.ceil(getMsLeftOfEffect() / 1000) + "s)";

		g.setColor(Color.black);	//draw shadow
		g.drawString(text, position.x + getImage().getWidth(null) - 2, position.y + 2);
		g.setColor(Color.white);	//and text
		g.drawString(text, position.x + getImage().getWidth(null), position.y);
	}
}