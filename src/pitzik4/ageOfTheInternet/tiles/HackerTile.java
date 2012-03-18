package pitzik4.ageOfTheInternet.tiles;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import pitzik4.ageOfTheInternet.Game;
import pitzik4.ageOfTheInternet.Level;
import pitzik4.ageOfTheInternet.Menu;
import pitzik4.ageOfTheInternet.graphics.Animation;
import pitzik4.ageOfTheInternet.graphics.Sprite;

public class HackerTile extends Tile {
	private Game owner;
	private Menu rightClickMenu = null;
	public static final byte TRIES_TO_HACK = 2;
	private byte yoursLeft = TRIES_TO_HACK;
	public static final int RI_CLI_MENU_WIDTH = 96;
	public String[] riCliMenuOptions = {"Hack"};
	public String riCliMenuTitle = "Hacker";
	private boolean beingUnhacked = false;
	private int unhackTimer = 0;
	public boolean initiatedUnhacking = false;
	public static int slowestUnhack = 80;
	public static int fastestUnhack = 60;
	public static int hackersOwned = 0;
	private static final int[] UNHACK_FLASH_FRAMES = {43, 45};
	private static final Random rnd = new Random();
	private static final int INVERSE_HACK_PROBABILITY = 100;
	
	public HackerTile(int x, int y) {
		this(x, y, Game.game);
	}
	public HackerTile(int x, int y, Game owner) {
		this.x = x;
		this.y = y;
		this.owner = owner;
		sprite = new Sprite(44, x, y, false);
	}
	
	public static void resetStats() {
		slowestUnhack = 80;
		fastestUnhack = 60;
		hackersOwned = 0;
	}

	@Override
	public void tick() {
		if(owner.mouseInsideOf(x, y, Sprite.SPRITE_WIDTH, Sprite.SPRITE_HEIGHT)) {
			if(owner.mouseDown) {
				if(rightClickMenu == null) {
					if((((Level) owner.currentLevel).canBeHackedBy(this) != null && ((Level) owner.currentLevel).getRAM() >= hackCost()) || (yoursLeft <= 0)) {
						rightClickMenu = new Menu(owner, x+Sprite.SPRITE_WIDTH, y, RI_CLI_MENU_WIDTH, riCliMenuOptions, riCliMenuTitle);
						owner.screen.addRenderable(rightClickMenu);
					}
				}
			}
		}
		if(rightClickMenu != null) {
			rightClickMenu.tick();
			if(rightClickMenu.buttons[0].nowClicked) {
				if(yoursLeft > 0) {
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
		if(yoursLeft < TRIES_TO_HACK) {
			if(rnd.nextInt(inverseUnhackProbability()) == 0 && !beingUnhacked) {
				beingUnhacked = true;
				sprite = new Animation(UNHACK_FLASH_FRAMES, 3, x, y, true);
				((Animation) sprite).go();
				riCliMenuOptions = new String[2];
				riCliMenuOptions[0] = (yoursLeft <= 0) ? "Unhack" : "Hack";
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
						String old = riCliMenuOptions[0];
						riCliMenuOptions = new String[] {old};
						sprite = new Sprite(isOwned() ? 43 : 44, x, y, false);
						rightClickMenu.exited = true;
					}
				}
			}
		}
		if(!isOwned() && rnd.nextInt(INVERSE_HACK_PROBABILITY) == 0) {
			Set<Tile> whatToHack = ((Level) owner.currentLevel).whatCanBeEviledBy(this);
			List<Tile> whatToHackList = new ArrayList<Tile>(whatToHack);
			if(whatToHackList.size() > 0) {
				((Level) owner.currentLevel).evil(whatToHackList.get(rnd.nextInt(whatToHackList.size())));
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
		if(yoursLeft > 0) {
			if(yoursLeft == 1) {
				//sprite = new Sprite(32, x, y, false);
				riCliMenuOptions[0] = "Unhack";
				hackersOwned++;
			}
			((Level) owner.currentLevel).hackHacker(this);
			yoursLeft--;
		}
	}
	@Override
	public void beOwned() {
		if(!beingUnhacked) {
			sprite = new Sprite(43, x, y, false);
		}
	}
	public void unHack() {
		if(yoursLeft < TRIES_TO_HACK) {
			initiatedUnhacking = true;
			sprite = new Sprite(44, x, y, false);
			riCliMenuOptions = new String[1];
			riCliMenuOptions[0] = "Hack";
			((Level) owner.currentLevel).unhack(this);
			initiatedUnhacking= false;
			yoursLeft = TRIES_TO_HACK;
			hackersOwned--;
		}
	}
	@Override
	public int hackCost() {
		return -10;
	}
	public boolean isOwned() {
		return yoursLeft <= 0;
	}
	@Override
	public boolean isEvil() {
		return true;
	}
	public int inverseUnhackProbability() {
		if(yoursLeft <= 0) {
			return 400;
		} else {
			return 20;
		}
	}

}
