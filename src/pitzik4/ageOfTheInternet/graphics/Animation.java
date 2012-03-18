package pitzik4.ageOfTheInternet.graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import pitzik4.ageOfTheInternet.RenderableTickable;

public class Animation implements RenderableTickable {
	private Renderable[] frames;
	private int[] durations;
	private int currentFrame = 0;
	private int countDown = 0;
	private int x=0, y=0;
	private boolean looping=false;
	private boolean going = false;
	
	public Animation(Renderable[] frames, int[] durations, int x, int y, boolean looping) {
		this.frames = frames;
		this.durations = durations;
		countDown = durations[currentFrame];
		this.x = x;
		this.y = y;
		this.looping = looping;
	}
	public Animation(Renderable[] frames, int duration, int x, int y, boolean looping) {
		this(frames, new int[frames.length], x, y, looping);
		int[] durs = new int[frames.length];
		for(int i=0; i<durs.length; i++) {
			durs[i] = duration;
		}
		durations = durs;
	}
	public Animation(int[] frames, int[] durations, int x, int y, boolean looping) {
		this(new Renderable[frames.length], durations, x, y, looping);
		for(int i=0; i<this.frames.length; i++) {
			this.frames[i] = new Sprite(frames[i], x, y, false);
		}
	}
	public Animation(int[] frames, int duration, int x, int y, boolean looping) {
		this(new Renderable[frames.length], duration, x, y, looping);
		for(int i=0; i<this.frames.length; i++) {
			this.frames[i] = new Sprite(frames[i], x, y, false);
		}
	}

	@Override
	public BufferedImage draw() {
		return frames[currentFrame].draw();
	}

	@Override
	public void drawOn(Graphics2D g, int scrollx, int scrolly) {
		frames[currentFrame].drawOn(g, scrollx, scrolly);
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
		return frames[currentFrame].getXOffset();
	}

	@Override
	public int getYOffset() {
		return frames[currentFrame].getYOffset();
	}

	@Override
	public void goTo(int x, int y) {
		int dx = x - this.x;
		int dy = y - this.y;
		this.x = x;
		this.y = y;
		for(Renderable r : frames) {
			r.goTo(r.getX()+dx, r.getY()+dy);
		}
	}
	public void go() {
		going = true;
	}
	public void stop() {
		going = false;
	}
	@Override
	public void tick() {
		if(going) {
			countDown--;
			if(countDown <= 0) {
				currentFrame++;
				if(currentFrame >= frames.length) {
					if(looping) {
						currentFrame = 0;
					} else {
						going = false;
						currentFrame--;
						return;
					}
				}
				countDown = durations[currentFrame];
			}
		}
	}

}
