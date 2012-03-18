package pitzik4.ageOfTheInternet.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import pitzik4.ageOfTheInternet.RenderableTickable;

public class ExplosionParticle implements RenderableTickable {
	private int lifetime = 0;
	private double direction;
	private double x=0;
	private double y=0;
	public boolean dead = false;
	public static final int LIFE_SPAN = 20;
	
	public ExplosionParticle(double x, double y, double direction) {
		this.x = x;
		this.y = y;
		this.direction = direction;
	}

	@Override
	public BufferedImage draw() {
		BufferedImage out = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = out.createGraphics();
		drawOn(g, (int) Math.round(x), (int) Math.round(y));
		g.dispose();
		return out;
	}

	@Override
	public void drawOn(Graphics2D g, int scrollx, int scrolly) {
		g.setPaint(new Color(0x60BFBF));
		g.fillRect((int) Math.round(x-scrollx), (int) Math.round(y-scrolly), 2, 2);
	}

	@Override
	public int getX() {
		return (int) Math.round(x);
	}

	@Override
	public int getY() {
		return (int) Math.round(y);
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
	}

	@Override
	public void tick() {
		lifetime++;
		if(lifetime > LIFE_SPAN) {
			dead = true;
		}
		x += Math.cos(direction);
		y += Math.sin(direction);
	}

}
