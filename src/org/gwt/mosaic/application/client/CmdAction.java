package org.gwt.mosaic.application.client;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method that will be used to define a {@link Action}. It also
 * identifies the resources that will be used to initialize the {@link Action
 * Action's} properties. Additional {@code &#064;Action} parameters can be used
 * to specify the name of the bound properties (from the same class) that
 * indicate if the {@link Action} is to be enabled/selected.
 * <p>
 * TODO
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CmdAction {
  String name() default "";

  String description() default "";

  String image() default "";

  String enabledProperty() default "";

  String selectedProperty() default "";
}
