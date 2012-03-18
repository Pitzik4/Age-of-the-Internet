package pitzik4.ageOfTheInternet.tiles;

import java.util.HashSet;
import java.util.Set;

import pitzik4.ageOfTheInternet.graphics.Sprite;

public class BrokenConnectionTile extends Tile {
	public static final Set<Integer> CONNECTABLE_TILES = connectableTiles();
	public static final int[] POSITIONAL_SPRITES = {13, 2, 1, 3, 2, 2, 4, 17, 1, 6, 1, 18, 5, 19, 20, 21};
	private byte neededComputers = 9;
	
	private static Set<Integer> connectableTiles() {
		Set<Integer> out = new HashSet<Integer>();
		out.add(0x0000FF);
		out.add(0x00FFFF);
		out.add(0x00FF00);
		out.add(0xFFFF00);
		out.add(0xFF00FF);
		out.add(0x808080);
		out.add(0x008000);
		out.add(1);
		return out;
	}
	
	public BrokenConnectionTile(int x, int y, int[] neighbors, byte neededComputers) {
		this.x = x;
		this.y = y;
		for(int i=0; i<neighbors.length; i++) {
			neighbors[i] = neighbors[i] & 0xFFFFFF;
		}
		boolean[] neighbools = new boolean[4];
		for(int i=0; i<4; i++) {
			neighbools[i] = CONNECTABLE_TILES.contains(neighbors[i]);
		}
		int position = 0;
		if(neighbools[0])
			position += 1;
		if(neighbools[1])
			position += 2;
		if(neighbools[2])
			position += 4;
		if(neighbools[3])
			position += 8;
		sprite = new Sprite(POSITIONAL_SPRITES[position]+191, x, y, false);
		this.neededComputers = neededComputers;
	}

	@Override
	public void tick() {
		
	}
	@Override
	public int hackCost() {
		return 0;
	}

}
