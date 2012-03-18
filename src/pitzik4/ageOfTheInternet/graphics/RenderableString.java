package pitzik4.ageOfTheInternet.graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class RenderableString implements Renderable {
	public String represents = "";
	private int x=0,y=0;
	private List<Sprite> charSprites = new ArrayList<Sprite>();
	public static final int LETTER_SPACING_HORIZ = 2;
	public static final int LETTER_SPACING_VERT = 2;
	public int width=0, height=0;
	
	public RenderableString(String represents, int x, int y) {
		this.represents = represents;
		this.x = x;
		this.y = y;
		char[] chars = represents.toCharArray();
		int x1 = 0;
		int y1 = 0;
		for(char c : chars) {
			if(c == '\n') {
				y1 += Sprite.LETTER_HEIGHT+LETTER_SPACING_VERT;
				x1 = 0;
			} else if(c == '\t') {
				x1 += (Sprite.LETTER_WIDTH+LETTER_SPACING_HORIZ)*4;
				if(x1 > width) {
					width = x1;
				}
			} else if(c == '\r') {
				x1 = 0;
			} else {
				charSprites.add(new Sprite(c-32, x+x1, y+y1, true));
				x1 += Sprite.LETTER_WIDTH+LETTER_SPACING_HORIZ;
				if(x1 > width) {
					width = x1;
				}
			}
		}
		height = y1 + Sprite.LETTER_HEIGHT;
		width -= LETTER_SPACING_HORIZ;
	}

	@Override
	public BufferedImage draw() {
		//TODO Image drawing code
		BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = out.createGraphics();
		drawOn(g, x, y);
		g.dispose();
		return out;
	}

	@Override
	public void drawOn(Graphics2D g, int scrollx, int scrolly) {
		for(Sprite s : charSprites) {
			s.drawOn(g, scrollx, scrolly);
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
		int dx = x - this.x;
		int dy = y - this.y;
		this.x = x;
		this.y = y;
		for(Sprite s : charSprites) {
			s.goTo(s.getX() + dx, s.getY() + dy);
		}
	}
	public void centerBetweenX(int x1, int x2) {
		goTo((x1+x2-width)/2, y);
	}
	public void centerBetweenY(int y1, int y2) {
		goTo(x, (y1+y2-height)/2);
	}

}
