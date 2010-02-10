package org.gwt.mosaic.showcase.client.content.draganddrop;

import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.ui.client.CaptionLayoutPanel;
import org.gwt.mosaic.ui.client.layout.ColumnLayout;
import org.gwt.mosaic.ui.client.layout.ColumnLayoutData;
import org.gwt.mosaic.ui.client.layout.FillLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.RowLayout;
import org.gwt.mosaic.ui.client.layout.RowLayoutData;

import com.google.gwt.user.client.ui.Widget;

public class CwDNDColumnRowLayout extends ContentWidget {

  public CwDNDColumnRowLayout(CwConstants constants) {
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
    
    layoutPanel.enableDragAndDrop(false);

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

    col1.add(newCaptionLayoutPanel(layoutPanel, "Drag me: #1-1"), new RowLayoutData(1, true));
    col1.add(newCaptionLayoutPanel(layoutPanel, "Drag me: #1-2"), new RowLayoutData(2, true));
    col1.add(newCaptionLayoutPanel(layoutPanel, "Drag me: #1-3"), new RowLayoutData(2, true));

    col2.add(newCaptionLayoutPanel(layoutPanel, "Drag me: #2-1"), new RowLayoutData(3, true));
    col2.add(newCaptionLayoutPanel(layoutPanel, "Drag me: #2-2"), new RowLayoutData(2, true));

    col3.add(newCaptionLayoutPanel(layoutPanel, "Drag me: #3-1"), new RowLayoutData(2, true));
    col3.add(newCaptionLayoutPanel(layoutPanel, "Drag me: #3-2"), new RowLayoutData(1, true));
    col3.add(newCaptionLayoutPanel(layoutPanel, "Drag me: #3-3"), new RowLayoutData(1, true));

    layoutPanel.add(col1, new ColumnLayoutData(1));
    layoutPanel.add(col2, new ColumnLayoutData(3));
    layoutPanel.add(col3, new ColumnLayoutData(1));
    
    layoutPanel.registerDropController(col1);
    layoutPanel.registerDropController(col2);
    layoutPanel.registerDropController(col3);

    return layoutPanel;
  }
  
  /**
   * @return a CaptionLayoutPanel widget.
   */
  @ShowcaseSource
  private Widget newCaptionLayoutPanel(LayoutPanel layoutPanel, String caption) {
    CaptionLayoutPanel panel = new CaptionLayoutPanel(caption);
    layoutPanel.makeDraggable(panel, panel.getHeader());
    return panel;
  }

}
