package gwt.mosaic.client.wtk;

/**
 * Component listener interface.
 */
public interface ComponentListener {

	/**
	 * Component listener adapter.
	 */
	public static class Adapter implements ComponentListener {
		@Override
		public void parentChanged(Component component, Container previousParent) {
		}

		@Override
		public void sizeChanged(Component component, int previousWidth,
				int previousHeight) {
		}

		@Override
		public void preferredSizeChanged(Component component,
				int previousPreferredWidth, int previousPreferredHeight) {
		}

		@Override
		public void widthLimitsChanged(Component component,
				int previousMinimumWidth, int previousMaximumWidth) {
		}

		@Override
		public void heightLimitsChanged(Component component,
				int previousMinimumHeight, int previousMaximumHeight) {
		}

		@Override
		public void locationChanged(Component component, int previousX,
				int previousY) {
		}

		@Override
		public void visibleChanged(Component component) {
		}

		@Override
		public void cursorChanged(Component component, Cursor previousCursor) {
		}

		@Override
		public void tooltipTextChanged(Component component,
				String previousTooltipText) {
		}

		// @Override
		// TODO public void tooltipDelayChanged(Component component, int previousTooltipDelay)

		@Override
		public void dragSourceChanged(Component component,
				DragSource previousDragSource) {
		}

		@Override
		public void dropTargetChanged(Component component,
				DropTarget previousDropTarget) {
		}

		@Override
		public void menuHandlerChanged(Component component,
				MenuHandler previousMenuHandler) {
		}

		@Override
		public void nameChanged(Component component, String previousName) {
		}
	}

	/**
	 * Called when a component's parent has changed (when the component is
	 * either added to or removed from a container).
	 * 
	 * @param component
	 * @param previousParent
	 */
	public void parentChanged(Component component, Container previousParent);

	/**
	 * Called when the component's size has changed.
	 * 
	 * @param component
	 * @param previousWidth
	 * @param previousHeight
	 */
	public void sizeChanged(Component component, int previousWidth,
			int previousHeight);

	/**
	 * Called when a component's preferred size has changed.
	 * 
	 * @param component
	 * @param previousPreferredWidth
	 * @param previousPreferredHeight
	 */
	public void preferredSizeChanged(Component component,
			int previousPreferredWidth, int previousPreferredHeight);

	/**
	 * Called when a component's preferred width limits have changed.
	 * 
	 * @param component
	 * @param previousMinimumWidth
	 * @param previousMaximumWidth
	 */
	public void widthLimitsChanged(Component component,
			int previousMinimumWidth, int previousMaximumWidth);

	/**
	 * Called when a component;s preferred height limits have changed.
	 * 
	 * @param component
	 * @param previousMinimumHeight
	 * @param previousMaximumHeight
	 */
	public void heightLimitsChanged(Component component,
			int previousMinimumHeight, int previousMaximumHeight);

	/**
	 * Called when a component's location has changed.
	 * 
	 * @param component
	 * @param previousX
	 * @param previousY
	 */
	public void locationChanged(Component component, int previousX,
			int previousY);

	/**
	 * Called when a component's visible flag has changed.
	 * 
	 * @param component
	 */
	public void visibleChanged(Component component);

	/**
	 * Called when the component's cursor has changed.
	 * 
	 * @param component
	 * @param previousCurosr
	 */
	public void cursorChanged(Component component, Cursor previousCurosr);

	/**
	 * Called when a component's tooltip text has changed.
	 * 
	 * @param component
	 * @param previousTooltipText
	 */
	public void tooltipTextChanged(Component component,
			String previousTooltipText);

	/**
	 * Called when a component's drag source has changed.
	 * 
	 * @param component
	 * @param previousDragSource
	 */
	public void dragSourceChanged(Component component,
			DragSource previousDragSource);

	/**
	 * Called when a component's drop target has changed.
	 * 
	 * @param component
	 * @param previousDropTarget
	 */
	public void dropTargetChanged(Component component,
			DropTarget previousDropTarget);

	/**
	 * Called when a component's context menu handler has changed.
	 * 
	 * @param component
	 * @param previousMenuHandler
	 */
	public void menuHandlerChanged(Component component,
			MenuHandler previousMenuHandler);

	/**
	 * Called when a component's name has changed.
	 * 
	 * @param component
	 * @param previousName
	 */
	public void nameChanged(Component component, String previousName);
}
