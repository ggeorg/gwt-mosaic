package org.gwt.mosaic2g.client.scene.control;

import org.gwt.mosaic2g.binding.client.Property;
import org.gwt.mosaic2g.client.scene.Assembly;
import org.gwt.mosaic2g.client.scene.Show;
import org.gwt.mosaic2g.client.scene.layout.Stack;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

public class Button extends Assembly {
	private final Stack enabled;

	private Property<String> text;

	public Button(Show show) {
		this(show, Property.valueOf(0), Property.valueOf(0));
	}

	public Button(Show show, int x, int y) {
		this(show, Property.valueOf(x), Property.valueOf(y));
	}

	public Button(Show show, int x, int y, int width, int height) {
		this(show, Property.valueOf(x), Property.valueOf(y), Property
				.valueOf(width), Property.valueOf(height));
	}

	public Button(Show show, Property<Integer> x, Property<Integer> y) {
		this(show, x, y, Property.valueOf(Integer.MIN_VALUE), Property
				.valueOf(Integer.MIN_VALUE));
	}

	public Button(Show show, Property<Integer> x, Property<Integer> y,
			Property<Integer> width, Property<Integer> height) {
		this(show, null, x, y, width, height);
	}

	public Button(Show show, Property<String> text, Property<Integer> x,
			Property<Integer> y, Property<Integer> width,
			Property<Integer> height) {
		super(show);

		this.text = (text != null) ? text : new Property<String>();

		enabled = new Stack(show, x, y);
		enabled.setHorizontalAlignment(Stack.ALIGN_CENTER);
		enabled.setVerticalAlignment(Stack.ALIGN_MIDDLE);

		final Text t = new Text(show, x, y);
		t.setBorder("5px solid green");

		final Box b = new Box(show, x, y, width, height);
		b.setBorder("10px solid blue");

		enabled.add(b);
		enabled.add(t);

		add(enabled);

		// TODO make 'text' in Text control a property
		this.text.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				t.setText(Button.this.text.$());
			}
		});
	}

	public Property<String> getText() {
		return text;
	}

}
