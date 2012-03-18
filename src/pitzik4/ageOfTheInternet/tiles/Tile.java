package pitzik4.ageOfTheInternet.tiles;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import pitzik4.ageOfTheInternet.Game;
import pitzik4.ageOfTheInternet.Tickable;
import pitzik4.ageOfTheInternet.graphics.Renderable;

public abstract class Tile implements Renderable, Tickable {
	public Renderable sprite;
	public int x, y;
	
	public static Tile getNewTile(int id, int x, int y, Tile[] neighbors, Game owner) {
		int[] ints = new int[neighbors.length];
		for(int i = 0; i < neighbors.length; i++) {
			ints[i] = intFromTile(neighbors[i]);
		}
		return getNewTile(id, x, y, ints, owner);
	}
	public static int intFromTile(Tile t) {
		if(t instanceof ComputerTile) {
			return 0x0000FF;
		}
		if(t instanceof HomeTile) {
			return 0x00FFFF;
		}
		if(t instanceof ConnectionTile) {
			return 0x00FF00;
		}
		if(t instanceof EndTile) {
			return 0xFFFF00;
		}
		if(t instanceof CorporationTile) {
			return 0xFF00FF;
		}
		if(t instanceof ChurchTile) {
			return 0x808080;
		}
		if(t instanceof HackerTile) {
			return 0x008000;
		}
		if(t instanceof BrokenConnectionTile) {
			return 0x008040;
		}
		return 0;
	}
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
		if(id == 0x008040) {
			return new BrokenConnectionTile(x, y, neighbors, (byte) Math.round((Math.random() * 8) + 1));
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
