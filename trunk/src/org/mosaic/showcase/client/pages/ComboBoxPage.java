package org.mosaic.showcase.client.pages;

import org.mosaic.showcase.client.pages.Annotations.MosaicData;
import org.mosaic.showcase.client.pages.Annotations.MosaicSource;
import org.mosaic.showcase.client.pages.Annotations.MosaicStyle;
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
@MosaicStyle( {".gwt-ComboBox"})
public class ComboBoxPage extends Page {
  
  /**
   * The constants used in this Page.
   */
  @MosaicSource
  public static interface DemoConstants extends Constants, Page.DemoConstants {
    
  }
  
  /**
   * An instance of the constants.
   */
  @MosaicData
  private DemoConstants constants;

  public ComboBoxPage(DemoConstants constants) {
    super(constants);
    this.constants = constants;
  }

  /**
   * Load this example.
   */
  @MosaicSource
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
