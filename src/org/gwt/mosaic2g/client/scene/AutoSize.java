package org.gwt.mosaic2g.client.scene;

import com.google.gwt.user.client.Command;

public class AutoSize extends Modifier {

	public static final int WIDTH_SIZABLE = 1;
	public static final int HEIGHT_SIZABLE = 1 << 1;

	public static final int MIN_X_MARGIN = 1 << 2;
	public static final int MIN_Y_MARGIN = 1 << 3;
	public static final int MAX_X_MARGIN = 1 << 4;
	public static final int MAX_Y_MARGIN = 1 << 5;

	private final int width, height;
	private final int flags;

	/**
	 * The part's initial width and height.
	 */
	private int partWidth, partHeight;

	private int lastDx, lastDy, lastW, lastH;

	public AutoSize(Show show, int width, int height, int flags) {
		super(show);
		this.width = check(width);
		this.height = check(height);
		this.flags = flags;
	}

	private int check(int v) {
		if (v <= 0) {
			throw new IllegalArgumentException();
		}
		return v;
	}

	@Override
	public void setPart(Feature part) {
		super.setPart(part);
	}

	protected void setActivateMode(boolean mode) {
		if (mode) {
			partWidth = getPart().getWidth();
			partHeight = getPart().getHeight();
			lastDx = lastDy = Integer.MAX_VALUE;
			lastW = lastH = Integer.MIN_VALUE;
		} else {
			// restore the part's initial width & height
			getPart().resize(partWidth, partHeight);
		}
		super.setActivateMode(mode);
	}

	@Override
	public boolean nextFrame(Scene scene) {
		changed = super.nextFrame(scene);

		if (partWidth == Integer.MIN_VALUE || partHeight == Integer.MIN_VALUE) {
			scene.getShow().runCommand(new Command() {
				public void execute() {
					partWidth = getPart().getWidth();
					partHeight = getPart().getHeight();
					changed = true;
				}
			});
			return false;
		}

		if (scene.isSizeChanged() || lastDx == Integer.MAX_VALUE
				|| lastDy == Integer.MAX_VALUE || lastW == Integer.MIN_VALUE
				|| lastH == Integer.MIN_VALUE) {
			final Feature f = getPart();
			final int fwidth = partWidth;
			final int fheight = partHeight;
			if (width != Integer.MIN_VALUE && height != Integer.MIN_VALUE) {
				
				final int clientWidth = scene.getElement().getClientWidth();
				final int clientHeight = scene.getElement().getClientHeight();
				if (clientWidth == 0 || clientHeight == 0) {
					return changed;
				}
				
				final int ftop = f.getY();
				final int fleft = f.getX();

				final int fright = width - (fleft + fwidth);
				final int fbottom = height - (ftop + fheight);

				int dx = 0;
				int dy = 0;
				int w = fwidth;
				int h = fheight;

				if ((flags & WIDTH_SIZABLE) != 0) {
					w = clientWidth - (fleft + fright);
				}

				if ((flags & HEIGHT_SIZABLE) != 0) {
					h = clientHeight - (ftop + fbottom);
				}

				if ((flags & MIN_X_MARGIN) != 0 && (flags & MAX_X_MARGIN) != 0) {
					dx = ((clientWidth - w) / 2) + (fleft - fright) - fleft;
				} else if ((flags & MIN_X_MARGIN) != 0) {
					dx = (clientWidth - fright - w) - fleft;
				} else if ((flags & MAX_X_MARGIN) != 0) {
					dx = 0;
				}

				if ((flags & MIN_Y_MARGIN) != 0 && (flags & MAX_Y_MARGIN) != 0) {
					dy = ((clientHeight - h) / 2) + (ftop - fbottom) - ftop;
				} else if ((flags & MIN_Y_MARGIN) != 0) {
					dy = (clientHeight - fbottom - h) - ftop;
				} else if ((flags & MAX_Y_MARGIN) != 0) {
					dy = 0;
				}

				if ((w >= 0 && h >= 0)
						&& (dx != lastDx || dy != lastDy || w != lastW || h != lastH)) {
					markAsChanged();
					lastDx = dx;
					lastDy = dy;
					lastW = w;
					lastH = h;
				}
			}
		}

		return changed;
	}

	@Override
	public void paintFrame(Scene scene) {
		if (!isActivated() || !changed) {
			return;
		}

		getPart().resize(lastW, lastH);

		scene.translate(lastDx, lastDy);
		super.paintFrame(scene);
		scene.translate(-lastDx, -lastDy);

		changed = false;
	}
}
