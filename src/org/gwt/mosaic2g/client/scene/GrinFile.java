package org.gwt.mosaic2g.client.scene;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Indicates the Grin file from which to generate a {@link GrinCommands}
 * instance.
 * 
 * @author ggeorg
 */
@Documented
@Target(ElementType.TYPE)
public @interface GrinFile {

	/**
	 * @return the file name
	 */
	String value();
}