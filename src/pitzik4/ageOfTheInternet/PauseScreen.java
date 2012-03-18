package pitzik4.ageOfTheInternet;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import pitzik4.ageOfTheInternet.graphics.BlueFrame;
import pitzik4.ageOfTheInternet.graphics.Renderable;
import pitzik4.ageOfTheInternet.graphics.RenderableString;

public class PauseScreen implements Renderable {
	private int x=0, y=0;
	private int width=0, height=0;
	private RenderableString message;
	private BlueFrame frame;
	
	public PauseScreen(int x, int y, int width, int height) {
		this(x, y, width, height, Game.game);
	}
	public PauseScreen(int x, int y, int width, int height, Game owner) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.frame = new BlueFrame(x, y, width, height);
		this.message = new RenderableString("Game Paused", 0, 0);
		this.message.centerBetweenX(x, x+width);
		this.message.centerBetweenY(y, y+height);
	}

	@Override
	public BufferedImage draw() {
		BufferedImage tmp = new BufferedImage(width+x, height+y, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = tmp.createGraphics();
		drawOn(g, 0, 0);
		g.dispose();
		BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		g = out.createGraphics();
		g.drawImage(tmp, -x, -y, null);
		g.dispose();
		return out;
	}

	@Override
	public void drawOn(Graphics2D g, int scrollx, int scrolly) {
		scrollx = scrolly = 0;
		frame.drawOn(g, scrollx, scrolly);
		message.drawOn(g, scrollx, scrolly);
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
		int dx = x - this.x;
		int dy = y - this.y;
		this.x = x;
		this.y = y;
		frame.goTo(frame.getX()+dx, frame.getY()+dy);
		message.goTo(message.getX()+dx, message.getY()+dy);
	}

}
