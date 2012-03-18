package pitzik4.ageOfTheInternet.tiles;

import pitzik4.ageOfTheInternet.Game;
import pitzik4.ageOfTheInternet.Level;
import pitzik4.ageOfTheInternet.Menu;
import pitzik4.ageOfTheInternet.graphics.Sprite;

public class EndTile extends Tile {
	private Game owner;
	private Menu rightClickMenu = null;
	public static final int RI_CLI_MENU_WIDTH = 96;
	public String[] riCliMenuOptions = {"Hack"};
	public String riCliMenuTitle = "Goal";
	
	public EndTile(int x, int y, Game owner) {
		this.x = x;
		this.y = y;
		this.owner = owner;
		sprite = new Sprite(37, x, y, false);
	}

	@Override
	public void tick() {
		if(owner.mouseInsideOf(x, y, Sprite.SPRITE_WIDTH, Sprite.SPRITE_HEIGHT)) {
			if(owner.mouseDown) {
				if(rightClickMenu == null) {
					if(((Level) owner.currentLevel).canBeHackedBy(this) != null && ((Level) owner.currentLevel).getRAM() >= hackCost()) {
						rightClickMenu = new Menu(owner, x+Sprite.SPRITE_WIDTH, y, RI_CLI_MENU_WIDTH, riCliMenuOptions, riCliMenuTitle);
						owner.screen.addRenderable(rightClickMenu);
					}
				}
			}
		}
		if(rightClickMenu != null) {
			rightClickMenu.tick();
			if(rightClickMenu.buttons[0].nowClicked) {
				hack();
				rightClickMenu.exited = true;
			}
			if(rightClickMenu.exited) {
				owner.screen.removeRenderable(rightClickMenu);
				rightClickMenu = null;
			}
		}
	}
	public void hack() {
		((Level) owner.currentLevel).hack(this);
	}
	@Override
	public void beOwned() {
		((Level) owner.currentLevel).win();
	}
	@Override
	public int hackCost() {
		return 10;
	}

}
