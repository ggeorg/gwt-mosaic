package org.mosaic.ui.client.demo;

import org.mosaic.ui.client.InfoPanel;
import org.mosaic.ui.client.InfoPanel.InfoPanelType;
import org.mosaic.ui.client.layout.BoxLayout;
import org.mosaic.ui.client.layout.BoxLayoutData;
import org.mosaic.ui.client.layout.LayoutPanel;
import org.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class InfoPanelPage extends Page {

  @Override
  protected void onPageLoad(LayoutPanel layoutPanel) {
    layoutPanel.setLayout(new BoxLayout(Orientation.VERTICAL));

    final TextBox caption1 = new TextBox();
    caption1.setText("Caption");

    final TextBox description1 = new TextBox();
    description1.setText("Description");

    final CheckBox type1 = new CheckBox("Humanized message");

    FlexTable layout1 = new FlexTable();
    layout1.getColumnFormatter().setWidth(0, "30%");
    layout1.getColumnFormatter().setWidth(1, "70%");
    layout1.setText(0, 0, "Caption:");
    layout1.setWidget(0, 1, caption1);
    layout1.setText(1, 0, "Description:");
    layout1.setWidget(1, 1, description1);

    Button btn1 = new Button("Show InfoPanel");
    btn1.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        if (type1.isChecked()) {
          InfoPanel.show(InfoPanelType.HUMANIZED_MESSAGE, caption1.getText(),
              description1.getText());
        } else {
          InfoPanel.show(caption1.getText(), description1.getText());
        }
      }
    });

    HorizontalPanel hpanel1 = new HorizontalPanel();
    hpanel1.add(btn1);
    hpanel1.add(new HTML("&nbsp;"));
    hpanel1.add(type1);

    layoutPanel.add(layout1, new BoxLayoutData(FillStyle.HORIZONTAL, true));
    layoutPanel.add(hpanel1);

    // ---
    
    layoutPanel.add(new HTML("&nbsp;"));
    
    // ---

    final TextBox name = new TextBox();
    name.setText("Maria");

    final TextBox message = new TextBox();
    message.setText("I love you");

    final String caption2 = "Formated Text";
    final String description2 = "Hello {0}! {1}.";

    final CheckBox type2 = new CheckBox("Humanized message");

    FlexTable layout2 = new FlexTable();
    layout2.getColumnFormatter().setWidth(0, "30%");
    layout2.getColumnFormatter().setWidth(1, "70%");
    layout2.setText(0, 0, "Caption:");
    layout2.setHTML(0, 1, "<em>" + caption2 + "</em>");
    layout2.setText(1, 0, "Description:");
    layout2.setHTML(1, 1, "<em>" + description2 + "</em>");
    layout2.setHTML(2, 0, "1<sup>st</sup> parameter:");
    layout2.setWidget(2, 1, name);
    layout2.setHTML(3, 0, "2<sup>nd</sup> parameter:");
    layout2.setWidget(3, 1, message);

    Button btn2 = new Button("Show InfoPanel");
    btn2.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        if (type2.isChecked()) {
          InfoPanel.show(InfoPanelType.HUMANIZED_MESSAGE, caption2, description2,
              name.getText());
        } else {
          InfoPanel.show(caption2, description2, name.getText());
        }
      }
    });

    HorizontalPanel hpanel2 = new HorizontalPanel();
    hpanel2.add(btn2);
    hpanel2.add(new HTML("&nbsp;"));
    hpanel2.add(type2);

    layoutPanel.add(layout2, new BoxLayoutData(FillStyle.HORIZONTAL, true));
    layoutPanel.add(hpanel2);

  }

}
