package org.gwt.mosaic.showcase.client.content.layout;

import java.util.Iterator;

import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.ui.client.ScrollLayoutPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.BorderLayoutData;
import org.gwt.mosaic.ui.client.layout.FlowLayout;
import org.gwt.mosaic.ui.client.layout.FlowLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout.Region;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

public class CwFlowLayout extends ContentWidget {

  public CwFlowLayout(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "FlowLayout description";
  }

  @Override
  public String getName() {
    return "FlowLayout";
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new ScrollLayoutPanel(new FlowLayout());
    layoutPanel.setPadding(0);

    layoutPanel.add(createWidget());
    layoutPanel.add(createWidget(), new FlowLayoutData("250px", "100px", true));
    layoutPanel.add(createWidget(), new FlowLayoutData("200px", "150px"));
    layoutPanel.add(createWidget(), new FlowLayoutData("150px", "200px", true));

    layoutPanel.add(createWidget());
    layoutPanel.add(createWidget(), new FlowLayoutData("250px", "100px", true));
    layoutPanel.add(createWidget(), new FlowLayoutData("200px", "150px"));
    layoutPanel.add(createWidget(), new FlowLayoutData("150px", "200px", true));

    return layoutPanel;
  }
  
  /**
   * @return a widget.
   */
  @ShowcaseSource
  private Widget createWidget() {
    final LayoutPanel layoutPanel = new LayoutPanel(new BorderLayout());
    layoutPanel.add(new Button("NORTH"), new BorderLayoutData(Region.NORTH));
    layoutPanel.add(new Button("SOUTH"), new BorderLayoutData(Region.SOUTH));
    layoutPanel.add(new Button("WEST"), new BorderLayoutData(Region.WEST));
    layoutPanel.add(new Button("EAST"), new BorderLayoutData(Region.EAST));
    layoutPanel.add(new Button("CENTER"), new BorderLayoutData(Region.CENTER, true));

    for (Iterator<Widget> iter = layoutPanel.iterator(); iter.hasNext();) {
      Widget w = iter.next();
      ((BorderLayoutData) w.getLayoutData()).setResizable(true);
    }

    return layoutPanel;
  }

}
