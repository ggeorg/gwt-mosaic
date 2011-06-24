package gwt.mosaic.grid.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * A grid with virtual scrolling, cell editing, complex rows, sorting, fixed
 * columns, sizable columns, etc.
 * <p>
 * {@code Grid} provides the full set of grid features without any direct
 * connection to a data store.
 * <p>
 * The grid exposes a get function for the grid, or optionally individual
 * columns, to populate cell contents.
 * <p>
 * The grid is rendered based on its structure, an object describing column and
 * cell layout.
 */
public class Grid extends Composite {

  interface MyUiBinder extends UiBinder<HTMLPanel, Grid> {
  }

  private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

  @UiField
  DivElement viewsHeaderNode;

  @UiField
  DivElement viewsNode;

  @UiField
  DivElement messagesNode;

  /** CSS class applied to the grid's domNode */
  private String classTag = "dojoxGrid";

  /** Number of rows to display */
  private int rowCount = 5;

  /** Number of rows to keep in the rendering cache. */
  private int keepRows = 75;

  /** Number of rows to render at a time. */
  private int rowsPerPage = 25;

  /** If {@code true} grid width is automatically set to fit the data. */
  private boolean autoWidth = false;

  /**
   * A css string to use to set out initial width (only used if
   * {@link #autoWidth} is {@code true}). The first rendering of the grid will
   * be this width, any resizing of columns, etc will result in the grid
   * switching to {@link #autoWidth} mode. Note, this width will override any
   * styling in a stylesheet or directly on the node.
   */
  private String initialWidth = "";

  /**
   * If {@code true} grid height is automatically set to fit the data.
   * 
   * TODO integer value
   */
  private boolean autoHeight = false;

  /**
   * If {@code rowHeight} is set to a positive number, it will define the height
   * of the rows in pixels. This can provide a significant performance
   * advantage, since it eliminates the need to measure row sizes during
   * rendering, which is one of the primary bottlenecks in the {@link DataGrid}
   * 's performance.
   */
  private int rowHeight = 0;

  /**
   * If {@code true} grid will render itself after initialization.
   */
  private boolean autoRenderer = true;

  /**
   * Default height of the grid, measured in any valid css unit.
   */
  private String defaultHeight = "15em";

  /**
   * Explicit height of the grid, measured in any valid css unit. This will be
   * populated (and overridden) if the height: css attribute exists on the
   * source node.
   */
  private String height;

  /**
   * View layout definition.
   */
  private Object structure;

  /**
   * Override defaults and make the indexed grid view elastic, thus filling
   * available horizontal space.
   */
  private int elasticView = -1;

  /**
   * Single-click starts editing; default is double-click.
   */
  private boolean singleClickEdit = false;

  /**
   * Set the selection mode of grid's selection.
   */
  private SelectionMode selectionMode = SelectionMode.EXTENDED;
  
  public SelectionMode getSelectionMode() {
    return selectionMode;
  }
  
  public void setSelectionMode(SelectionMode selectionMode) {
    this.selectionMode = selectionMode;
  }

  /**
   * If set to {@code true}, will add a row selector view to this grid.
   * 
   * TODO string value
   */
  private boolean rowSelector;

  /**
   * If set to {@code true}, will add drag and drop reordering to views with one
   * row of columns.
   */
  private boolean columnReordering = false;

  /**
   * The menu to use as a context menu for the grid headers.
   */
  private Object /* Menu */headerMenu;

  /**
   * Label of place holders to search for in the header menu to replace with
   * column toggling menu items.
   */
  private String placeholderLabel = "GridColumns";

  /**
   * Set to {@code true} if you want to be able to select the text within the
   * grid.
   */
  private boolean selectable = false;

  /**
   * Used to store the last two clicks, to ensure double-clicking based on the
   * intended row.
   */
  private Object _click = null;

  /** Message that shows while the grid is loading. */
  private String loadingMessage = "<span class='dojoxGridLoading'>${loadingState}</span>";

  /** Message that show when the grid encounters an error loading. */
  private String errorMessage = "<span class='dojoxGridError'>${errorState}</span>";

  /**
   * Message that shows if the grid has no data - wrap it in span with class
   * {@code dojoxGridNoData} if you want it to be styled similar to the loading
   * and error messages.
   */
  private String noDataMessage = "";

  /**
   * This will escape HTML brackets from the data to prevent HTML from
   * user-inputed data being rendered with may contain JavaScript and result in
   * XSS attacks. This is true by default, and it is recommended that it remain
   * true. Setting this to false will allow data to be displayed in the grid
   * without filtering, and should be only used if it is known that the data
   * won't contain malicious scripts. If HTML is needed in grid cells, it is
   * recommended that you use the formatter function to generate the HTML (the
   * output of formatter functions is not filtered, even with escapeHTMLInData
   * set to true).
   */
  private boolean escapeHTMLInData = true;

  /**
   * An object to execute format functions within. If not set, the format
   * functions will execute within the scope of the cell that has a format
   * function.
   */
  private Object formatterScope = null;

  /**
   * Indicates if the grid contains editable cells, default is false set to true
   * if editable cell encountered during rendering
   */
  private boolean editable = false;
  
  private int sortInfo = 0;
  private boolean themable = true;
  private Object _placeholders = null;

  public Grid() {
    initWidget(uiBinder.createAndBindUi(this));

    Element elem = getElement();
    elem.setAttribute("role", "grid");
  }

  /**
   * Selection mode of {@link Grid}'s selection.
   * 
   * @author ggeorg
   * 
   */
  public static enum SelectionMode {
    SINGLE, MULTIPLE, EXTENDED, NONE
  }
}
