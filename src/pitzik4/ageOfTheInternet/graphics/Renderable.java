package pitzik4.ageOfTheInternet.graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public interface Renderable {
	public BufferedImage draw();
	public void drawOn(Graphics2D g, int scrollx, int scrolly);
	public int getX();
	public int getY();
	public int getXOffset();
	public int getYOffset();
	public void goTo(int x, int y);
}
