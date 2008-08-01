/*
 * Copyright 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.mosaic.ui.client.scrolltable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ImageBundle;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;

/**
 * A panel that wraps a {@link PagingScrollTable} and includes options to
 * manipulate the page.
 * 
 * <h3>CSS Style Rules</h3>
 * <ul class="css">
 * <li> .gwt-PagingOptions { applied to the entire widget } </li>
 * <li> .gwt-PagingOptions .errorMessage { applied to the error message } </li>
 * </ul>
 */
public class PagingOptions extends Composite {
  /**
   * An {@link com.google.gwt.user.client.ui.ImageBundle} that provides images
   * for {@link PagingOptions}.
   */
  public static interface PagingOptionsImages extends ImageBundle {
    /**
     * An image used to navigate to the first page.
     * 
     * @return a prototype of this image
     */
    AbstractImagePrototype pagingOptionsFirstPage();

    /**
     * An image used to navigate to the last page.
     * 
     * @return a prototype of this image
     */
    AbstractImagePrototype pagingOptionsLastPage();

    /**
     * An image used to navigate to the next page.
     * 
     * @return a prototype of this image
     */
    AbstractImagePrototype pagingOptionsNextPage();

    /**
     * An image used to navigate to the previous page.
     * 
     * @return a prototype of this image
     */
    AbstractImagePrototype pagingOptionsPrevPage();
  }

  /**
   * The default style name.
   */
  public static final String DEFAULT_STYLENAME = "gwt-PagingOptions";

  /**
   * The label used to display errors.
   */
  private HTML errorLabel;

  /**
   * Goto first page button.
   */
  private Image firstImage;

  /**
   * Goto last page button.
   */
  private Image lastImage;

  /**
   * The loading image.
   */
  private Image loadingImage;

  /**
   * Goto next page button.
   */
  private Image nextImage;

  /**
   * The HTML field that contains the number of pages.
   */
  private HTML numPagesLabel;

  /**
   * Goto previous page button.
   */
  private Image prevImage;

  /**
   * The box where the user can select the current page.
   */
  private TextBox curPageBox = new TextBox();

  /**
   * The table being affected.
   */
  private PagingScrollTable<?> table;

  /**
   * Constructor.
   * 
   * @param table the table being ad
   */
  public PagingOptions(PagingScrollTable<?> table) {
    this(table, GWT.<PagingOptionsImages> create(PagingOptionsImages.class));
  }

  /**
   * Constructor.
   * 
   * @param table the table being affected
   * @param images the images to use
   */
  public PagingOptions(PagingScrollTable<?> table, PagingOptionsImages images) {
    this.table = table;

    // Create the main widget
    HorizontalPanel hPanel = new HorizontalPanel();
    initWidget(hPanel);
    hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
    setStyleName(DEFAULT_STYLENAME);

    // Create the paging image buttons
    createPageButtons(images);

    // Create the current page box
    createCurPageBox();

    // Create the page count label
    numPagesLabel = new HTML();

    // Create the loading image
    loadingImage = new Image("scrollTableLoading.gif");
    loadingImage.setVisible(false);

    // Create the error label
    errorLabel = new HTML();
    errorLabel.setStylePrimaryName("errorMessage");

    // Add the widgets to the panel
    hPanel.add(createSpacer());
    hPanel.add(firstImage);
    hPanel.add(createSpacer());
    hPanel.add(prevImage);
    hPanel.add(createSpacer());
    hPanel.add(curPageBox);
    hPanel.add(createSpacer());
    hPanel.add(numPagesLabel);
    hPanel.add(createSpacer());
    hPanel.add(nextImage);
    hPanel.add(createSpacer());
    hPanel.add(lastImage);
    hPanel.add(createSpacer());
    hPanel.add(loadingImage);
    hPanel.add(errorLabel);

    // Add a listener to the table
    RowPagingListener rowPagingListener = new RowPagingListener() {
      public void onNumPagesChanges(int numPages) {
        if (numPages < 0) {
          numPagesLabel.setHTML("");
          lastImage.setVisible(false);
        } else {
          numPagesLabel.setHTML("of&nbsp;&nbsp;" + numPages);
          numPagesLabel.setVisible(true);
          lastImage.setVisible(true);
        }
      }

      public void onPageChanged(int page) {
        curPageBox.setText((page + 1) + "");
        loadingImage.setVisible(true);
        errorLabel.setHTML("");
      }

      public void onPageLoaded(int page) {
        loadingImage.setVisible(false);
        errorLabel.setHTML("");
      }

      public void onPagingFailure(Throwable caught) {
        loadingImage.setVisible(false);
        errorLabel.setHTML(caught.getMessage());
      }
    };
    table.addRowPagingListener(rowPagingListener);
    rowPagingListener.onNumPagesChanges(table.getNumPages());
  }

  /**
   * @return the {@link PagingScrollTable}.
   */
  public PagingScrollTable<?> getPagingScrollTable() {
    return table;
  }

  /**
   * Create a widget that can be used to add space.
   * 
   * @return a spacer widget
   */
  protected Widget createSpacer() {
    return new HTML("&nbsp;&nbsp;");
  }

  /**
   * Create a box that holds the current page.
   */
  private void createCurPageBox() {
    // Setup the widget
    curPageBox.setWidth("3em");
    curPageBox.setText("1");
    curPageBox.setTextAlignment(TextBoxBase.ALIGN_RIGHT);

    // Disallow non-numeric pages
    KeyboardListenerAdapter listener = new KeyboardListenerAdapter() {
      @Override
      public void onKeyPress(Widget sender, char keyCode, int modifiers) {
        if (keyCode == (char) KEY_ENTER) {
          PagingOptions.this.table.gotoPage(getPagingBoxValue(), false);
        } else if ((!Character.isDigit(keyCode)) && (keyCode != (char) KEY_TAB)
            && (keyCode != (char) KEY_BACKSPACE)
            && (keyCode != (char) KEY_DELETE) && (keyCode != (char) KEY_ENTER)
            && (keyCode != (char) KEY_HOME) && (keyCode != (char) KEY_END)
            && (keyCode != (char) KEY_LEFT) && (keyCode != (char) KEY_UP)
            && (keyCode != (char) KEY_RIGHT) && (keyCode != (char) KEY_DOWN)) {
          ((TextBox) sender).cancelKey();
        }
      }
    };

    // Add the listener
    curPageBox.addKeyboardListener(listener);
  }

  /**
   * Create a paging image buttons.
   * 
   * @param images the images to use
   */
  private void createPageButtons(PagingOptionsImages images) {
    // Create the images
    firstImage = images.pagingOptionsFirstPage().createImage();
    prevImage = images.pagingOptionsPrevPage().createImage();
    nextImage = images.pagingOptionsNextPage().createImage();
    lastImage = images.pagingOptionsLastPage().createImage();

    // Create the listener
    ClickListener listener = new ClickListener() {
      public void onClick(Widget sender) {
        if (sender == firstImage) {
          table.gotoFirstPage();
        } else if (sender == lastImage) {
          table.gotoLastPage();
        } else if (sender == nextImage) {
          table.gotoNextPage();
        } else if (sender == prevImage) {
          table.gotoPreviousPage();
        }
      }
    };

    // Add the listener to each image
    firstImage.addClickListener(listener);
    prevImage.addClickListener(listener);
    nextImage.addClickListener(listener);
    lastImage.addClickListener(listener);
  }

  /**
   * Get the value of in the page box. If the value is invalid, it will be set
   * to 1 automatically.
   * 
   * @return the value in the page box
   */
  private int getPagingBoxValue() {
    int page = 0;
    try {
      page = Integer.parseInt(curPageBox.getText()) - 1;
    } catch (NumberFormatException e) {
      // This will catch an empty box
      curPageBox.setText("1");
    }

    // Replace values less than 1
    if (page < 1) {
      curPageBox.setText("1");
      page = 0;
    }

    // Return the 0 based page, not the 1 based visible value
    return page;
  }
}
