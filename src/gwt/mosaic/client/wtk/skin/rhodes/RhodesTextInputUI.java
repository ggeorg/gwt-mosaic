package gwt.mosaic.client.wtk.skin.rhodes;

import gwt.mosaic.client.wtk.Component;
import gwt.mosaic.client.wtk.FocusTraversalDirection;
import gwt.mosaic.client.wtk.Font;
import gwt.mosaic.client.wtk.style.Color;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class RhodesTextInputUI extends Composite implements
		RhodesTextInputSkin.UI, /* Focusable, */KeyDownHandler, KeyUpHandler,
		KeyPressHandler {
	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	interface MyUiBinder extends UiBinder<Widget, RhodesTextInputUI> {
	}

	interface MyStyle extends CssResource {
		String focused();
	}

	@UiField
	MyStyle style;

	@UiField
	HTMLPanel inputDiv;

	@UiField
	TextBox textBox;

	@UiField
	PasswordTextBox passwordTextBox;

	private Component presender;

	RhodesTextInputUI() {
		initWidget(uiBinder.createAndBindUi(this));

		passwordTextBox.removeFromParent();

		textBox.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				if (!presender.requestFocus()) {
					setFocus(false);
				} else {
					setFocus(true);
				}
			}
		});

		textBox.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				presender.transferFocus(FocusTraversalDirection.FORWARD);
				setFocus(false);
			}
		});

		textBox.addKeyDownHandler(this);
		textBox.addKeyUpHandler(this);
		textBox.addKeyPressHandler(this);

		setFocus(false);
	}

	protected void setFocus(boolean focused) {
		if (textBox.isAttached()) {
			if (focused) {
				inputDiv.addStyleName(style.focused());
			} else {
				inputDiv.removeStyleName(style.focused());
			}
		} else if (passwordTextBox.isAttached()) {

		}
	}

	@Override
	public void setPresender(Component component) {
		this.presender = component;
	}

	@Override
	protected void onLoad() {
		super.onLoad();

		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				if (presender != null) {
					//presender.repaint();
					presender.invalidate();
				}
			}
		});
	}

	@Override
	public String getValue() {
		if (textBox.isAttached()) {
			return textBox.getText();
		} else if (passwordTextBox.isAttached()) {
			return passwordTextBox.getValue();
		} else {
			throw new IllegalStateException();
		}
	}

	@Override
	public void setValue(String value) {
		setValue(value, false);
	}

	@Override
	public void setValue(String value, boolean fireEvents) {
		value = value != null ? value : "";
		if (textBox.isAttached() && !textBox.getValue().equals(value)) {
			textBox.setValue(value, fireEvents);
		} else if (passwordTextBox.isAttached()
				&& !passwordTextBox.getValue().equals(value)) {
			passwordTextBox.setValue(value, fireEvents);
		}
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<String> handler) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setFont(Font font) {
		if (font != null) {
			font.applyTo(inputDiv.getElement());
			font.applyTo(textBox.getElement());
			font.applyTo(passwordTextBox.getElement());
		}
	}

	@Override
	public void setColor(Color color) {
		if (color != null) {
			color.applyTo(textBox.getElement(), false);
			color.applyTo(passwordTextBox.getElement(), false);
		}
	}

	@Override
	public void setBackgroundColor(Color backgroundColor) {
		if (backgroundColor != null) {
			backgroundColor.applyTo(inputDiv.getElement(), true);
			backgroundColor.applyTo(textBox.getElement(), true);
			backgroundColor.applyTo(passwordTextBox.getElement(), true);
		}
	}

	@Override
	public void setPromptColor(Color color) {
		if (color != null) {
			color.applyTo(textBox.getElement(), false);
			color.applyTo(passwordTextBox.getElement(), false);
		}
	}

	@Override
	public void setPrompt(String prompt) {
		if ((prompt != null) && (prompt.length() > 0)) {
			textBox.setValue(prompt);
		} else {
			textBox.setValue("");
		}
	}

	@Override
	public void setBorderColor(Color borderColor) {
		if (borderColor != null) {
			inputDiv.getElement().getStyle()
					.setBorderColor(borderColor.toString());
		}
	}

	@Override
	public void onKeyDown(KeyDownEvent event) {
		if (presender != null) {
			presender.getDisplay().getDisplayHost().processKeyDownEvent(event);
		}
	}

	@Override
	public void onKeyUp(KeyUpEvent event) {
		if (presender != null) {
			presender.getDisplay().getDisplayHost().processKeyUpEvent(event);
		}
	}

	@Override
	public void onKeyPress(KeyPressEvent event) {
		if (presender != null) {
			presender.getDisplay().getDisplayHost().processKeyPressEvent(event);
		}
	}

}
