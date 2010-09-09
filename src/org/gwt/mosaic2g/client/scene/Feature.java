package org.gwt.mosaic2g.client.scene;

import com.google.gwt.user.client.Command;

public abstract class Feature implements Node {

	private final Show show;

	private int activateCount = 0;
	private int setupCount = 0;

	protected boolean changed = false;

	protected Feature(Show show) {
		assert show != null;
		this.show = show;
	}

	public Show getShow() {
		return show;
	}

	/**
	 * Get the upper-left hand corner of this feature as presently displayed.
	 * Return Integer.MAX_VALUE if this feature has no visible representation.
	 * 
	 * @return the x coordinate
	 */
	public int getX() {
		return Integer.MAX_VALUE;
	}

	/**
	 * Get the upper-left hand corner of this feature as presently displayed
	 * Return Integer.MAX_VALUE if this feature has no visible representation.
	 * 
	 * @return the y coordinate
	 **/
	public int getY() {
		return Integer.MAX_VALUE;
	}

	/**
	 * Get the width of this feature as presently displayed. Return
	 * Integer.MIN_VALUE if this feature has no visible representation.
	 * 
	 * @return the width
	 */
	public int getWidth() {
		return Integer.MIN_VALUE;
	}

	public void resize(int width, int height) {
		// do nothing
	}

	/**
	 * Get the height of this feature as presently displayed. Return
	 * Integer.MIN_VALUE if this feature has no visible representation.
	 * 
	 * @return the height
	 */
	public int getHeight() {
		return Integer.MIN_VALUE;
	}

	protected void setHeight() {
		// do nothing
	}

	/**
	 * Called from {@link Segment} to advance us to the state we should be in
	 * for the next frame. Never call this method directly.
	 */
	public abstract boolean nextFrame(Scene scene);

	/**
	 * Paint the current state of this feature to the {@link scene}. Never call
	 * this method directly.
	 */
	public void paintFrame(Scene scene) {
		if (!isActivated() || !changed) {
			return;
		}
	}

	/**
	 * Mark this feature as modified for the next call to
	 * {@link #paintFrame(Scene)}. This can be called by a parent node on its
	 * children, e.g. by setting a new alpha value.
	 */
	public void markAsChanged() {
		changed = true;
	}

	/**
	 * Called by the show when it's time to begin setting up this feature. This
	 * might be called from the show multiple times; each call will eventually
	 * be matched by a call to {@link #unsetup()}. Never call this method
	 * directly.
	 * 
	 * @see #unsetup()
	 */
	void setup() {
		if (++setupCount == 1) {
			setSetupMode(true);
		}
	}

	/**
	 * Change the setup mode of this feature. The new mode will be always be
	 * different than the old. Custom feature extensions must implements this
	 * method.
	 * 
	 * @param b
	 *            the setup mode of this feature
	 */
	protected abstract void setSetupMode(boolean mode);

	/**
	 * Called by the show when this feature is no longer needed by whatever
	 * contains it. When the last call to {@link #setup()} has been matched by a
	 * call to {@link #unsetup()}, it's time to unload this feature's assets.
	 * 
	 * @see #setup()
	 */
	void unsetup() {
		if (--setupCount == 0) {
			setSetupMode(false);
		}
	}

	/**
	 * Check to see if this feature has been set up, or has a pending request to
	 * be set up.
	 * 
	 * @return {@code true} if this feature has been set up or has a pending
	 *         request to be set up; {@code false} otherwise
	 */
	public final boolean isSetup() {
		return setupCount > 0;
	}

	/**
	 * This is where the feature says whether or not it needs more setup. The
	 * implementation of this method must not call any outside code or call any
	 * show/animation methods. For a given setup cycle, this method is called
	 * only after setup(). Clients should never call this method directly.
	 * 
	 * @return whether or not the feature needs more setup
	 */
	protected boolean needsMoreSetup() {
		return false;
	}

	/**
	 * Called by the show when this feature becomes activated, that is, it
	 * starts presenting. That nest, so this can be called multiple times. When
	 * the last call to {@link #activate()} is undone by a call to
	 * {@link #deactivate()}, that means this feature is no longer being shown.
	 * Never call this method directly.
	 * 
	 * @see #deactivate()
	 */
	void activate() {
		if (++activateCount == 1) {
			setActivateMode(true);
		}
	}

	/**
	 * Change the activated mode of this feature. The new mode will always be
	 * different than the old. Never call this method directly. Custom feature
	 * extensions must implement this method.
	 * 
	 * @param mode
	 *            the activated mode of this feature
	 */
	protected abstract void setActivateMode(boolean mode);

	/**
	 * Called by the show when this feature is no longer being presented by
	 * whatever contains it.
	 * 
	 * @see #activate()
	 */
	void deactivate() {
		if (--activateCount == 0) {
			setActivateMode(false);
		}
	}

	/**
	 * Check to see if this feature has been activated.
	 * 
	 * @return {@code true} if this feature has been activated; {@code false}
	 *         otherwise
	 */
	public final boolean isActivated() {
		return activateCount > 0;
	}

	private Command featureSetupCommand = null;

	/**
	 * When a feature finishes its setup, it should call this to tell the show
	 * about it.
	 */
	protected void sendFeatureSetup() {
		if (featureSetupCommand == null) {
			featureSetupCommand = new FeatureSetupCommand(show);
		}
		show.runCommand(featureSetupCommand);
	}

}
