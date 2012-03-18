package pitzik4.ageOfTheInternet;

import java.awt.Dimension;

public interface Stage extends RenderableTickable {
	public boolean isClosing();
	public boolean isResetting();
	public int getWidth();
	public int getHeight();
	public Dimension getSize();
	public boolean isScrollable();
}
