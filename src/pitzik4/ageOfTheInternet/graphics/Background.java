package pitzik4.ageOfTheInternet.graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import pitzik4.ageOfTheInternet.Tickable;

public class Background implements Renderable, Tickable {
	private int x=0, y=0;
	private int width=0;
	private Sprite[][] tiles;
	public static final byte MOVE_COUNTDOWN = 2;
	private byte moveCountdown = MOVE_COUNTDOWN;
	public static final int BG_SPRITE = 16;
	
	public Background(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		if(width%Sprite.SPRITE_WIDTH != 0) {
			throw new RuntimeException("Illegal width for Background: " + width);
		}
		if(height%Sprite.SPRITE_HEIGHT != 0) {
			throw new RuntimeException("Illegal height for Background: " + height);
		}
		tiles = new Sprite[height/Sprite.SPRITE_HEIGHT][width/Sprite.SPRITE_WIDTH+1];
		for(int i=0; i<width/Sprite.SPRITE_WIDTH+1; i++) {
			for(int j=0; j<height/Sprite.SPRITE_HEIGHT; j++) {
				tiles[j][i] = new Sprite(BG_SPRITE, x+(i-1)*Sprite.SPRITE_WIDTH, y+j*Sprite.SPRITE_HEIGHT, false);
			}
		}
	}

	@Override
	public BufferedImage draw() {
		// TODO Add code here
		return null;
	}

	@Override
	public void drawOn(Graphics2D g, int scrollx, int scrolly) {
		for(Sprite[] ss : tiles) {
			for(Sprite s : ss) {
				s.drawOn(g, 0, 0);
			}
		}
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
		for(Sprite[] ss : tiles) {
			for(Sprite s : ss) {
				s.goTo(s.getX()+dx, s.getY()+dy);
			}
		}
	}

	@Override
	public void tick() {
		moveCountdown--;
		if(moveCountdown==0) {
			moveCountdown = MOVE_COUNTDOWN;
			for(int i=0; i<tiles.length; i++) {
				for(Sprite s : tiles[i]) {
					if(i%2 == 0) {
						s.goTo(s.getX()+1, s.getY());
						if(s.getX() > x+width) {
							s.goTo(x-Sprite.SPRITE_WIDTH+1, s.getY());
						}
					} else {
						s.goTo(s.getX()-1, s.getY());
						if(s.getX() < x-Sprite.SPRITE_WIDTH) {
							s.goTo(x+width-1, s.getY());
						}
					}
				}
			}
		}
	}

}
