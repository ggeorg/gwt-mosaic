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
package org.gwt.mosaic.ui.client.infopanel;

import java.util.ArrayList;
import java.util.List;

import org.gwt.mosaic.ui.client.infopanel.InfoPanel.InfoPanelType;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
/**
 * This is the default implementation for rendering system events in the right bottom of your application.
 * if InfoPanelType.HUMANIZED_MESSAGE is used then the event is showed in modal mode and centered to the screen.
 *
 * @author luciano.broussal(at)gmail.com
 */
public class TrayInfoPanelNotifier implements InfoPanelNotifier, PopupListener {

    //TODO Separate Humanized message from tray ones. no sense to have both in this class.
    
    List<InfoPanel> waintingInfoPanel = new ArrayList<InfoPanel>();
    List<InfoPanel> visibleInfoPanel = new ArrayList<InfoPanel>();
    int currentLevel = 1;
    
    private static TrayInfoPanelNotifier instance = null;
    
    public static TrayInfoPanelNotifier getInstance() {
      if (instance == null) {
        instance = new TrayInfoPanelNotifier();
      }
      return instance;
    }
    
    public static void notifyTrayEvent(String caption , String text){
      getInstance().show(InfoPanelType.TRAY_NOTIFICATION, caption, text);
    }

    public static void notifyModalEvent(String caption , String text){
      getInstance().show(InfoPanelType.HUMANIZED_MESSAGE, caption, text);
    }
    
    /**
     * Default constructor.
     */
    protected TrayInfoPanelNotifier() {
      // Nothing to do here!
    }

    public void show(InfoPanelType infoPanelType, String caption, String content) {
	if (infoPanelType == InfoPanelType.TRAY_NOTIFICATION) {
	    InfoPanel infoPanel = new InfoPanel(caption, content);

	    if (canLayoutInfoPanel(infoPanel)) {
		showInfoPanel(infoPanel, currentLevel);
		currentLevel++;
		visibleInfoPanel.add(infoPanel);
	    } else {
		visibleInfoPanel.add(infoPanel);
		waintingInfoPanel.add(infoPanel);
	    }
	    return;
	}
	
	if(infoPanelType == InfoPanelType.HUMANIZED_MESSAGE){
	    final InfoPanel infoPanel = new InfoPanel(caption, content, true);
	    infoPanel.showModal();
	    return;
	}
    }

    private void showInfoPanel(InfoPanel infoPanel, int level) {
	int[] coordinates = calculateInfoPanelPosition(infoPanel, level);
	infoPanel.addPopupListener(this);
	infoPanel.setPopupPosition(coordinates[0], coordinates[1]);
	infoPanel.show();
    }

    private boolean canLayoutInfoPanel(InfoPanel infoPanel) {
	final int ch = Window.getClientHeight();
	final int top = ch - InfoPanel.HEIGHT - 20 - (currentLevel * (InfoPanel.HEIGHT + 20));
	return top >= 0;
    }

    private int[] calculateInfoPanelPosition(InfoPanel infoPanel, int level) {
	final int windowWidth = Window.getClientWidth();
	final int windowHeight = Window.getClientHeight();
	final int left = (windowWidth - InfoPanel.WIDTH - 20);
	final int top = windowHeight - (level * (InfoPanel.HEIGHT + 20));

	return new int[] { left, top };

    }

    public void onPopupClosed(PopupPanel sender, boolean autoClosed) {
	visibleInfoPanel.remove(sender);
	currentLevel--;
	if (visibleInfoPanel.size() > 0) {
	    pushDownVisibleInfoPanels();
	}
	if (waintingInfoPanel.size() > 0) {
	    pushUpVisibleInfoPanels();
	    InfoPanel infoPanel = waintingInfoPanel.remove(0);
	    showInfoPanel(infoPanel, currentLevel + 1);
	    currentLevel++;
	}
    }

    private void pushUpVisibleInfoPanels() {
	for (int i = visibleInfoPanel.size() - 1; i >= 0; i--) {
	    InfoPanel infoPanel = visibleInfoPanel.get(i);
	    int[] newCoordinates = calculateInfoPanelPosition(infoPanel, i + 1);
	    infoPanel.setPopupPosition(newCoordinates[0], newCoordinates[1]);
	}
    }

    private void pushDownVisibleInfoPanels() {
	for (int i = 0; i < visibleInfoPanel.size(); i++) {
	    InfoPanel infoPanel = visibleInfoPanel.get(i);
	    int[] newCoordinates = calculateInfoPanelPosition(infoPanel, i + 1);
	    infoPanel.setPopupPosition(newCoordinates[0], newCoordinates[1]);
	}
    }

}
