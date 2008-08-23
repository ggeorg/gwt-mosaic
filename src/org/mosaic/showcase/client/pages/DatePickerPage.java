package org.mosaic.showcase.client.pages;

import java.util.Date;

import org.mosaic.showcase.client.Page;
import org.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseData;
import org.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.mosaic.ui.client.InfoPanel;
import org.mosaic.ui.client.MessageBox;
import org.mosaic.ui.client.ToolButton;
import org.mosaic.ui.client.MessageBox.PromptCallback;
import org.mosaic.ui.client.datepicker.DatePicker;
import org.mosaic.ui.client.layout.BoxLayout;
import org.mosaic.ui.client.layout.BoxLayoutData;
import org.mosaic.ui.client.layout.FillLayoutData;
import org.mosaic.ui.client.layout.LayoutPanel;
import org.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.event.ChangeEvent;
import com.google.gwt.widgetideas.client.event.ChangeHandler;

/**
 * Example file.
 */
@ShowcaseStyle( {".mosaic-DatePicker"})
public class DatePickerPage extends Page {

  /**
   * The constants used in this Page.
   */
  @ShowcaseSource
  public static interface DemoConstants extends Constants, Page.DemoConstants {

  }

  /**
   * An instance of the constants.
   */
  @ShowcaseData
  private DemoConstants constants;

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public DatePickerPage(DemoConstants constants) {
    super(constants);
    this.constants = constants;
  }

  /**
   * Load this example.
   */
  @ShowcaseSource
  @Override
  protected void onPageLoad(LayoutPanel layoutPanel) {
    layoutPanel.setLayout(new BoxLayout());

    final DatePicker datePicker = new DatePicker();
    final Date d = new Date();
    d.setMonth(2);
    d.setDate(1);
    datePicker.setSelectedDate(d);
    layoutPanel.add(datePicker, new BoxLayoutData(FillStyle.BOTH, true));

    // Log select events.
    final ChangeHandler<Date> changeHandler = new ChangeHandler<Date>() {
      public void onChange(ChangeEvent<Date> event) {
        InfoPanel.show("DatePicker ChangeHandler", event.getOldValue()
            + " --> " + event.getNewValue());
      }
    };
    datePicker.addChangeHandler(changeHandler);
    
    final LayoutPanel vPanel = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
    layoutPanel.add(vPanel);
    vPanel.add(new ToolButton("DatePicker Prompt", new ClickListener() {
      public void onClick(Widget sender) {
        datePickerPrompt(new PromptCallback() {
          public void onResult(String input) {
            InfoPanel.show("DatePicker Prompt", input);
          }
        });
      }
    }));
  }

  private void datePickerPrompt(final PromptCallback callback) {
    final DatePicker datePicker = new DatePicker();
    Date d = new Date();
    d.setMonth(2);
    d.setDate(1);
    datePicker.setSelectedDate(d);

    final MessageBox prompt = new MessageBox("DatePicker Prompt") {
      @Override
      public void onClose(boolean result) {
        hide();
        if (result) {
          callback.onResult(datePicker.getSelectedDate().toString());
        } else {
          callback.onResult(null);
        }
      }
    };
    prompt.setAnimationEnabled(true);
    prompt.setWidth("256px");

    Button buttonOK = new Button("OK");
    buttonOK.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        prompt.onClose(true);
      }
    });

    Button buttonCancel = new Button("Cancel");
    buttonCancel.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        prompt.onClose(false);
      }
    });

    prompt.getButtonPanel().add(buttonOK);
    prompt.getButtonPanel().add(buttonCancel);

    prompt.setWidget(datePicker, 0);
    prompt.center();
  }

  @Override
  public String getName() {
    return "DatePicker";
  }

}
