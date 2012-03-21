package pitzik4.ageOfTheInternet.cutScenes;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import pitzik4.ageOfTheInternet.Game;
import pitzik4.ageOfTheInternet.Stage;
import pitzik4.ageOfTheInternet.Tickable;
import pitzik4.ageOfTheInternet.graphics.Animation;
import pitzik4.ageOfTheInternet.graphics.ConfettiParticle;
import pitzik4.ageOfTheInternet.graphics.InfoBox;
import pitzik4.ageOfTheInternet.graphics.Renderable;
import pitzik4.ageOfTheInternet.graphics.Screen;
import pitzik4.ageOfTheInternet.graphics.Sprite;

public class EndingCutscene implements Stage {
	private static final int[] tobyWalkFrames = {160+16, 161+16, 160+16, 162+16};
	private static final int[] drThompsonWalkFrames = {97, 98, 99, 100};
	private static final int[] dedigitizerFrames = {16, 17, 18, 19, 20, 21, 22, 23, 23, 23, 23, 23, 23, 23, 22, 21, 20, 19, 18, 17, 16};
	private static final int[] screenFrames = {0, 1, 2, 3, 4};
	private static final int[] screen2Frames = {300, 3, 2, 1, 0};
	private Animation dedigitizer = createDedigitizer();
	private Animation tobyWalk = new Animation(tobyWalkFrames, 4, 286, 208, true);
	private Animation drThompsonWalk = new Animation(drThompsonWalkFrames, 4, -16, 212, true);
	private Renderable toby = new Sprite(176, tobyWalk.getX(), tobyWalk.getY(), false);
	private Renderable drThompson = drThompsonWalk;
	private Renderable drGreen = new Sprite(128, 76, 212, bigSpriteSheet, 16, 16);
	private Renderable insideDedigitizer = new Sprite(24, 270, 196, bigSpriteSheet, 32, 32);
	private boolean tobyBeforeDe = false;
	private InfoBox dialogue = null;
	public static final BufferedImage bg = Screen.spritesheet("startSceneBG");
	private int lifeTime = 0;
	private Game owner;
	private boolean done = false;
	private boolean closing = false;
	private Set<Integer> lastKeysPressed = new HashSet<Integer>();
	private Animation screen = createScreen();
	private Animation screen2 = createScreen2();
	private Set<ConfettiParticle> confetti = new HashSet<ConfettiParticle>();
	private static final Random rnd = new Random();
	public static final Renderable drThompsonHead = new Sprite(32, 0, 0, Screen.spritesheet, 32, 32);
	public static final Renderable drThompsonOhNoHead = new Sprite(33, 0, 0, Screen.spritesheet, 32, 32);
	public static final Renderable tobyHead = new Sprite(34, 0, 0, Screen.spritesheet, 32, 32);
	public static final BufferedImage bigSpriteSheet = Screen.spritesheet("startSceneSprites");
	public static final Renderable drGreenHead = new Sprite(25, 270, 196, bigSpriteSheet, 32, 32);
	
	private static Animation createDedigitizer() {
		Sprite[] sprites = new Sprite[dedigitizerFrames.length];
		for(int i = 0; i<sprites.length; i++) {
			sprites[i] = new Sprite(dedigitizerFrames[i], 270, 196, bigSpriteSheet, 32, 32);
		}
		return new Animation(sprites, 2, 270, 196, false);
	}
	private static Animation createScreen() {
		Sprite[] sprites = new Sprite[screenFrames.length];
		for(int i = 0; i<sprites.length; i++) {
			sprites[i] = new Sprite(screenFrames[i], 220, 180, bigSpriteSheet, 36, 26);
		}
		return new Animation(sprites, 1, 220, 180, false);
	}
	private static Animation createScreen2() {
		Sprite[] sprites = new Sprite[screen2Frames.length];
		for(int i = 0; i<sprites.length; i++) {
			sprites[i] = new Sprite(screen2Frames[i], 220, 180, bigSpriteSheet, 36, 26);
		}
		return new Animation(sprites, 1, 220, 180, false);
	}
	
	public EndingCutscene(Game owner) {
		this.owner = owner;
		tobyWalk.go();
		drThompsonWalk.go();
		/*for(int i = 0; i < 100; i++) {
			confetti.add(new ConfettiParticle((int) (Math.round(Math.random() * 260) + 32), 77));
		}*/
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
		insideDedigitizer.drawOn(g, 0, 0);
		toby.drawOn(g, 0, 0);
		drThompson.drawOn(g, 0, 0);
		if(dialogue != null) {
			dialogue.drawOn(g, 0, 0);
		}
		screen.drawOn(g, 0, 0);
		screen2.drawOn(g, 0, 0);
		drGreen.drawOn(g, 0, 0);
		dedigitizer.drawOn(g, 0, 0);
		if(tobyBeforeDe) {
			toby.drawOn(g, 0, 0);
		}
		for(ConfettiParticle cp : confetti) {
			cp.drawOn(g, scrollx, scrolly);
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
		for(ConfettiParticle cp : confetti) {
			if(cp.getY() < 227) {
				cp.tick();
			}
		}
		dedigitizer.tick();
		screen.tick();
		screen2.tick();
		if(dialogue == null) {
			lifeTime++;
		}
		if(owner.keysDown.contains(10)) {
			done = true;
		}
		if(lifeTime > 20 && lifeTime < 50) {
			drThompson.goTo(drThompson.getX()+2, drThompson.getY());
		} else if(lifeTime == 50) {
			drThompson = new Sprite(96, drThompson.getX(), drThompson.getY(), false);
			drGreen = new Sprite(129, 76, 212, bigSpriteSheet, 16, 16);
		} else if(lifeTime == 60) {
			dialogue = new InfoBox(4, 4, 312, 64, drGreenHead, "Who are you?");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 62) {
			dialogue = new InfoBox(4, 4, 312, 64, drThompsonHead, "I am Dr Thompson. And who are you?");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 64) {
			dialogue = new InfoBox(4, 4, 312, 64, drGreenHead, "Dr Green. What are you doing here?");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 66) {
			dialogue = new InfoBox(4, 4, 312, 64, drThompsonHead, "I'm here to collect Toby. Is he here yet?");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 68) {
			dialogue = new InfoBox(4, 4, 312, 64, drGreenHead, "I don't know who that is. Sorry.");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 70) {
			screen.go();
			drGreen = new Sprite(128, 76, 212, bigSpriteSheet, 16, 16);
		} else if(lifeTime == 80) {
			dialogue = new InfoBox(4, 4, 312, 64, tobyHead, "I am Toby! I have taken control of this facility! I -");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 82) {
			dialogue = new InfoBox(4, 4, 312, 64, drThompsonHead, "Toby! What are you doing?");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 84) {
			dialogue = new InfoBox(4, 4, 312, 64, tobyHead, "Oh! Dr Thompson! Uh... Heheh...");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 86) {
			dialogue = new InfoBox(4, 4, 312, 64, drThompsonHead, "I told you to activate the Dedigitizer as soon as you arrived!");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 88) {
			dialogue = new InfoBox(4, 4, 312, 64, tobyHead, "Do I have to?");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 90) {
			dialogue = new InfoBox(4, 4, 312, 64, drThompsonHead, "Of course you do, Toby.");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 92) {
			dialogue = new InfoBox(4, 4, 312, 64, tobyHead, "Fine.");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 94) {
			dedigitizer.go();
			screen2.go();
		} else if(lifeTime == 110) {
			toby = tobyWalk;
			tobyBeforeDe = true;
		} else if(lifeTime > 110 && lifeTime < 200) {
			toby.goTo(toby.getX()-2, toby.getY());
			if(lifeTime == 117) {
				toby.goTo(toby.getX(), toby.getY()+4);
			}
		} else if(lifeTime == 200) {
			toby = new Sprite(176, toby.getX(), toby.getY(), false);
		} else if(lifeTime == 202) {
			dialogue = new InfoBox(4, 4, 312, 64, drGreenHead, "I suppose you're Toby?");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 204) {
			dialogue = new InfoBox(4, 4, 312, 64, tobyHead, "No! I am a wizard!");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 206) {
			dialogue = new InfoBox(4, 4, 312, 64, drThompsonHead, "Excellent. Let's be going, then.");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime == 208) {
			drThompson = new Sprite(130, drThompson.getX(), drThompson.getY(), bigSpriteSheet, 16, 16);
			drGreen = new Sprite(130, drGreen.getX(), drGreen.getY(), bigSpriteSheet, 16, 16);
			for(int i = 0; i < 15; i++) {
				confetti.add(new ConfettiParticle(drThompson.getX() + rnd.nextInt(Sprite.SPRITE_WIDTH), drThompson.getY() + rnd.nextInt(Sprite.SPRITE_HEIGHT)));
			}
			for(int i = 0; i < 15; i++) {
				confetti.add(new ConfettiParticle(drGreen.getX() + rnd.nextInt(Sprite.SPRITE_WIDTH), drGreen.getY() + rnd.nextInt(Sprite.SPRITE_HEIGHT)));
			}
		} else if(lifeTime == 210) {
			drThompson.goTo(-16, 0);
			drGreen.goTo(-16, 0);
			dialogue = new InfoBox(4, 4, 312, 64, tobyHead, "I win!!!!!!!");
			dialogue.go();
			lifeTime++;
		} else if(lifeTime > 212) {
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
