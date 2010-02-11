/*
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
package org.gwt.mosaic.showcase.client.content.tables;

import org.gwt.mosaic.core.client.CoreConstants;
import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.Showcase;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseData;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.ListBox;
import org.gwt.mosaic.ui.client.ListBox.CellRenderer;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.gwt.mosaic.ui.client.list.DefaultListModel;
import org.gwt.mosaic.ui.client.list.Filter;
import org.gwt.mosaic.ui.client.list.FilterProxyListModel;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {".mosaic-LayoutPanel"})
public class CwRichListBox extends ContentWidget {

  /**
   * 
   * @return
   */
  @ShowcaseSource
  static class Person {
    private String name;
    private String gender;
    private Boolean isMarried = false;

    public Person(String name, String gender, boolean isMarried) {
      this.name = name;
      this.gender = gender;
      this.isMarried = isMarried;
    }

    public String getGender() {
      return gender;
    }

    public String getName() {
      return name;
    }

    public boolean isMarried() {
      return isMarried;
    }

    public void setGender(String gender) {
      this.gender = gender;
    }

    public void setIsMarried(boolean isMarried) {
      this.isMarried = isMarried;
    }

    public void setName(String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return getName() + " " + getGender() + " " + String.valueOf(isMarried());
    }
  }

  /**
   * 
   */
  @ShowcaseData
  private TextBox textBox;

  /**
   * 
   */
  @ShowcaseData
  private FilterProxyListModel<Person, String> filterModel;

  /**
   * 
   */
  @ShowcaseSource
  private Timer filterTimer = new Timer() {
    @Override
    public void run() {
      filterModel.filter(textBox.getText());
    }
  };

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwRichListBox(CwConstants constants) {
    super(constants);
  }

  /**
   * 
   * @return
   */
  @ShowcaseSource
  private Widget createRichListBoxCell(Person item) {
    final FlexTable table = new FlexTable();
    final FlexCellFormatter cellFormatter = table.getFlexCellFormatter();

    table.setWidth("100%");
    table.setBorderWidth(0);
    table.setCellPadding(3);
    table.setCellSpacing(0);
    table.setStyleName("RichListBoxCell");
    
    table.setWidget(0, 0, new Image(Showcase.IMAGES.person()));
    cellFormatter.setRowSpan(0, 0, 3);
    cellFormatter.setWidth(0, 0, "32px");
    
    table.setHTML(0, 1, "<b>" + item.getName() + "</b>");
    
    table.setText(1, 0, "Genter: " + item.getGender());
    table.setText(2, 0, "Married: " + item.isMarried());

    return table;
  }

  /**
   * 
   * @return
   */
  @ShowcaseSource
  public ListBox<?> createListBox() {
    final ListBox<Person> listBox = new ListBox<Person>();
    listBox.setCellRenderer(new CellRenderer<Person>() {
      public void renderCell(ListBox<Person> listBox, int row, int column,
          Person item) {
        switch (column) {
          case 0:
            listBox.setWidget(row, column, createRichListBoxCell(item));
            break;
          default:
            throw new RuntimeException("Should not happen");
        }
      }
    });

    final DefaultListModel<Person> model = new DefaultListModel<Person>();
    model.add(new Person("Rainer Zufall", "male", true));
    model.add(new Person("Marie Darms", "female", false));
    model.add(new Person("Holger Adams", "male", true));
    model.add(new Person("Juliane Adams", "female", true));

    filterModel = new FilterProxyListModel<Person, String>(model);
    filterModel.setModelFilter(new Filter<Person, String>() {
      public boolean select(Person element, String filter) {
        final String regexp = ".*" + filter + ".*";
        if (regexp == null || regexp.length() == 0) {
          return true;
        }
        return element.getName().matches(regexp);
      }
    });

    listBox.setModel(filterModel);

    return listBox;
  }

  @Override
  public String getDescription() {
    return "A ListBox with a custom CellRenderer.";
  }

  @Override
  public String getName() {
    return "Rich ListBox";
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout(
        Orientation.VERTICAL));
    layoutPanel.setPadding(0);

    textBox = new TextBox();
    textBox.addKeyPressHandler(new KeyPressHandler() {
      public void onKeyPress(KeyPressEvent event) {
        filterTimer.schedule(CoreConstants.DEFAULT_DELAY_MILLIS);
      }
    });

    layoutPanel.add(textBox, new BoxLayoutData(FillStyle.HORIZONTAL));
    layoutPanel.add(createListBox(), new BoxLayoutData(FillStyle.BOTH));

    return layoutPanel;
  }

}
