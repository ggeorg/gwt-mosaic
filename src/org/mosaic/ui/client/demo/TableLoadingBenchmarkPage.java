package org.mosaic.ui.client.demo;

import org.mosaic.ui.client.InfoPanel;
import org.mosaic.ui.client.InfoPanel.InfoPanelType;
import org.mosaic.ui.client.demo.Annotations.MosaicData;
import org.mosaic.ui.client.demo.Annotations.MosaicSource;
import org.mosaic.ui.client.demo.Annotations.MosaicStyle;
import org.mosaic.ui.client.layout.BorderLayout;
import org.mosaic.ui.client.layout.BorderLayoutData;
import org.mosaic.ui.client.layout.BoxLayout;
import org.mosaic.ui.client.layout.BoxLayoutData;
import org.mosaic.ui.client.layout.LayoutPanel;
import org.mosaic.ui.client.layout.BorderLayout.BorderLayoutRegion;
import org.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.mosaic.ui.client.scrolltable.ClientTableModel;
import org.mosaic.ui.client.scrolltable.FlexTableBulkRenderer;
import org.mosaic.ui.client.scrolltable.PreloadedTable;
import org.mosaic.ui.client.scrolltable.RendererCallback;
import org.mosaic.ui.client.scrolltable.overrides.FlexTable;
import org.mosaic.ui.client.scrolltable.overrides.Grid;
import org.mosaic.ui.client.scrolltable.overrides.HTMLTable;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 */
@MosaicStyle( {".gwt-ScrollTable"})
public class TableLoadingBenchmarkPage extends Page {

  /**
   * The constants used in this Page.
   */
  @MosaicSource
  public static interface DemoConstants extends Constants, Page.DemoConstants {
  }

  int numColumns = 15;
  int numRows = 120;

  HTMLTable curTable;

  ClientTableModel<Object> tableModel = new ClientTableModel<Object>() {

    @Override
    public Object getCell(int rowNum, int cellNum) {
      if (rowNum >= numRows | cellNum >= numColumns) {
        return null;
      }
      return "cell " + rowNum + ", " + cellNum;
    }

    @Override
    protected boolean onRowInserted(int beforeRow) {
      return false;
    }

    @Override
    protected boolean onRowRemoved(int row) {
      return false;
    }

    @Override
    protected boolean onSetData(int row, int cell, Object data) {
      return false;
    }

  };

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
  public TableLoadingBenchmarkPage(DemoConstants constants) {
    super(constants);
    this.constants = constants;
  }

  private ScrollPanel center = new ScrollPanel();

  /**
   * Load this example.
   */
  @MosaicSource
  @Override
  protected void onPageLoad(LayoutPanel layoutPanel) {
    layoutPanel.setLayout(new BorderLayout());

    HTML caption = new HTML(
        "<b>A very boring demo showing the speed difference of using bulk loading tables.</b>");
    layoutPanel.add(caption, new BorderLayoutData(BorderLayoutRegion.NORTH));

    LayoutPanel panel = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));

    panel.add(new Label("Number of rows:"), new BoxLayoutData(FillStyle.HORIZONTAL));
    final TextBox rows = new TextBox();
    panel.add(rows, new BoxLayoutData(FillStyle.HORIZONTAL));
    rows.setText(numRows + "");
    rows.addChangeListener(new ChangeListener() {
      public void onChange(Widget sender) {
        numRows = Integer.parseInt(rows.getText().trim());
      }
    });

    panel.add(new Label("Number of columns"), new BoxLayoutData(FillStyle.HORIZONTAL));
    final TextBox columns = new TextBox();
    panel.add(columns, new BoxLayoutData(FillStyle.HORIZONTAL));
    columns.addChangeListener(new ChangeListener() {
      public void onChange(Widget sender) {
        numColumns = Integer.parseInt(columns.getText());
      }
    });
    columns.setText(numColumns + "");
    
    Button clearBtn = new Button("Clear Table now", new ClickListener() {
      public void onClick(Widget sender) {
        clearTable();
      }
    });
    panel.add(clearBtn, new BoxLayoutData(FillStyle.HORIZONTAL));
    panel.add(new HTML(
        "<em><small>Clearing will also happen if the buttons are clicked below</small></em><hr>"));

    Button flexTableAPI = new Button("Use the traditional FlexTable API",
        new ClickListener() {
          public void onClick(Widget sender) {
            clearTable();
            long milli = System.currentTimeMillis();
            FlexTable newTable = new FlexTable();
            usingFlexTableAPI(newTable);
            finishTable("Traditional FlexTable API", newTable, milli);
          }
        });
    panel.add(flexTableAPI, new BoxLayoutData(FillStyle.HORIZONTAL));

    Button gridAPI = new Button("Use the traditional Grid API", new ClickListener() {
      public void onClick(Widget sender) {
        clearTable();
        long milli = System.currentTimeMillis();
        Grid newTable = new Grid();
        usingGridAPI(newTable);
        finishTable("Traditional Grid API", newTable, milli);
      }

    });
    panel.add(gridAPI, new BoxLayoutData(FillStyle.HORIZONTAL));

    Button detachedGridAPI = new Button("Use the attached Grid API", new ClickListener() {
      public void onClick(Widget sender) {
        clearTable();
        long milli = System.currentTimeMillis();
        Grid table = new Grid();
        usingGridAPI(table);
        finishTable("Attached Grid API", table, milli);
      }
    });
    panel.add(detachedGridAPI, new BoxLayoutData(FillStyle.HORIZONTAL));

    Button asyncAPI = new Button("Use Async BulkLoadedTable API", new ClickListener() {
      public void onClick(Widget sender) {
        clearTable();
        long milli = System.currentTimeMillis();
        FlexTable table = new FlexTable();
        usingBulkLoadedTableAPI(table, milli);
      }
    });
    panel.add(asyncAPI, new BoxLayoutData(FillStyle.HORIZONTAL));

    Button pendingAPI = new Button("Using the preloadedTable  API", new ClickListener() {
      public void onClick(Widget sender) {
        clearTable();
        long milli = System.currentTimeMillis();
        PreloadedTable table = new PreloadedTable();
        usingPreloadedTableAPI(table);
        finishTable("PreloadedTable  API", table, milli);
      }
    });
    panel.add(pendingAPI, new BoxLayoutData(FillStyle.HORIZONTAL));

    layoutPanel.add(panel, new BorderLayoutData(BorderLayoutRegion.WEST, 250));
    layoutPanel.add(center, new BorderLayoutData(true));
  }

  private void clearTable() {
    if (curTable != null) {
      curTable.removeFromParent();
      curTable = null;
    }
  }

  private void finishTable(final String caption, final HTMLTable table, final long milli) {

    // In order to compare apples-to-apples for rendering time letting event cue
    // flush once.

    curTable = table;
    table.setBorderWidth(2);
    center.add(table);

    table.setWidget(0, 3, new Button("A widget"));

    InfoPanel.show(InfoPanelType.HUMANIZED_MESSAGE, caption, "Finished in "
        + (System.currentTimeMillis() - milli) + " milliseconds");
  }

  private void usingBulkLoadedTableAPI(final FlexTable table, final long milli) {
    RendererCallback callback = new RendererCallback() {
      public void onRendered() {
        finishTable("Async BulkLoadedTable API", table, milli);
      }
    };

    FlexTableBulkRenderer renderer = new FlexTableBulkRenderer(table);
    renderer.renderRows(tableModel, callback);
  }

  private void usingFlexTableAPI(FlexTable table) {
    for (int i = 0; i < numRows; i++) {
      for (int j = 0; j < numColumns; j++) {
        table.setHTML(i, j, "cell " + i + ", " + j);
      }
    }
  }

  private void usingGridAPI(Grid table) {
    table.resize(numRows, numColumns);
    for (int i = 0; i < numRows; i++) {
      for (int j = 0; j < numColumns; j++) {
        table.setHTML(i, j, "cell " + i + ", " + j);
      }
    }
  }

  private void usingPreloadedTableAPI(PreloadedTable table) {
    for (int i = 0; i < numRows; i++) {
      for (int j = 0; j < numColumns; j++) {
        table.setPendingHTML(i, j, "cell " + i + ", " + j);
      }
    }
  }

}
