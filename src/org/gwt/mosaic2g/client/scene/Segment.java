package org.gwt.mosaic2g.client.scene;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import com.google.gwt.user.client.Command;

public final class Segment implements Node, HasFeatures {

	private final FeatureCollection activeFeatures = new FeatureCollection(this);
	private final Map<Feature, Boolean> featureWasActiveted = new HashMap<Feature, Boolean>();
	// XXX do we really need this map?

	private Command onEntryCommand;
	private Command onExitCommand;

	private boolean active = false;

	// # of features in setup clause and activate clause that have
	// been checked so far for setup
	private int setupCheckedInActive;

	private ActivateSegmentCommand cmdToActivate;
	private ActivateSegmentCommand cmdToActivatePush;

	private final Show show;

	public Segment(Show show) {
		this(show, null, null);
	}

	public Segment(Show show, Command onEntryCommand, Command onExitCommand) {
		this.show = show;
		this.onEntryCommand = onEntryCommand;
		this.onExitCommand = onExitCommand;
	}

	public Show getShow() {
		return show;
	}

	public Command getOnEntryCommand() {
		return onEntryCommand;
	}

	public void setOnEntryCommand(Command onEntryCommand) {
		this.onEntryCommand = onEntryCommand;
	}

	public Command getOnExitCommand() {
		return onExitCommand;
	}

	public void setOnExitCommand(Command onExitCommand) {
		this.onExitCommand = onExitCommand;
	}

	public boolean isActive() {
		return active;
	}

	/**
	 * Used by {@link Show}, never call this method directly.
	 */
	Command getCommandToActivate(boolean push) {
		if (push) {
			if (cmdToActivatePush == null) {
				cmdToActivatePush = new ActivateSegmentCommand(this, true,
						false);
			}
			return cmdToActivatePush;
		} else {
			if (cmdToActivate == null) {
				cmdToActivate = new ActivateSegmentCommand(this);
			}
			return cmdToActivate;
		}
	}

	/**
	 * Used by {@link Show}, never call this method directly.
	 * 
	 * @param lastSegment
	 *            the last segment we're coming from
	 */
	void activate(Segment lastSegment) {
		if (lastSegment == this) {
			return;
		}

		active = true;

		setupCheckedInActive = 0;

		Iterator<Feature> it = activeFeatures.iterator();
		while (it.hasNext()) {
			final Feature f = it.next();
			f.setup();
			if (!f.needsMoreSetup()) {
				f.activate();
				featureWasActiveted.put(f, Boolean.TRUE);
			}
		}

		if (lastSegment != null) {
			lastSegment.deactivate();
		}
		if (onEntryCommand != null) {
			show.runCommand(onEntryCommand);
		}

		runFeatureSetup();
	}

	private boolean getFeatureWasActiveted(Feature f) {
		if (featureWasActiveted.containsKey(f)) {
			return featureWasActiveted.get(f);
		}
		return false;
	}

	/**
	 * When a feature is setup, we get this call. We have to be a little
	 * conservative: it's possible that a feature from a previous, stale segment
	 * could finish its setup after we become the current segment, so this call
	 * really means: one of our features probably finished setup, but we'd
	 * better check to be sure .
	 */
	void runFeatureSetup() {
		// check to see if all active features are set up
		final int length = activeFeatures.size();
		if (setupCheckedInActive < length) {
			while (setupCheckedInActive < length) {
				final Feature f = activeFeatures.get(setupCheckedInActive);
				if (!getFeatureWasActiveted(f) && f.needsMoreSetup()) {
					return;
				}
				++setupCheckedInActive;
			}
			Iterator<Feature> it = activeFeatures.iterator();
			while (it.hasNext()) {
				final Feature f = it.next();
				if (!getFeatureWasActiveted(f)) {
					f.activate();
					featureWasActiveted.put(f, Boolean.TRUE);
				}
			}
		}
	}

	/**
	 * Called when another segment is activated, and called on the active
	 * segment when the show is destroyed.
	 */
	void deactivate() {
		active = false;
		Iterator<Feature> it = activeFeatures.iterator();
		while (it.hasNext()) {
			final Feature f = it.next();
			if (getFeatureWasActiveted(f)) {
				f.deactivate();
				featureWasActiveted.put(f, Boolean.FALSE);
			}
			f.unsetup();
		}

		if (onExitCommand != null) {
			show.runCommand(onExitCommand);
		}
	}

	public void nextFrameForActiveFeatures(Scene scene) {
		Iterator<Feature> it = activeFeatures.iterator();
		while (it.hasNext()) {
			it.next().nextFrame(scene);
		}
	}

	public void paintFrame(Scene scene) {
		Iterator<Feature> it = activeFeatures.iterator();
		while (it.hasNext()) {
			it.next().paintFrame(scene);
		}
	}

	public void add(Feature f) {
		activeFeatures.add(f);
	}

	public void clear() {
		Iterator<Feature> it = iterator();
		while (it.hasNext()) {
			it.next();
			it.remove();
		}
	}

	public Iterator<Feature> iterator() {
		return activeFeatures.iterator();
	}

	public boolean remove(Feature f) {
		try {
			activeFeatures.remove(f);
		} catch (NoSuchElementException ex) {
			return false;
		}
		return true;
	}

}
