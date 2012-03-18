package pitzik4.ageOfTheInternet.cutScenes;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import pitzik4.ageOfTheInternet.Game;
import pitzik4.ageOfTheInternet.Stage;
import pitzik4.ageOfTheInternet.graphics.InfoBox;
import pitzik4.ageOfTheInternet.graphics.Renderable;
import pitzik4.ageOfTheInternet.graphics.Sprite;

public class ChurchCutscene implements Stage {
	private int lifeTime = 0;
	private Renderable drThompson = new Sprite(96, 131, 212, false);
	private Renderable screen = new Sprite(4, 220, 180, StartingCutscene.bigSpriteSheet, 36, 26);
	private Renderable digitizer = new Sprite(16, 286, 196, StartingCutscene.bigSpriteSheet, 16, 32);
	private InfoBox dialogue = null;
	private boolean done = false;
	private boolean closing = false;
	private Game owner;
	private Set<Integer> lastKeysPressed = new HashSet<Integer>();
	
	public ChurchCutscene(Game owner) {
		this.owner = owner;
	}

	@Override
	public BufferedImage draw() {
		BufferedImage out = new BufferedImage(320, 240, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = out.createGraphics();
		drawOn(g, 0, 0);
		g.dispose();
		return null;
	}

	@Override
	public void drawOn(Graphics2D g, int scrollx, int scrolly) {
		scrollx = 0;
		scrolly = 0;
		g.drawImage(StartingCutscene.bg, 0, 0, null);
		drThompson.drawOn(g, 0, 0);
		screen.drawOn(g, 0, 0);
		digitizer.drawOn(g, 0, 0);
		if(dialogue != null) {
			dialogue.drawOn(g, 0, 0);
		}
	}

	@Override
	public int getX() {
		return 0;
	}

	@Override
	public int getY() {
		return 0;
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

	}

	@Override
	public void tick() {
		Set<Integer> keysPressed = new HashSet<Integer>(owner.keysDown);
		if(dialogue == null) {
			lifeTime++;
		} else {
			dialogue.tick();
			if(keysPressed.contains(32) && !lastKeysPressed.contains(32)) {
				if(dialogue.isGoing()) {
					dialogue.finish();
				} else {
					dialogue = null;
				}
			}
		}
		if(lifeTime == 10) {
			dialogue = new InfoBox(4, 4, 312, 64, StartingCutscene.tobyHead, "What's new?");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 12) {
			dialogue = new InfoBox(4, 4, 312, 64, StartingCutscene.drThompsonHead, "Well, I heard the Church of Pitzik4 has weak Internet security.");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 14) {
			dialogue = new InfoBox(4, 4, 312, 64, StartingCutscene.tobyHead, "I don't believe in Pitzik4. I'll hack them.");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 16) {
			dialogue = new InfoBox(4, 4, 312, 64, StartingCutscene.drThompsonHead, "That's not very nice.");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 18) {
			dialogue = new InfoBox(4, 4, 312, 64, StartingCutscene.tobyHead, "Don't worry, I have an idea on what to do with them.");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 20) {
			dialogue = new InfoBox(4, 4, 312, 64, StartingCutscene.tobyHead, "I'll schedule a whole bunch of events so that corporations'");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 22) {
			dialogue = new InfoBox(4, 4, 312, 64, StartingCutscene.tobyHead, "counter-hackers are occupied.");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 24) {
			dialogue = new InfoBox(4, 4, 312, 64, StartingCutscene.drThompsonHead, "That's actually quite clever...");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 26) {
			dialogue = new InfoBox(4, 4, 312, 64, StartingCutscene.tobyHead, "You sound surprised.");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 28) {
			done = true;
		}
		if(owner.keysDown.contains(10)) {
			done = true;
		}
		lastKeysPressed = keysPressed;
		if(done) {
			if(owner.screen.getFade() < 255) {
				if(owner.screen.getFade() > 245) {
					owner.screen.fadeTo(255);
				} else {
					owner.screen.fadeTo(owner.screen.getFade()+10);
				}
			} else {
				closing = true;
			}
		}
		if(owner.screen.getFade() > 0 && !done) {
			if(owner.screen.getFade() < 10) {
				owner.screen.fadeTo(0);
			} else {
				owner.screen.fadeTo(owner.screen.getFade()-10);
			}
		}
	}

	@Override
	public boolean isClosing() {
		return closing;
	}

	@Override
	public boolean isResetting() {
		return false;
	}

	@Override
	public int getWidth() {
		return 320;
	}

	@Override
	public int getHeight() {
		return 240;
	}

	@Override
	public Dimension getSize() {
		return new Dimension(320, 240);
	}

	@Override
	public boolean isScrollable() {
		return false;
	}

}
