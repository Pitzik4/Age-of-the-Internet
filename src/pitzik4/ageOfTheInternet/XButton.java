package pitzik4.ageOfTheInternet;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import pitzik4.ageOfTheInternet.graphics.Renderable;
import pitzik4.ageOfTheInternet.graphics.Screen;
import pitzik4.ageOfTheInternet.graphics.Sprite;

public class XButton implements Renderable, Tickable {
	private Game owner;
	private Sprite sprite;
	private int x=0, y=0;
	public boolean isClicked = false;
	public boolean isScrolled = false;
	public static final int BUTTON_SPRITE = 260;
	public static final int BU_SI = 8;
	
	public XButton(Game owner, int x, int y) {
		this.owner = owner;
		this.x = x;
		this.y = y;
		sprite = new Sprite(BUTTON_SPRITE, x, y, Screen.spritesheet, BU_SI, BU_SI);
	}

	@Override
	public BufferedImage draw() {
		BufferedImage out = new BufferedImage(BU_SI, BU_SI, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = out.createGraphics();
		drawOn(g, x, y);
		g.dispose();
		return out;
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
		if(owner.mouseInsideOf(x, y, BU_SI, BU_SI)) {
			beScrolled(true);
			if(owner.mouseDown) {
				beClicked(true);
			} else {
				beClicked(false);
			}
		} else {
			beClicked(false);
			beScrolled(false);
		}
	}
	public void beScrolled(boolean newScrolled) {
		if(isScrolled != newScrolled) {
			isScrolled = newScrolled;
			if(isScrolled) {
				sprite = new Sprite(BUTTON_SPRITE+1, x, y, Screen.spritesheet, BU_SI, BU_SI);
			} else {
				sprite = new Sprite(BUTTON_SPRITE, x, y, Screen.spritesheet, BU_SI, BU_SI);
			}
		}
	}
	public void beClicked(boolean newClicked) {
		if(isClicked != newClicked) {
			isClicked = newClicked;
			if(isClicked) {
				sprite = new Sprite(BUTTON_SPRITE+2, x, y, Screen.spritesheet, BU_SI, BU_SI);
			} else {
				sprite = new Sprite(BUTTON_SPRITE+1, x, y, Screen.spritesheet, BU_SI, BU_SI);
			}
		}
	}

}
