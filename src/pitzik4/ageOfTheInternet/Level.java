package pitzik4.ageOfTheInternet;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;

import pitzik4.ageOfTheInternet.graphics.ExplosionParticle;
import pitzik4.ageOfTheInternet.graphics.RenderableString;
import pitzik4.ageOfTheInternet.graphics.Sprite;
import pitzik4.ageOfTheInternet.tiles.ConnectionTile;
import pitzik4.ageOfTheInternet.tiles.HackerTile;
import pitzik4.ageOfTheInternet.tiles.HomeTile;
import pitzik4.ageOfTheInternet.tiles.Tile;

public class Level implements Stage {
	public Tile[][] tiles;
	public int width;
	public int height;
	public int x=0;
	public int y=0;
	public Game owner;
	public Map<Tile, Player> players = new HashMap<Tile, Player>();
	public Map<Tile, Player> oldPlayers = new HashMap<Tile, Player>();
	public Set<ExplosionParticle> particles = new HashSet<ExplosionParticle>();
	public Map<Player, Tile> goingPlayers = new HashMap<Player, Tile>();
	public Map<Tile, Hacker> hackers = new HashMap<Tile, Hacker>();
	public Map<Hacker, Tile> goingHackers = new HashMap<Hacker, Tile>();
	private boolean done = false;
	private boolean closing = false;
	private int ram = 0;
	private RenderableString ramRender;
	private boolean resetting = false;
	private boolean lost = false;
	private boolean almostResetting = false;
	private LoseScreen loseScreen = null;
	private int money = 0;
	private RenderableString moneyRender;
	private int ramCost = 5;
	private static final int RAM_UPGRADE_AMT = 10;
	private static final Random rnd = new Random();
	public static final int[] levelRAMs = {20, 30, 0, 15, 10, 25};
	public static final int[] levelMoneys = {0, 0, 26, 0, 0, 0};
	
	public Level(BufferedImage img, Game owner, int x, int y, int ram, int money) {
		this(img, owner, ram, money);
		goTo(x, y);
	}
	public Level(String name, Game owner, int x, int y, int ram, int money) throws IOException {
		this(name, owner, ram, money);
		goTo(x, y);
	}
	public Level(int number, Game owner, int x, int y) throws IOException {
		this(number, owner);
		goTo(x, y);
	}
	public Level(BufferedImage img, Game owner, int ram, int money) {
		this.owner = owner;
		setRAM(ram);
		setMoney(money);
		width = img.getWidth();
		height = img.getHeight();
		tiles = new Tile[width][height];
		int[] pixels = img.getRGB(0, 0, width, height, null, 0, width);
		for(int i=0; i<width; i++) {
			for(int j=0; j<height; j++) {
				int[] neighbors = new int[4];
				if(j==0) {
					neighbors[0] = 0;
				} else {
					neighbors[0] = pixels[((j-1)*width)+i];
				}
				if(j==height-1) {
					neighbors[2] = 0;
				} else {
					neighbors[2] = pixels[((j+1)*width)+i];
				}
				if(i==0) {
					neighbors[3] = 0;
				} else {
					neighbors[3] = pixels[(j*width)+i-1];
				}
				if(i==width-1) {
					neighbors[1] = 0;
				} else {
					neighbors[1] = pixels[(j*width)+i+1];
				}
				tiles[i][j] = Tile.getNewTile(pixels[(j*width)+i], i*Sprite.SPRITE_WIDTH, j*Sprite.SPRITE_HEIGHT, neighbors, owner);
				if(tiles[i][j] instanceof HomeTile) {
					Point[] path = {new Point(tiles[i][j].getX(), tiles[i][j].getY())};
					addPlayer(tiles[i][j], new Player(tiles[i][j].getX(), tiles[i][j].getY(), path));
				}
			}
		}
	}
	public Level(String name, Game owner, int ram, int money) throws IOException {
		this(ImageIO.read(Game.class.getResourceAsStream("/levels/"+name+".gif")), owner, ram, money);
	}
	public Level(int number, Game owner) throws IOException {
		this("lvl"+number, owner, levelRAMs[number-1], levelMoneys[number-1]);
	}
	public Level(int number) throws IOException {
		this(number, Game.game);
	}
	public Level(String name, int ram, int money) throws IOException {
		this(name, Game.game, ram, money);
	}
	public Level(BufferedImage img, int ram, int money) {
		this(img, Game.game, ram, money);
	}

	@Override
	public void tick() {
		for(Tile[] tt : tiles) {
			for(Tile t : tt) {
				if(t != null) {
					t.tick();
				}
			}
		}
		for(Player p : goingPlayers.keySet()) {
			p.tick();
		}
		for(Hacker p : goingHackers.keySet()) {
			p.tick();
		}
		for(Iterator<ExplosionParticle> it = particles.iterator(); it.hasNext();) {
			ExplosionParticle ep = it.next();
			ep.tick();
			if(ep.dead) {
				it.remove();
			}
		}
		if(owner.screen.getFade() > 0 && !done && !almostResetting) {
			if(owner.screen.getFade() < 10) {
				owner.screen.fadeTo(0);
			} else {
				owner.screen.fadeTo(owner.screen.getFade()-10);
			}
		}
		for(Iterator<Map.Entry<Player, Tile>> it = goingPlayers.entrySet().iterator(); it.hasNext();) {
			Map.Entry<Player, Tile> ent = it.next();
			if(!ent.getKey().going) {
				if(isEvil(ent.getValue())) {
					deevil(ent.getValue());
				}
				if(!(ent.getValue() instanceof HackerTile) || isOwned(ent.getValue())) {
					ent.getValue().beOwned();
				}
				if(ent.getKey() != oldPlayers.get(ent.getValue())) {
					oldPlayers.remove(ent.getValue());
				}
				it.remove();
			}
		}
		for(Iterator<Map.Entry<Hacker, Tile>> it = goingHackers.entrySet().iterator(); it.hasNext();) {
			Map.Entry<Hacker, Tile> ent = it.next();
			if(!ent.getKey().going) {
				unhack(ent.getValue());
				ent.getValue().beEvil();
				it.remove();
			}
		}
		if(done || almostResetting) {
			if(owner.screen.getFade() < 255) {
				if(owner.screen.getFade() > 245) {
					owner.screen.fadeTo(255);
				} else {
					owner.screen.fadeTo(owner.screen.getFade()+10);
				}
			} else {
				if(done) {
					closing = true;
				} else {
					resetting = true;
				}
			}
		}
		if(loseScreen != null) {
			loseScreen.tick();
			if(loseScreen.retry.nowClicked) {
				almostResetting = true;
			}
		}
	}
	private boolean connected(Player begin) {
		List<Player> l = new ArrayList<Player>(Collections.singleton(begin));
		List<Player> k = new ArrayList<Player>(l);
		while(!k.isEmpty()) {
			Player y = k.get(0);
			k.remove(0);
			Tile t = null;
			for(Map.Entry<Tile, Player> ent : players.entrySet()) {
				if(ent.getValue() == y) {
					t = ent.getKey();
					break;
				}
			}
			Set<Tile> zz = allCanBeHackedBy(t);
			for(Tile z : zz) {
				if(!l.contains(players.get(z))) {
					l.add(players.get(z));
					k.add(players.get(z));
				}
			}
		}
		return l.size() >= players.size();
	}
	@Override
	public BufferedImage draw() {
		BufferedImage out = new BufferedImage(width*Sprite.SPRITE_WIDTH, height*Sprite.SPRITE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = out.createGraphics();
		drawOn(g, x, y);
		g.dispose();
		return out;
	}

	@Override
	public void drawOn(Graphics2D g, int scrollx, int scrolly) {
		for(Tile[] tt : tiles) {
			for(Tile t : tt) {
				if(t != null) {
					t.drawOn(g, scrollx, scrolly);
				}
			}
		}
		for(Player p : players.values()) {
			p.drawOn(g, scrollx, scrolly);
		}
		for(Player p : oldPlayers.values()) {
			p.drawOn(g, scrollx, scrolly);
		}
		for(Player p : goingPlayers.keySet()) {
			p.drawOn(g, scrollx, scrolly);
		}
		for(Hacker h : hackers.values()) {
			h.drawOn(g, scrollx, scrolly);
		}
		for(Hacker h : goingHackers.keySet()) {
			h.drawOn(g, scrollx, scrolly);
		}
		for(ExplosionParticle ep : particles) {
			ep.drawOn(g, scrollx, scrolly);
		}
		ramRender.drawOn(g, 0, 0);
		moneyRender.drawOn(g, 0, 0);
		if(loseScreen != null) {
			loseScreen.drawOn(g, 0, 0);
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
		for(Tile[] tt : tiles) {
			for(Tile t : tt) {
				t.goTo(t.getX()+dx, t.getY()+dy);
			}
		}
	}
	@Override
	public boolean isClosing() {
		return closing;
	}
	@Override
	public int getWidth() {
		return width*Sprite.SPRITE_WIDTH;
	}
	@Override
	public int getHeight() {
		return height*Sprite.SPRITE_HEIGHT;
	}
	@Override
	public Dimension getSize() {
		return new Dimension(getWidth(), getHeight());
	}
	public boolean isOwned(int x, int y) {
		x /= Sprite.SPRITE_WIDTH;
		y /= Sprite.SPRITE_HEIGHT;
		return isOwned(tiles[x][y]);
	}
	public boolean isOwned(Tile tile) {
		if(tile instanceof HackerTile) {
			return ((HackerTile) tile).isOwned();
		}
		return players.containsKey(tile);
	}
	public Tile[] neighbors(Tile t) {
		int tileX = t.getX()/Sprite.SPRITE_WIDTH;
		int tileY = t.getY()/Sprite.SPRITE_HEIGHT;
		Tile[] neighbors = new Tile[4];
		if(tileY==0) {
			neighbors[0] = null;
		} else {
			neighbors[0] = tiles[tileX][tileY-1];
		}
		if(tileY==height-1) {
			neighbors[2] = null;
		} else {
			neighbors[2] = tiles[tileX][tileY+1];
		}
		if(tileX==0) {
			neighbors[3] = null;
		} else {
			neighbors[3] = tiles[tileX-1][tileY];
		}
		if(tileX==width-1) {
			neighbors[1] = null;
		} else {
			neighbors[1] = tiles[tileX+1][tileY];
		}
		return neighbors;
	}
	public Tile[] connectionNeighbors(Tile t, Tile couldBe) {
		Tile[] out = neighbors(t);
		for(int i=0; i<out.length; i++) {
			if(!(out[i] instanceof ConnectionTile) && !(out[i] == couldBe)) {
				out[i] = null;
			}
		}
		return out;
	}
	/*public Point[] getPlayerPath(int x, int y) {
		Set<Point> endPoints = new HashSet<Point>();
		endPoints.add(new Point(x, y));
		Set<Point> out = new HashSet<Point>();
		Set<Point> done = new HashSet<Point>();
		while(endPoints.size() > 0) {
			
		}
		return out;
	}
	public boolean isHackable(int x, int y) {
		
	}*/
    private List<Tile> findConnectionStep(List<Tile> visited, Tile endPoint) {
        Tile[] nodes = connectionNeighbors(visited.get(visited.size()-1), endPoint);
        boolean done = false;
        for (Tile node : nodes) {
        	if(node != null) {
	            if (visited.contains(node)) {
	                continue;
	            }
	            if (node.equals(endPoint)) {
	                visited.add(node);
	                done = true;
	                break;
            	}
        	}
        }
        if(!done) {
        	for (Tile node : nodes) {
        		if(node == null) continue;
        		if (visited.contains(node) || node.equals(endPoint)) {
        			continue;
        		}
        		visited.add(node);
        		List<Tile> outMaybe = findConnectionStep(visited, endPoint);
        		if(outMaybe != null) {
        			return outMaybe;
        		}
        		visited.remove(visited.size()-1);
        	}
        } else {
        	return visited;
        }
        return null;
    }
    public Tile canBeHackedBy(Tile tile) {
    	for(Tile[] tt : tiles) {
    		for(Tile t : tt) {
    			if(!isHackerOwned(t)) continue;
    			if(findConnectionStep(new ArrayList<Tile>(Collections.singleton(t)), tile) != null) return t;
    		}
    	}
    	return null;
    }
    public Tile canBeEviledBy(Tile tile) {
    	for(Tile[] tt : tiles) {
    		for(Tile t : tt) {
    			if(!isEvil(t)) continue;
    			if(findConnectionStep(new ArrayList<Tile>(Collections.singleton(t)), tile) != null) return t;
    		}
    	}
    	return null;
    }
    public Set<Tile> allCanBeHackedBy(Tile tile) {
    	Set<Tile> out = new HashSet<Tile>();
    	for(Tile[] tt : tiles) {
    		for(Tile t : tt) {
    			if(!isHackerOwned(t)) continue;
    			if(findConnectionStep(new ArrayList<Tile>(Collections.singleton(t)), tile) != null) out.add(t);
    		}
    	}
    	return out;
    }
    public Set<Tile> allCanBeEviledBy(Tile tile) {
    	Set<Tile> out = new HashSet<Tile>();
    	for(Tile[] tt : tiles) {
    		for(Tile t : tt) {
    			if(!isEvil(t)) continue;
    			if(findConnectionStep(new ArrayList<Tile>(Collections.singleton(t)), tile) != null) out.add(t);
    		}
    	}
    	return out;
    }
    public Set<Tile> whatCanBeEviledBy(Tile tile) {
    	Set<Tile> out = new HashSet<Tile>();
    	for(Tile[] tt : tiles) {
    		for(Tile t : tt) {
    			if(allCanBeEviledBy(t).contains(tile) && !isEvil(t) && t.canBeEvil()) {
    				out.add(t);
    			}
    		}
    	}
    	return out;
    }
    public Set<Tile> whatCanBeEviled() {
    	Set<Tile> out = new HashSet<Tile>();
    	for(Tile[] tt : tiles) {
    		for(Tile t : tt) {
    			if(canBeEviledBy(t) != null && !isEvil(t)) {
    				out.add(t);
    			}
    		}
    	}
    	return out;
    }
    public void hack(Tile tile) {
    	if(tile instanceof HackerTile) {
    		hackHacker((HackerTile) tile);
    	} else {
    		hack2(tile);
    	}
    }
    public void hack2(Tile tile) {
    	if(isOwned(tile))
    		return;
    	if(tile.hackCost() > ram)
    		return;
    	Tile source = canBeHackedBy(tile);
    	List<Tile> tilePath = findConnectionStep(new ArrayList<Tile>(Collections.singleton(source)), tile);
    	Point[] path = new Point[tilePath.size()];
    	for(int i=0; i<tilePath.size(); i++) {
    		Tile t = tilePath.get(i);
    		path[i] = new Point(t.getX(), t.getY());
    	}
    	Player player = new Player(source.getX(), source.getY(), path);
    	addPlayer(tile, player);
    	player.go();
    	goingPlayers.put(player, tile);
    	setRAM(ram - tile.hackCost());
    }
    public void evil(Tile tile) {
    	if(isEvil(tile))
    		return;
    	Tile source = canBeEviledBy(tile);
    	List<Tile> tilePath = findConnectionStep(new ArrayList<Tile>(Collections.singleton(source)), tile);
    	Point[] path = new Point[tilePath.size()];
    	for(int i=0; i<tilePath.size(); i++) {
    		Tile t = tilePath.get(i);
    		path[i] = new Point(t.getX(), t.getY());
    	}
    	Hacker player = new Hacker(source.getX(), source.getY(), path);
    	addHacker(tile, player);
    	player.go();
    	goingHackers.put(player, tile);
    	tile.startEvil();
    }
    public void unhack(Tile tile) {
    	if(tile instanceof HackerTile) {
    		if(!((HackerTile) tile).initiatedUnhacking) {
    			tile.unHack();
    		}
    	} else {
    		tile.unHack();
    	}
    	Player player = players.get(tile);
    	if(player != null) {
    		boolean was = isOwned(tile);
	    	for(int i=0; i<6; i++) {
	    		particles.add(new ExplosionParticle(player.getX()+Sprite.SPRITE_WIDTH/2, player.getY()+Sprite.SPRITE_HEIGHT/2, rnd.nextDouble()*Math.PI*2));
	   		}
	   		removePlayer(tile);
	   		goingPlayers.remove(player);
	   		if(was) {
	   			setRAM(ram + tile.hackCost());
	   		}
    	}
    }
    public void deevil(Tile tile) {
    	Hacker player = hackers.get(tile);
    	if(player != null) {
	    	for(int i=0; i<6; i++) {
	    		particles.add(new ExplosionParticle(player.getX()+Sprite.SPRITE_WIDTH/2, player.getY()+Sprite.SPRITE_HEIGHT/2, rnd.nextDouble()*Math.PI*2));
	   		}
	   		removeHacker(tile);
	   		goingHackers.remove(player);
    	}
    	tile.deEvil();
    }
	@Override
	public boolean isScrollable() {
		return !lost;
	}
	public void win() {
		done = true;
	}
	public void setRAM(int newRam) {
		this.ram = newRam;
		ramRender = new RenderableString("RAM: " + ram + "MB", 2, 2);
	}
	public int getRAM() {
		return ram;
	}
	@Override
	public boolean isResetting() {
		return resetting;
	}
	public void lose(String reason) {
		lost = true;
		owner.screen.scrollTo(0, 0);
		loseScreen = new LoseScreen(owner, 4, 4, 312, 232, reason);
	}
	public void addPlayer(Tile t, Player p) {
		if(players.containsKey(t)) {
			oldPlayers.put(t, players.get(t));
			players.remove(t);
		}
		players.put(t, p);
		ensureConnected();
	}
	public void addHacker(Tile t, Hacker p) {
		hackers.put(t, p);
		ensureConnected();
	}
	public void removePlayer(Tile t) {
		players.remove(t);
		oldPlayers.remove(t);
		ensureConnected();
	}
	public void removeHacker(Tile t) {
		hackers.remove(t);
		ensureConnected();
	}
	public void ensureConnected() {
		if(!lost) {
			if(!connected(players.values().iterator().next())) {
				lose("You lost connection to\nsome of your instances.");
			}
		}
	}
	public void removePlayer(Player p) {
		for(Iterator<Map.Entry<Tile, Player>> it = players.entrySet().iterator(); it.hasNext();) {
			Map.Entry<Tile, Player> ent = it.next();
			if(ent.getValue()==p) {
				it.remove();
			}
		}
		for(Iterator<Map.Entry<Tile, Player>> it = oldPlayers.entrySet().iterator(); it.hasNext();) {
			Map.Entry<Tile, Player> ent = it.next();
			if(ent.getValue()==p) {
				it.remove();
			}
		}
	}
	public void removeHacker(Hacker p) {
		for(Iterator<Map.Entry<Tile, Hacker>> it = hackers.entrySet().iterator(); it.hasNext();) {
			Map.Entry<Tile, Hacker> ent = it.next();
			if(ent.getValue()==p) {
				it.remove();
			}
		}
	}
	public void setMoney(int money) {
		this.money = money;
		moneyRender = new RenderableString("Money: $" + money, 2, 16);
	}
	public int getMoney() {
		return money;
	}
	public void upgradeHome() {
		if(canUpgrade()) {
			setMoney(money - ramCost);
			setRAM(ram + RAM_UPGRADE_AMT);
			ramCost++;
		}
	}
	public boolean canUpgrade() {
		return money >= ramCost;
	}
	public void hackHacker(HackerTile tile) {
		if(isOwned(tile))
    		return;
		if(isHackerOwned(tile)) {
			hack2(tile);
			return;
		}
    	Tile source = canBeHackedBy(tile);
    	List<Tile> tilePath = findConnectionStep(new ArrayList<Tile>(Collections.singleton(source)), tile);
    	Point[] path = new Point[tilePath.size()];
    	for(int i=0; i<tilePath.size(); i++) {
    		Tile t = tilePath.get(i);
    		path[i] = new Point(t.getX(), t.getY());
    	}
    	Player player = new Player(source.getX(), source.getY(), path);
    	addPlayer(tile, player);
    	player.go();
    	goingPlayers.put(player, tile);
	}
	public boolean isHackerOwned(Tile t) {
		if(t instanceof HackerTile) {
			return players.containsKey(t);
		} else {
			return isOwned(t);
		}
	}
	public boolean isEvil(int x, int y) {
		return isEvil(tiles[x/Sprite.SPRITE_WIDTH][y/Sprite.SPRITE_HEIGHT]);
	}
	public boolean isEvil(Tile t) {
		if(t == null) {
			return false;
		}
		return t.isEvil();
	}

}
