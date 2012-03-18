package pitzik4.ageOfTheInternet.graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import pitzik4.ageOfTheInternet.Tickable;

public class InfoBox implements Renderable, Tickable {
	private RenderableString str;
	private Renderable sideGraphic;
	private int sGrWidth, sGrHeight;
	private int width, height;
	private int x, y;
	private String displayWhat, currentDisp="";
	private static final int[][] BG_SPRITES = {{192, 193, 194}, {224, 225, 226}, {256, 257, 258}};
	private static final int BG_SPRITE_SIZE = 8;
	private static final int WAIT_BETWEEN_CHARS = 2;
	private Sprite[][] background;
	private byte nextLetterCountdown = WAIT_BETWEEN_CHARS;
	private boolean going = false;
	
	public InfoBox(int x, int y, int width, int height, Renderable sideGraphic, String displayWhat) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sideGraphic = sideGraphic;
		this.displayWhat = displayWhat;
		background = new Sprite[width/BG_SPRITE_SIZE][height/BG_SPRITE_SIZE];
		for(int i=0; i<width; i+=BG_SPRITE_SIZE) {
			for(int j=0; j<height; j+=BG_SPRITE_SIZE) {
				int whichx;
				int whichy;
				if(i==0) {
					whichy = 0;
				} else if(i==width-BG_SPRITE_SIZE) {
					whichy = 2;
				} else {
					whichy = 1;
				}
				if(j==0) {
					whichx = 0;
				} else if(j==height-BG_SPRITE_SIZE) {
					whichx = 2;
				} else {
					whichx = 1;
				}
				//try {
					background[i/BG_SPRITE_SIZE][j/BG_SPRITE_SIZE] = new Sprite(BG_SPRITES[whichx][whichy], i+x, j+x, Screen.spritesheet, BG_SPRITE_SIZE, BG_SPRITE_SIZE);
				//} catch(ArrayIndexOutOfBoundsException e) {}
			}
		}
		if(sideGraphic != null) {
			sGrWidth = sideGraphic.draw().getWidth();
			sGrHeight = sideGraphic.draw().getHeight();
			this.str = new RenderableString("", x+BG_SPRITE_SIZE*2+sGrWidth+2, y+BG_SPRITE_SIZE);
		} else {
			this.str = new RenderableString("", x+BG_SPRITE_SIZE, y+BG_SPRITE_SIZE);
		}
	}

	@Override
	public BufferedImage draw() {
		BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = out.createGraphics();
		drawOn(g, 0, 0);
		g.dispose();
		return out;
	}

	@Override
	public void drawOn(Graphics2D g, int scrollx, int scrolly) {
		scrollx = scrolly = 0;
		for(Sprite[] ss : background) {
			for(Sprite s : ss) {
				s.drawOn(g, scrollx, scrolly);
			}
		}
		str.drawOn(g, scrollx, scrolly);
		if(sideGraphic != null) {
			//sideGraphic.drawOn(g, sideGraphic.getX()+scrollx-BG_SPRITE_SIZE-x, sideGraphic.getY()+scrolly-BG_SPRITE_SIZE-y);
			g.drawImage(sideGraphic.draw(), x-scrollx+BG_SPRITE_SIZE, (y + y + height - sGrHeight) / 2, null);
		}
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return x;
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
		for(Sprite[] ss : background) {
			for(Sprite s : ss) {
				s.goTo(s.getX()+dx, s.getY()+dy);
			}
		}
		str.goTo(str.getX()+dx, str.getY()+dy);
	}

	@Override
	public void tick() {
		if(going) {
			nextLetterCountdown--;
			if(nextLetterCountdown <= 0) {
				nextLetterCountdown = WAIT_BETWEEN_CHARS;
				nextLetter();
			}
		}
	}
	public void nextLetter() {
		if(currentDisp.length() < displayWhat.length()) {
			String string = str.represents;
			char nextChar = displayWhat.charAt(currentDisp.length());
			string += nextChar;
			currentDisp += nextChar;
			//System.out.format("Next character: %c%n", nextChar);
			RenderableString tmpStr = new RenderableString(string, str.getX(), str.getY());
			if(tmpStr.width > width-tmpStr.getX()+x-BG_SPRITE_SIZE) {
				//System.out.println("Making new line");
				int whereNewLine = tmpStr.represents.lastIndexOf(" ")+1;
				StringBuilder sb = new StringBuilder(tmpStr.represents);
				if(whereNewLine == sb.toString().length()) {
					sb.replace(sb.toString().length()-1, sb.toString().length(), "\n");
				} else {
					sb.insert(whereNewLine, '\n');
				}
				//string = str.represents;
				//string += "\n" + nextChar;
				string = sb.toString();
				tmpStr = new RenderableString(string, str.getX(), str.getY());
				if(tmpStr.height > height-tmpStr.getY()+y-BG_SPRITE_SIZE) {
					going = false;
					tmpStr = str;
					//System.out.println("Too tall, stopping");
				}
			}
			str = tmpStr;
			if(Character.isWhitespace(nextChar)) {
				nextLetter();
			}
		} else {
			going = false;
		}
	}
	public InfoBox go() {
		going = true;
		return this;
	}
	public InfoBox stop() {
		going = false;
		return this;
	}
	public InfoBox finish() {
		go();
		while(going) {
			nextLetter();
		}
		return this;
	}
	public boolean isGoing() {
		return going;
	}

}
