package gwt.mosaic.client.beans;

/**
 * Thrown when an error is encountered during binding.
 */
@SuppressWarnings("serial")
public class BindException extends RuntimeException {

	public BindException() {
		super();
	}

	public BindException(String message) {
		super(message);
	}

	public BindException(Throwable cause) {
		super(cause);
	}

	public BindException(String message, Throwable cause) {
		super(message, cause);
	}
}
