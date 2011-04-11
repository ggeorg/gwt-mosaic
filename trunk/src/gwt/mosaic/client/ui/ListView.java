package gwt.mosaic.client.ui;

import java.util.List;

import com.google.gwt.user.client.ui.Composite;

/**
 * Widget that displays a sequence of items, optionally allowing a user
 * to select or check one or more items.
 */
public class ListView extends Composite {

	/**
	 * Enumeration defining supported selection modes.
	 */
	public enum SelectMode {
		/**
		 * Selection is disabled.
		 */
		NONE,
		
		/**
		 * A single index may be selected at a time.
		 */
		SINGLE,
		
		/**
		 * Multiple indexes may be concurrently selected.
		 */
		MULTI
	}
	
	private List<?> listData = null;
	
	private boolean checkmarksEnabled = false;
}
