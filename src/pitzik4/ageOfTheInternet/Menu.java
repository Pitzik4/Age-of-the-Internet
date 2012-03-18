package pitzik4.ageOfTheInternet;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import pitzik4.ageOfTheInternet.graphics.Renderable;
import pitzik4.ageOfTheInternet.graphics.RenderableString;
import pitzik4.ageOfTheInternet.graphics.Sprite;

public class Menu implements Renderable, Tickable {
	public XButton exitButton;
	public Button[] buttons;
	private RenderableString str;
	private int x=0, y=0;
	private int width=0, height=0;
	private boolean exiting=false;
	public boolean exited=false;
	
	public Menu(Game owner, int x, int y, int width, String[] extraButtons, String title) {
		this.x = x;
		this.y = y;
		str = new RenderableString(title, x, y);
		this.height = extraButtons.length*(Sprite.SPRITE_HEIGHT+2)-2+XButton.BU_SI+str.height;
		if(height <= 0)
			height = 1;
		this.width = str.width<width?width:str.width;
		exitButton = new XButton(owner, x+width-XButton.BU_SI, y+str.height);
		buttons = new Button[extraButtons.length];
		for(int i=0; i<extraButtons.length; i++) {
			buttons[i] = new Button(owner, x, y+(i*(Sprite.SPRITE_HEIGHT+2))+XButton.BU_SI+str.height, width, extraButtons[i]);
		}
	}

	@Override
	public void tick() {
		for(Button b : buttons) {
			b.tick();
		}
		exitButton.tick();
		if(exiting && !exitButton.isClicked) {
			exited = true;
		}
		exiting = exitButton.isClicked;
	}

	@Override
	public BufferedImage draw() {
		BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = out.createGraphics();
		drawOn(g, x, y);
		g.dispose();
		return out;
	}

	@Override
	public void drawOn(Graphics2D g, int scrollx, int scrolly) {
		if(!exited) {
			for(Button b : buttons) {
				b.drawOn(g, scrollx, scrolly);
			}
			exitButton.drawOn(g, scrollx, scrolly);
			str.drawOn(g, scrollx, scrolly);
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
		for(Button b : buttons) {
			b.goTo(b.getX()+dx, b.getY()+dy);
		}
		exitButton.goTo(exitButton.getX()+dx, exitButton.getY()+dy);
		str.goTo(x, y);
	}

}
