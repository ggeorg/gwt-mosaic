/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.gwt.beansbinding.core.client.Binding;
import org.gwt.beansbinding.core.client.BindingGroup;
import org.gwt.beansbinding.core.client.Bindings;
import org.gwt.beansbinding.core.client.Property;
import org.gwt.beansbinding.core.client.AutoBinding.UpdateStrategy;
import org.gwt.beansbinding.core.client.util.HasPropertyChangeSupport;
import org.gwt.mosaic.core.client.DOM;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.UIObject;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 * @param <T>
 */
public abstract class ActionBindings<T> {

  public class TargetBean implements HasPropertyChangeSupport {
    protected T target;

    protected PropertyChangeSupport changeSupport = new PropertyChangeSupport(
        this);

    private AbstractImagePrototype image;

    public TargetBean(T target) {
      this.target = target;
    }

    public synchronized void addPropertyChangeListener(
        final PropertyChangeListener listener) {
      changeSupport.addPropertyChangeListener(listener);
    }

    public Character getAccessKey() {
      return null;
    }

    public Boolean getEnabled() {
      if (target instanceof FocusWidget) {
        return ((FocusWidget) target).isEnabled();
      } else {
        throw new UnsupportedOperationException();
      }
    }

    public String getHTML() {
      if (target instanceof HasHTML) {
        return ((HasHTML) target).getHTML();
      } else {
        throw new UnsupportedOperationException();
      }
    }

    public AbstractImagePrototype getImage() {
      return image;
    }

    public String getText() {
      if (target instanceof HasText) {
        return ((HasText) target).getText();
      } else {
        throw new UnsupportedOperationException();
      }
    }

    public String getTitle() {
      if (target instanceof UIObject) {
        return ((UIObject) target).getTitle();
      } else {
        throw new UnsupportedOperationException();
      }
    }

    public Boolean getVisible() {
      if (target instanceof UIObject) {
        return ((UIObject) target).isVisible();
      } else {
        throw new UnsupportedOperationException();
      }
    }

    public synchronized void removePropertyChangeListener(
        final PropertyChangeListener listener) {
      changeSupport.removePropertyChangeListener(listener);
    }

    public void setAccessKey(Character key) {
      if (target instanceof FocusWidget) {
        String accessKey = DOM.getStyleAttribute(
            ((FocusWidget) target).getElement(), "accessKey");
        Character oldValue = Character.valueOf(accessKey != null
            && accessKey.length() > 0 ? accessKey.charAt(0) : '\0');
        ((FocusWidget) target).setAccessKey(key == null ? '\0' : key);
        changeSupport.firePropertyChange("enabled", oldValue, key);
      } else {
        throw new UnsupportedOperationException();
      }
    }

    public void setEnabled(Boolean enabled) {
      if (target instanceof FocusWidget) {
        enabled = toBoolean(enabled, Boolean.TRUE);
        Boolean oldValue = toBoolean(((FocusWidget) target).isEnabled(),
            Boolean.TRUE);
        ((FocusWidget) target).setEnabled(enabled);
        changeSupport.firePropertyChange("enabled", oldValue, enabled);
      } else {
        throw new UnsupportedOperationException();
      }
    }

    public void setHTML(String html) {
      if (target instanceof HasHTML) {
        String oldValue = ((HasHTML) target).getHTML();
        ((HasHTML) target).setHTML(html);
        changeSupport.firePropertyChange("html", oldValue, html);
      } else {
        throw new UnsupportedOperationException();
      }
    }

    public void setImage(AbstractImagePrototype image) {
      AbstractImagePrototype oldValue = this.image;
      this.image = image;
      changeSupport.firePropertyChange("image", oldValue, image);
    }

    public void setText(String text) {
      if (target instanceof HasText) {
        String oldValue = ((HasText) target).getText();
        ((HasText) target).setText(text);
        changeSupport.firePropertyChange("text", oldValue, text);
      } else {
        throw new UnsupportedOperationException();
      }
    }

    public void setTitle(String title) {
      if (target instanceof UIObject) {
        String oldValue = ((UIObject) target).getTitle();
        ((UIObject) target).setTitle(title);
        changeSupport.firePropertyChange("title", oldValue, title);
      } else {
        throw new UnsupportedOperationException();
      }
    }

    public void setVisible(Boolean visible) {
      if (target instanceof UIObject) {
        visible = toBoolean(visible, Boolean.TRUE);
        Boolean oldValue = toBoolean(((UIObject) target).isVisible(),
            Boolean.TRUE);
        ((UIObject) target).setVisible(visible);
        changeSupport.firePropertyChange("visible", oldValue, visible);
      } else {
        throw new UnsupportedOperationException();
      }
    }

    protected Boolean toBoolean(Boolean value, Boolean defaultValue) {
      assert (defaultValue != null);
      return value == null ? defaultValue : value;
    }
  }

  private final Action source;
  private final T target;
  private final BindingGroup bindingGroup = new BindingGroup();

  public ActionBindings(Action source, T target) {
    this.source = source;
    this.target = target;
  }

  @SuppressWarnings("unchecked")
  public final void addBinding(Binding binding) {
    bindingGroup.addBinding(binding);
  }

  @SuppressWarnings("unchecked")
  protected Binding addBinding(String name, Property actionProperty,
      Property targetProperty) {
    return addBinding(name, UpdateStrategy.READ, actionProperty, targetProperty);
  }

  @SuppressWarnings("unchecked")
  protected Binding addBinding(String name, UpdateStrategy updateStrategy,
      Property actionProperty, Property targetProperty) {
    Binding binding = Bindings.createAutoBinding(updateStrategy, source,
        actionProperty, getTargetBean(), targetProperty, name);
    addBinding(binding);
    return binding;
  }

  public synchronized void addPropertyChangeListener(
      final PropertyChangeListener listener) {
    getTargetBean().addPropertyChangeListener(listener);
  }

  public final void bind() {
    bindingGroup.bind();
    onBind();
  }

  @SuppressWarnings("unchecked")
  public final Binding getBinding(String name) {
    return bindingGroup.getBinding(name);
  }

  protected BindingGroup getBindingGroup() {
    return bindingGroup;
  }

  public Action getSource() {
    return source;
  }

  public T getTarget() {
    return target;
  }

  protected abstract TargetBean getTargetBean();

  protected abstract void onBind();

  protected abstract void onUnBind();

  public synchronized void removePropertyChangeListener(
      final PropertyChangeListener listener) {
    getTargetBean().removePropertyChangeListener(listener);
  }

  public final void unbind() {
    bindingGroup.unbind();
    onUnBind();
  }
}
