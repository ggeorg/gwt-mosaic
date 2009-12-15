package org.gwt.mosaic.core.client.util;

import com.google.gwt.dom.client.Style.Unit;

public class UnitParser {

  public static Unit parseUnit(String str, Unit defaultValue) {
    final Unit result = parseUnit(str);
    return result != null ? result : defaultValue;
  }
  
  public static Unit parseUnit(String str) {
    str = str.toLowerCase().trim();
    if(str.endsWith(Unit.CM.getType())) {
      return Unit.CM;
    } else if (str.endsWith(Unit.EM.getType())) {
      return Unit.EM;
    } else if (str.endsWith(Unit.EX.getType())) {
      return Unit.EX;
    } else if (str.endsWith(Unit.IN.getType())) {
      return Unit.IN;
    } else if (str.endsWith(Unit.MM.getType())) {
      return Unit.MM;
    } else if (str.endsWith(Unit.PC.getType())) {
      return Unit.PC;
    } else if (str.endsWith(Unit.PCT.getType())) {
      return Unit.PCT;
    } else if (str.endsWith(Unit.PT.getType())) {
      return Unit.PT;
    } else if (str.endsWith(Unit.PX.getType())) {
      return Unit.PX;
    }
    return null;
  }
}
