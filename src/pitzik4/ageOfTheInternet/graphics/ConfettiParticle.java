package pitzik4.ageOfTheInternet.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import pitzik4.ageOfTheInternet.RenderableTickable;

public class ConfettiParticle implements RenderableTickable {
	private Color color = Color.WHITE;
	private boolean goingRight = false;
	private boolean goingUp = false;
	private boolean moving = false;
	private int x, y;
	private static final Random rnd = new Random();
	public static final Color[] colorfulColors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.ORANGE, Color.MAGENTA, Color.CYAN};
	public static final int INVERSE_DIR_SWITCH_PROB = 8;
	public static final int INVERSE_GO_UP_PROB = 8;
	
	public ConfettiParticle(Color color, int x, int y, boolean goingRight) {
		this.color = color;
		this.x = x;
		this.y = y;
		this.goingRight = goingRight;
	}
	public ConfettiParticle(Color color, int x, int y) {
		this(color, x, y, rnd.nextBoolean());
	}
	public ConfettiParticle(int x, int y, boolean goingRight) {
		this(colorfulColors[rnd.nextInt(colorfulColors.length - 1)], x, y, goingRight);
	}
	public ConfettiParticle(int x, int y) {
		this(x, y, rnd.nextBoolean());
	}

	@Override
	public BufferedImage draw() {
		BufferedImage out = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = out.createGraphics();
		g.setColor(color);
		g.fillRect(0, 0, 1, 1);
		g.dispose();
		return out;
	}

	@Override
	public void drawOn(Graphics2D g, int scrollx, int scrolly) {
		g.setColor(color);
		g.fillRect(x-scrollx, y-scrolly, 1, 1);
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
	}

	@Override
	public void tick() {
		if(moving) {
			if(goingRight) {
				goTo(x+1, y);
			} else {
				goTo(x-1, y);
			}
			if(goingUp) {
				goingUp = false;
				goTo(x, y-1);
			} else {
				goTo(x, y+1);
			}
			if(rnd.nextInt(INVERSE_DIR_SWITCH_PROB) == 0) {
				goingRight = !goingRight;
			}
			if(rnd.nextInt(INVERSE_GO_UP_PROB) == 0) {
				goingUp = true;
			}
		}
		moving = !moving;
	}

}
