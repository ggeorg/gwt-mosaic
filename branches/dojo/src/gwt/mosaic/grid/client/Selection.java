package gwt.mosaic.grid.client;

import gwt.mosaic.grid.client.Grid.SelectionMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages row selection for grid. Owned by grid and used internally for
 * selection. Override to implement custom selection.
 */
public class Selection {
  private Grid grid;
  private List<?> selected;

  private SelectionMode mode;
  
  private int updating = 0;
  private int selectedIndex = -1;

  public SelectionMode getMode() {
    return mode;
  }

  public void setMode(SelectionMode mode) {
    if (selected.size() > 0) {
      selected.clear();
    }
    this.mode = mode != null ? mode : SelectionMode.EXTENDED;
  }

  public Selection(Grid inGrid) {
    this.grid = inGrid;
    this.selected = new ArrayList<Object>();

    this.setMode(inGrid.getSelectionMode());
  }

  protected boolean onCanSelect(int inIndex) {
    throw new UnsupportedOperationException();
  }

  protected boolean onCanDeselect(int inIndex) {
    throw new UnsupportedOperationException();
  }

  protected void onSelected(int inIndex) {
  }

  protected void onDeselected(int inIndex) {
  }

  protected void onChanging() {
  }

  protected void onChanged() {
  }

  public boolean isSelected(int inIndex) {
    throw new UnsupportedOperationException();
  }

  public int getFirstSelected() {
    throw new UnsupportedOperationException();
  }
  
  public int getNextSelected() {
    throw new UnsupportedOperationException();
  }
  
  public Object[] getSelected() {
    throw new UnsupportedOperationException();
  }
  
  public int getSelectedCount() {
    throw new UnsupportedOperationException();
  }
  
  protected void beginUpdate() {
    if(updating == 0) {
      onChanging();
    }
    updating++;
  }
  
  protected void endUpdate() {
    updating--;
    if(updating == 0) {
      onChanged();
    }
  }
  
  public void select(int inIndex) {
    if(mode == SelectionMode.NONE) {
      return;
    }
    if(mode != SelectionMode.MULTIPLE) {
      deselectAll();
      addToSelection(inIndex);
    } else {
      toggleSelect(inIndex);
    }
  }
  
  public void addToSelection(int inIndex) {
    throw new UnsupportedOperationException();
  }
  
  public void deselect(int inIndex) {
    throw new UnsupportedOperationException();
  }
  
  public void setSelected(int inIndex, boolean inSelect) {
    if(inSelect) {
      addToSelection(inIndex);
    } else {
      deselect(inIndex);
    }
  }
  
  public void toggleSelect(int inIndex) {
    throw new UnsupportedOperationException();
  }
  
  protected void range(int inFrom, int inFo, SelectionCallback callback) {
    throw new UnsupportedOperationException();
  }
  
  public void selectRange(int inFrom, int inTo) {
    throw new UnsupportedOperationException();
  }
  
  public void deselectRange(int inFrom, int inTo) {
    throw new UnsupportedOperationException();
  }
  
  public void insert() {
    throw new UnsupportedOperationException();
  }
  
  public void remove() {
    throw new UnsupportedOperationException();
  }
  
  public void deselectAll() {
    throw new UnsupportedOperationException();
  }
  
  // clickSelect
  
  // clickSelectEvent
  
  public void clear() {
    beginUpdate();
    deselectAll();
    endUpdate();
  }
}
