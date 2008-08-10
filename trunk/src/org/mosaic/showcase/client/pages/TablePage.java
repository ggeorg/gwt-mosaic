package org.mosaic.showcase.client.pages;

import org.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.mosaic.ui.client.Table;
import org.mosaic.ui.client.layout.LayoutPanel;
import org.mosaic.ui.client.table.DefaultTableColumnModel;
import org.mosaic.ui.client.table.TableColumn;
import org.mosaic.ui.client.table.TableColumnModel;

/**
 * 
 */
@ShowcaseStyle({""})
public class TablePage extends Page {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public TablePage(DemoConstants constants) {
    super(constants);
    // TODO Auto-generated constructor stub
  }

  /**
   * 
   */
  @ShowcaseSource
  @Override
  protected void onPageLoad(LayoutPanel layoutPanel) {

    String[] columnNames = {"First Name", "Last Name", "Sport", "# of Years", "Vegetarian"};

    Object[][] data = {
        {"Mary", "Campione", "Snowboarding", new Integer(5), new Boolean(false)},
        {"Alison", "Huml", "Rowing", new Integer(3), new Boolean(true)},
        {"Kathy", "Walrath", "Knitting", new Integer(2), new Boolean(false)},
        {"Sharon", "Zakhour", "Speed reading", new Integer(20), new Boolean(true)},
        {"Philip", "Milne", "Pool", new Integer(10), new Boolean(false)}};
    
    TableColumnModel cm = new DefaultTableColumnModel();
    for (int i = 0; i < columnNames.length; i++) {
      TableColumn column = new TableColumn(i);
      column.setHeaderValue(columnNames[i]);
      column.setPreferredWidth(100);
      cm.addColumn(column);
    }

    Table table = new Table(null, cm);

    layoutPanel.add(table);
  }

}
