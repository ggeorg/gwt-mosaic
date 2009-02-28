/*
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopolos.
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
package org.gwt.mosaic.actions.client;

import com.google.gwt.i18n.client.Constants;

/**
 * Common action names and short descriptions.
 * 
 * @author ggeorgopoulos.georgios(at)gmail.com
 */
public interface ActionConstants extends Constants {

  @DefaultStringValue("Add an empty folder")
  String addFolderDescription();

  @DefaultStringValue("Add Folder")
  String addFolderName();

  @DefaultStringValue("Close All")
  String closeAllName();

  @DefaultStringValue("Close")
  String closeName();

  @DefaultStringValue("Copy")
  String copyName();

  @DefaultStringValue("Copy the selection")
  String copyShortDescription();

  @DefaultStringValue("Cut")
  String cutName();

  @DefaultStringValue("Cut the selection")
  String cutShortDescription();

  @DefaultStringValue("Delete the selected objects permanently")
  String deleteSelectedDescription();
  
  @DefaultStringValue("Delete Selected")
  String deleteSelectedName();

  @DefaultStringValue("Deselect All")
  String deselectAllName();

  @DefaultStringValue("Exit")
  String exitName();

  @DefaultStringValue("Exit application")
  String exitShortDescription();

  @DefaultStringValue("Browse your computer to select a file to upload")
  String fileUploadDescription();

  @DefaultStringValue("File Upload...")
  String fileUploadName();

  @DefaultStringValue("Add")
  String formAddName();

  @DefaultStringValue("Delete")
  String formDeleteName();

  @DefaultStringValue("Edit")
  String formEditName();

  @DefaultStringValue("Open...")
  String openName();

  @DefaultStringValue("Open the parent folder")
  String openParentFolderDescription();

  @DefaultStringValue("Open Parent")
  String openParentFolderName();

  @DefaultStringValue("Paste")
  String pasteName();

  @DefaultStringValue("Paste the clipboard")
  String pasteShortDescription();

  @DefaultStringValue("Reload")
  String reloadName();

  @DefaultStringValue("Reload current view")
  String reloadShortDescription();

  @DefaultStringValue("Select All")
  String selectAllName();
}
