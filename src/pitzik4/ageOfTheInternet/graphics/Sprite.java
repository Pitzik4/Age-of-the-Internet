package pitzik4.ageOfTheInternet.graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Sprite implements Renderable {
	private int x=0, y=0;
	private BufferedImage img;
	public static final int SPRITE_WIDTH = 16;
	public static final int SPRITE_HEIGHT = 16;
	public static final int LETTER_WIDTH = 8;
	public static final int LETTER_HEIGHT = 12;
	
	public Sprite(int which, int x, int y, BufferedImage spritesheet, int width, int height) {
		this.x = x;
		this.y = y;
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		g.drawImage(spritesheet, 0-((which*width)%spritesheet.getWidth()), 0-(((which)/(spritesheet.getWidth()/width))*height), null);
		g.dispose();
	}
	public Sprite(int which, int x, int y, boolean letter) {
		this(which, x, y, (letter?Screen.font:Screen.spritesheet), letter?LETTER_WIDTH:SPRITE_WIDTH, letter?LETTER_HEIGHT:SPRITE_HEIGHT);
	}
	public Sprite(int which) {
		this(which, 0, 0, false);
	}

	@Override
	public BufferedImage draw() {
		return img;
	}

	@Override
	public void drawOn(Graphics2D g, int scrollx, int scrolly) {
		g.drawImage(img, x-scrollx, y-scrolly, null);
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

}
