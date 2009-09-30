package gwtunite.testing.framework;

import java.util.Date;

import com.google.gwt.user.client.Timer;

public abstract class TestCase {
	/** Called by the framework just before a test is run */
	public void setUp() throws Exception{
		/** By default, does nothing, should be overriden by SubClasses */
	}
	
	/** Called by the framework after the a test has run */
	public void tearDown() throws Exception {
		/** By default, does nothing, should be overriden by SubClasses */
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
	
	/** Spin waiting whilst the timeout or the condition is not true 
	 * 
	 * WARNING : This is a REALLY evil method and SHOULD NOT really be used.  However, we
	 * don't have a sleep method in JavaScript and doing things in an Async way creates
	 * is difficult and leads to pretty bad tests
	 */
	protected void spinWait(long timeout, Condition condition) {
		long timeoutTime = (new Date()).getTime() + timeout;
		
		long dummyCounter = 0;
		while ((new Date()).getTime() < timeoutTime && !condition.isTrue())
			dummyCounter++;
		
		if (!condition.isTrue())
			throw new TimeoutException(timeout);
	}
	
	public static class TimeoutException extends RuntimeException {
		TimeoutException(long timeout) {
			super("Timeout waiting "+timeout+"ms");
		}
	}
	
	public interface Condition {
		public boolean isTrue();
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
