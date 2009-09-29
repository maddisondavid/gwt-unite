package gwtunite.testing.framework;

import com.google.gwt.user.client.Timer;

public abstract class TestCase {
	private final boolean isInAsync = false;
	
	/** Called by the framework just before a test is run */
	public void setUp() throws Exception{
		/** By default, does nothing, should be overriden by SubClasses */
	}
	
	/** Called by the framework after the a test has run */
	public void tearDown() throws Exception {
		/** By default, does nothing, should be overriden by SubClasses */
	}

	/** Returns true if this TestCase is currently running in Async mode */
	public boolean isInAsync() {
		return isInAsync;
	}
	
	/** Throws an AssertionFailureException if the given condition is not true */
	protected void assertTrue(boolean condition) {
		if (!condition)
			throw new AssertionFailureException("AssertTrue failed");
	}

	/** Throws an AssertionFailureException containing the given message if the given condition is not true */
	protected void assertTrue(boolean condition, String message) {
		if (!condition)
			throw new AssertionFailureException("AssertTrue Failed:"+message);
	}
	
	/** Throws an AssertionFailureException if the given condition is true */
	protected void assertFalse(boolean condition) {
		if (condition)
			throw new AssertionFailureException("AssertFalse failed");
	}

	/** Throws an AssertionFailureException containing the given message if the given condition is true */
	protected void assertFalse(boolean condition, String message) {
		if (condition)
			throw new AssertionFailureException("AssertFalse Failed:"+message);
	}

	/** Throws an AssertionFailureException if Obj1 is NOT equal to Obj2 */
	protected void assertEquals(Object obj1, Object obj2) {
		if (obj1 == null) {
			if (obj2 != null)
				throw new AssertionFailureException("Expected:null Actual:"+obj2);
		} else if (!obj1.equals(obj2)) {
			throw new AssertionFailureException("Expected:"+obj1+" Actual:"+obj2);
		}
	}

	/** Throws an AssertionFailureException containing the given message if Obj1 is NOT equal to Obj2 */
	protected void assertEquals(Object obj1, Object obj2, String message) {
		if (obj1 == null) {
			if (obj2 != null)
				throw new AssertionFailureException("Expected:null Actual:"+obj2);
		} else if (!obj1.equals(obj2)) {
			throw new AssertionFailureException("Expected:"+obj1+" Actual:"+obj2+" "+message);
		}
	}
	
	/** Throws an AssertionFailureException if obj IS null */
	protected void assertNotNull(Object obj) {
		if (obj == null)
			throw new AssertionFailureException("AssertNotNull Failed");
	}

	/** Throws an AssertionFailureException if obj IS null */
	protected void assertNotNull(Object obj, String message) {
		if (obj == null)
			throw new AssertionFailureException("AssertNotNull failed : " + message);
	}

	/** Throws an AssertionFailureException */
	protected void fail() {
		throw new AssertionFailureException();
	}

	/** Throws an AssertionFailureException containing the given message*/
	protected void fail(String message) {
		throw new AssertionFailureException(message);
	}

	/** Delays the test finishing by the given amount */
	final Timer delayTestTimer = new Timer() {
		@Override public void run() {
			fail("Test timeout");
		}
	};
	
	public void delayTestFinish(int timeout) {
		delayTestTimer.schedule(timeout);
	}
	
	public void finishTest() {
		delayTestTimer.cancel();
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
