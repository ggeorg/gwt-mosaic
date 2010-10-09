package org.gwt.mosaic2g.client.scene.control;

import org.gwt.mosaic2g.binding.client.AbstractBinder;
import org.gwt.mosaic2g.binding.client.Converter;
import org.gwt.mosaic2g.binding.client.Property;
import org.gwt.mosaic2g.client.MyClientBundle;
import org.gwt.mosaic2g.client.scene.Assembly;
import org.gwt.mosaic2g.client.scene.Group;
import org.gwt.mosaic2g.client.scene.InterpolatedModel;
import org.gwt.mosaic2g.client.scene.Show;

public class ProgressBar extends Assembly {

	private final Group enabled;
	private final Group collapsed;

	private final Box bgBox, fgBox;

	/**
	 * A number from 0.0 to 1.0 that indicates the amount of progress.
	 */
	private final Property<Double> progress = new Property<Double>(
			new AbstractBinder<Double>() {
				Double value = 0.0;

				@Override
				public Double get() {
					return value;
				}

				public void set(Double value) {
					if (value != this.value) {
						if (value < 0.0) {
							value = 0.0;
						} else if (value > 1.0) {
							value = 1.0;
						}
						if (value != this.value) {
							this.value = value;
							fireValueChangeEvent(this.value);
						}
					}
				}

				@Override
				public boolean isReadOnly() {
					return false;
				}
			});

	public ProgressBar(Show show) {
		super(show);

		enabled = new Group(show);
		collapsed = new Group(show);

		bgBox = new Box(show);
		bgBox.getX().bind(getX());
		bgBox.getY().bind(getY());
		bgBox.getWidth().bind(getWidth());
		bgBox.getHeight().bind(getHeight());

		fgBox = new Box(show);
		fgBox.getX().bind(getX());
		fgBox.getY().bind(getY());
		fgBox.getWidth().bindReadOnly(progress, new Converter<Double, Integer>() {
			@Override
			public Integer convertForward(Double value) {
				if (value != null) {
					int width = bgBox.getWidth().$();
					if (width == Integer.MIN_VALUE) {
						width = bgBox.getPrefWidth();
					}
					width = Math.max(0, width);
					return (int) (value * width);
				} else {
					return 0;
				}
			}
		});
		fgBox.getHeight().bind(getHeight());

		enabled.add(bgBox);
		enabled.add(fgBox);

		add(enabled);
		add(collapsed);

		MyClientBundle.INSTANCE.css().ensureInjected();
		bgBox.setStyleName(MyClientBundle.INSTANCE.css().progressBarBgBox());
		fgBox.setStyleName(MyClientBundle.INSTANCE.css().progressBarFgBox());
	}

	public Property<Double> getProgress() {
		return progress;
	}

}
