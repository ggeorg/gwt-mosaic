package org.mosaic.showcase.client.pages;

import java.util.Date;

import org.mosaic.showcase.client.Page;
import org.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseData;
import org.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.mosaic.ui.client.datepicker.DatePicker;
import org.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.i18n.client.Constants;

/**
 * Example file.
 */
@ShowcaseStyle( {".mosaic-DatePicker"})
public class DatePickerPage extends Page {
  
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
  @ShowcaseSource
  @Override
  protected void onPageLoad(LayoutPanel layoutPanel) {
    DatePicker picker = new DatePicker();
    Date d = new Date();
    d.setMonth(2);
    d.setDate(1);
    picker.setSelectedDate(d);
    
    layoutPanel.add(picker);
  }

  @Override
  public String getName() {
    return "DatePicker";
  }

}
