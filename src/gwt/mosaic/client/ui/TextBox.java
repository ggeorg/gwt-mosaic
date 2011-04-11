package gwt.mosaic.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

/**
 * A standard single-line text box.
 */
public class TextBox extends Composite implements ConstrainedVisual, HasName,
		HasValue<String> {

	interface MyUiBinder extends UiBinder<Widget, TextBox> {
	}

	interface MyStyle extends CssResource {
		String prompt();
	}

	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	@UiField
	MyStyle style;

	@UiField
	com.google.gwt.user.client.ui.TextBox textBox;

	private String prompt;

	private transient boolean focused;
	private transient boolean promptVisibility = true;

	public TextBox() {
		initWidget(uiBinder.createAndBindUi(this));

		textBox.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				focused = true;
				showPrompt(false);
			}
		});

		textBox.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				focused = false;
				if (prompt != null && promptVisibility) {
					showPrompt(true);
				}
			}
		});

		textBox.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				String value = getValue();
				promptVisibility = !(value != null && value.length() > 0);
			}
		});

		setVisibleLength(12);
	}

	/**
	 * Returns the text input's prompt.
	 * 
	 * @return The text input's prompt.
	 */
	public String getPrompt() {
		return prompt;
	}

	/**
	 * Sets the text input's prompt.
	 * 
	 * @param prompt
	 *            The prompt text, or <tt>null</tt> for no prompt.
	 */
	public void setPrompt(String prompt) {
		String oldPrompt = this.prompt;
		this.prompt = prompt;

		if (!focused) {
			String value = getValue();
			if (value == null || value.length() == 0) {
				showPrompt(true);
			} else if ((value != null) && value.equals(oldPrompt)) {
				showPrompt(true);
			}
		}
	}

	private void showPrompt(boolean show) {
		if (show && promptVisibility && (prompt != null)) {
			setValue(prompt, false);
			addStyleName(style.prompt());
			addStyleDependentName("WithPrompt");
		} else {
			if (promptVisibility && (prompt != null)) {
				setValue(null, false);
			}
			removeStyleName(style.prompt());
			removeStyleDependentName("WithPrompt");
		}
	}

	/**
	 * Gets the maximum allowable length of the text box.
	 * 
	 * @return the maximum length, in characters
	 */
	public int getMaxLength() {
		return textBox.getMaxLength();
	}

	/**
	 * Gets the number of visible characters in the text box.
	 * 
	 * @return the number of visible characters
	 */
	public int getVisibleLength() {
		return textBox.getVisibleLength();
	}

	/**
	 * Sets the maximum allowable length of the text box.
	 * 
	 * @param length
	 *            the maximum length, in characters
	 */
	public void setMaxLength(int length) {
		textBox.setMaxLength(length);
	}

	/**
	 * Sets the number of visible characters in the text box.
	 * 
	 * @param length
	 *            the number of visible characters
	 */
	public void setVisibleLength(int length) {
		textBox.setVisibleLength(length);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<String> handler) {
		return textBox.addValueChangeHandler(handler);
	}

	@Override
	public String getValue() {
		return textBox.getValue();
	}

	@Override
	public void setValue(String value) {
		if (value != null && value.length() > 0) {
			promptVisibility = false;
			showPrompt(false);
		}
		textBox.setValue(value);
	}

	@Override
	public void setValue(String value, boolean fireEvents) {
		if (value != null && !value.equals(prompt)) {
			promptVisibility = false;
			showPrompt(false);
		}
		textBox.setValue(value, fireEvents);
	}

	@Override
	public void setName(String name) {
		textBox.setName(name);
	}

	@Override
	public String getName() {
		return textBox.getName();
	}

	@Override
	public int getBaseline() {
		Element elem = getElement();
		return getBaseline(elem.getClientWidth(), elem.getClientHeight());
	}

	@Override
	public int getPreferredWidth(int height) {
		return WidgetHelper.getPreferredWidthImpl(this, height);
	}

	@Override
	public int getPreferredHeight(int width) {
		return WidgetHelper.getPreferredHeightImpl(this, width);
	}

	@Override
	public Dimensions getPreferredSize() {
		return new Dimensions(getPreferredWidth(-1), getPreferredHeight(-1));
	}

	@Override
	public int getBaseline(int width, int height) {
		return 0;
	}

	//
	// UiBinder related layout hints
	//

	@Override
	public void setPreferredWidth(String preferredWidth) {
		WidgetHelper.setPreferredWidth(this, preferredWidth);
	}

	@Override
	public void setPreferredHeight(String preferredHeight) {
		WidgetHelper.setPreferredHeight(this, preferredHeight);
	}

	@Override
	public void setColumnSpan(int columnSpan) {
		WidgetHelper.setColumnSpan(this, columnSpan);
	}

	@Override
	public void setRowSpan(int rowSpan) {
		WidgetHelper.setRowSpan(this, rowSpan);
	}

	@Override
	public void setWeight(int weight) {
		WidgetHelper.setWeight(this, weight);
	}

}
