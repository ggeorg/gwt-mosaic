# Introduction #

Box layout panels are used to arrange widgets sequentially: horizontal box layout panels arrange out their children horizontally from left to right, and vertical box layout panels arrange their children from top to bottom.

The box layout panels support a number of options that allow a caller to customize the arrangement of child widgets:

  * `spacing` - the amount of space the box layout panel inserts between widgets.
  * `horizontalAlignment` - how the box layout panel aligns widgets on the x-axis.
  * `verticalAlignment` - how the box layout panel aligns widgets on the y-axis.
  * `fill` - whether or not the box layout panel should size all widgets to fill the available space; if `true`, horizontal box layout panels will make all widgets the same height, and vertical box layout panels will make all widgets the same width.
  * `weight` - this option indicates how box layout panels distribute remaining empty space among its children. Weighted widgets grow and shrink to fit their given space. Widgets with larger weight values will be made larger than the widgets with lower weight values, at the ratio determined by the two widgets. The actual value of weight is not relevant unless there are other weighted widgets within the same box layout panel.

# Details #

The basic syntax of a box layout is as follows:

```
<m:HBox>
	<!-- horizontal widgets -->
</m:HBox>

<m:VBox>
	<!-- vertical widgets -->
</m:VBox>
```

The HBox layout panel is used to create a horizontal oriented box. Each widget placed in the HBox will be placed horizontally in a row. The VBox layout panel is used to create a vertically oriented box. Added widgets will be placed underneath each other in a column.

There us also the generic Box layout panel which defaults to horizontal orientation, meaning it is equivalent to the HBox. However you can use the `orientation` property to control the orientation of Box layout panel. You can set this attribute to the value `HORIZONTAL` to create a horizontal box and `VERTICAL` to create a vertical box.

Thus, the two lines below are equivalent:

```
<m:VBox></m:VBox>

<m:Box orientation="VERTICAL"></m:Box>
```

## Example 1 ##

```
<ui:UiBinder xmlns:ui='urn:ui:com.google.gwt.uibinder'
	xmlns:g='urn:import:com.google.gwt.user.client.ui' xmlns:m='urn:import:gwt.mosaic.client.ui'>

	<m:VBox>
		<m:Button>Yes</m:Button>
		<m:Button>No</m:Button>
		<m:Button>Maybe</m:Button>
	</m:VBox>

</ui:UiBinder>
```

[Screenshot](http://gwt-mosaic.googlecode.com/svn/wiki/screenshots/BoxLayoutsE1.png)

The three buttons here are oriented vertically as was indicated by the box layout panel. To change the buttons so that they are oriented horizontally, all you need to do is change the VBox layout panel to a HBox layout panel.

## Example 2 (Login prompt) ##

![http://gwt-mosaic.googlecode.com/svn/wiki/screenshots/BoxLayoutsE2.png](http://gwt-mosaic.googlecode.com/svn/wiki/screenshots/BoxLayoutsE2.png)

```
<ui:UiBinder xmlns:ui='urn:ui:com.google.gwt.uibinder'
	xmlns:g='urn:import:com.google.gwt.user.client.ui' xmlns:m='urn:import:gwt.mosaic.client.ui'>

	<ui:style>
		.login {
			margin: 10px;
			border: 10px solid Gray;
			padding: 10px;
		}
	</ui:style>

	<m:VBox addStyleNames="{style.login}" fill="true">
		<m:HBox>
			<g:InlineLabel>Login:</g:InlineLabel>
			<g:TextBox />
		</m:HBox>
		<m:HBox>
			<g:InlineLabel>Password:</g:InlineLabel>
			<g:PasswordTextBox />
		</m:HBox>
		<m:Button>OK</m:Button>
		<m:Button>Cancel</m:Button>
	</m:VBox>

</ui:UiBinder>
```

Here four widgets have been oriented vertically, two inner HBox layout panels and two button widgets. Notice that only the widgets that are direct descendants of the box are oriented vertically. The labels and text boxes are inside the inner HBox layout panels, so they are oriented according to those boxes, which are horizontal.

## Example 3 (Aligning text boxes) ##

![http://gwt-mosaic.googlecode.com/svn/wiki/screenshots/BoxLayoutsE3.png](http://gwt-mosaic.googlecode.com/svn/wiki/screenshots/BoxLayoutsE3.png)

```
<ui:UiBinder xmlns:ui='urn:ui:com.google.gwt.uibinder'
	xmlns:g='urn:import:com.google.gwt.user.client.ui' xmlns:m='urn:import:gwt.mosaic.client.ui'>

	<ui:style>
		.login {
			margin: 10px;
			border: 10px solid Gray;
			padding: 10px;
		}
	</ui:style>

	<m:VBox addStyleNames="{style.login}" fill="true">
		<m:HBox>
			<m:VBox >
				<g:InlineLabel>Login:</g:InlineLabel>
				<g:InlineLabel>Password:</g:InlineLabel>
			</m:VBox>
			<m:VBox>
				<g:TextBox />
				<g:PasswordTextBox />
			</m:VBox>
		</m:HBox>
		<m:Button>OK</m:Button>
		<m:Button>Cancel</m:Button>
	</m:VBox>

</ui:UiBinder>
```

Notice that the text boxes are now aligned with each other. The issue now is that the 'Password' label is too high. We should really use the [Grid](Grid.md) layout panel to fix this.

## Example 4 (Find files dialog) ##

![http://gwt-mosaic.googlecode.com/svn/wiki/screenshots/BoxLayoutsE4.png](http://gwt-mosaic.googlecode.com/svn/wiki/screenshots/BoxLayoutsE4.png)

```
<ui:UiBinder xmlns:ui='urn:ui:com.google.gwt.uibinder'
	xmlns:g='urn:import:com.google.gwt.user.client.ui' xmlns:m='urn:import:gwt.mosaic.client.ui'>

	<ui:style>
		.login {
			margin: 10px;
			border: 10px solid Gray;
			padding: 10px;
		}
	</ui:style>

	<m:VBox addStyleNames="{style.login}" fill="true">

		<g:Label>
			Enter your search criteria and select
			the Find button to begin
			the search.
		</g:Label>

		<m:HBox>
			<g:InlineLabel>Search for:</g:InlineLabel>
			<g:TextBox />
		</m:HBox>

		<m:HBox>
			<m:Spacer weight="1" />
			<m:Button>Find</m:Button>
			<m:Button>Cancel</m:Button>
		</m:HBox>
	</m:VBox>

</ui:UiBinder>
```

The vertical box causes the main text, the box with the text box and the box with the buttons to orient vertically. The inner boxes orient their child widgets horizontally. As you see in the image above, the label and text input are placed side by side. The spacer and the two buttons are also placed horizontally in their box. Notice how the spacer causes the buttons to appear on the right side, because it is weighted.

In the next section, we will look at [specifying the sizes of individual widgets and how to constraint their sizes](Positioning.md).

&lt;wiki:gadget url="http://mosaic.arkasoft.com/gwt-mosaic-wiki.xml?v=3" height="95" width="728" border="0"/&gt;