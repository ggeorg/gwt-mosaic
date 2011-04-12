package gwt.mosaic.client.effects;

public abstract class Translator implements AnimationClient {

	public static final int OFFSCREEN = Integer.MAX_VALUE;

	public final static int X_FIELD = 0;
	public final static int Y_FIELD = 1;

	private final InterpolatedModel model;

	private transient int lastDx;
	private transient int lastDy;

	private transient boolean changed = false;

	public Translator() {
		this(InterpolatedModel.makeDefaultTranslatorModel());
	}

	public Translator(InterpolatedModel model) {
		this.model = model;
	}

	public InterpolatedModel getModel() {
		return model;
	}

	@Override
	public void nextFrame() {
		model.nextFrame();

		int dx = model.getField(X_FIELD);
		int dy = model.getField(Y_FIELD);

		if (dx != OFFSCREEN && dy != OFFSCREEN && dx != lastDx || dy != lastDy) {
			lastDx = dx;
			lastDy = dy;
			changed = true;
		}
	}

	@Override
	public void update() {
		if (changed) {
			update(lastDx, lastDy);
			changed = false;
		}
	}

	protected abstract void update(int dx, int dy);

}
