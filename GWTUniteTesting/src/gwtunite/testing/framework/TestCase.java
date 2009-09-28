package gwtunite.testing.framework;

public abstract class TestCase {

	/** Called by the framework just before a test is run */
	public void setUp() throws Exception{
		/** By default, does nothing, should be overriden by SubClasses */
	}
	
	/** Called by the framework after the test a test has run */
	public void tearDown() throws Exception {
		/** By default, does nothing, should be overriden by SubClasses */
	}

	/** Throws an AssertionFailureException if the given condition is not true */
	protected void assertTrue(boolean condition) {
		if (!condition)
			throw new AssertionFailureException();
	}

	/** Throws an AssertionFailureException containing the given message if the given condition is not true */
	protected void assertTrue(boolean condition, String message) {
		if (!condition)
			throw new AssertionFailureException(message);
	}
	
	/** Throws an AssertionFailureException if the given condition is true */
	protected void assertFalse(boolean condition) {
		if (condition)
			throw new AssertionFailureException();
	}

	/** Throws an AssertionFailureException containing the given message if the given condition is true */
	protected void assertFalse(boolean condition, String message) {
		if (condition)
			throw new AssertionFailureException();
	}

	/** Throws an AssertionFailureException if Obj1 is NOT equal to Obj2 */
	protected void assertEquals(Object obj1, Object obj2) {
		if (!obj1.equals(obj2))
			throw new AssertionFailureException();
	}

	/** Throws an AssertionFailureException containing the given message if Obj1 is NOT equal to Obj2 */
	protected void assertEquals(Object obj1, Object obj2, String message) {
		if (!obj1.equals(obj2))
			throw new AssertionFailureException(message);
	}
	
	/** Throws an AssertionFailureException if obj IS null */
	protected void assertNotNull(Object obj) {
		if (obj == null)
			throw new AssertionFailureException();
	}
	
	/** Throws an AssertionFailureException */
	protected void fail() {
		throw new AssertionFailureException();
	}

	/** Throws an AssertionFailureException containing the given message*/
	protected void fail(String message) {
		throw new AssertionFailureException(message);
	}
	
	public static class AssertionFailureException extends RuntimeException {
		private static final long serialVersionUID = 4127980293192958142L;

		private AssertionFailureException() {
			super("Assertion Failure");
		}
		
		private AssertionFailureException(String message) {
			super(message);
		}
		
		@Override
		public String toString() {
			return getMessage();
		}
	}
}
