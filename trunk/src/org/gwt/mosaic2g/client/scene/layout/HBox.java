package org.gwt.mosaic2g.client.scene.layout;

import java.util.Iterator;

import org.gwt.mosaic2g.client.scene.Container;
import org.gwt.mosaic2g.client.scene.Feature;
import org.gwt.mosaic2g.client.scene.Scene;
import org.gwt.mosaic2g.client.scene.Show;
import org.gwt.mosaic2g.client.util.Rectangle;

public class HBox extends Container {

	private int count;

	public HBox(Show show) {
		this(show, 0, 0, Integer.MIN_VALUE, Integer.MIN_VALUE);
	}

	public HBox(Show show, int x, int y) {
		this(show, x, y, Integer.MIN_VALUE, Integer.MIN_VALUE);
	}

	public HBox(Show show, int x, int y, int width, int height) {
		super(show, x, y, width, height);
	}

	private int spacing = 6;

	public int getSpacing() {
		return spacing;
	}

	public void setSpacing(int spacing) {
		this.spacing = spacing;
		markAsChanged();
	}

	@Override
	public int getPrefWidth() {
		count = -1;

		int result = 0;
		Iterator<Feature> it = iterator();
		while (it.hasNext()) {
			final Feature f = it.next();
			if (f instanceof Resizable) {
				result += ((Resizable) f).getPrefWidth();
			} else {
				int val = f.getWidth();
				if (val == Integer.MIN_VALUE) {
					continue;
				}
				result += val;
			}
			++count;
		}

		if (count > 0) {
			result += spacing * count;
		}

		return result;
	}

	@Override
	public void paintFrame(Scene scene) {
		if (!isActivated() || !changed) {
			return;
		}

		int direction = 0;

		final Rectangle bounds = new Rectangle(getX(), getY(), getWidth(),
				getHeight());

		int startX, startY;
		if (direction > 0) {
			startX = bounds.x;
		} else if (direction < 0) {
			startX = bounds.x + (bounds.width - getPrefWidth());
		} else {
			startX = bounds.x + (bounds.width - getPrefWidth()) / 2;
		}
		startY = bounds.y;

		Iterator<Feature> it = iterator();
		while (it.hasNext()) {
			final Feature f = it.next();

			final int fw = f.getWidth();
			final int fh = f.getHeight();
			if (fw == Integer.MIN_VALUE || fh == Integer.MIN_VALUE) {
				continue;
			}

			int dx = startX - f.getX();
			int dy = startY - f.getY();

			f.resize(fw, fh);

			scene.translate(dx, dy);
			f.paintFrame(scene);
			scene.translate(-dx, -dy);

			startX += fw + spacing;
		}

		changed = false;
	}

}
