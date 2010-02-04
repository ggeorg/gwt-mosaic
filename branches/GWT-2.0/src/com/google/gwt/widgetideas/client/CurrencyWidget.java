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

package com.google.gwt.widgetideas.client;

/**
 * Currency Text Widget
 * 
 * This class provides a text widget implementation for input currency amount.
 * It addresses a few special requirement for currency input, namely, - only
 * digits, decimal separator and group separator should be allowed in input. -
 * native digits should be handles. - correct currency symbol should be picked
 * according to user's locale. - currency symbol should be displayed on the
 * right side of the box.
 * 
 * Usage: RootPanel rootPanel = RootPanel.get();
 * 
 * CurrencyWidget currencyWidget = new CurrencyWidget();
 * rootPanel.add(currencyWidget, 180, 32);
 * 
 * clickMeButton = new Button(); rootPanel.add(clickMeButton, 394, 32);
 * clickMeButton.setText("See what you got!");
 * clickMeButton.addClickListener(new ClickListener() { public void
 * onClick(Widget sender) { Window.alert("You entered: " +
 * Double.toString(currencyWidget.getAmount())); } });
 * 
 */

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.CurrencyData;
import com.google.gwt.i18n.client.CurrencyList;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.i18n.client.constants.NumberConstants;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.overrides.DOMHelper;

/**
 * TODO:Add javadoc
 */
public class CurrencyWidget extends Composite {
  protected static final NumberConstants numberConstants = LocaleInfo.getCurrentLocale().getNumberConstants();
  protected static String acceptableCharset = getAcceptedCharset();

  private static String getAcceptedCharset() {
    if (!GWT.isClient()) {
      return null;
    }
    StringBuffer strbuf = new StringBuffer();
    strbuf.append("0123456789");
    if (numberConstants.zeroDigit().charAt(0) != '0') {
      for (int i = 0; i < 10; i++) {
        strbuf.append(numberConstants.zeroDigit().charAt(0) + i);
      }
    }
    strbuf.append(numberConstants.decimalSeparator().charAt(0));
    if (numberConstants.groupingSeparator().charAt(0) == '\u00a0') {
      strbuf.append(' ');
    } else {
      strbuf.append(numberConstants.groupingSeparator().charAt(0));
    }
    strbuf.append((char) KeyboardListener.KEY_TAB);
    strbuf.append((char) KeyboardListener.KEY_BACKSPACE);
    strbuf.append((char) KeyboardListener.KEY_DELETE);
    strbuf.append((char) KeyboardListener.KEY_ENTER);
    strbuf.append((char) KeyboardListener.KEY_HOME);
    strbuf.append((char) KeyboardListener.KEY_END);
    strbuf.append((char) KeyboardListener.KEY_LEFT);
    strbuf.append((char) KeyboardListener.KEY_UP);
    strbuf.append((char) KeyboardListener.KEY_RIGHT);
    strbuf.append((char) KeyboardListener.KEY_DOWN);
    strbuf.append((char) DOMHelper.OTHER_KEY_LEFT);
    strbuf.append((char) DOMHelper.OTHER_KEY_UP);
    strbuf.append((char) DOMHelper.OTHER_KEY_RIGHT);
    strbuf.append((char) DOMHelper.OTHER_KEY_DOWN);
    return strbuf.toString();
  }

  protected final HorizontalPanel horizontalPanel = new HorizontalPanel();
  protected final Label currencySymbol;

  protected final TextBox amountBox = new TextBox();
  protected final NumberFormat formatter;

  protected boolean valueInitiated = false;
  protected CurrencyData currencyData;
  
  /**
   * Constructs a CurrencyWidget object.
   */
  public CurrencyWidget() {
    this(numberConstants.defCurrencyCode());
  }

  /**
   * Constructs a CurrencyWidget object with specified currency.
   * 
   * @param currencyCode International currency code (ISO 4217).
   */
  public CurrencyWidget(String currencyCode) {
    currencyData = CurrencyList.get().lookup(currencyCode);
    assert (currencyData != null);
    currencySymbol = new Label(currencyData.getPortableCurrencySymbol());
    formatter = NumberFormat.getFormat(getCurrencyAmountPattern());
    
    initWidget(horizontalPanel);
    horizontalPanel.setSpacing(2);
    if (isLeadingSymbol()) {
      horizontalPanel.add(currencySymbol);
      horizontalPanel.add(amountBox);
      amountBox.setTextAlignment(TextBoxBase.ALIGN_LEFT);
    } else {
      horizontalPanel.add(amountBox);
      horizontalPanel.add(currencySymbol);
      amountBox.setTextAlignment(TextBoxBase.ALIGN_RIGHT);
    }
    amountBox.setText(formatter.format(0));

    amountBox.addFocusListener(new FocusListener() {
      public void onFocus(Widget sender) {
        if (!valueInitiated) {
          amountBox.setText("");
          valueInitiated = true;
        }
      }

      public void onLostFocus(Widget sender) {
        amountBox.setText(reformatContent());
      }
    });

    amountBox.addKeyboardListener(new KeyboardListenerAdapter() {
      public void onKeyDown(Widget sender, char keyCode, int modifiers) {
        if (keyCode == (char) KEY_ENTER) {
          amountBox.setText(reformatContent());
        }
      }

      public void onKeyPress(Widget sender, char keyCode, int modifiers) {
        if (acceptableCharset.indexOf(keyCode) == -1) {
          amountBox.cancelKey();
        }
        if (amountBox.getText().length() >= 16) {
          amountBox.cancelKey();
        }
      }
    });
  }

  /*
   * This method is used to retrieve the amount value user inputed. @return user
   * inputed value in double.
   */
  public double getAmount() {
    return formatter.parse(amountBox.getText());
  }

  protected String getCurrencyAmountPattern() {
    StringBuffer strBuf = new StringBuffer("#,##0");
    int precision = currencyData.getDefaultFractionDigits();
    if (precision > 0) {
      strBuf.append('.');
      for (int i = 0; i < precision; i++) {
        strBuf.append('0');
      }
    }
    return strBuf.toString();
  }

  protected boolean isLeadingSymbol() {
    return numberConstants.currencyPattern().startsWith("\u00a4");
  }

  protected String reformatContent() {
    String str = amountBox.getText();
    if (numberConstants.groupingSeparator().charAt(0) == '\u00a0') {
      str = str.replace(' ', '\u00a0');
    }
    double amount;
    try {
      amount = formatter.parse(str);
    } catch (NumberFormatException e) {
      amount = 0.0;
    }
    return formatter.format(amount);
  }
}
