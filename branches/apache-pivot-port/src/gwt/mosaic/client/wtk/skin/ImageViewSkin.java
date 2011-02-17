/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gwt.mosaic.client.wtk.skin;

import gwt.mosaic.client.wtk.Component;
import gwt.mosaic.client.wtk.Dimensions;
import gwt.mosaic.client.wtk.HorizontalAlignment;
import gwt.mosaic.client.wtk.ImageView;
import gwt.mosaic.client.wtk.ImageViewListener;
import gwt.mosaic.client.wtk.VerticalAlignment;
import gwt.mosaic.client.wtk.media.Image;
import gwt.mosaic.client.wtk.media.ImageListener;
import gwt.mosaic.client.wtk.style.Color;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
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
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Image view skin.
 * <p>
 * TODO Add a rotation (float) style.
 */
public class ImageViewSkin extends ComponentSkin implements ImageViewListener {
	private Color backgroundColor = null;
	private float opacity = 1.0f;

	private HorizontalAlignment horizontalAlignment;
	private boolean horizontalAlignmentChanged = false;

	private VerticalAlignment verticalAlignment;
	private boolean verticalAlignmentChanged = false;

	private boolean fill = false;
	private boolean preserveAspectRatio = true;

	private int imageX = 0;
	private int imageY = 0;
	private float scaleX = 1;
	private float scaleY = 1;

	private ImageWidget widget = null;

	private ImageListener imageListener = new ImageListener() {
		@Override
		public void sizeChanged(Image image, int previousWidth,
				int previousHeight) {
			invalidateComponent();
		}

		@Override
		public void baselineChanged(Image image, int previousBaseline) {
			invalidateComponent();
		}

		@Override
		public void onLoad(Image image) {
			repaintComponent();
		}

		@Override
		public void onError(Image image) {
			repaintComponent();
		}
	};

	@Override
	public void install(Component component) {
		super.install(component);

		ImageView imageView = (ImageView) component;
		imageView.getImageViewListeners().add(this);

		Image image = imageView.getImage();
		if (image != null) {
			image.getImageListeners().add(imageListener);
		}
	}

	@Override
	public Widget asWidget() {
		if (widget == null) {
			widget = new ImageWidget();

			setHorizontalAlignment(HorizontalAlignment.CENTER);
			setVerticalAlignment(VerticalAlignment.CENTER);

			widget.addStyleName("m-ImageView");
		}
		return widget;
	}

	@Override
	public int getPreferredWidth(int height) {
		ImageView imageView = (ImageView) getComponent();
		Image image = imageView.getImage();

		return (image == null) ? 0 : image.getWidth();
	}

	@Override
	public int getPreferredHeight(int width) {
		ImageView imageView = (ImageView) getComponent();
		Image image = imageView.getImage();

		return (image == null) ? 0 : image.getHeight();
	}

	@Override
	public Dimensions getPreferredSize() {
		ImageView imageView = (ImageView) getComponent();
		Image image = imageView.getImage();

		return (image == null) ? new Dimensions(0, 0) : new Dimensions(
				image.getWidth(), image.getHeight());
	}

	@Override
	public int getBaseline(int width, int height) {
		ImageView imageView = (ImageView) getComponent();
		Image image = imageView.getImage();

		int baseline = -1;

		if (image != null) {
			baseline = image.getBaseline();

			if (baseline != -1) {
				Dimensions imageSize = image.getSize();

				if (fill) {
					// Scale to fit
					if (preserveAspectRatio) {
						float aspectRatio = (float) width / (float) height;
						float imageAspectRatio = (float) imageSize.width
								/ (float) imageSize.height;

						if (aspectRatio > imageAspectRatio) {
							baseline *= (float) height
									/ (float) imageSize.height;
						} else {
							float scaleY = (float) width
									/ (float) imageSize.width;
							baseline *= scaleY;
							baseline += (int) (height - imageSize.height
									* scaleY) / 2;
						}
					} else {
						baseline *= (float) height / (float) imageSize.height;
					}
				} else {
					if (verticalAlignment == VerticalAlignment.CENTER) {
						baseline += (height - imageSize.height) / 2;
					} else if (verticalAlignment == VerticalAlignment.BOTTOM) {
						baseline += height - imageSize.height;
					}
				}
			}
		}

		return baseline;
	}

	@Override
	public void layout() {
		super.layout();

		ImageView imageView = (ImageView) getComponent();
		Image image = imageView.getImage();

		if (image != null) {
			int width = getWidth();
			int height = getHeight();

			Dimensions imageSize = image.getSize();

			if (fill) {
				// Scale to fit
				if (preserveAspectRatio) {
					float aspectRatio = (float) width / (float) height;
					float imageAspectRatio = (float) imageSize.width
							/ (float) imageSize.height;

					if (aspectRatio > imageAspectRatio) {
						imageY = 0;
						scaleY = (float) height / (float) imageSize.height;

						imageX = (int) (width - imageSize.width * scaleY) / 2;
						scaleX = scaleY;
					} else {
						imageX = 0;
						scaleX = (float) width / (float) imageSize.width;

						imageY = (int) (height - imageSize.height * scaleX) / 2;
						scaleY = scaleX;
					}
				} else {
					imageX = 0;
					scaleX = (float) width / (float) imageSize.width;

					imageY = 0;
					scaleY = (float) height / (float) imageSize.height;
				}
			} else {
				if (horizontalAlignment == HorizontalAlignment.CENTER) {
					imageX = (width - imageSize.width) / 2;
				} else if (horizontalAlignment == HorizontalAlignment.RIGHT) {
					imageX = width - imageSize.width;
				} else {
					imageX = 0;
				}

				scaleX = 1.0f;

				if (verticalAlignment == VerticalAlignment.CENTER) {
					imageY = (height - imageSize.height) / 2;
				} else if (verticalAlignment == VerticalAlignment.BOTTOM) {
					imageY = height - imageSize.height;
				} else {
					imageY = 0;
				}

				scaleY = 1.0f;
			}
		}
	}

	@Override
	public void paint(Widget context) {
		ImageView imageView = (ImageView) getComponent();
		Image image = imageView.getImage();

		int width = getWidth();
		int height = getHeight();

		if (horizontalAlignmentChanged) {
			if (horizontalAlignment == HorizontalAlignment.LEFT) {
				((ImageWidget) widget)
						.setHorizontalAlignment(ImageWidget.ALIGN_LEFT);
			} else if (horizontalAlignment == HorizontalAlignment.CENTER) {
				((ImageWidget) widget)
						.setHorizontalAlignment(ImageWidget.ALIGN_CENTER);
			} else if (horizontalAlignment == HorizontalAlignment.RIGHT) {
				((ImageWidget) widget)
						.setHorizontalAlignment(ImageWidget.ALIGN_RIGHT);
			}
			horizontalAlignmentChanged = false;
		}

		if (verticalAlignmentChanged) {
			if (verticalAlignment == VerticalAlignment.TOP) {
				((ImageWidget) widget)
						.setVerticalAlignment(ImageWidget.ALIGN_TOP);
			} else if (verticalAlignment == VerticalAlignment.CENTER) {
				((ImageWidget) widget)
						.setVerticalAlignment(ImageWidget.ALIGN_MIDDLE);
			} else if (verticalAlignment == VerticalAlignment.BOTTOM) {
				((ImageWidget) widget)
						.setVerticalAlignment(ImageWidget.ALIGN_BOTTOM);
			}
			verticalAlignmentChanged = false;
		}

		widget.setWidget(imageView.getImage());

		// if (backgroundColor != null) {
		// graphics.setPaint(backgroundColor);
		// graphics.fillRect(0, 0, width, height);
		// }

		// if (image != null) {
		// Graphics2D imageGraphics = (Graphics2D)graphics.create();
		// imageGraphics.translate(imageX, imageY);
		// imageGraphics.scale(scaleX, scaleY);
		//
		// // Apply an alpha composite if the opacity value is less than
		// // the current alpha
		// float alpha = 1.0f;
		//
		// Composite composite = imageGraphics.getComposite();
		// if (composite instanceof AlphaComposite) {
		// AlphaComposite alphaComposite = (AlphaComposite)composite;
		// alpha = alphaComposite.getAlpha();
		// }
		//
		// if (opacity < alpha) {
		// imageGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
		// opacity));
		// }
		//
		// image.paint(imageGraphics);
		// imageGraphics.dispose();
		// }
	}

	/**
	 * @return <tt>false</tt>; image views are not focusable.
	 */
	@Override
	public boolean isFocusable() {
		return false;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		repaintComponent();
	}

	public final void setBackgroundColor(String backgroundColor) {
		if (backgroundColor == null) {
			throw new IllegalArgumentException("backgroundColor is null.");
		}

		throw new UnsupportedOperationException();
		// setBackgroundColor(GraphicsUtilities.decodeColor(backgroundColor));
	}

	public float getOpacity() {
		return opacity;
	}

	public void setOpacity(float opacity) {
		if (opacity < 0 || opacity > 1) {
			throw new IllegalArgumentException("Opacity out of range [0,1].");
		}

		this.opacity = opacity;
		repaintComponent();
	}

	public final void setOpacity(Number opacity) {
		if (opacity == null) {
			throw new IllegalArgumentException("opacity is null.");
		}

		setOpacity(opacity.floatValue());
	}

	public HorizontalAlignment getHorizontalAlignment() {
		return horizontalAlignment;
	}

	public void setHorizontalAlignment(HorizontalAlignment horizontalAlignment) {
		if (horizontalAlignment == null) {
			throw new IllegalArgumentException("horizontalAlignment is null.");
		}

		if (!horizontalAlignment.equals(this.horizontalAlignment)) {
			this.horizontalAlignment = horizontalAlignment;
			this.horizontalAlignmentChanged = true;
			layout();
			repaintComponent();
		}
	}

	public VerticalAlignment getVerticalAlignment() {
		return verticalAlignment;
	}

	public void setVerticalAlignment(VerticalAlignment verticalAlignment) {
		if (verticalAlignment == null) {
			throw new IllegalArgumentException("verticalAlignment is null.");
		}

		if (verticalAlignment != this.verticalAlignment) {
			this.verticalAlignment = verticalAlignment;
			this.verticalAlignmentChanged = true;
			layout();
			repaintComponent();
		}
	}

	public boolean getFill() {
		return fill;
	}

	public void setFill(boolean fill) {
		this.fill = fill;
		layout();
		repaintComponent();
	}

	public boolean getPreserveAspectRatio() {
		return preserveAspectRatio;
	}

	public void setPreserveAspectRatio(boolean preserveAspectRatio) {
		this.preserveAspectRatio = preserveAspectRatio;
		layout();
		repaintComponent();
	}

	// Image view events
	@Override
	public void imageChanged(ImageView imageView, Image previousImage) {
		if (previousImage != null) {
			previousImage.getImageListeners().remove(imageListener);
		}

		Image image = imageView.getImage();
		if (image != null) {
			image.getImageListeners().add(imageListener);
		}

		invalidateComponent();
	}

	@Override
	public void asynchronousChanged(ImageView imageView) {
		// No-op
	}

	// -------------------------
	private class ImageWidget extends Composite implements HasAlignment,
			HasClickHandlers, HasDoubleClickHandlers, HasAllMouseHandlers {
		private final SimplePanel div;
		private final SimplePanel innerDiv;

		private HorizontalAlignmentConstant halign = HasHorizontalAlignment.ALIGN_CENTER;
		private VerticalAlignmentConstant valign = HasVerticalAlignment.ALIGN_MIDDLE;

		public ImageWidget() {
			initWidget(div = new SimplePanel());
			div.add(innerDiv = new SimplePanel());

			SkinClientBundle.INSTANCE.css().ensureInjected();
			setStyleName(SkinClientBundle.INSTANCE.css().imageWidget());
			innerDiv.setStyleName(SkinClientBundle.INSTANCE.css()
					.imageWidgetInner());
		}

		@Override
		public Widget getWidget() {
			return innerDiv.getWidget();
		}

		@Override
		public void setWidget(Widget w) {
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

		@Override
		public void setWidth(String width) {
			super.setWidth(width);
			innerDiv.setWidth(width);
		}

		@Override
		public void setHeight(String height) {
			super.setHeight(height);
			innerDiv.setHeight(height);
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

}
