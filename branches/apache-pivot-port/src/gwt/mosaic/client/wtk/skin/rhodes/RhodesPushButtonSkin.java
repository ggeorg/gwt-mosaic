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
package gwt.mosaic.client.wtk.skin.rhodes;

import com.google.gwt.user.client.ui.Widget;

import gwt.mosaic.client.collections.Dictionary;
import gwt.mosaic.client.wtk.ApplicationContext.DisplayHost;
import gwt.mosaic.client.wtk.Button;
import gwt.mosaic.client.wtk.Component;
import gwt.mosaic.client.wtk.Dimensions;
import gwt.mosaic.client.wtk.Font;
import gwt.mosaic.client.wtk.Insets;
import gwt.mosaic.client.wtk.Mouse;
import gwt.mosaic.client.wtk.PushButton;
import gwt.mosaic.client.wtk.skin.PushButtonSkin;
import gwt.mosaic.client.wtk.style.Color;

/**
 * Terra push button skin.
 */
public class RhodesPushButtonSkin extends PushButtonSkin {
    private Font font;
    private Color color;
    private Color disabledColor;
    private Color backgroundColor;
    private Color disabledBackgroundColor;
    private Color borderColor;
    private Color disabledBorderColor;
    private Insets padding;
    private float minimumAspectRatio;
    private float maximumAspectRatio;
    private boolean toolbar;

    private Color bevelColor;
    private Color pressedBevelColor;
    private Color disabledBevelColor;

    private static final int CORNER_RADIUS = 4;
    
    private Widget widget = null;

    public RhodesPushButtonSkin() {
        //TerraTheme theme = (TerraTheme)Theme.getTheme();

//        font = theme.getFont();
//        color = theme.getColor(1);
//        disabledColor = theme.getColor(7);
//        backgroundColor = theme.getColor(10);
//        disabledBackgroundColor = theme.getColor(10);
//        borderColor = theme.getColor(7);
//        disabledBorderColor = theme.getColor(7);
        padding = new Insets(2, 3, 2, 3);
        minimumAspectRatio = Float.NaN;
        maximumAspectRatio = Float.NaN;
        toolbar = false;

        // Set the derived colors
//        bevelColor = TerraTheme.brighten(backgroundColor);
//        pressedBevelColor = TerraTheme.darken(backgroundColor);
        disabledBevelColor = disabledBackgroundColor;
    }
    
	@Override
	public Widget asWidget() {
		if(widget == null) {
			widget = (Widget) new com.google.gwt.user.client.ui.Button("Hello!");
		}
		return widget;
	}

    @Override
    public int getPreferredWidth(int height) {
        int preferredWidth = 0;

        if (height == -1) {
            preferredWidth = getPreferredSize().width;
        } else {
            PushButton pushButton = (PushButton) getComponent();
            Button.DataRenderer dataRenderer = pushButton.getDataRenderer();

            dataRenderer.render(pushButton.getButtonData(), pushButton, false);

            // Include padding in constraint
            int contentHeight = height;
            if (contentHeight != -1) {
                contentHeight = Math.max(contentHeight - (padding.top + padding.bottom + 2), 0);
            }

            preferredWidth = dataRenderer.getPreferredWidth(contentHeight)
                + padding.left + padding.right + 2;

            // Adjust for preferred aspect ratio
            if (!Float.isNaN(minimumAspectRatio)
                && (float) preferredWidth / (float) height < minimumAspectRatio) {
                preferredWidth = (int) (height * minimumAspectRatio);
            }
        }

        return preferredWidth;
    }

    @Override
    public int getPreferredHeight(int width) {
        int preferredHeight = 0;

        if (width == -1) {
            preferredHeight = getPreferredSize().height;
        } else {
            PushButton pushButton = (PushButton) getComponent();
            Button.DataRenderer dataRenderer = pushButton.getDataRenderer();

            dataRenderer.render(pushButton.getButtonData(), pushButton, false);

            // Include padding in constraint
            int contentWidth = width;
            if (contentWidth != -1) {
                contentWidth = Math.max(contentWidth - (padding.left + padding.right + 2), 0);
            }

            preferredHeight = dataRenderer.getPreferredHeight(contentWidth)
                + padding.top + padding.bottom + 2;

            // Adjust for preferred aspect ratio
            if (!Float.isNaN(maximumAspectRatio)
                && (float) width / (float) preferredHeight > maximumAspectRatio) {
                preferredHeight = (int) (width / maximumAspectRatio);
            }
        }

        return preferredHeight;
    }

    @Override
    public Dimensions getPreferredSize() {
        PushButton pushButton = (PushButton) getComponent();
        Button.DataRenderer dataRenderer = pushButton.getDataRenderer();

        dataRenderer.render(pushButton.getButtonData(), pushButton, false);

        Dimensions preferredContentSize = dataRenderer.getPreferredSize();

        int preferredWidth = preferredContentSize.width
            + padding.left + padding.right + 2;

        int preferredHeight = preferredContentSize.height
            + padding.top + padding.bottom + 2;

        // Adjust for preferred aspect ratio
        float aspectRatio = (float) preferredWidth / (float) preferredHeight;

        if (!Float.isNaN(minimumAspectRatio)
            && aspectRatio < minimumAspectRatio) {
            preferredWidth = (int) (preferredHeight * minimumAspectRatio);
        }

        if (!Float.isNaN(maximumAspectRatio)
            && aspectRatio > maximumAspectRatio) {
            preferredHeight = (int) (preferredWidth / maximumAspectRatio);
        }

        return new Dimensions(preferredWidth, preferredHeight);
    }

    @Override
    public int getBaseline(int width, int height) {
        PushButton pushButton = (PushButton) getComponent();

        Button.DataRenderer dataRenderer = pushButton.getDataRenderer();
        dataRenderer.render(pushButton.getButtonData(), pushButton, false);

        int clientWidth = Math.max(width - (padding.left + padding.right + 2), 0);
        int clientHeight = Math.max(height - (padding.top + padding.bottom + 2), 0);

        int baseline = dataRenderer.getBaseline(clientWidth, clientHeight);

        if (baseline != -1) {
            baseline += padding.top + 1;
        }

        return baseline;
    }

//    @Override
//    public void paint(Graphics2D graphics) {
//        PushButton pushButton = (PushButton) getComponent();
//
//        int width = getWidth();
//        int height = getHeight();
//
//        Color backgroundColor = null;
//        Color bevelColor = null;
//        Color borderColor = null;
//
//        if (!toolbar
//            || highlighted
//            || pushButton.isFocused()) {
//            if (pushButton.isEnabled()) {
//                backgroundColor = this.backgroundColor;
//                bevelColor = (pressed
//                    || pushButton.isSelected()) ? pressedBevelColor : this.bevelColor;
//                borderColor = this.borderColor;
//            } else {
//                backgroundColor = disabledBackgroundColor;
//                bevelColor = disabledBevelColor;
//                borderColor = disabledBorderColor;
//            }
//        }
//
//        // Paint the background
//        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//            RenderingHints.VALUE_ANTIALIAS_ON);
//
//        if (backgroundColor != null
//            && bevelColor != null) {
//            graphics.setPaint(new GradientPaint(width / 2f, 0, bevelColor,
//                width / 2f, height / 2f, backgroundColor));
//            graphics.fill(new RoundRectangle2D.Double(0.5, 0.5, width - 1, height - 1,
//                CORNER_RADIUS, CORNER_RADIUS));
//        }
//
//        // Paint the content
//        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//            RenderingHints.VALUE_ANTIALIAS_OFF);
//
//        Button.DataRenderer dataRenderer = pushButton.getDataRenderer();
//        dataRenderer.render(pushButton.getButtonData(), pushButton, highlighted);
//        dataRenderer.setSize(Math.max(width - (padding.left + padding.right + 2), 0),
//            Math.max(getHeight() - (padding.top + padding.bottom + 2), 0));
//
//        Graphics2D contentGraphics = (Graphics2D) graphics.create();
//        contentGraphics.translate(padding.left + 1, padding.top + 1);
//        contentGraphics.clipRect(0, 0, dataRenderer.getWidth(), dataRenderer.getHeight());
//        dataRenderer.paint(contentGraphics);
//        contentGraphics.dispose();
//
//        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//            RenderingHints.VALUE_ANTIALIAS_ON);
//
//        // Paint the border
//        if (borderColor != null) {
//            graphics.setPaint(borderColor);
//            graphics.setStroke(new BasicStroke(1));
//            graphics.draw(new RoundRectangle2D.Double(0.5, 0.5, width - 1, height - 1,
//                CORNER_RADIUS, CORNER_RADIUS));
//        }
//
//        // Paint the focus state
//        if (pushButton.isFocused()
//            && !toolbar) {
//            BasicStroke dashStroke = new BasicStroke(1.0f, BasicStroke.CAP_ROUND,
//                BasicStroke.JOIN_ROUND, 1.0f, new float[] {0.0f, 2.0f}, 0.0f);
//            graphics.setStroke(dashStroke);
//            graphics.setColor(this.borderColor);
//            graphics.draw(new RoundRectangle2D.Double(2.5, 2.5, Math.max(width - 5, 0),
//                Math.max(height - 5, 0), CORNER_RADIUS / 2, CORNER_RADIUS / 2));
//        }
//    }

    @Override
    public boolean isFocusable() {
        return true;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        if (font == null) {
            throw new IllegalArgumentException("font is null.");
        }

        this.font = font;
        invalidateComponent();
    }

    public final void setFont(String font) {
        if (font == null) {
            throw new IllegalArgumentException("font is null.");
        }

        throw new UnsupportedOperationException();
        //setFont(decodeFont(font));
    }

    public final void setFont(Dictionary<String, ?> font) {
        if (font == null) {
            throw new IllegalArgumentException("font is null.");
        }

        throw new UnsupportedOperationException();
        //setFont(Theme.deriveFont(font));
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        if (color == null) {
            throw new IllegalArgumentException("color is null.");
        }

        this.color = color;
        repaintComponent();
    }

    public final void setColor(String color) {
        if (color == null) {
            throw new IllegalArgumentException("color is null.");
        }

        throw new UnsupportedOperationException();
        //setColor(GraphicsUtilities.decodeColor(color));
    }

    public final void setColor(int color) {
    	throw new UnsupportedOperationException();
        //TerraTheme theme = (TerraTheme)Theme.getTheme();
        //setColor(theme.getColor(color));
    }

    public Color getDisabledColor() {
        return disabledColor;
    }

    public void setDisabledColor(Color disabledColor) {
        if (disabledColor == null) {
            throw new IllegalArgumentException("disabledColor is null.");
        }

        this.disabledColor = disabledColor;
        repaintComponent();
    }

    public final void setDisabledColor(String disabledColor) {
        if (disabledColor == null) {
            throw new IllegalArgumentException("disabledColor is null.");
        }

        throw new UnsupportedOperationException();
        //setDisabledColor(GraphicsUtilities.decodeColor(disabledColor));
    }

    public final void setDisabledColor(int disabledColor) {
        //TerraTheme theme = (TerraTheme)Theme.getTheme();
        //setDisabledColor(theme.getColor(disabledColor));
    	throw new UnsupportedOperationException();
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        if (backgroundColor == null) {
            throw new IllegalArgumentException("backgroundColor is null.");
        }

        this.backgroundColor = backgroundColor;
        // TODO bevelColor = TerraTheme.brighten(backgroundColor);
        // TODO pressedBevelColor = TerraTheme.darken(backgroundColor);
        repaintComponent();
    }

    public final void setBackgroundColor(String backgroundColor) {
        if (backgroundColor == null) {
            throw new IllegalArgumentException("backgroundColor is null.");
        }

        // setBackgroundColor(GraphicsUtilities.decodeColor(backgroundColor));
    }

    public final void setBackgroundColor(int backgroundColor) {
        //TerraTheme theme = (TerraTheme)Theme.getTheme();
        //setBackgroundColor(theme.getColor(backgroundColor));
    	throw new UnsupportedOperationException();
    }

    public Color getDisabledBackgroundColor() {
        return disabledBackgroundColor;
    }

    public void setDisabledBackgroundColor(Color disabledBackgroundColor) {
        if (disabledBackgroundColor == null) {
            throw new IllegalArgumentException("disabledBackgroundColor is null.");
        }

        this.disabledBackgroundColor = disabledBackgroundColor;
        disabledBevelColor = disabledBackgroundColor;
        repaintComponent();
    }

    public final void setDisabledBackgroundColor(String disabledBackgroundColor) {
        if (disabledBackgroundColor == null) {
            throw new IllegalArgumentException("disabledBackgroundColor is null.");
        }

        //setDisabledBackgroundColor(GraphicsUtilities.decodeColor(disabledBackgroundColor));
        throw new UnsupportedOperationException();
    }

    public final void setDisabledBackgroundColor(int disabledBackgroundColor) {
        //TerraTheme theme = (TerraTheme)Theme.getTheme();
        //setDisabledBackgroundColor(theme.getColor(disabledBackgroundColor));
    	throw new UnsupportedOperationException();
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        if (borderColor == null) {
            throw new IllegalArgumentException("borderColor is null.");
        }

        this.borderColor = borderColor;
        repaintComponent();
    }

    public final void setBorderColor(String borderColor) {
        if (borderColor == null) {
            throw new IllegalArgumentException("borderColor is null.");
        }

        //setBorderColor(GraphicsUtilities.decodeColor(borderColor));
        throw new UnsupportedOperationException();
    }

    public final void setBorderColor(int borderColor) {
        //TerraTheme theme = (TerraTheme)Theme.getTheme();
        //setBorderColor(theme.getColor(borderColor));
    	throw new UnsupportedOperationException();
    }

    public Color getDisabledBorderColor() {
        return disabledBorderColor;
    }

    public void setDisabledBorderColor(Color disabledBorderColor) {
        if (disabledBorderColor == null) {
            throw new IllegalArgumentException("disabledBorderColor is null.");
        }

        this.disabledBorderColor = disabledBorderColor;
        repaintComponent();
    }

    public final void setDisabledBorderColor(String disabledBorderColor) {
        if (disabledBorderColor == null) {
            throw new IllegalArgumentException("disabledBorderColor is null.");
        }

        //setDisabledBorderColor(GraphicsUtilities.decodeColor(disabledBorderColor));
        throw new UnsupportedOperationException();
    }

    public final void setDisabledBorderColor(int disabledBorderColor) {
        //TerraTheme theme = (TerraTheme)Theme.getTheme();
        //setDisabledBorderColor(theme.getColor(disabledBorderColor));
    	throw new UnsupportedOperationException();
    }

    public Insets getPadding() {
        return padding;
    }

    public void setPadding(Insets padding) {
        if (padding == null) {
            throw new IllegalArgumentException("padding is null.");
        }

        this.padding = padding;
        invalidateComponent();
    }

    public final void setPadding(Dictionary<String, ?> padding) {
        if (padding == null) {
            throw new IllegalArgumentException("padding is null.");
        }

        setPadding(new Insets(padding));
    }

    public final void setPadding(int padding) {
        setPadding(new Insets(padding));
    }

    public final void setPadding(Number padding) {
        if (padding == null) {
            throw new IllegalArgumentException("padding is null.");
        }

        setPadding(padding.intValue());
    }

    public final void setPadding(String padding) {
        if (padding == null) {
            throw new IllegalArgumentException("padding is null.");
        }

        setPadding(Insets.decode(padding));
    }

    public float getMinimumAspectRatio() {
        return minimumAspectRatio;
    }

    public void setMinimumAspectRatio(float minimumAspectRatio) {
        if (!Float.isNaN(maximumAspectRatio)
            && minimumAspectRatio > maximumAspectRatio) {
            throw new IllegalArgumentException("minimumAspectRatio is greater than maximumAspectRatio.");
        }

        this.minimumAspectRatio = minimumAspectRatio;
        invalidateComponent();
    }

    public final void setMinimumAspectRatio(Number minimumAspectRatio) {
        if (minimumAspectRatio == null) {
            throw new IllegalArgumentException("minimumAspectRatio is null.");
        }

        setMinimumAspectRatio(minimumAspectRatio.floatValue());
    }

    public float getMaximumAspectRatio() {
        return maximumAspectRatio;
    }

    public void setMaximumAspectRatio(float maximumAspectRatio) {
        if (!Float.isNaN(minimumAspectRatio)
            && maximumAspectRatio < minimumAspectRatio) {
            throw new IllegalArgumentException("maximumAspectRatio is less than minimumAspectRatio.");
        }

        this.maximumAspectRatio = maximumAspectRatio;
        invalidateComponent();
    }

    public final void setMaximumAspectRatio(Number maximumAspectRatio) {
        if (maximumAspectRatio == null) {
            throw new IllegalArgumentException("maximumAspectRatio is null.");
        }

        setMaximumAspectRatio(maximumAspectRatio.floatValue());
    }

    public boolean isToolbar() {
        return toolbar;
    }

    public void setToolbar(boolean toolbar) {
        this.toolbar = toolbar;

        if (toolbar &&
            getComponent().isFocused()) {
            Component.clearFocus();
        }

        repaintComponent();
    }

    @Override
    public void mouseOut(Component component) {
        super.mouseOut(component);

        if (toolbar
            && component.isFocused()) {
            Component.clearFocus();
        }
    }

    @Override
    public boolean mouseClick(Component component, Mouse.Button button, int x, int y, int count) {
        if (!toolbar) {
            component.requestFocus();
        }

        return super.mouseClick(component, button, x, y, count);
    }

	@Override
	public void paint(DisplayHost displayHost) {
		// TODO Auto-generated method stub
		
	}
}
