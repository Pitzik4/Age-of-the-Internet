package pitzik4.ageOfTheInternet.tiles;

import pitzik4.ageOfTheInternet.Game;
import pitzik4.ageOfTheInternet.Level;
import pitzik4.ageOfTheInternet.Menu;
import pitzik4.ageOfTheInternet.graphics.Sprite;

public class HomeTile extends Tile {
	public static final int ANIMATION_COUNTDOWN = 4;
	private int animationCountDown = ANIMATION_COUNTDOWN;
	private int currentAnimation = 34;
	private Menu rightClickMenu = null;
	private Game owner;
	public static final int RI_CLI_MENU_WIDTH = 96;
	public static final String[] RI_CLI_MENU_OPTIONS = {"Upgrade"};
	public static final String RI_CLI_MENU_TITLE = "Home";
	
	public HomeTile(int x, int y, Game owner) {
		this.x = x;
		this.y = y;
		this.owner = owner;
		sprite = new Sprite(currentAnimation, x, y, false);
	}

	@Override
	public void tick() {
		animationCountDown--;
		if(animationCountDown <= 0) {
			animationCountDown = ANIMATION_COUNTDOWN;
			currentAnimation++;
			if(currentAnimation > 36) {
				currentAnimation = 34;
			}
			sprite = new Sprite(currentAnimation, x, y, false);
		}
		if(owner.mouseInsideOf(x, y, Sprite.SPRITE_WIDTH, Sprite.SPRITE_HEIGHT)) {
			if(owner.mouseDown) {
				if(rightClickMenu == null && ((Level) owner.currentLevel).canUpgrade()) {
					rightClickMenu = new Menu(owner, x+Sprite.SPRITE_WIDTH, y, RI_CLI_MENU_WIDTH, RI_CLI_MENU_OPTIONS, RI_CLI_MENU_TITLE);
					owner.screen.addRenderable(rightClickMenu);
				}
			}
		}
		if(rightClickMenu != null) {
			rightClickMenu.tick();
			if(rightClickMenu.buttons[0].nowClicked) {
				((Level) owner.currentLevel).upgradeHome();
				rightClickMenu.exited = true;
			}
			if(rightClickMenu.exited) {
				owner.screen.removeRenderable(rightClickMenu);
				rightClickMenu = null;
			}
		}
	}
	@Override
	public int hackCost() {
		return 0;
	}

}
