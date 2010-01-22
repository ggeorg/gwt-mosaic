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

import org.gwt.beansbinding.core.client.BeanProperty;
import org.gwt.beansbinding.core.client.Binding;
import org.gwt.beansbinding.core.client.BindingGroup;
import org.gwt.beansbinding.core.client.Bindings;
import org.gwt.beansbinding.core.client.Converter;
import org.gwt.beansbinding.core.client.AutoBinding.UpdateStrategy;
import org.gwt.beansbinding.core.client.ext.BeanAdapterFactory;
import org.gwt.beansbinding.ui.client.adapters.HasValueAdapterProvider;
import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.ui.client.SliderBar;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class CwSliderBarBinding extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwSliderBarBinding(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "SliderBar & TextBox binding example. "
        + "Notice that the TextBox is filled with the default value of the SliderBar, "
        + "indicating that the two widgets are connected. "
        + "In other words, change something in the SliderBar and the TextBox will automatically be updated. "
        + "Now, type a value between 0.0 and 100.0 in the TextBox and hit [Enter]... the SliderBar is updated!";
  }

  @Override
  public String getName() {
    return "SliderBar/TextBox Binding";
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    BeanAdapterFactory.addProvider(new HasValueAdapterProvider<Double>());
    BeanAdapterFactory.addProvider(new HasValueAdapterProvider<String>());

    HasValueAdapterProvider.<Double> register(SliderBar.class);

    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout(
        Orientation.VERTICAL));
    layoutPanel.setPadding(20);

    final SliderBar sliderBar = new SliderBar(0, 100);
    sliderBar.setCurrentValue(50);

    final TextBox textBox = new TextBox();

    final BindingGroup bindingGroup = new BindingGroup();

    // create the binding from SliderBar to TextBox
    Binding<SliderBar, Double, TextBox, String> binding = Bindings.createAutoBinding(
        UpdateStrategy.READ_WRITE, sliderBar,
        BeanProperty.<SliderBar, Double> create("value"), textBox,
        BeanProperty.<TextBox, String> create("value"));

    binding.setConverter(new Converter<Double, String>() {
      @Override
      public String convertForward(Double value) {
        return value.toString();
      }

      @Override
      public Double convertReverse(String value) {
        try {
          return Double.parseDouble(value);
        } catch (Exception ex) {
          return 0.0;
        }
      }
    });

    // realize the binding
    bindingGroup.addBinding(binding);
    bindingGroup.bind();

    layoutPanel.add(textBox, new BoxLayoutData(FillStyle.HORIZONTAL));
    layoutPanel.add(sliderBar, new BoxLayoutData(FillStyle.HORIZONTAL));

    return layoutPanel;
  }

  @Override
  public boolean hasStyle() {
    return false;
  }

}
