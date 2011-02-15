package gwt.mosaic.client.wtk;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * Interface defining a "skin". A skin is the graphical representation of a
 * component. In MVC terminology, a skin represents the "view" of the "model"
 * data provided by a component. Components delegate a number of methods to the
 * skin, including all methods defined by the <tt>Visual</tt> interface as well
 * as style properties and layout. In conjunction with renderers
 * (implementations of the <tt>Renderer</tt> interface), skins define the
 * overall look and feel of an application.
 * <p>
 * Skins are primarily responsible for the following:
 * <p>
 * TODO
 * <p>
 * Skins will often change their appearnace in response to events fired by the
 * component but may also be in response to input events (e.g. keyboard, mouse).
 * Skins are not required to register for such events - skin base classes
 * implement all relevant listener interfaces and the component calls them as
 * appropriate.
 * <p>
 * Skins may (but are not required to) expose internal properties that affect
 * the appearance of the component as "style properties", similar to CSS styles.
 * For example a component might provide styles to let a caller set the
 * foreground color and font. Since callers are not allowed to interact with a
 * component's skin directly, access to styles is via the component's styles
 * collection, which delegates to the diectionary methods in the installed skin.
 * Skins are responsible for invalidating or repainting the component as
 * appropriate in response to events and style changes.
 */
public interface Skin extends ConstrainedVisual, IsWidget {

	/**
	 * Associates a skin with a component.
	 * 
	 * @param component
	 *            The component to which the skin is being attached.
	 */
	public void install(Component component);

	/**
	 * Returns the component with which a skin is associated.
	 */
	public Component getComponent();

	/**
	 * If the component on which the skin is installed is a container, lays out
	 * the container's children.
	 */
	public void layout();

	/**
	 * Returns the skin's focusable state.
	 * 
	 * @return <tt>true</tt> if this skin is focusable; <tt>false</tt>,
	 *         otherwise.
	 */
	public boolean isFocusable();

}
