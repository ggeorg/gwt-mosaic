/*
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopoulos.
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
package org.gwt.mosaic.showcase.client.content.other;

import java.util.ArrayList;
import java.util.List;

import org.gwt.beansbinding.core.client.BeanProperty;
import org.gwt.beansbinding.core.client.Binding;
import org.gwt.beansbinding.core.client.BindingGroup;
import org.gwt.beansbinding.core.client.Bindings;
import org.gwt.beansbinding.core.client.AutoBinding.UpdateStrategy;
import org.gwt.beansbinding.core.client.ext.BeanAdapterFactory;
import org.gwt.beansbinding.observablecollections.client.ObservableCollections;
import org.gwt.beansbinding.observablecollections.client.ObservableList;
import org.gwt.beansbinding.ui.client.adapters.TextBoxAdapterProvider;
import org.gwt.mosaic.beansbinding.client.ListBoxBinding;
import org.gwt.mosaic.beansbinding.client.MosaicBindings;
import org.gwt.mosaic.beansbinding.client.adapters.ListBoxAdapterProvider;
import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseData;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.ui.client.ListBox;
import org.gwt.mosaic.ui.client.LoadingPanel;
import org.gwt.mosaic.ui.client.ListBox.CellRenderer;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class CwListBoxBinding extends ContentWidget {

  /**
   * 
   */
  @ShowcaseData
  private ObservableList<Customer> list;

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwListBoxBinding(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "ListBox bindings description";
  }

  @Override
  public String getName() {
    return "ListBox Binding";
  }

  /**
   * 
   */
  @ShowcaseData
  private ListBox<Customer> listBox;

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    BeanAdapterFactory.addProvider(new TextBoxAdapterProvider());
    BeanAdapterFactory.addProvider(new ListBoxAdapterProvider());

    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout(
        Orientation.VERTICAL));
    layoutPanel.setPadding(0);

    listBox = new ListBox<Customer>(
        new String[] {"Id", "Discount Code", "Name"});
    listBox.setCellRenderer(new CellRenderer<Customer>() {
      public void renderCell(ListBox<Customer> litsBox, int row, int column,
          Customer item) {
        switch (column) {
          case 0:
            listBox.setText(row, column, item.getCustomerId().toString());
            break;
          case 1:
            listBox.setText(row, column, String.valueOf(item.getDiscountCode()));
            break;
          case 2:
            listBox.setText(row, column, item.getName());
            break;
        }
      }
    });

    list = ObservableCollections.observableList(new ArrayList<Customer>());
    list.supportsElementPropertyChanged();

    final BindingGroup bindingGroup = new BindingGroup();

    // create the binding from List to ListBox
    final ListBoxBinding listBoxBinding = MosaicBindings.createListBoxBinding(
        UpdateStrategy.READ, list, listBox);

    // add columns bindings to the ListBoxBinding
    listBoxBinding.addColumnBinding(BeanProperty.create("discountCode"));
    listBoxBinding.addColumnBinding(BeanProperty.create("name"));

    TextBox textBox1 = new TextBox();
    textBox1.setMaxLength(1);

    final Binding textBoxBinding1 = Bindings.createAutoBinding(
        UpdateStrategy.READ_WRITE, listBox,
        BeanProperty.create("selectedElement.discountCode"), textBox1,
        BeanProperty.create("text"));

    TextBox textBox2 = new TextBox();

    final Binding textBoxBinding2 = Bindings.createAutoBinding(
        UpdateStrategy.READ_WRITE, listBox,
        BeanProperty.create("selectedElement.name"), textBox2,
        BeanProperty.create("text"));

    // realize the binding
    bindingGroup.addBinding(listBoxBinding);
    bindingGroup.addBinding(textBoxBinding1);
    bindingGroup.addBinding(textBoxBinding2);
    bindingGroup.bind();

    layoutPanel.add(listBox, new BoxLayoutData(FillStyle.BOTH));
    layoutPanel.add(textBox1, new BoxLayoutData(FillStyle.HORIZONTAL));
    layoutPanel.add(textBox2, new BoxLayoutData(FillStyle.HORIZONTAL));

    return layoutPanel;
  }

  /**
   * This method is called immediately after a widget becomes attached to the
   * browser's document.
   * 
   * @see org.gwt.mosaic.showcase.client.ContentWidget#onLoad()
   */
  @ShowcaseSource
  @Override
  protected void onLoad() {
    super.onLoad();

    // Run after loading
    DeferredCommand.addCommand(new Command() {
      public void execute() {
        // RPC call to fetch the data
        final LoadingPanel loadingPanel = LoadingPanel.show(listBox,
            "Loading...");
        CustomerServiceFactory.get().getCustomers(
            new AsyncCallback<List<Customer>>() {
              public void onFailure(Throwable caught) {
                try {
                  Window.alert("Error: " + caught.getLocalizedMessage());
                } finally {
                  loadingPanel.hide();
                }
              }

              public void onSuccess(List<Customer> result) {
                try {
                  if (result != null) {
                    list.clear();
                    list.addAll(result);
                  }
                } finally {
                  loadingPanel.hide();
                }
              }
            });
      }
    });
  }

  @Override
  public boolean hasStyle() {
    return false;
  }

  @Override
  protected void asyncOnInitialize(final AsyncCallback<Widget> callback) {
    GWT.runAsync(new RunAsyncCallback() {

      public void onFailure(Throwable caught) {
        callback.onFailure(caught);
      }

      public void onSuccess() {
        callback.onSuccess(onInitialize());
      }
    });
  }
  
}