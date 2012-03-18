package pitzik4.ageOfTheInternet;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import pitzik4.ageOfTheInternet.graphics.BlueFrame;
import pitzik4.ageOfTheInternet.graphics.RenderableString;

public class LoseScreen implements RenderableTickable {
	private RenderableString youLose;
	private RenderableString loseReason;
	public Button retry;
	private BlueFrame bg;
	private int x=0, y=0;
	
	public LoseScreen(Game owner, int x, int y, int width, int height, String reason) {
		this.x = x;
		this.y = y;
		bg = new BlueFrame(x, y, width, height);
		youLose = new RenderableString("Level Unsuccessful", 0, 0);
		youLose.goTo(x+((width-youLose.width)/2), y+(height/3-youLose.height/2));
		loseReason = new RenderableString(reason, 0, 0);
		loseReason.goTo(x+((width-loseReason.width)/2), y+(height/2-loseReason.height/2));
		retry = new Button(owner, x+width/4, y+(height/3)*2, width/2, "Retry");
	}

	@Override
	public BufferedImage draw() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void drawOn(Graphics2D g, int scrollx, int scrolly) {
		bg.drawOn(g, scrollx, scrolly);
		youLose.drawOn(g, scrollx, scrolly);
		loseReason.drawOn(g, scrollx, scrolly);
		retry.drawOn(g, scrollx, scrolly);
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
		youLose.goTo(youLose.getX()+dx, youLose.getY()+dy);
		loseReason.goTo(loseReason.getX()+dx, loseReason.getY()+dy);
		retry.goTo(retry.getX()+dx, retry.getY()+dy);
		bg.goTo(bg.getX()+dx, bg.getY()+dy);
	}

	@Override
	public void tick() {
		retry.tick();
	}

}
