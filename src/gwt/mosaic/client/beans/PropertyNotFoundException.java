package gwt.mosaic.client.beans;

/**
 * Thrown when a caller attempts to set the value of a non-existing bean
 * property.
 */
@SuppressWarnings("serial")
public class PropertyNotFoundException extends RuntimeException {

	public PropertyNotFoundException() {
		super();
	}

	public PropertyNotFoundException(String message) {
		super(message);
	}

	public PropertyNotFoundException(Throwable cause) {
		super(cause);
	}

	public PropertyNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
