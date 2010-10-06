package org.gwt.mosaic2g.client.scene.layout;

import org.gwt.mosaic2g.binding.client.Property;
import org.gwt.mosaic2g.client.scene.Container;
import org.gwt.mosaic2g.client.scene.Scene;
import org.gwt.mosaic2g.client.scene.Show;

import com.google.gwt.i18n.client.BidiUtils;
import com.google.gwt.i18n.client.HasDirection.Direction;
import com.google.gwt.user.client.ui.HasAutoHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

public abstract class AbstractLayout extends Container implements
		HasAutoHorizontalAlignment, HasVerticalAlignment {

	private HorizontalAlignmentConstant horzAlign = null;

	private VerticalAlignmentConstant vertAlign = null;

	private AutoHorizontalAlignmentConstant autoHorizontalAlignment;
	private boolean autoHorzAlignChanged;
	
	private boolean equalSize = false;

	public AbstractLayout(Show show, Property<Integer> x, Property<Integer> y,
			Property<Integer> width, Property<Integer> height) {
		super(show, x, y, width, height);
	}
	
	@Override
	public boolean nextFrame(Scene scene) {
		changed = (changed || super.nextFrame(scene));
		if (autoHorzAlignChanged) {
			HorizontalAlignmentConstant align;
			if (autoHorizontalAlignment == null) {
				align = horzAlign;
			} else if (autoHorizontalAlignment instanceof HorizontalAlignmentConstant) {
				align = (HorizontalAlignmentConstant) autoHorizontalAlignment;
			} else {
				/*
				 * autoHorizontalAlignment is a truly automatic policy, i.e.
				 * either ALIGN_CONTENT_START or ALIGN_CONTENT_END
				 */
				Direction sceneDir = BidiUtils.getDirectionOnElement(scene
						.getElement());
				align = (autoHorizontalAlignment == ALIGN_CONTENT_START) ? HorizontalAlignmentConstant
						.startOf(sceneDir) : HorizontalAlignmentConstant
						.endOf(sceneDir);
			}
			if (horzAlign != align) {
				horzAlign = align;
				markAsChanged();
			}
			System.out.println(horzAlign.getTextAlignString());
			autoHorzAlignChanged = false;
		}
		return changed;
	}

	public boolean isEqualSize() {
		return equalSize;
	}

	public void setEqualSize(boolean equalSize) {
		this.equalSize = equalSize;
		changed = true;
	}

	public HorizontalAlignmentConstant getHorizontalAlignment() {
		return horzAlign;
	}

	public void setHorizontalAlignment(HorizontalAlignmentConstant align) {
		if (horzAlign == align) {
			return;
		}
		setAutoHorizontalAlignment(horzAlign = align);
	}

	public AutoHorizontalAlignmentConstant getAutoHorizontalAlignment() {
		return autoHorizontalAlignment;
	}

	public void setAutoHorizontalAlignment(
			AutoHorizontalAlignmentConstant autoHorizontalAlignment) {
		if (this.autoHorizontalAlignment == autoHorizontalAlignment) {
			return;
		}
		this.autoHorizontalAlignment = autoHorizontalAlignment;
		this.autoHorzAlignChanged = true;
	}

	public VerticalAlignmentConstant getVerticalAlignment() {
		return vertAlign;
	}

	public void setVerticalAlignment(VerticalAlignmentConstant align) {
		if (this.vertAlign == align) {
			return;
		}
		this.vertAlign = align;
		changed = true;
	}

}
