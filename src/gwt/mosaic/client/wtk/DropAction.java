package gwt.mosaic.client.wtk;

/**
 * Enumeration defining supported drop actions.
 */
public enum DropAction {
	COPY,
	MOVE,
	LINK;
	
	public int getMask() {
		return 1 << ordinal();
	}
	
	public boolean isSelected(int dropActions) {
		return ((dropActions & getMask()) > 0);
	}
}
