package pitzik4.ageOfTheInternet.graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import pitzik4.ageOfTheInternet.RenderableTickable;

public class MoneyParticle implements RenderableTickable {
	private int lifetime = 0;
	private int x=0;
	private int y=0;
	private Sprite sprite;
	public boolean dead = false;
	public static final int LIFE_SPAN = 20;
	
	public MoneyParticle(int x, int y) {
		this.x = x;
		this.y = y;
		sprite = new Sprite(82, x, y, false);
	}

	@Override
	public BufferedImage draw() {
		return sprite.draw();
	}

	@Override
	public void drawOn(Graphics2D g, int scrollx, int scrolly) {
		sprite.drawOn(g, scrollx, scrolly);
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public int getXOffset() {
		return 0;
	}

	@Override
	public int getYOffset() {
		return 0;
	}

	@Override
	public void goTo(int x, int y) {
		this.x = x;
		this.y = y;
		sprite.goTo(x, y);
	}

	@Override
	public void tick() {
		lifetime++;
		if(lifetime > LIFE_SPAN) {
			dead = true;
		}
		goTo(x, y-1);
	}

}
