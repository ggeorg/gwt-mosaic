/*
 * Copyright (c) 2008-2010 GWT Mosaic Georgios J. Georgopoulos
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This is derived work from GWT Incubator project:
 * http://code.google.com/p/google-web-toolkit-incubator/
 * 
 * Copyright 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.gwt.mosaic.ui.client.table;

import org.gwt.mosaic.ui.client.table.TableDefinition.AbstractCellView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Derived work from GWT Incubator project
 * @author georgopoulos.georgios(at)gmail.com
 * 
 * @param <RowType> the data type of the row values
 */
public class NumberColumnDefinition<RowType> extends
    AbstractColumnDefinition<RowType, Double> {
  /**
   * The {@link NumberCellRenderer} used by the {@link NumberColumnDefinition}
   * when the user does not specify one.
   */
  class NumberCellRenderer implements CellRenderer<RowType, Double> {
    private final NumberFormat numberFormat;

    public NumberCellRenderer(NumberFormat numberFormat) {
      this.numberFormat = numberFormat;
    }

    public void renderRowValue(RowType rowValue,
        ColumnDefinition<RowType, Double> columnDef,
        AbstractCellView<RowType> view) {
      Double cellValue = columnDef.getCellValue(rowValue);
      if (cellValue == null) {
        view.setText("");
      } else {
        view.setText(numberFormat.format(cellValue));
      }
      view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    }
  }

  /**
   * An {@link InlineCellEditor} that can be used to edit {@link Number Numbers}
   */
  class NumberCellEditor extends SimpleInlineCellEditor<Double> {
    /**
     * The text field used in this editor.
     */
    private TextBoxBase textBox;
    private NumberFormat numberFormat;

    /**
     * Construct a new {@link TextCellEditor} using a normal {@link TextBox} .
     */
    public NumberCellEditor(NumberFormat numberFormat) {
      this(new TextBox(), numberFormat);
    }

    /**
     * Construct a new {@link TextCellEditor} using the specified
     * {@link TextBox}.
     * 
     * @param textBox the text box to use
     */
    public NumberCellEditor(TextBoxBase textBox, NumberFormat numberFormat) {
      super(textBox);
      this.textBox = textBox;
      this.numberFormat = numberFormat;

      textBox.addKeyUpHandler(new KeyUpHandler() {
        public void onKeyUp(KeyUpEvent event) {
          int keyCode = event.getNativeKeyCode();
          switch (keyCode) {
            case KeyCodes.KEY_ENTER:
            case KeyCodes.KEY_TAB:
              NumberCellEditor.this.accept();
              break;
            case KeyCodes.KEY_ESCAPE:
              NumberCellEditor.this.cancel();
              break;
          }
        }
      });
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.gwt.mosaic.ui.client.table.SimpleInlineCellEditor#editCell(org.gwt.mosaic.ui.client.table.CellEditor.CellEditInfo,
     *      java.lang.Object,
     *      org.gwt.mosaic.ui.client.table.CellEditor.Callback)
     */
    @Override
    public void editCell(CellEditInfo cellEditInfo, Double cellValue,
        Callback<Double> callback) {
      super.editCell(cellEditInfo, cellValue, callback);
      textBox.setFocus(true);
    }

    /**
     * @return the text box used in the editor
     */
    protected TextBoxBase getTextBox() {
      return textBox;
    }

    @Override
    protected Double getValue() {
      return new Double(numberFormat.parse(textBox.getText()));
    }

    @Override
    protected void setValue(Double cellValue) {
      if (cellValue == null) {
        textBox.setText("");
      } else {
        textBox.setText(numberFormat.format(cellValue.doubleValue()));
      }
    }
  }

  /**
   * Resources used.
   */
  public interface NumberColumnStyle extends ClientBundle {
  }

  public interface NumberColumnMessages extends Messages {
    @DefaultMessage("Shows numbers that are equal")
    String equalTooltip();

    @DefaultMessage("Shows numbers that not equal")
    String notEqualTooltip();

    @DefaultMessage("Show numbers less than given value")
    String lessThanTooltip();

    @DefaultMessage("Show numbers greater than given value")
    String greaterThanTooltip();

    @DefaultMessage("Show numbers between the given values")
    String betweenTooltip();
  }

  public interface NumberColumnResources {
    NumberColumnStyle getStyle();

    NumberColumnMessages getMessages();
  }

  protected static class DefaultNumberColumnResources implements
      NumberColumnResources {
    private NumberColumnStyle style;
    private NumberColumnMessages constants;

    public NumberColumnStyle getStyle() {
      if (style == null) {
        style = ((NumberColumnStyle) GWT.create(NumberColumnStyle.class));
      }
      return style;
    }

    public NumberColumnMessages getMessages() {
      if (constants == null) {
        constants = ((NumberColumnMessages) GWT.create(NumberColumnMessages.class));
      }
      return constants;
    }
  }

  protected NumberFormat numberFormat;

  public NumberColumnDefinition() {
    this(NumberFormat.getDecimalFormat(), true,
        new DefaultNumberColumnResources());
  }

  public NumberColumnDefinition(Widget headerWidget, NumberFormat numberFormat,
      boolean editingEnabled) {
    this(headerWidget, numberFormat, editingEnabled,
        new DefaultNumberColumnResources());
  }

  public NumberColumnDefinition(Widget headerWidget, NumberFormat numberFormat,
      boolean editingEnabled, NumberColumnResources resources) {
    this(numberFormat, editingEnabled, resources);
    setHeader(0, headerWidget);
  }

  public NumberColumnDefinition(String header, NumberFormat numberFormat,
      boolean editingEnabled) {
    this(header, numberFormat, editingEnabled,
        new DefaultNumberColumnResources());
  }

  public NumberColumnDefinition(String header, NumberFormat numberFormat,
      boolean editingEnabled, NumberColumnResources resources) {
    this(numberFormat, editingEnabled, resources);
    setHeader(0, header);
  }

  public NumberColumnDefinition(NumberFormat numberFormat,
      boolean editingEnabled) {
    this(numberFormat, editingEnabled, new DefaultNumberColumnResources());
  }

  public NumberColumnDefinition(NumberFormat numberFormat,
      boolean editingEnabled, NumberColumnResources resources) {
    this.numberFormat = numberFormat;
    setCellRenderer(createNumberCellRenderer(numberFormat));
    if (editingEnabled) {
      setCellEditor(createNumberCellEditor(numberFormat));
    }
  }

  /**
   * Creates the default text editor implementation. Override this method to
   * provide custom text editor
   * 
   * @return the created cell editor suitable for editing text
   */
  protected CellEditor<Double> createNumberCellEditor(NumberFormat numberFormat) {
    return new NumberCellEditor(numberFormat);
  }

  /**
   * Creates the default text renderer implementation. Override this method to
   * provide custom text renderer
   * 
   * @return the created cell renderer suitable for rendering text
   */
  protected CellRenderer<RowType, Double> createNumberCellRenderer(
      NumberFormat numberFormat) {
    return new NumberCellRenderer(numberFormat);
  }
}