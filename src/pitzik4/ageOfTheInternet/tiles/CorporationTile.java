package pitzik4.ageOfTheInternet.tiles;

import java.awt.Graphics2D;
import java.util.Random;

import pitzik4.ageOfTheInternet.Game;
import pitzik4.ageOfTheInternet.Level;
import pitzik4.ageOfTheInternet.Menu;
import pitzik4.ageOfTheInternet.graphics.Animation;
import pitzik4.ageOfTheInternet.graphics.Sprite;

public class CorporationTile extends Tile {
	private Game owner;
	private Menu rightClickMenu = null;
	private boolean yours = false;
	private boolean theirs = false;
	public static final int RI_CLI_MENU_WIDTH = 96;
	public String[] riCliMenuOptions = {"Hack"};
	public String riCliMenuTitle = "Corporation";
	private boolean beingUnhacked = false;
	private int unhackTimer = 0;
	private static final int MAX_GAIN = 15;
	public static int slowestUnhack = 80;
	public static int fastestUnhack = 60;
	public static int inverseUnhackProbability = 400;
	private static final int[] UNHACK_FLASH_FRAMES = {38, 40};
	private static final Random rnd = new Random();
	
	public CorporationTile(int x, int y) {
		this(x, y, Game.game);
	}
	public CorporationTile(int x, int y, Game owner) {
		this.x = x;
		this.y = y;
		this.owner = owner;
		sprite = new Sprite(39, x, y, false);
	}
	
	public static void resetStats() {
		slowestUnhack = 80;
		fastestUnhack = 60;
		inverseUnhackProbability = 400;
	}

	@Override
	public void tick() {
		if(owner.mouseInsideOf(x, y, Sprite.SPRITE_WIDTH, Sprite.SPRITE_HEIGHT)) {
			if(owner.mouseDown) {
				if(rightClickMenu == null) {
					if((((Level) owner.currentLevel).canBeHackedBy(this) != null && ((Level) owner.currentLevel).getRAM() >= hackCost()) || yours) {
						rightClickMenu = new Menu(owner, x+Sprite.SPRITE_WIDTH, y, RI_CLI_MENU_WIDTH, riCliMenuOptions, riCliMenuTitle);
						owner.screen.addRenderable(rightClickMenu);
					}
				}
			}
		}
		if(rightClickMenu != null) {
			rightClickMenu.tick();
			if(rightClickMenu.buttons[0].nowClicked) {
				if(!yours) {
					hack();
				} else {
					unHack();
				}
				rightClickMenu.exited = true;
			}
			if(rightClickMenu.exited) {
				owner.screen.removeRenderable(rightClickMenu);
				rightClickMenu = null;
			}
		}
		if(yours) {
			if(rnd.nextInt(100) == 0) {
				((Level) owner.currentLevel).setMoney(((Level) owner.currentLevel).getMoney()+rnd.nextInt(MAX_GAIN-1)+1);
				((Level) owner.currentLevel).emitMoneyParticleFrom(this);
			}
			if(rnd.nextInt(inverseUnhackProbability) == 0) {
				beingUnhacked = true;
				sprite = new Animation(UNHACK_FLASH_FRAMES, 3, x, y, true);
				((Animation) sprite).go();
				riCliMenuOptions = new String[2];
				riCliMenuOptions[0] = "Unhack";
				riCliMenuOptions[1] = "Secure!!";
				unhackTimer = rnd.nextInt(slowestUnhack-fastestUnhack)+fastestUnhack;
			}
			if(beingUnhacked) {
				unhackTimer--;
				if(unhackTimer <= 0) {
					unHack();
					beingUnhacked = false;
				}
				if(rightClickMenu != null && rightClickMenu.buttons.length >= 2) {
					if(rightClickMenu.buttons[1].nowClicked) {
						beingUnhacked = false;
						riCliMenuOptions = new String[1];
						riCliMenuOptions[0] = "Unhack";
						sprite = new Sprite(38, x, y, false);
						rightClickMenu.exited = true;
					}
				}
			}
		}
		if(sprite instanceof Animation) {
			((Animation) sprite).tick();
		}
	}
	@Override
	public void furtherDraw(Graphics2D g, int scrollx, int scrolly) {
		//if(rightClickMenu != null)
		//	rightClickMenu.drawOn(g, scrollx, scrolly);
	}
	public void hack() {
		if(!yours) {
			yours = true;
			//sprite = new Sprite(32, x, y, false);
			riCliMenuOptions = new String[1];
			riCliMenuOptions[0] = "Unhack";
			((Level) owner.currentLevel).hack(this);
		}
	}
	@Override
	public void beOwned() {
		sprite = new Sprite(38, x, y, false);
	}
	@Override
	public void beEvil() {
		sprite = new Sprite(23, x, y, false);
	}
	@Override
	public void startEvil() {
		theirs = true;
	}
	@Override
	public void deEvil() {
		theirs = false;
		sprite = new Sprite(39, x, y, false);
	}
	@Override
	public boolean canBeEvil() {
		return true;
	}
	public void unHack() {
		if(yours) {
			yours = false;
			sprite = new Sprite(39, x, y, false);
			riCliMenuOptions = new String[1];
			riCliMenuOptions[0] = "Hack";
			((Level) owner.currentLevel).unhack(this);
		}
	}
	@Override
	public int hackCost() {
		return 10;
	}
	@Override
	public boolean isEvil() {
		return theirs;
	}

}
