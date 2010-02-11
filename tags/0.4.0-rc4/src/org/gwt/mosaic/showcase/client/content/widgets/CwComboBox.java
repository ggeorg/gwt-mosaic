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
package org.gwt.mosaic.showcase.client.content.widgets;

import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseData;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.ComboBox;
import org.gwt.mosaic.ui.client.ListBox;
import org.gwt.mosaic.ui.client.ComboBox.ComboBoxCellRenderer;
import org.gwt.mosaic.ui.client.datepicker.DateComboBox;
import org.gwt.mosaic.ui.client.datepicker.DateTimeComboBox;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.gwt.mosaic.ui.client.list.DefaultComboBoxModel;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {".gwt-ComboBox"})
public class CwComboBox extends ContentWidget {

  /**
   * The constants used in this Page.
   */
  @ShowcaseSource
  public static interface CwConstants extends Constants,
      ContentWidget.CwConstants {
    String mosaicComboBoxDescription();

    String mosaicComboBoxName();
  }

  /**
   * An instance of the constants.
   */
  @ShowcaseData
  private CwConstants constants;

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwComboBox(CwConstants constants) {
    super(constants);
    this.constants = constants;
  }

  @Override
  public String getDescription() {
    return constants.mosaicComboBoxDescription();
  }

  @Override
  public String getName() {
    return constants.mosaicComboBoxName();
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout(
        Orientation.VERTICAL));

    ComboBox<String> comboBox1 = new ComboBox<String>();
    DefaultComboBoxModel<String> model1 = (DefaultComboBoxModel<String>) comboBox1.getModel();
    model1.add("foo");
    model1.add("bar");
    model1.add("baz");
    model1.add("toto");
    model1.add("tintin");

    ComboBox<Person> comboBox2 = new ComboBox<Person>(new String[] {
        "Name", "Gender", "Married"});
    comboBox2.setCellRenderer(new ComboBoxCellRenderer<Person>() {
      public void renderCell(ListBox<Person> listBox, int row, int column,
          Person item) {
        switch (column) {
          case 0:
            listBox.setText(row, column, item.getName());
            break;
          case 1:
            listBox.setText(row, column, item.getGender());
            break;
          case 2:
            listBox.setText(row, column, String.valueOf(item.isMarried()));
            break;
          default:
            throw new RuntimeException("Should not happen");
        }
      }

      public String getDisplayText(Person item) {
        return item.getName();
      }
    });
    DefaultComboBoxModel<Person> model2 = (DefaultComboBoxModel<Person>) comboBox2.getModel();
    model2.add(new Person("Rainer Zufall", "male", true));
    model2.add(new Person("Marie Darms", "female", false));
    model2.add(new Person("Holger Adams", "male", true));
    model2.add(new Person("Juliane Adams", "female", true));

    DateComboBox comboBox3 = new DateComboBox();
    comboBox3.ensureDebugId("mosaicAbstractComboBox-normal");

    DateTimeComboBox comboBox4 = new DateTimeComboBox();
    comboBox4.ensureDebugId("mosaicAbstractComboBox-normal");

    DateComboBox comboBox5 = new DateComboBox();
    comboBox5.ensureDebugId("mosaicAbstractComboBox-normal");
    comboBox5.setEnabled(false);

    DateComboBox comboBox6 = new DateComboBox();
    comboBox6.ensureDebugId("mosaicAbstractComboBox-normal");

    layoutPanel.add(comboBox1, new BoxLayoutData());
    layoutPanel.add(comboBox2, new BoxLayoutData());
    layoutPanel.add(comboBox3, new BoxLayoutData());
    layoutPanel.add(comboBox4, new BoxLayoutData(FillStyle.HORIZONTAL));
    layoutPanel.add(comboBox5, new BoxLayoutData(FillStyle.VERTICAL));
    layoutPanel.add(comboBox6, new BoxLayoutData(FillStyle.BOTH));
    layoutPanel.add(new SimplePanel(), new BoxLayoutData(FillStyle.BOTH));

    return layoutPanel;
  }

  /**
   * 
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

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getGender() {
      return gender;
    }

    public void setGender(String gender) {
      this.gender = gender;
    }

    public boolean isMarried() {
      return isMarried;
    }

    public void setIsMarried(boolean isMarried) {
      this.isMarried = isMarried;
    }

    @Override
    public String toString() {
      return getName() + " " + getGender() + " " + String.valueOf(isMarried());
    }
  }

}
