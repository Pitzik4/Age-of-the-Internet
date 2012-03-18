package pitzik4.ageOfTheInternet.tiles;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import pitzik4.ageOfTheInternet.Game;
import pitzik4.ageOfTheInternet.Tickable;
import pitzik4.ageOfTheInternet.graphics.Renderable;

public abstract class Tile implements Renderable, Tickable {
	public Renderable sprite;
	public int x, y;
	
	public static Tile getNewTile(int id, int x, int y, int[] neighbors, Game owner) {
		id = id&0xFFFFFF;
		if(id == 0x0000FF) {
			return new ComputerTile(x, y, owner);
		}
		if(id == 0x00FFFF) {
			return new HomeTile(x, y, owner);
		}
		if(id == 0x00FF00) {
			return new ConnectionTile(x, y, neighbors);
		}
		if(id == 0xFFFF00) {
			return new EndTile(x, y, owner);
		}
		if(id == 0xFF00FF) {
			return new CorporationTile(x, y, owner);
		}
		if(id == 0x808080) {
			return new ChurchTile(x, y, owner);
		}
		if(id == 0x008000) {
			return new HackerTile(x, y, owner);
		}
		return null;
	}

	@Override
	public BufferedImage draw() {
		return sprite.draw();
	}

	@Override
	public void drawOn(Graphics2D g, int scrollx, int scrolly) {
		sprite.drawOn(g, scrollx, scrolly);
		furtherDraw(g, scrollx, scrolly);
	}
	public void furtherDraw(Graphics2D g, int scrollx, int scrolly) {
		
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
	public void beOwned() {
		
	}
	public void beEvil() {
		
	}
	public void startEvil() {
		
	}
	public void deEvil() {
		
	}
	public boolean isEvil() {
		return false;
	}
	public boolean canBeEvil() {
		return false;
	}
	public void unHack() {
		
	}
	public abstract int hackCost();

}
