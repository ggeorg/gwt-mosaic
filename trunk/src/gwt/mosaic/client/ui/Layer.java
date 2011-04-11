package gwt.mosaic.client.ui;

abstract class Layer extends LayoutPanel {
	private static final Dimensions zeroDimensions = new Dimensions();

	@Override
	public int getPreferredWidth(int height) {
		return 0;
	}

	@Override
	public int getPreferredHeight(int width) {
		return 0;
	}

	@Override
	public Dimensions getPreferredSize() {
		return zeroDimensions;
	}

	@Override
	public int getBaseline(int width, int height) {
		return 0;
	}

	@Override
	protected void doLayout() {
		// do nothing
	}

}
