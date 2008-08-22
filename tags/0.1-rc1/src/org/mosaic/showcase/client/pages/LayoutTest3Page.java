package org.mosaic.showcase.client.pages;

import org.mosaic.showcase.client.Page;
import org.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasAlignment;

/**
 * 
 */
@ShowcaseStyle( {".mosaic-LayoutPanel"})
public class LayoutTest3Page extends Page {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public LayoutTest3Page(DemoConstants constants) {
    super(constants);
    // TODO Auto-generated constructor stub
  }

  /**
   * 
   */
  @ShowcaseSource
  @Override
  protected void onPageLoad(LayoutPanel layoutPanel) {
    final Grid grid = new Grid(2, 2);
    
    grid.getCellFormatter().setAlignment(0, 0, HasAlignment.ALIGN_LEFT,
        HasAlignment.ALIGN_TOP);
    grid.setWidget(0, 0, new Button("Top Left"));
    
    grid.getCellFormatter().setAlignment(0, 1, HasAlignment.ALIGN_RIGHT,
        HasAlignment.ALIGN_TOP);
    grid.setWidget(0, 1, new Button("Top Right"));
    
    grid.getCellFormatter().setAlignment(1, 0, HasAlignment.ALIGN_LEFT,
        HasAlignment.ALIGN_BOTTOM);
    grid.setWidget(1, 0, new Button("Bottom Left"));
    
    grid.getCellFormatter().setAlignment(1, 1, HasAlignment.ALIGN_RIGHT,
        HasAlignment.ALIGN_BOTTOM);
    grid.setWidget(1, 1, new Button("Bottom Right"));
    
    layoutPanel.add(grid);
  }

  @Override
  public String getName() {
    return "Test3";
  }

}
