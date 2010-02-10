package org.gwt.mosaic.ui.client.treetable;

import org.gwt.mosaic.ui.client.table.DefaultTableDefinition;
import org.gwt.mosaic.ui.client.table.FixedWidthFlexTable;
import org.gwt.mosaic.ui.client.table.FixedWidthGrid;
import org.gwt.mosaic.ui.client.table.TableModel;
import org.gwt.mosaic.ui.client.table.TableModel.Callback;
import org.gwt.mosaic.ui.client.table.TableModelHelper.SerializableResponse;

import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class RemoteTreeTable<RowType extends TreeTableItem> extends
    TreeTable<RowType> {
  static class RemoteTreeTableModel<RowType extends TreeTableItem> extends
      TreeTableModel<RowType> {
    private RemoteTreeTable<RowType> remoteTreeTable;

    public RemoteTreeTable<RowType> getRemoteTreeTable() {
      return remoteTreeTable;
    }

    public void setRemoteTreeTable(RemoteTreeTable<RowType> remoteTreeTable) {
      this.remoteTreeTable = remoteTreeTable;
    }

    public void requestTreeItems(TreeRequest request,
        TableModel.Callback<RowType> callback) {
      remoteTreeTable.requestTreeItems(request, callback);
    }
  }

  public RemoteTreeTable(DefaultTableDefinition<RowType> tableDefinition) {
    super(new RemoteTreeTableModel<RowType>(), new FixedWidthGrid(),
        new FixedWidthFlexTable(), tableDefinition, false);
    ((RemoteTreeTableModel<RowType>) getTableModel()).setRemoteTreeTable(this);
  }

  public RemoteTreeTable(DefaultTableDefinition<RowType> tableDefinition,
      boolean open) {
    super(new RemoteTreeTableModel<RowType>(), new FixedWidthGrid(),
        new FixedWidthFlexTable(), tableDefinition, open);
    ((RemoteTreeTableModel<RowType>) getTableModel()).setRemoteTreeTable(this);
  }

  protected void requestTreeItems(final TreeRequest request, final Callback<RowType> callback) {
    requestTreeItems(request, new AsyncCallback<SerializableResponse<RowType>>() {
    public void onFailure(Throwable caught) {
      callback.onFailure(caught);
    }

    public void onSuccess(SerializableResponse<RowType> response) {
      callback.onRowsReady(request, response);
    }
    });
  }
  
  protected abstract void requestTreeItems(TreeRequest request,
      AsyncCallback<SerializableResponse<RowType>> callback);
}
