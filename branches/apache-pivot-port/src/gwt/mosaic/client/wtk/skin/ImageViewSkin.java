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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * Image view skin.
 * <p>
 * TODO Add a rotation (float) style.
 */
public class ImageViewSkin extends ComponentSkin implements ImageViewListener {
	
	public interface UI extends IsWidget, HasAlignment, AcceptsOneWidget {
		void setPresender(Component component);
	}
	
	// ---------------------------------------------------------------------
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
	
	private UI ui = null;
	
	public ImageViewSkin() {
		// No-op
	}

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
	public Widget getWidget() {
		if(ui == null) {
			ui = GWT.create(UI.class);
			ui.setPresender(getComponent());
			ui.asWidget().addStyleName("m-ImageView");

			setHorizontalAlignment(HorizontalAlignment.CENTER);
			setVerticalAlignment(VerticalAlignment.CENTER);
		}
		return ui.asWidget();
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
						float imageAspectRatio = (float) imageSize.getWidth()
								/ (float) imageSize.getHeight();

						if (aspectRatio > imageAspectRatio) {
							baseline *= (float) height
									/ (float) imageSize.getHeight();
						} else {
							float scaleY = (float) width
									/ (float) imageSize.getWidth();
							baseline *= scaleY;
							baseline += (int) (height - imageSize.getHeight()
									* scaleY) / 2;
						}
					} else {
						baseline *= (float) height / (float) imageSize.getHeight();
					}
				} else {
					if (verticalAlignment == VerticalAlignment.CENTER) {
						baseline += (height - imageSize.getHeight()) / 2;
					} else if (verticalAlignment == VerticalAlignment.BOTTOM) {
						baseline += height - imageSize.getHeight();
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
					float imageAspectRatio = (float) imageSize.getWidth()
							/ (float) imageSize.getHeight();

					if (aspectRatio > imageAspectRatio) {
						imageY = 0;
						scaleY = (float) height / (float) imageSize.getHeight();

						imageX = (int) (width - imageSize.getWidth() * scaleY) / 2;
						scaleX = scaleY;
					} else {
						imageX = 0;
						scaleX = (float) width / (float) imageSize.getWidth();

						imageY = (int) (height - imageSize.getHeight() * scaleX) / 2;
						scaleY = scaleX;
					}
				} else {
					imageX = 0;
					scaleX = (float) width / (float) imageSize.getWidth();

					imageY = 0;
					scaleY = (float) height / (float) imageSize.getHeight();
				}
			} else {
				if (horizontalAlignment == HorizontalAlignment.CENTER) {
					imageX = (width - imageSize.getWidth()) / 2;
				} else if (horizontalAlignment == HorizontalAlignment.RIGHT) {
					imageX = width - imageSize.getWidth();
				} else {
					imageX = 0;
				}

				scaleX = 1.0f;

				if (verticalAlignment == VerticalAlignment.CENTER) {
					imageY = (height - imageSize.getHeight()) / 2;
				} else if (verticalAlignment == VerticalAlignment.BOTTOM) {
					imageY = height - imageSize.getHeight();
				} else {
					imageY = 0;
				}

				scaleY = 1.0f;
			}
		}
	}

	@Override
	public void paint() {
		UI ui = (UI) getWidget();

		ImageView imageView = (ImageView) getComponent();
		Image image = imageView.getImage();
		
		if (horizontalAlignmentChanged) {
			if (horizontalAlignment == HorizontalAlignment.LEFT) {
				ui.setHorizontalAlignment(UI.ALIGN_LEFT);
			} else if (horizontalAlignment == HorizontalAlignment.CENTER) {
				ui.setHorizontalAlignment(UI.ALIGN_CENTER);
			} else if (horizontalAlignment == HorizontalAlignment.RIGHT) {
				ui.setHorizontalAlignment(UI.ALIGN_RIGHT);
			}
			horizontalAlignmentChanged = false;
		}
		
		if (verticalAlignmentChanged) {
			if (verticalAlignment == VerticalAlignment.TOP) {
				ui.setVerticalAlignment(UI.ALIGN_TOP);
			} else if (verticalAlignment == VerticalAlignment.CENTER) {
				ui.setVerticalAlignment(UI.ALIGN_MIDDLE);
			} else if (verticalAlignment == VerticalAlignment.BOTTOM) {
				ui.setVerticalAlignment(UI.ALIGN_BOTTOM);
			}
			verticalAlignmentChanged = false;
		}

		ui.setWidget(imageView.getImage());

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
		
		super.paint();
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

	public /* TODO final*/ void setBackgroundColor(String backgroundColor) {
		if (backgroundColor == null) {
			throw new IllegalArgumentException("backgroundColor is null.");
		}
		setBackgroundColor(Color.decode(backgroundColor));
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

}
