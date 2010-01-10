/*
 * Copyright (c) 2008-2010 GWT Mosaic Georgios J. Georgopolos.
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

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Common action images.
 * 
 * @author ggeorgopoulos.georgios(at)gmail.com
 */
public interface ActionImages extends ClientBundle {

  @Source("add_folder_action.png")
  ImageResource add_folder_action();

  @Source("award_star_bronze_1.png")
  ImageResource award_star_bronze_1();

  @Source("bell.png")
  ImageResource bell();

  @Source("bomb.png")
  ImageResource bomb();

  @Source("close_action.png")
  ImageResource close_action();

  @Source("copy_action.png")
  ImageResource copy_action();

  @Source("cut_action.png")
  ImageResource cut_action();

  @Source("delete_action.png")
  ImageResource delete_action();

  @Source("exit_action.png")
  ImageResource exit_action();

  @Source("file_upload_action.png")
  ImageResource file_upload_action();

  @Source("form_add_action.png")
  ImageResource form_add_action();

  @Source("form_delete_action.png")
  ImageResource form_delete_action();

  @Source("form_edit_action.png")
  ImageResource form_edit_action();

  @Source("menuitem_checkbox.png")
  ImageResource menuitem_checkbox();

  @Source("menuitem_radiobutton.png")
  ImageResource menuitem_radiobutton();

  @Source("noimage.png")
  ImageResource noimage();

  @Source("open_action.png")
  ImageResource open_action();

  @Source("open_parent_folder_action.png")
  ImageResource open_parent_folder_action();

  @Source("paste_action.png")
  ImageResource paste_action();

  @Source("reload_action.png")
  ImageResource reload_action();

  @Source("reload_db_action.png")
  ImageResource reload_db_action();
}
