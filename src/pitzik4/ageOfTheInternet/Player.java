package pitzik4.ageOfTheInternet;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import pitzik4.ageOfTheInternet.graphics.Sprite;
import pitzik4.ageOfTheInternet.tiles.HackerTile;

public class Player implements RenderableTickable {
	private Sprite sprite;
	private int x=0, y=0;
	public boolean going=false;
	private Point[] path;
	private int progress = 0;
	
	public static final int stepSize() {
		return 3 + HackerTile.hackersOwned;
	}
	
	public Player(int x, int y, Point[] path) {
		this.x = x;
		this.y = y;
		this.path = path;
		sprite = new Sprite(80, x, y, false);
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
		if(going) {
			int nextX = path[progress].x;
			int nextY = path[progress].y;
			if(nextX > x) {
				goTo(x+stepSize(), y);
				if(nextX < x) {
					goTo(nextX, y);
				}
			} else if(nextX < x) {
				goTo(x-stepSize(), y);
				if(nextX > x) {
					goTo(nextX, y);
				}
			}
			if(nextY > y) {
				goTo(x, y+stepSize());
				if(nextY < y) {
					goTo(x, nextY);
				}
			} else if(nextY < y) {
				goTo(x, y-stepSize());
				if(nextY > y) {
					goTo(x, nextY);
				}
			}
			if(nextX == x && nextY == y) {
				progress++;
				if(progress >= path.length) {
					going = false;
				}
			}
		}
	}
	public void go() {
		going = true;
	}
	public void stop() {
		going = false;
	}

}
