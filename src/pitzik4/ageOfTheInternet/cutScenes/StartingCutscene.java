package pitzik4.ageOfTheInternet.cutScenes;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import pitzik4.ageOfTheInternet.Game;
import pitzik4.ageOfTheInternet.Stage;
import pitzik4.ageOfTheInternet.Tickable;
import pitzik4.ageOfTheInternet.graphics.Animation;
import pitzik4.ageOfTheInternet.graphics.InfoBox;
import pitzik4.ageOfTheInternet.graphics.Renderable;
import pitzik4.ageOfTheInternet.graphics.RenderableString;
import pitzik4.ageOfTheInternet.graphics.Screen;
import pitzik4.ageOfTheInternet.graphics.Sprite;

public class StartingCutscene implements Stage {
	private static final int[] coffeeBreakFrames = {13, 103, 104, 105, 106, 107};
	private Animation coffeeBreak = new Animation(coffeeBreakFrames, 1, 146, 212, false);
	private static final int[] tobyWalkFrames = {160, 161, 160, 162};
	private static final int[] drThompsonWalkFrames = {97, 98, 99, 100};
	private static final int[] digitizerFrames = {16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 16};
	private static final int[] screenFrames = {0, 1, 2, 3, 4};
	private Animation digitizer = createDigitizer();
	private Animation tobyWalk = new Animation(tobyWalkFrames, 4, -16, 212, true);
	private Animation drThompsonWalk = new Animation(drThompsonWalkFrames, 4, 76, 212, true);
	private Renderable toby = tobyWalk;
	private Renderable drThompson = new Sprite(96, 76, 212, false);
	private InfoBox dialogue = null;
	public static final BufferedImage bg = Screen.spritesheet("startSceneBG");
	private int lifeTime = 0;
	private Game owner;
	private boolean done = false;
	private boolean closing = false;
	private Set<Integer> lastKeysPressed = new HashSet<Integer>();
	private Animation screen = createScreen();
	private RenderableString instructions = new RenderableString("Space to advance text\nEnter to skip cutscene", 102, 214);
	public static final Renderable drThompsonHead = new Sprite(32, 0, 0, Screen.spritesheet, 32, 32);
	public static final Renderable drThompsonOhNoHead = new Sprite(33, 0, 0, Screen.spritesheet, 32, 32);
	public static final Renderable tobyHead = new Sprite(34, 0, 0, Screen.spritesheet, 32, 32);
	public static final BufferedImage bigSpriteSheet = Screen.spritesheet("startSceneSprites");
	
	private static Animation createDigitizer() {
		Sprite[] sprites = new Sprite[digitizerFrames.length];
		for(int i = 0; i<sprites.length; i++) {
			sprites[i] = new Sprite(digitizerFrames[i], 286, 196, bigSpriteSheet, 16, 32);
		}
		return new Animation(sprites, 2, 286, 196, false);
	}
	private static Animation createScreen() {
		Sprite[] sprites = new Sprite[screenFrames.length];
		for(int i = 0; i<sprites.length; i++) {
			sprites[i] = new Sprite(screenFrames[i], 220, 180, bigSpriteSheet, 36, 26);
		}
		return new Animation(sprites, 1, 220, 180, false);
	}
	
	public StartingCutscene(Game owner) {
		this.owner = owner;
		tobyWalk.go();
		drThompsonWalk.go();
	}

	@Override
	public BufferedImage draw() {
		BufferedImage out = new BufferedImage(320, 240, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = out.createGraphics();
		drawOn(g, 0, 0);
		g.dispose();
		return out;
	}

	@Override
	public void drawOn(Graphics2D g, int scrollx, int scrolly) {
		scrollx = 0;
		scrolly = 0;
		g.drawImage(bg, 0, 0, null);
		toby.drawOn(g, 0, 0);
		drThompson.drawOn(g, 0, 0);
		if(dialogue != null) {
			dialogue.drawOn(g, 0, 0);
		}
		screen.drawOn(g, 0, 0);
		digitizer.drawOn(g, 0, 0);
		coffeeBreak.drawOn(g, 0, 0);
		if(instructions != null) {
			instructions.drawOn(g, 0, 0);
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
		if(toby instanceof Tickable) {
			((Tickable) toby).tick();
		}
		if(drThompson instanceof Tickable) {
			((Tickable) drThompson).tick();
		}
		if(dialogue != null) {
			dialogue.tick();
		}
		digitizer.tick();
		screen.tick();
		coffeeBreak.tick();
		if(dialogue == null) {
			lifeTime++;
		}
		if(owner.keysDown.contains(10)) {
			done = true;
		}
		if(lifeTime > 20 && lifeTime < 50) {
			toby.goTo(toby.getX()+2, toby.getY());
		} else if(lifeTime == 50) {
			toby = new Sprite(160, toby.getX(), toby.getY(), false);
			drThompson = new Sprite(112, drThompson.getX(), drThompson.getY(), false);
		} else if(lifeTime == 60) {
			dialogue = new InfoBox(4, 4, 312, 64, drThompsonHead, "Ah! You must be Toby. I'm Dr Thompson.");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 62) {
			dialogue = new InfoBox(4, 4, 312, 64, drThompsonHead, "I'll be conducting today's tests.");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 64) {
			dialogue = new InfoBox(4, 4, 312, 64, tobyHead, "Hi, Dr Thompson! So you're going to put me in a computer?");
			dialogue.go();
			lifeTime++;
			instructions = null;
		} else if(lifeTime == 66) {
			dialogue = new InfoBox(4, 4, 312, 64, drThompsonHead, "I suppose -");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 68) {
			dialogue = new InfoBox(4, 4, 312, 64, tobyHead, "Wow! That's the biggest computer I've ever seen!");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 70) {
			toby = tobyWalk;
		} else if(lifeTime > 70 && lifeTime < 110) {
			toby.goTo(toby.getX()+2, toby.getY());
		} else if(lifeTime == 110) {
			toby = new Sprite(160, toby.getX(), toby.getY(), false);
			drThompson = new Sprite(96, 76, 212, false);
		} else if(lifeTime == 120) {
			dialogue = new InfoBox(4, 4, 312, 64, drThompsonHead, "That's the computer where you will be, uh, put in.");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 122) {
			toby = new Sprite(176, toby.getX(), toby.getY(), false);
			dialogue = new InfoBox(4, 4, 312, 64, tobyHead, "Wow! This whole computer is just for me?");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 124) {
			dialogue = new InfoBox(4, 4, 312, 64, drThompsonHead, "No way! I mean, no, it's for the project.");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 126) {
			toby = new Sprite(160, toby.getX(), toby.getY(), false);
			dialogue = new InfoBox(4, 4, 312, 64, tobyHead, "Oh. It's still awesome, though.");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 128) {
			toby = new Sprite(176, toby.getX(), toby.getY(), false);
			dialogue = new InfoBox(4, 4, 312, 64, tobyHead, "So, what do I do?");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 130) {
			dialogue = new InfoBox(4, 4, 312, 64, drThompsonHead, "Just get into that machine over there.");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 132) {
			toby = new Sprite(160, toby.getX(), toby.getY(), false);
			dialogue = new InfoBox(4, 4, 312, 64, tobyHead, "That's all? Ok, I'll get in.");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 134) {
			toby = tobyWalk;
		} else if(lifeTime > 134 && lifeTime < 210) {
			toby.goTo(toby.getX()+2, toby.getY());
		} else if(lifeTime == 212) {
			toby = new Sprite(160, toby.getX(), toby.getY(), false);
			digitizer.go();
		} else if(lifeTime == 219) {
			toby = tobyWalk;
			toby.goTo(toby.getX()+2, toby.getY());
		} else if(lifeTime > 219 && lifeTime < 226) {
			toby.goTo(toby.getX()+2, toby.getY());
		}
		if(lifeTime == 222) {
			drThompson = drThompsonWalk;
		} else if(lifeTime == 226) {
			toby = new Sprite(13, 0, 0, false);
			drThompson.goTo(drThompson.getX()+2, drThompson.getY());
		} else if(lifeTime > 222 && lifeTime < 250) {
			drThompson.goTo(drThompson.getX()+2, drThompson.getY());
		} else if(lifeTime == 250) {
			drThompson = new Sprite(96, drThompson.getX(), drThompson.getY(), false);
		} else if(lifeTime == 260) {
			screen.go();
		} else if(lifeTime == 270) {
			dialogue = new InfoBox(4, 4, 312, 64, tobyHead, "Wow! This is neat!");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 272) {
			dialogue = new InfoBox(4, 4, 312, 64, drThompsonHead, "It worked? Yes! It worked! I can't believe it!");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 274) {
			dialogue = new InfoBox(4, 4, 312, 64, drThompsonHead, "I can't wait to tell the others!");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 276) {
			dialogue = new InfoBox(4, 4, 312, 64, drThompsonHead, "Oh, but, of course... Better get you out of there.");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 278) {
			drThompson = new Sprite(112, drThompson.getX(), drThompson.getY(), false);
		} else if(lifeTime == 284) {
			drThompson = new Sprite(96, drThompson.getX(), drThompson.getY(), false);
		} else if(lifeTime == 290) {
			drThompson = new Sprite(112, drThompson.getX(), drThompson.getY(), false);
		} else if(lifeTime == 296) {
			drThompson = new Sprite(96, drThompson.getX(), drThompson.getY(), false);
		} else if(lifeTime == 310) {
			drThompson = new Sprite(102, drThompson.getX(), drThompson.getY(), false);
			coffeeBreak.go();
		} else if(lifeTime == 330) {
			dialogue = new InfoBox(4, 4, 312, 64, drThompsonOhNoHead, "Oh, no!! The machine to bring you back is missing!");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 332) {
			dialogue = new InfoBox(4, 4, 312, 64, drThompsonOhNoHead, "We can't get you out now!");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 334) {
			dialogue = new InfoBox(4, 4, 312, 64, tobyHead, "This is the coolest thing that's ever happened to me!!");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 336) {
			dialogue = new InfoBox(4, 4, 312, 64, drThompsonOhNoHead, "You can't stay in there forever!");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 338) {
			dialogue = new InfoBox(4, 4, 312, 64, tobyHead, "Yeah, I guess you're right... How can I get out?");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 340) {
			dialogue = new InfoBox(4, 4, 312, 64, drThompsonOhNoHead, "You'll have to go to the Internet and hack computers!");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 342) {
			dialogue = new InfoBox(4, 4, 312, 64, drThompsonOhNoHead, "Click a computer to open a menu on it, and click \"Hack\" to hack it.");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 344) {
			dialogue = new InfoBox(4, 4, 312, 64, drThompsonOhNoHead, "Hack one of our research facilities and bring yourself back.");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 346) {
			dialogue = new InfoBox(4, 4, 312, 64, drThompsonOhNoHead, "Remember, it takes RAM to hack computers. Only hack what you need to.");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 348) {
			dialogue = new InfoBox(4, 4, 312, 64, tobyHead, "Got it!");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime > 350) {
			done = true;
		}
		if(keysPressed.contains(32) && !lastKeysPressed.contains(32)) {
			if(dialogue != null) {
				if(dialogue.isGoing()) {
					dialogue.finish();
				} else {
					dialogue = null;
				}
			}
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
	}

	@Override
	public boolean isClosing() {
		return closing;
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
	@Override
	public boolean isResetting() {
		return false;
	}

}
