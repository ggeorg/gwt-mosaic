package gwt.mosaic.client.wtk.skin;

import gwt.mosaic.client.wtk.Component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class ImageViewUI extends Composite implements ImageViewSkin.UI {
	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	interface MyUiBinder extends UiBinder<Widget, ImageViewUI> {
	}	

	@UiField
	SimplePanel innerDiv;
	
	private HorizontalAlignmentConstant halign = HasHorizontalAlignment.ALIGN_CENTER;
	private VerticalAlignmentConstant valign = HasVerticalAlignment.ALIGN_MIDDLE;

	private Component presender;

	ImageViewUI() {
		initWidget(uiBinder.createAndBindUi(this));
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
					// presender.repaint();
					presender.invalidate();
				}
			}
		});
	}
	
	@Override
	public Widget getWidget() {
		return innerDiv.getWidget();
	}

	@Override
	public void setWidget(IsWidget w) {
		innerDiv.setWidget(w);
	}

	@Override
	public HorizontalAlignmentConstant getHorizontalAlignment() {
		return halign;
	}

	@Override
	public void setHorizontalAlignment(HorizontalAlignmentConstant align) {
		halign = align;
		DOM.setStyleAttribute(innerDiv.getElement(), "textAlign",
				halign == null ? "" : halign.getTextAlignString());
	}

	@Override
	public VerticalAlignmentConstant getVerticalAlignment() {
		return valign;
	}

	@Override
	public void setVerticalAlignment(VerticalAlignmentConstant align) {
		valign = align;
		DOM.setStyleAttribute(innerDiv.getElement(), "verticalAlign",
				valign == null ? "" : valign.getVerticalAlignString());
	}

	public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
		return addDomHandler(handler, MouseDownEvent.getType());
	}

	public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
		return addDomHandler(handler, MouseUpEvent.getType());
	}

	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return addDomHandler(handler, MouseOutEvent.getType());
	}

	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return addDomHandler(handler, MouseOverEvent.getType());
	}

	public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
		return addDomHandler(handler, MouseMoveEvent.getType());
	}

	public HandlerRegistration addMouseWheelHandler(
			MouseWheelHandler handler) {
		return addDomHandler(handler, MouseWheelEvent.getType());
	}

	public HandlerRegistration addDoubleClickHandler(
			DoubleClickHandler handler) {
		return addHandler(handler, DoubleClickEvent.getType());
	}

	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addDomHandler(handler, ClickEvent.getType());
	}

}
