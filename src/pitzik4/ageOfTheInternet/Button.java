package pitzik4.ageOfTheInternet;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import pitzik4.ageOfTheInternet.graphics.Renderable;
import pitzik4.ageOfTheInternet.graphics.RenderableString;
import pitzik4.ageOfTheInternet.graphics.Sprite;

public class Button implements Renderable, Tickable {
	private Game owner;
	private Sprite[] sprites;
	private RenderableString str;
	private int x=0, y=0;
	private int width;
	public boolean isClicked = false;
	public boolean isScrolled = false;
	public boolean nowClicked = false;
	public static final int BUTTON_SPRITE = 50;
	
	public Button(Game owner, int x, int y, int width, String text) {
		this.owner = owner;
		this.x = x;
		this.y = y;
		this.width = width;
		sprites = new Sprite[width/Sprite.SPRITE_WIDTH];
		for(int i=0; i<width/Sprite.SPRITE_WIDTH; i++) {
			if(i==0) {
				sprites[i] = new Sprite(BUTTON_SPRITE, x, y, false);
			} else if(i==width/Sprite.SPRITE_WIDTH-1) {
				sprites[i] = new Sprite(BUTTON_SPRITE+2, x+i*Sprite.SPRITE_WIDTH, y, false);
			} else {
				sprites[i] = new Sprite(BUTTON_SPRITE+1, x+i*Sprite.SPRITE_WIDTH, y, false);
			}
		}
		str = new RenderableString(text, 0, 0);
		int strWidth = str.width;
		str.goTo(x+(width-strWidth)/2, y+2);
	}

	@Override
	public BufferedImage draw() {
		BufferedImage out = new BufferedImage(width, Sprite.SPRITE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = out.createGraphics();
		drawOn(g, x, y);
		g.dispose();
		return out;
	}

	@Override
	public void drawOn(Graphics2D g, int scrollx, int scrolly) {
		for(Sprite s : sprites) {
			s.drawOn(g, scrollx, scrolly);
		}
		str.drawOn(g, scrollx, scrolly);
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
		int dx = x-this.x;
		int dy = y-this.y;
		this.x = x;
		this.y = y;
		for(Sprite s : sprites) {
			s.goTo(s.getX()+dx, s.getY()+dy);
		}
		str.goTo(str.getX()+dx, str.getY()+dy);
	}

	@Override
	public void tick() {
		if(owner.mouseInsideOf(x, y, width, Sprite.SPRITE_HEIGHT)) {
			beScrolled(true);
			boolean wasClicked = isClicked;
			if(owner.mouseDown) {
				beClicked(true);
			} else {
				beClicked(false);
			}
			if(wasClicked && !isClicked) {
				nowClicked = true;
			} else {
				nowClicked = false;
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
				for(int i=0; i<sprites.length; i++) {
					if(i==0) {
						sprites[i] = new Sprite(BUTTON_SPRITE+3, x, y, false);
					} else if(i==width/Sprite.SPRITE_WIDTH-1) {
						sprites[i] = new Sprite(BUTTON_SPRITE+5, x+i*Sprite.SPRITE_WIDTH, y, false);
					} else {
						sprites[i] = new Sprite(BUTTON_SPRITE+4, x+i*Sprite.SPRITE_WIDTH, y, false);
					}
				}
			} else {
				for(int i=0; i<sprites.length; i++) {
					if(i==0) {
						sprites[i] = new Sprite(BUTTON_SPRITE, x, y, false);
					} else if(i==width/Sprite.SPRITE_WIDTH-1) {
						sprites[i] = new Sprite(BUTTON_SPRITE+2, x+i*Sprite.SPRITE_WIDTH, y, false);
					} else {
						sprites[i] = new Sprite(BUTTON_SPRITE+1, x+i*Sprite.SPRITE_WIDTH, y, false);
					}
				}
			}
		}
	}
	public void beClicked(boolean newClicked) {
		if(isClicked != newClicked) {
			isClicked = newClicked;
			if(isClicked) {
				for(int i=0; i<sprites.length; i++) {
					if(i==0) {
						sprites[i] = new Sprite(BUTTON_SPRITE+6, x, y, false);
					} else if(i==width/Sprite.SPRITE_WIDTH-1) {
						sprites[i] = new Sprite(BUTTON_SPRITE+8, x+i*Sprite.SPRITE_WIDTH, y, false);
					} else {
						sprites[i] = new Sprite(BUTTON_SPRITE+7, x+i*Sprite.SPRITE_WIDTH, y, false);
					}
				}
			} else {
				for(int i=0; i<sprites.length; i++) {
					if(i==0) {
						sprites[i] = new Sprite(BUTTON_SPRITE+3, x, y, false);
					} else if(i==width/Sprite.SPRITE_WIDTH-1) {
						sprites[i] = new Sprite(BUTTON_SPRITE+5, x+i*Sprite.SPRITE_WIDTH, y, false);
					} else {
						sprites[i] = new Sprite(BUTTON_SPRITE+4, x+i*Sprite.SPRITE_WIDTH, y, false);
					}
				}
			}
		}
	}

}
