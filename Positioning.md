# Introduction #

Here we'll look at controlling the position and size of a widget.

# Details #

So far, we know how to position widgets either horizontally or vertically inside a box layout panel. We will often need more control over the position and size of widgets within the box layout panel. For this, we first need to look at how a box works.

The position of an widget is determined by the layout style of its parent. For example, the position of a button in a horizontal box is to the right of the previous button, if any. The size of an widget is determined by two factors, the size that the widget wants to be and the size you specify. The size that an widget wants to be is determined by what is in the widget. For example, a button's width is determined by the amount of text inside the button.

A widget will generally be as large as it needs to be to hold its contents, and no larger. Some widgets, such as text boxes have a default size, which will be used. A box layout panel will be large enough to hold its child widgets. A horizontal layout panel with three buttons in it will be as wide as three buttons,plus a small amount of padding.

![http://gwt-mosaic.googlecode.com/svn/wiki/screenshots/Positioning1.png](http://gwt-mosaic.googlecode.com/svn/wiki/screenshots/Positioning1.png)

```
<ui:UiBinder xmlns:ui='urn:ui:com.google.gwt.uibinder'
	XML:g='urn:import:com.google.gwt.user.client.ui' xmlns:m='urn:import:gwt.mosaic.client.ui'>
	<ui:style>
		.login { margin: 10px; border: 10px solid Gray; padding: 10px; }
	</ui:style>
	<m:VBox>
		<m:HBox addStyleNames="{style.login}">
			<m:Button>Yes</m:Button>
			<m:Button>No</m:Button>
			<m:Button>I really don't know one way or the other</m:Button>
		</m:HBox>
	</m:VBox>
</ui:UiBinder>
```

## Preferred width and height ##

You may need to have more control over the size of a widget. The recommended way is to use the `preferredWidth` and `preferredHeight` properties (the `width` and `height` CSS properties are used internally and will be ignored).

By setting either of the two properties, the widget will be created with that width and height. If you specify only one size property, the other is calculated as needed. The size of these properties should be specified as a number followed by a unit (e.g. 2em).

## Weighted widgets ##

The sizes are fairly easy to calculate for non-weighted widgets. They simply obey their specified preferred widths and heights, and if the size wasn't specified, the widget's default size is just large enough to fit the contents. For weighted widgets, the calculation is slightly trickier.

Weighted widgets are those that have a 'weight' property set to a value greater than 0. Recall that flexible widgets grow and shrink to fit the available space. Their default size is still calculated the same as for inflexible widgets. The following example demonstrates this:

http://gwt-mosaic.googlecode.com/svn/wiki/screenshots/Positioning2.png?t=1

```
<ui:UiBinder xmlns:ui='urn:ui:com.google.gwt.uibinder'
	XML:g='urn:import:com.google.gwt.user.client.ui' xmlns:m='urn:import:gwt.mosaic.client.ui'>
	<ui:style>
		.login { margin: 10px; border: 10px solid Gray; padding: 10px; }
	</ui:style>
	<m:VBox fill="true">
		<m:HBox addStyleNames="{style.login}">
			<m:Button weight="1">Yes</m:Button>
			<m:Button>No</m:Button>
			<m:Button>I really don't know one way or the other</m:Button>
		</m:HBox>
	</m:VBox>
</ui:UiBinder>
```

Notice that in order to see the effect of the weighted widget we changed the `fill` property of the vertical box to `true` so that the horizontal box will expand to fit all the available horizontal space. Then, when you resize the browser's window, extra space will be available,so that the horizontal box will grow to fill the extra space. Because the horizontal box is larger, more extra space will be created inside it, and the weighted button inside it will grow to fit the available space.

## Minimum and Maximum Sizes ##

You may also want to allow an widget ti be weighted but constrain the size so that it cannot be larger than a sertain size. Or, you may want to set a minimum size. You can set this by using the CSS properties `min-width`, `min-height`, `max-width` and `max-height`.

## Our find files layout ##

Lets make it so that the text box will resize to fit the entire browser's window.

```
<ui:style>
	.findTB {
		min-width: 15em;
	}
</ui:style>

...

<m:TextBox addStyleNames="{style.findTB}" weight="1" />
```

![http://gwt-mosaic.googlecode.com/svn/wiki/screenshots/Positioning3.png](http://gwt-mosaic.googlecode.com/svn/wiki/screenshots/Positioning3.png)

Here, the text box has been made weighted. This way, it will grow if the user changes the size of the browser's window. This is useful if the user wants to enter a long string of text. Also, a minimum width of 15 ems has been set so that the text box will always show at least 15 characters. If the user resizes the browser's window to be very small, the text box will not shrink past 15 ems.

Notice that in order to use `weight` as a widget we switched to the GWTMosaic's implementation of text box.