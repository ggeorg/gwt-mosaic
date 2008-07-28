package org.mosaic.ui.client.layout;


/**
 * Defines the interface for classes that know how to lay out a
 * <code>LayoutPanel</code>.
 */
public interface LayoutManager {

  /**
   * Lays out the specified <code>LayoutPanel</code>.
   * 
   * @param layoutPanel the <code>LayoutPanel</code> to be laid out
   */
  void layoutPanel(LayoutPanel layoutPanel);
  
  /**
   * 
   * @param layoutPanel
   * @return
   */
  int[] getPreferredSize(LayoutPanel layoutPanel);
  
}
