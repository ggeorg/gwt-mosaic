package org.gwt.mosaic2g.client.animator;

import org.gwt.mosaic2g.client.scene.Show;

public interface AnimationClient {

	/**
	 * Advance the state of the {@link Show} to the next frame. This is called
	 * once per frame; the first time it is called can be considered "frame 0",
	 * and can monotonically increase from there.
	 * <p>
	 * This method can be called multiple times before any attempt is made to
	 * display the UI state. This happens when animation falls behind; the
	 * {@link AnimationEngine} catches up by skipping frames. Animation clients
	 * should only perform quick updates in this method; any more time-consuming
	 * calculations should be deferred until an object is first painted for a
	 * given frame.
	 */
	void nextFrame();

	/**
	 * Indicate to the animation client that we're not behind in the animation,
	 * so that the current frame will actually be displayed. Clients shouldn't
	 * make any changes to the model in this call; all such changes need to
	 * happen in the nextFrame().
	 * 
	 * @see #nextFrame()
	 */
	void setCaughtUp();

	/**
	 * Paint the current frame of the animation.
	 * 
	 * @param gc
	 *            the graphics context
	 */
	void paintFrame(/* Graphics gc */);

}
