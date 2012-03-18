package pitzik4.ageOfTheInternet.tiles;

import java.util.Set;

import pitzik4.ageOfTheInternet.graphics.Sprite;

public class BrokenConnectionTile extends Tile {
	public static final Set<Integer> CONNECTABLE_TILES = ConnectionTile.connectableTiles();
	public static final int[] POSITIONAL_SPRITES = {13, 2, 1, 3, 2, 2, 4, 17, 1, 6, 1, 18, 5, 19, 20, 21};
	private byte neededComputers = 9;
	private byte ownedComputers = 0;
	private int position = 0;
	
	public BrokenConnectionTile(int x, int y, int[] neighbors, byte neededComputers) {
		this.x = x;
		this.y = y;
		for(int i=0; i<neighbors.length; i++) {
			neighbors[i] = neighbors[i] & 0xFFFFFF;
		}
		boolean[] neighbools = new boolean[4];
		for(int i=0; i<4; i++) {
			neighbools[i] = (CONNECTABLE_TILES.contains(neighbors[i])) || ((neighbors[i] & 0xFFFF) == 0x8040);
		}
		if(neighbools[0])
			position += 1;
		if(neighbools[1])
			position += 2;
		if(neighbools[2])
			position += 4;
		if(neighbools[3])
			position += 8;
		sprite = new Sprite(POSITIONAL_SPRITES[position]+191, x, y, false);
		((Sprite) sprite).mergeSprite(new Sprite(207-neededComputers));
		this.neededComputers = neededComputers;
	}

	@Override
	public void tick() {
		
	}
	@Override
	public int hackCost() {
		return 0;
	}
	public boolean notifyOwnedChange(int newOwned) {
		if(newOwned != ownedComputers) {
			ownedComputers = (byte) (newOwned % 256);
			sprite = new Sprite(POSITIONAL_SPRITES[position]+191, x, y, false);
			((Sprite) sprite).mergeSprite(new Sprite(207-(neededComputers-ownedComputers)));
		}
		if(ownedComputers >= neededComputers) {
			sprite = new Sprite(POSITIONAL_SPRITES[position], x, y, false);
			((Sprite) sprite).mergeSprite(new Sprite(207-((neededComputers-ownedComputers) > 0 ? neededComputers-ownedComputers : 0)));
		}
		return ownedComputers >= neededComputers;
	}
	public boolean usable() {
		return ownedComputers >= neededComputers;
	}

}
