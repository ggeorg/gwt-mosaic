package org.mosaic.ui.client;

import java.util.HashMap;
import java.util.Map;

import org.mosaic.core.client.DOM;
import org.mosaic.ui.client.Caption.CaptionRegion;
import org.mosaic.ui.client.layout.BoxLayout;
import org.mosaic.ui.client.layout.BoxLayoutData;
import org.mosaic.ui.client.layout.LayoutPanel;
import org.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.allen_sauer.gwt.dnd.client.AbstractDragController;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.BoundaryDropController;
import com.allen_sauer.gwt.dnd.client.util.Location;
import com.allen_sauer.gwt.dnd.client.util.WidgetLocation;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class WindowPanel extends DecoratedPopupPanel implements HasHTML {

  class AttachableFocusPanel extends FocusPanel {
    protected void onAttach() {
      super.onAttach();
    }

    protected void onDetach() {
      super.onDetach();
    }
  }

  /**
   * WindowPanel direction constant, used in {@link ResizeDragController}.
   */
  static final class DirectionConstant {

    final int directionBits;

    final String directionLetters;

    private DirectionConstant(int directionBits, String directionLetters) {
      this.directionBits = directionBits;
      this.directionLetters = directionLetters;
    }
  }

  final class MoveDragController extends PickupDragController {

    public MoveDragController(AbsolutePanel boundaryPanel) {
      super(boundaryPanel, true);
    }

    @Override
    public void dragStart() {
      body.setVisible(false);
      super.dragStart();
    }

    @Override
    public void dragEnd() {
      body.setVisible(true);
      super.dragEnd();
    }
  }

  class CaptionHanldeProxy extends Widget {
    public CaptionHanldeProxy(Element elem) {
      setElement(elem);
    }
  }

  final class ResizeDragController extends AbstractDragController {

    private static final int MIN_WIDGET_SIZE = 32;

    private Map<Widget, DirectionConstant> directionMap = new HashMap<Widget, DirectionConstant>();

    private WindowPanel windowPanel = null;

    public ResizeDragController(AbsolutePanel boundaryPanel, WindowPanel windowPanel) {
      super(boundaryPanel);
      this.windowPanel = windowPanel;
    }

    // protected Widget newDragProxy(DragContext)

    // @Override
    // public void startDrag() {
    // super.dragStart();
    //      
    // WidgetLocation currentDraggableLocation = new
    // WidgetLocation(context.draggable, context.boundaryPanel);
    //      
    // movablePanel = newDragProxy(context);
    // context.boundaryPanel.add(movablePanel.
    // currentDraggableLocation.getLeft(), currentDraggableLocation.getTop());
    // }

    public void dragMove() {
      int direction = ((ResizeDragController) context.dragController).getDirection(context.draggable).directionBits;
      if ((direction & WindowPanel.DIRECTION_NORTH) != 0) {
        int delta = context.draggable.getAbsoluteTop() - context.desiredDraggableY;
        if (delta != 0) {
          int contentHeight = windowPanel.getContentHeight();
          int newHeight = Math.max(contentHeight + delta, MIN_WIDGET_SIZE);
          if (newHeight != contentHeight) {
            windowPanel.moveBy(0, contentHeight - newHeight);
          }
          windowPanel.setContentSize(windowPanel.getContentWidth(), newHeight);
        }
      } else if ((direction & WindowPanel.DIRECTION_SOUTH) != 0) {
        int delta = context.desiredDraggableY - context.draggable.getAbsoluteTop();
        if (delta != 0) {
          windowPanel.setContentSize(windowPanel.getContentWidth(),
              windowPanel.getContentHeight() + delta);
        }
      }
      if ((direction & WindowPanel.DIRECTION_WEST) != 0) {
        int delta = context.draggable.getAbsoluteLeft() - context.desiredDraggableX;
        if (delta != 0) {
          int contentWidth = windowPanel.getContentWidth();
          int newWidth = Math.max(contentWidth + delta, MIN_WIDGET_SIZE);
          if (newWidth != contentWidth) {
            windowPanel.moveBy(contentWidth - newWidth, 0);
          }
          windowPanel.setContentSize(newWidth, windowPanel.getContentHeight());
        }
      } else if ((direction & WindowPanel.DIRECTION_EAST) != 0) {
        int delta = context.desiredDraggableX - context.draggable.getAbsoluteLeft();
        if (delta != 0) {
          windowPanel.setContentSize(windowPanel.getContentWidth() + delta,
              windowPanel.getContentHeight());
        }
      }

    }

    private DirectionConstant getDirection(Widget draggable) {
      return directionMap.get(draggable);
    }

    public void makeDraggable(Widget widget, WindowPanel.DirectionConstant direction) {
      super.makeDraggable(widget);
      directionMap.put(widget, direction);
    }

    protected BoundaryDropController newBoundaryDropController(
        AbsolutePanel boundaryPanel, boolean allowDroppingOnBoundaryPanel) {
      if (allowDroppingOnBoundaryPanel) {
        throw new IllegalArgumentException();
      }
      return new BoundaryDropController(boundaryPanel, false);
    }

  }

  final class WindowController {

    private final AbsolutePanel boundaryPanel;

    private final MoveDragController moveDragController;

    private final ResizeDragController resizeDragController;

    WindowController(AbsolutePanel boundaryPanel, WindowPanel windowPanel) {
      this.boundaryPanel = boundaryPanel;

      moveDragController = new MoveDragController(boundaryPanel);
      moveDragController.setBehaviorConstrainedToBoundaryPanel(true);
      // moveDragController.setBehaviorDragProxy(true);
      moveDragController.setBehaviorMultipleSelection(false);

      resizeDragController = new ResizeDragController(boundaryPanel, windowPanel);
      resizeDragController.setBehaviorConstrainedToBoundaryPanel(true);
      resizeDragController.setBehaviorMultipleSelection(false);
    }

    AbsolutePanel getBoundaryPanel() {
      return boundaryPanel;
    }

    public MoveDragController getMoveDragController() {
      return moveDragController;
    }

    public ResizeDragController getResizeDragController() {
      return resizeDragController;
    }

  }

  /**
   * The caption images to use.
   */
  public static final CaptionImages CAPTION_IMAGES = (CaptionImages) GWT.create(CaptionImages.class);

  /**
   * The default style name.
   */
  private static final String DEFAULT_STYLENAME = "mosaic-WindowPanel";

  /**
   * Specifies that resizing occur at the east edge.
   */
  static final int DIRECTION_EAST = 0x0001;

  /**
   * Specifies that resizing occur at the both edge.
   */
  static final int DIRECTION_NORTH = 0x0002;

  /**
   * Specifies that resizing occur at the south edge.
   */
  static final int DIRECTION_SOUTH = 0x0004;

  /**
   * Specifies that resizing occur at the west edge.
   */
  static final int DIRECTION_WEST = 0x0008;

  /**
   * Specifies that resizing occur at the east edge.
   */
  static final DirectionConstant EAST = new WindowPanel.DirectionConstant(DIRECTION_EAST,
      "e");

  /**
   * Specifies that resizing occur at the both edge.
   */
  static final DirectionConstant NORTH = new DirectionConstant(DIRECTION_NORTH, "n");

  /**
   * Specifies that resizing occur at the north-east edge.
   */
  static final DirectionConstant NORTH_EAST = new DirectionConstant(DIRECTION_NORTH
      | DIRECTION_EAST, "ne");

  /**
   * Specifies that resizing occur at the north-west edge.
   */
  static final DirectionConstant NORTH_WEST = new DirectionConstant(DIRECTION_NORTH
      | DIRECTION_WEST, "nw");

  /**
   * Specifies that resizing occur at the south edge.
   */
  static final DirectionConstant SOUTH = new DirectionConstant(DIRECTION_SOUTH, "s");

  /**
   * Specifies that resizing occur at the south-east edge.
   */
  static final DirectionConstant SOUTH_EAST = new DirectionConstant(DIRECTION_SOUTH
      | DIRECTION_EAST, "se");

  /**
   * Specifies that resizing occur at the south-west edge.
   */
  static final DirectionConstant SOUTH_WEST = new DirectionConstant(DIRECTION_SOUTH
      | DIRECTION_WEST, "sw");
  /**
   * Specifies that resizing occur at the west edge.
   */
  static final DirectionConstant WEST = new DirectionConstant(DIRECTION_WEST, "w");

  private AttachableFocusPanel nwFocusPanel, nFocusPanel, neFocusPanel;
  private AttachableFocusPanel swFocusPanel, sFocusPanel, seFocusPanel;
  private AttachableFocusPanel wFocusPanel, eFocusPanel;

  private int contentWidth, contentHeight;
  private final WindowController windowController;
  private final LayoutPanel layoutPanel;

  private final Caption caption;

  private final LayoutPanel body;

  private final boolean resizable, modal;

  private boolean initialized;

  private Timer layoutTimer = new Timer() {
    public void run() {
      layoutPanel.layout();
    }
  };

  public WindowPanel() {
    this(null);
  }

  public WindowPanel(String text) {
    this(text, true, false);
  }

  public WindowPanel(String text, boolean resizable, boolean modal) {
    this(text, resizable, false, modal);
  }

  protected WindowPanel(String text, boolean resizable, boolean autoHide, boolean modal) {
    super(autoHide, modal);

    this.resizable = resizable;
    this.modal = modal;

    windowController = new WindowController(RootPanel.get(), this);

    if (isResizable()) {
      nwFocusPanel = setupCell(0, 0, NORTH_WEST);
      nFocusPanel = setupCell(0, 1, NORTH);
      neFocusPanel = setupCell(0, 2, NORTH_EAST);

      wFocusPanel = setupCell(1, 0, WEST);
      eFocusPanel = setupCell(1, 2, EAST);

      swFocusPanel = setupCell(2, 0, SOUTH_WEST);
      sFocusPanel = setupCell(2, 1, SOUTH);
      seFocusPanel = setupCell(2, 2, SOUTH_EAST);
    }

    caption = new Caption(text);

    ImageButton closeBtn = new ImageButton(CAPTION_IMAGES.windowClose());
    closeBtn.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        hide();
      }
    });
    caption.add(closeBtn, CaptionRegion.RIGHT);

    windowController.getMoveDragController().makeDraggable(this, caption);

    body = new LayoutPanel();
    body.setStyleName("Body");

    BoxLayout boxLayout = new BoxLayout(Orientation.VERTICAL);
    //boxLayout.setMargin(0);
    boxLayout.setSpacing(0);

    layoutPanel = new LayoutPanel(boxLayout);
    layoutPanel.add(caption, new BoxLayoutData(FillStyle.HORIZONTAL));
    layoutPanel.add(body, new BoxLayoutData(FillStyle.BOTH));

    super.setWidget(layoutPanel);

    // addClickListener(new ClickListener() {
    // public void onClick(Widget sender) {
    // // TODO force out panel to the top of our z-index context
    // AbsolutePanel boundaryPanel = windowController.getBoundaryPanel();
    // WidgetLocation location = new WidgetLocation(WindowPanel.this,
    // boundaryPanel);
    // boundaryPanel.add(WindowPanel.this, location.getLeft(),
    // location.getTop());
    // }
    // });

    // Add the style name (keep
    // TODO I don't understand it
    addStyleName(DEFAULT_STYLENAME);
  }

  @Override
  protected void doAttachChildren() {
    super.doAttachChildren();

    // See comment in doDetachChildren for an explanation of this call
    if (isResizable()) {
      nFocusPanel.onAttach();
      sFocusPanel.onAttach();
      wFocusPanel.onAttach();
      eFocusPanel.onAttach();
      nwFocusPanel.onAttach();
      neFocusPanel.onAttach();
      swFocusPanel.onAttach();
      seFocusPanel.onAttach();
    }
  }

  @Override
  protected void doDetachChildren() {
    super.doDetachChildren();

    // We need to detach the caption specifically because it is not part of the
    // iterator of Widgets that the {@link SimplePanel} super class returns.
    // This is similar to a {@link ComplexPanel}, but we do not want to expose
    // the caption widget, as its just an internal implementation.
    if (isResizable()) {
      nFocusPanel.onDetach();
      sFocusPanel.onDetach();
      wFocusPanel.onDetach();
      eFocusPanel.onDetach();
      nwFocusPanel.onDetach();
      neFocusPanel.onDetach();
      swFocusPanel.onDetach();
      seFocusPanel.onDetach();
    }
  }

  public int getContentHeight() {
    return contentHeight;
  }

  public int getContentWidth() {
    return contentWidth;
  }

  public String getHTML() {
    return caption.getHTML();
  }

  protected LayoutPanel getLayoutPanel() {
    return body;
  }

  /**
   * Gets the caption's text.
   * 
   * @return the caption's text
   */
  public String getText() {
    return caption.getText();
  }

  public boolean isModal() {
    return modal;
  }

  public boolean isResizable() {
    return resizable;
  }

  public void moveBy(int right, int down) {
    AbsolutePanel parent = (AbsolutePanel) getParent();
    Location location = new WidgetLocation(this, parent);
    int left = location.getLeft() + right;
    int top = location.getTop() + down;
    parent.setWidgetPosition(this, left, top);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.Panel#onLoad()
   */
  protected void onLoad() {
    if (!initialized) {
      initialized = true;
      final int[] box = DOM.getClientSize(getElement());
      setContentSize(box[0], box[1]);
      layoutTimer.schedule(1);
    }
  }

  public void setContentSize(int width, int height) {
    if (isResizable()) {
      if (width != contentWidth) {
        contentWidth = width;
        nFocusPanel.setWidth(contentWidth + "px");
        sFocusPanel.setWidth(contentWidth + "px");
      }
      if (height != contentHeight) {
        contentHeight = height;
        wFocusPanel.setHeight((contentHeight) + "px");
        eFocusPanel.setHeight((contentHeight) + "px");
      }
    }
    layoutPanel.setPixelSize(width, height);
    layoutTimer.schedule(333);
  }

  /**
   * Sets the html string inside the caption.
   * 
   * Use {@link #setWidget(Widget)} to set the contents inside the
   * {@link WindowPanel}.
   * 
   * @param html the object's new HTML
   */
  public void setHTML(String html) {
    caption.setHTML(html);
  }

  /**
   * Sets the text inside the caption.
   * 
   * Use {@link #setWidget(Widget)} to set the contents inside the
   * {@link WindowPanel}.
   * 
   * @param text the object's new text
   */
  public void setText(final String text) {
    caption.setText(text);
  }

  private AttachableFocusPanel setupCell(int row, int col, DirectionConstant direction) {
    final AttachableFocusPanel widget = new AttachableFocusPanel();
    Element td = getCellElement(row, col);
    DOM.appendChild(td, widget.getElement());
    adopt(widget);
    windowController.getResizeDragController().makeDraggable(widget, direction);
    widget.setStyleName("Resize-" + direction.directionLetters);
    return widget;
  }

}
