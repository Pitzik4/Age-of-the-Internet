package pitzik4.ageOfTheInternet.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import pitzik4.ageOfTheInternet.Game;
import pitzik4.ageOfTheInternet.Menu;
import pitzik4.ageOfTheInternet.Tickable;

public class Screen implements Tickable {
	private Game owner;
	private int width;
	private int height;
	private int scrollx=0, scrolly=0;
	private volatile List<Renderable> renderables = new ArrayList<Renderable>();
	private byte rumbleX=0, rumbleY=0;
	private byte rumbleForce = 0;
	private Random rumbleRand = new Random(System.currentTimeMillis());
	private int fade = 0;
	private int xvelocity = 0;
	private int yvelocity = 0;
	public static final int POOR_RES = 2;
	public static final BufferedImage spritesheet = spritesheet("grid");
	public static final BufferedImage font = spritesheet("font");

	public Screen(Game game) {
		this.owner = game;
		width = owner.getWidth()/POOR_RES+1;
		height = owner.getHeight()/POOR_RES+1;
	}
	
	public static BufferedImage spritesheet(String name) {
		BufferedImage out = null;
		try {
			out = ImageIO.read(Game.class.getResourceAsStream("/" + name + ".gif"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out;
	}
	
	public synchronized BufferedImage draw() {
		BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = out.createGraphics();
		g.setPaint(Color.MAGENTA);
		g.fillRect(0, 0, width-1, height-1);
		for(Renderable r : renderables) {
			r.drawOn(g, scrollx, scrolly);
		}
		//g.setPaint(Color.WHITE);
		//g.drawString("Test", 10, 10);
		g.setPaint(new Color(fade<<24, true));
		g.fillRect(0, 0, width-1, height-1);
		g.dispose();
		BufferedImage out2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		g = out2.createGraphics();
		g.setPaint(Color.BLACK);
		g.fillRect(0, 0, width-1, height-1);
		g.drawImage(out, rumbleX, rumbleY, null);
		g.dispose();
		return out2;
	}
	public synchronized void addRenderable(Renderable r) {
		renderables.add(r);
	}
	public synchronized void addRenderable(int index, Renderable r) {
		renderables.add(index, r);
	}
	public synchronized void removeRenderable(Renderable r) {
		while(renderables.contains(r)) {
			renderables.remove(r);
		}
	}
	public synchronized void removeRenderable(int index) {
		renderables.remove(index);
	}
	public synchronized void sendToFront(Renderable r) {
		renderables.remove(r);
		renderables.add(r);
	}
	public synchronized void sendToBack(Renderable r) {
		renderables.remove(r);
		renderables.add(0, r);
	}
	public synchronized void sendTo(int index, Renderable r) {
		renderables.remove(r);
		renderables.add(index, r);
	}

	@Override
	public void tick() {
		if(rumbleForce > 0) {
			rumbleX = (byte) (rumbleRand.nextInt(rumbleForce)-(rumbleForce/2));
			rumbleY = (byte) (rumbleRand.nextInt(rumbleForce)-(rumbleForce/2));
		}
		if(owner.keysDown.contains(37)) {
			if(xvelocity > -6) {
				xvelocity -= 1;
			}
		}
		if(owner.keysDown.contains(39)) {
			if(xvelocity < 6) {
				xvelocity += 1;
			}
		}
		if(owner.keysDown.contains(38)) {
			if(yvelocity > -6) {
				yvelocity -= 1;
			}
		}
		if(owner.keysDown.contains(40)) {
			if(yvelocity < 6) {
				yvelocity += 1;
			}
		}
		if(!(owner.keysDown.contains(37) || owner.keysDown.contains(39))) {
			if(xvelocity < 0) {
				xvelocity += 1;
			} else if(xvelocity > 0) {
				xvelocity -= 1;
			}
		}
		if(!(owner.keysDown.contains(38) || owner.keysDown.contains(40))) {
			if(yvelocity < 0) {
				yvelocity += 1;
			} else if(yvelocity > 0) {
				yvelocity -= 1;
			}
		}
		if(owner.currentLevel.isScrollable()) {
			scrollx += xvelocity;
			scrolly += yvelocity;
		}
	}
	public void rumble(int i) {
		rumbleForce = (byte) i;
	}

	public int getFade() {
		return fade;
	}

	public void fadeTo(int fade) {
		this.fade = fade;
	}
	public void scrollTo(int scrollx, int scrolly) {
		this.scrollx = scrollx;
		this.scrolly = scrolly;
	}
	public void scrollToX(int scrollx) {
		this.scrollx = scrollx;
	}
	public void scrollToY(int scrolly) {
		this.scrolly = scrolly;
	}
	public Point getScroll() {
		return new Point(scrollx, scrolly);
	}
	public int getScrollX() {
		return scrollx;
	}
	public int getScrollY() {
		return scrolly;
	}
	public void closeAllMenus() {
		for(Iterator<Renderable> it = renderables.iterator(); it.hasNext();) {
			Renderable r = it.next();
			if(r instanceof Menu) {
				it.remove();
			}
		}
	}

}
