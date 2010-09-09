package org.gwt.mosaic2g.client.scene;

import org.gwt.mosaic2g.client.util.Rectangle;

/**
 * Represents a clipped version of another feature. When painting, a clipped
 * rectangle is set;
 * 
 * @author Bill Foote (http://jovial.com)
 * @author ggeorg
 */
public class Clipped extends Modifier {

	private Rectangle clipRegion;

	private Rectangle lastClipRegion = new Rectangle();
	private Rectangle tmpI = null;

	public Clipped(Show show, Rectangle clipRegion) {
		super(show);
		this.clipRegion = clipRegion;
	}

	@Override
	public void paintFrame(Scene scene) {
		lastClipRegion.x = Integer.MIN_VALUE;
		scene.getClipBounds(lastClipRegion);
		if (lastClipRegion.x == Integer.MIN_VALUE) {
			scene.setClipBounds(clipRegion);
			getPart().paintFrame(scene);
			scene.setClipBounds(null);
		} else {
			if (tmpI == null) {
				tmpI = new Rectangle(); // Holds intersection
			}
			tmpI.setBounds(lastClipRegion);
			if (tmpI.x < clipRegion.x) {
				tmpI.width -= clipRegion.x - tmpI.x;
				tmpI.x = clipRegion.x;
			}
			if (tmpI.y < clipRegion.y) {
				tmpI.height -= clipRegion.y - tmpI.y;
				tmpI.y = clipRegion.y;
			}
			if (tmpI.x + tmpI.width > clipRegion.x + clipRegion.width) {
				tmpI.width = clipRegion.x + clipRegion.width - tmpI.x;
			}
			if (tmpI.y + tmpI.height > clipRegion.y + clipRegion.height) {
				tmpI.height = clipRegion.y + clipRegion.height - tmpI.y;
			}
			if (tmpI.width > 0 && tmpI.height > 0) {
				scene.setClipBounds(tmpI);
				getPart().paintFrame(scene);
				scene.setClipBounds(lastClipRegion);
			}
		}
	}

}
