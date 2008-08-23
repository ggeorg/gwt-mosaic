package org.mosaic.ui.client.datepicker;

import java.util.Date;

import org.mosaic.ui.client.layout.BoxLayout;
import org.mosaic.ui.client.layout.BoxLayoutData;
import org.mosaic.ui.client.layout.HasLayoutManager;
import org.mosaic.ui.client.layout.LayoutPanel;
import org.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

/**
 * Date picker.
 */
public class DatePicker extends com.google.gwt.widgetideas.datepicker.client.DatePicker
    implements HasLayoutManager {

  /**
   * The default style name.
   */
  private static final String DEFAULT_STYLENAME = "mosaic-DatePicker";

  /**
   * Sets up the date picker.
   */
  protected void setup() {
    final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
    layoutPanel.setWidgetSpacing(0);
    initWidget(layoutPanel);
    setStyleName(DEFAULT_STYLENAME);
    layoutPanel.add(this.getMonthSelector(), new BoxLayoutData(FillStyle.HORIZONTAL));
    showDate(new Date());
    layoutPanel.add(this.getCalendarView(), new BoxLayoutData(FillStyle.BOTH));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.Composite#getWidget()
   */
  @Override
  protected LayoutPanel getWidget() {
    return (LayoutPanel) super.getWidget();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mosaic.ui.client.layout.HasLayoutManager#getPreferredSize()
   */
  public int[] getPreferredSize() {
    return getWidget().getPreferredSize();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mosaic.ui.client.layout.HasLayoutManager#layout()
   */
  public void layout() {
    getWidget().layout();
  }
}