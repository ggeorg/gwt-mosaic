package gwt.mosaic.client.wtk;

/**
 * Interface representing a drop target.
 */
public interface DropTarget {

	/**
	 * Called when the mouse first enters a drop target during a drag operation.
	 * 
	 * @param compoent
	 * @param dragContent
	 * @param supportedDropActions
	 * @param userDropAction
	 * 
	 * @return The drop action that would result if the user dropped the item at
	 *         this location, or <tt>null</tt> if the target cannot accpet the
	 *         drop.
	 */
	public DropAction dragEnter(Component compoent, Manifest dragContent,
			int supportedDropActions, DropAction userDropAction);

	/**
	 * Called when the mouse leaves a drop target during a drag operation.
	 * 
	 * @param component
	 */
	public void dragExit(Component component);

	/**
	 * Called when the mouse is moved while positioned over a drop target during
	 * a drag operation.
	 * 
	 * @param component
	 * @param dragContent
	 * @param supportedDropActions
	 * @param x
	 * @param y
	 * @param userDropAction
	 * 
	 * @return The drop action that would result if the user dropped the item at
	 *         this location, or
	 *         <tt>null<tt> if the target cannot accept the drop.
	 */
	public DropAction dragMove(Component component, Manifest dragContent,
			int supportedDropActions, int x, int y, DropAction userDropAction);

	/**
	 * Called when the user drop cation changes while the mouse is positioned
	 * over a drop target during a drag operation.
	 * 
	 * @param component
	 * @param dragContent
	 * @param supportedDropActions
	 * @param x
	 * @param y
	 * @param userDropAction
	 * 
	 * @return The drop action that would result if the user dropped the item at
	 *         this location, or <tt>null</tt> if the target cannot accept the
	 *         drop.
	 */
	public DropAction userDropActionChanged(Component component,
			Manifest dragContent, int supportedDropActions, int x, int y,
			DropAction userDropAction);

	/**
	 * Called to drop the drag content.
	 * 
	 * @param component
	 * @param dragContent
	 * @param supportedDropActions
	 * @param x
	 * @param y
	 * @param userDropAction
	 * 
	 * @return The drop action used to perform the drop, or <tt>null</tt> if the
	 *         target rejected the drop.
	 */
	public DropAction drop(Component component, Manifest dragContent,
			int supportedDropActions, int x, int y, DropAction userDropAction);
}
