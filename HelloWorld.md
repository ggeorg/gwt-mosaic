# Introduction #

This version of the application is entirely in Java; a UiBinder example is shown in the next section.

![http://gwt-mosaic.googlecode.com/svn/wiki/screenshots/HelloWorld.png](http://gwt-mosaic.googlecode.com/svn/wiki/screenshots/HelloWorld.png)

# Details #

Java code:

```
import gwt.mosaic.client.ui.Box;
import gwt.mosaic.client.ui.HorizontalAlignment;
import gwt.mosaic.client.ui.Orientation;
import gwt.mosaic.client.ui.VerticalAlignment;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class HelloWorld implements EntryPoint {

	public void onModuleLoad() {
		Box box = new Box();
		box.setOrientation(Orientation.VERTICAL);
		box.setHorizontalAlignment(HorizontalAlignment.CENTER);
		box.setVerticalAlignment(VerticalAlignment.MIDDLE);
		
		InlineLabel label = new InlineLabel();
		label.setText("Hello, World!");
		label.getElement().getStyle().setFontSize(24.0, Unit.PT);
		label.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		label.getElement().getStyle().setColor("red");
		
		box.add(label);
		
		RootLayoutPanel.get().add(box);
	}

}
```

&lt;wiki:gadget url="http://mosaic.arkasoft.com/gwt-mosaic-wiki.xml?v=3" height="95" width="728" border="0"/&gt;