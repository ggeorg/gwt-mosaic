package org.mosaic.showcase.client.pages;

import java.util.Date;

import org.mosaic.showcase.client.pages.Annotations.MosaicData;
import org.mosaic.showcase.client.pages.Annotations.MosaicSource;
import org.mosaic.showcase.client.pages.Annotations.MosaicStyle;
import org.mosaic.ui.client.datepicker.DatePicker;
import org.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.i18n.client.Constants;

/**
 * Example file.
 */
@MosaicStyle( {".mosaic-DatePicker"})
public class DatePickerPage extends Page {
  
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

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public DatePickerPage(DemoConstants constants) {
    super(constants);
    this.constants = constants;
  }

  /**
   * Load this example.
   */
  @MosaicSource
  @Override
  protected void onPageLoad(LayoutPanel layoutPanel) {
    DatePicker picker = new DatePicker();
    Date d = new Date();
    d.setMonth(2);
    d.setDate(1);
    picker.setSelectedDate(d);
    
    layoutPanel.add(picker);
  }

}
