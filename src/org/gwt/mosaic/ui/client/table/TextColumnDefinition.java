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
import com.google.gwt.resources.client.ClientBundle;
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
public abstract class TextColumnDefinition<RowType> extends
    AbstractColumnDefinition<RowType, String> {
  /**
   * The {@link TextCellRenderer} used by the {@link TextColumnDefinition} when
   * the user does not specify one.
   * 
   * @param <RowType> the type of the row value
   * @param <ColType> the data type of the column
   */
  class TextCellRenderer implements CellRenderer<RowType, String> {
    public void renderRowValue(RowType rowValue,
        ColumnDefinition<RowType, String> columnDef,
        AbstractCellView<RowType> view) {
      String cellValue = columnDef.getCellValue(rowValue);
      if (cellValue == null) {
        view.setText("");
      } else {
        view.setHTML(cellValue.toString());
      }
    }
  }

  /**
   * An {@link InlineCellEditor} that can be used to edit {@link String Strings}
   */
  class TextCellEditor extends SimpleInlineCellEditor<String> {
    /**
     * The text field used in this editor.
     */
    private TextBoxBase textBox;

    /**
     * Construct a new {@link TextCellEditor} using a normal {@link TextBox}.
     */
    public TextCellEditor() {
      this(new TextBox());
    }

    /**
     * Construct a new {@link TextCellEditor} using the specified
     * {@link TextBox}.
     * 
     * @param textBox the text box to use
     */
    public TextCellEditor(TextBoxBase textBox) {
      super(textBox);
      this.textBox = textBox;

      textBox.addKeyUpHandler(new KeyUpHandler() {
        public void onKeyUp(KeyUpEvent event) {
          int keyCode = event.getNativeKeyCode();
          switch (keyCode) {
            case KeyCodes.KEY_ENTER:
            case KeyCodes.KEY_TAB:
              TextCellEditor.this.accept();
              break;
            case KeyCodes.KEY_ESCAPE:
              TextCellEditor.this.cancel();
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
    public void editCell(CellEditInfo cellEditInfo, String cellValue,
        Callback<String> callback) {
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
    protected String getValue() {
      return textBox.getText();
    }

    @Override
    protected void setValue(String cellValue) {
      if (cellValue == null) {
        cellValue = "";
      }
      textBox.setText(cellValue);
    }
  }

  /**
   * Resources used.
   */
  public interface TextColumnStyle extends ClientBundle {
  }

  public interface TextColumnMessages extends Messages {
    @DefaultMessage("Show text starting with")
    String startsWithTooltip();

    @DefaultMessage("Show items starting with (case sensitive)")
    String startsWithCaseSensitiveTooltip();

    @DefaultMessage("Show items containing")
    String containsTooltip();

    @DefaultMessage("Show items containing (case sensitive)")
    String containsCaseSensitiveTooltip();

    @DefaultMessage("Show text ending with")
    String endsWithTooltip();

    @DefaultMessage("Show items ending with (case sensitive)")
    String endsWithCaseSensitiveTooltip();
  }

  public interface TextColumnResources {
    TextColumnStyle getStyle();

    TextColumnMessages getMessages();
  }

  protected static class DefaultTextColumnResources implements
      TextColumnResources {
    private TextColumnStyle style;
    private TextColumnMessages constants;

    public TextColumnStyle getStyle() {
      if (style == null) {
        style = ((TextColumnStyle) GWT.create(TextColumnStyle.class));
      }
      return style;
    }

    public TextColumnMessages getMessages() {
      if (constants == null) {
        constants = ((TextColumnMessages) GWT.create(TextColumnMessages.class));
      }
      return constants;
    }
  }

  public TextColumnDefinition(Widget headerWidget, boolean sortable,
      boolean editingEnabled) {
    this(headerWidget, sortable, editingEnabled,
        new DefaultTextColumnResources());
  }

  public TextColumnDefinition(Widget headerWidget, boolean sortable,
      boolean editingEnabled, TextColumnResources resources) {
    this(sortable, editingEnabled, resources);
    setHeader(0, headerWidget);
  }

  public TextColumnDefinition(String header, boolean sortable,
      boolean editingEnabled) {
    this(sortable, editingEnabled, new DefaultTextColumnResources());
    setHeader(0, header);
  }

  public TextColumnDefinition(String header, boolean sortable,
      boolean editingEnabled, TextColumnResources resources) {
    this(sortable, editingEnabled, resources);
    setHeader(0, header);
  }

  public TextColumnDefinition(boolean sortable, boolean editingEnabled) {
    this(sortable, editingEnabled, new DefaultTextColumnResources());
  }

  public TextColumnDefinition(boolean sortable, boolean editingEnabled,
      TextColumnResources resources) {
    setColumnSortable(sortable);
    setCellRenderer(createTextCellRenderer());
    if (editingEnabled) {
      setCellEditor(createTextCellEditor());
    }
  }

  /**
   * Creates the default text editor implementation. Override this method to
   * provide custom text editor
   * 
   * @return the created cell editor suitable for editing text
   */
  protected CellEditor<String> createTextCellEditor() {
    return new TextCellEditor();
  }

  /**
   * Creates the default text renderer implementation. Override this method to
   * provide custom text renderer
   * 
   * @return the created cell renderer suitable for rendering text
   */
  protected CellRenderer<RowType, String> createTextCellRenderer() {
    return new TextCellRenderer();
  }

  public void setCellValue(RowType rowValue, String cellValue) {
  };
}