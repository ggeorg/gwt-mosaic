package org.mosaic.showcase.client.pages;

import org.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseData;
import org.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.mosaic.ui.client.ComboBox;
import org.mosaic.ui.client.layout.BoxLayout;
import org.mosaic.ui.client.layout.BoxLayoutData;
import org.mosaic.ui.client.layout.LayoutPanel;
import org.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.i18n.client.Constants;

/**
 * Example file.
 */
@ShowcaseStyle( {".gwt-ComboBox"})
public class ComboBoxPage extends Page {
  
  /**
   * The constants used in this Page.
   */
  @ShowcaseSource
  public static interface DemoConstants extends Constants, Page.DemoConstants {
    
  }
  
  /**
   * An instance of the constants.
   */
  @ShowcaseData
  private DemoConstants constants;

  public ComboBoxPage(DemoConstants constants) {
    super(constants);
    this.constants = constants;
  }

  /**
   * Load this example.
   */
  @ShowcaseSource
  @Override
  protected void onPageLoad(LayoutPanel layoutPanel) {
    final BoxLayout vLayout = new BoxLayout(Orientation.VERTICAL);
    layoutPanel.setLayout(vLayout);
    
    //
    //
    //
    
    final LayoutPanel hBox1 = new LayoutPanel(new BoxLayout());
    layoutPanel.add(hBox1, new BoxLayoutData(FillStyle.BOTH, true));
    
    ComboBox comboBox = new ComboBox();
    comboBox.ensureDebugId("mosaicAbstractComboBox-normal");
    
    hBox1.add(comboBox, new BoxLayoutData(FillStyle.BOTH));
  }

}
