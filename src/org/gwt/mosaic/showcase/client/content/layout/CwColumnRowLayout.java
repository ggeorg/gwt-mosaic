package org.gwt.mosaic.showcase.client.content.layout;

import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.ui.client.CaptionLayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.ColumnLayout;
import org.gwt.mosaic.ui.client.layout.ColumnLayoutData;
import org.gwt.mosaic.ui.client.layout.FillLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.RowLayout;
import org.gwt.mosaic.ui.client.layout.RowLayoutData;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;

import com.google.gwt.user.client.ui.Widget;

public class CwColumnRowLayout extends ContentWidget {

  public CwColumnRowLayout(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "ColoumnLayout & RowLayout description";
  }

  @Override
  public String getName() {
    return "Column/RowLayout";
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new LayoutPanel(new ColumnLayout());

    layoutPanel.setPadding(0);
    layoutPanel.setWidgetSpacing(8);
    
    LayoutPanel col1 = new LayoutPanel(new RowLayout());
    col1.setPadding(0);
    col1.setWidgetSpacing(8);

    LayoutPanel col2 = new LayoutPanel(new RowLayout());
    col2.setPadding(0);
    col2.setWidgetSpacing(8);

    LayoutPanel col3 = new LayoutPanel(new RowLayout());
    col3.setPadding(0);
    col3.setWidgetSpacing(8);

    col1.add(newCaptionLayoutPanel("Caption #1"), new RowLayoutData(1));
    col1.add(newCaptionLayoutPanel("Caption #2"), new RowLayoutData(2));
    col1.add(newCaptionLayoutPanel("Caption #3"), new RowLayoutData(2));

    col2.add(newCaptionLayoutPanel("Caption #1"), new RowLayoutData(3));
    col2.add(newCaptionLayoutPanel("Caption #2"), new RowLayoutData(2));

    col3.add(newCaptionLayoutPanel("Caption #1"), new RowLayoutData(2));
    col3.add(newCaptionLayoutPanel("Caption #2"), new RowLayoutData(1));
    col3.add(newCaptionLayoutPanel("Caption #3"), new RowLayoutData(1));

    layoutPanel.add(col1, new ColumnLayoutData(1));
    layoutPanel.add(col2, new ColumnLayoutData(3));
    layoutPanel.add(col3, new ColumnLayoutData(1));

    return layoutPanel;
  }
  
  /**
   * @return a CaptionLayoutPanel widget.
   */
  @ShowcaseSource
  private Widget newCaptionLayoutPanel(String caption) {
    LayoutPanel layoutPanel = new LayoutPanel();
    layoutPanel.add(new CaptionLayoutPanel(caption), new FillLayoutData(true));
    return layoutPanel;
  }

}
