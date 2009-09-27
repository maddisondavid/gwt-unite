package gwtunite.testing.client;

/**
 * Signifies an assertion has failed 
 */
public class AssertionFailureException extends RuntimeException {
	private static final long serialVersionUID = 4127980293192958142L;

	public AssertionFailureException() {
		super("Assertion Failure");
	}
	
	public AssertionFailureException(String message) {
		super(message);
	}
}
