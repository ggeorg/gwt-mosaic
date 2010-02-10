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
import org.gwt.beansbinding.ui.client.adapters.HasTextAdapterProvider;
import org.gwt.mosaic.beansbinding.client.GWTMosaicBindings;
import org.gwt.mosaic.beansbinding.client.ListBoxBinding;
import org.gwt.mosaic.beansbinding.client.adapters.ListBoxAdapterProvider;
import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseData;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.ui.client.ListBox;
import org.gwt.mosaic.ui.client.LoadingPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasText;
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
    BeanAdapterFactory.addProvider(new HasTextAdapterProvider());
    BeanAdapterFactory.addProvider(new ListBoxAdapterProvider());

    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout(
        Orientation.VERTICAL));
    layoutPanel.setPadding(0);

    listBox = new ListBox<Customer>();

    list = ObservableCollections.observableList(new ArrayList<Customer>());
    list.supportsElementPropertyChanged();

    final BindingGroup bindingGroup = new BindingGroup();

    // create the binding from List to ListBox
    final ListBoxBinding<Customer, List<Customer>, ListBox<Customer>> listBoxBinding = GWTMosaicBindings.createListBoxBinding(
        UpdateStrategy.READ, list, listBox);

    BeanProperty<Customer, String> codeP = BeanProperty.<Customer, String> create("discountCode");
    BeanProperty<Customer, String> nameP = BeanProperty.<Customer, String> create("name");

    // add columns bindings to the ListBoxBinding
    listBoxBinding.addColumnBinding(codeP).setColumnName("Discount Code");
    listBoxBinding.addColumnBinding(nameP).setColumnName("Name");

    // The text property can be used to get the text property of any HasText implementation
    final BeanProperty<HasText, String> textP = BeanProperty.<HasText, String> create("text");

    TextBox textBox1 = new TextBox();
    textBox1.setMaxLength(1);

    final Binding<ListBox<Customer>, Customer, HasText, String> textBoxBinding1 = Bindings.createAutoBinding(
        UpdateStrategy.READ_WRITE,
        listBox,
        BeanProperty.<ListBox<Customer>, Customer> create("selectedElement.discountCode"),
        textBox1, textP);

    TextBox textBox2 = new TextBox();

    final Binding<ListBox<Customer>, Customer, HasText, String> textBoxBinding2 = Bindings.createAutoBinding(
        UpdateStrategy.READ_WRITE,
        listBox,
        BeanProperty.<ListBox<Customer>, Customer> create("selectedElement.name"),
        textBox2, textP);

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

}
