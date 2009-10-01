package gwtunite.testing.framework;

import javax.print.attribute.standard.Finishings;

import com.google.gwt.user.client.Timer;

public abstract class TestCase {
	private boolean isInAsyncMode = false;
	private TestResultListener userResultListener;

	/** The timer used should delayTestFinish be called */
	private Timer delayFinishTimer = new Timer() {
		@Override public void run() {
			getResultListener().onFailed("Test timed out");
		}
	};
	
	private TestResultListener tearDownListener = new TestResultListener() {
		public void onPass() {
			try {
				tearDown();
			}catch(Exception e) {
			} finally {
				userResultListener.onPass();
			}
		}
		
		public void onFailed(String message) {
			try {
				tearDown();
			}catch(Exception e) {
			} finally {
				userResultListener.onFailed(message);
			}
		}
		
		public void onError(Exception t) {
			try {
				tearDown();
			}catch(Exception e) {
			}
			finally {
				userResultListener.onError(t);
			}
		}
	};

	/** Executes the test with the given name, calling the resultListener with the result of the test */
	public void executeTest(String testName, TestResultListener resultListener) {
		setResultListener(resultListener);
		
		try {
			resetAsyncMode();
			setUp();
		} catch(Exception e) {
			getResultListener().onError(e);
			return;
		}
	
		try {
			executeTestMethod(testName);

			// Test Finished
			if (!isInAsyncMode()) 
				getResultListener().onPass();
		} catch(Exception e) {
			handleException(e);
		}
	}
	
	
	/** Executes the method with the given name */
	protected void executeTestMethod(String testName) throws Exception{
		// This is override at compile time with the correct method implementation
	}
	
	protected void resetAsyncMode() {
		isInAsyncMode = false;
	}
		
	protected void setResultListener(TestResultListener resultListener) {
		this.userResultListener = resultListener;
	}

	protected TestResultListener getResultListener() {
		return tearDownListener;
	}

	/** Returns true if this test is currently running in Asynchrous mode, that is a call to {@link #delayTestFinish(int)} has been
	 * made
	 */
	protected boolean isInAsyncMode() {
		return isInAsyncMode;
	}
	
	/** Delays a test finishing.
	 * 
	 * Normally when a test method ends, the test has deemed to pass, however this doesn't work when the test does asynchronous work.
	 * Since JavaScript cannot block, this method tells the framework that the test shouldn't be considered finished when the 
	 * test method ends.
	 * 
	 * {@link #finishTest()} should be called when the test has actually finished.  If {@link #finishTest()} has not been called
	 * before the given timeout, the test fails with a "Timeout" message 
	 * 
	 * If an assertion fails, or the test throws an exception, the test is marked as finished.
	 * 
	 */
	protected void delayTestFinish(int timeout) {
		isInAsyncMode = true;
		delayFinishTimer.schedule(timeout);
	}
	
	/**
	 * Informs the framework that a test which contained the {@link #delayTestFinish(int)} call, has now finished.
s	 */
	protected void finishTest() {
		delayFinishTimer.cancel();
		getResultListener().onPass();
	}

	/** 
	 * When a test is running synchronusly, the framework will automatically catch any exceptions and end the test.  However,
	 * when using asynchronous methods, there is no such code to catch any exceptions.  If using asynchronous methods, 
	 * any exceptions should be caught and {@link #handleException(Exception)} should be called to handle the exception.
	 * 
	 * For example:
	 * 
	 * <code>
	 * try {
	 * 		// Do some work
	 * 		assertEquals(obj1, obj2);
	 * } catch(Exception e) {
	 * 	 handleException(e);
	 * }
	 * </code>
	 */
	protected void handleException(Exception e) {
		if (e instanceof AssertionFailureException) {
			getResultListener().onFailed(((AssertionFailureException)e).getMessage());
		} else {
			getResultListener().onError(e);
		}
	}
	
	/** Called by the framework just before a test is run */
	public void setUp() throws Exception{
		/** By default, does nothing, should be overriden by SubClasses */
	}
	
	/** Called by the framework after the a test has run */
	public void tearDown() throws Exception {
		/** By default, does nothing, should be overriden by SubClasses */
	}

	/** Cleans up when an assertion fails */
	private void assertionFailed(String message) {
		delayFinishTimer.cancel();
		throw new AssertionFailureException(message);
	}
	
	/** Throws an AssertionFailureException if the given condition is not true */
	protected void assertTrue(boolean condition) {
		if (!condition)
			assertionFailed("AssertTrue failed");
	}

	/** Throws an AssertionFailureException containing the given message if the given condition is not true */
	protected void assertTrue(boolean condition, String message) {
		if (!condition)
			assertionFailed("AssertTrue Failed:"+message);
	}
	
	/** Throws an AssertionFailureException if the given condition is true */
	protected void assertFalse(boolean condition) {
		if (condition)
			assertionFailed("AssertFalse failed");
	}

	/** Throws an AssertionFailureException containing the given message if the given condition is true */
	protected void assertFalse(boolean condition, String message) {
		if (condition)
			assertionFailed("AssertFalse Failed:"+message);
	}

	/** Throws an AssertionFailureException if Obj1 is NOT equal to Obj2 */
	protected void assertEquals(Object obj1, Object obj2) {
		if (obj1 == null) {
			if (obj2 != null)
				assertionFailed("Expected:null Actual:"+obj2);
		} else if (!obj1.equals(obj2)) {
			assertionFailed("Expected:"+obj1+" Actual:"+obj2);
		}
	}

	/** Throws an AssertionFailureException containing the given message if Obj1 is NOT equal to Obj2 */
	protected void assertEquals(Object obj1, Object obj2, String message) {
		if (obj1 == null) {
			if (obj2 != null)
				assertionFailed("Expected:null Actual:"+obj2);
		} else if (!obj1.equals(obj2)) {
			assertionFailed("Expected:"+obj1+" Actual:"+obj2+" "+message);
		}
	}
	
	/** Throws an AssertionFailureException if obj IS null */
	protected void assertNotNull(Object obj) {
		if (obj == null)
			assertionFailed("AssertNotNull Failed");
	}

	/** Throws an AssertionFailureException if obj IS null */
	protected void assertNotNull(Object obj, String message) {
		if (obj == null)
			assertionFailed("AssertNotNull failed : " + message);
	}

	/** Throws an AssertionFailureException */
	protected void fail() {
		assertionFailed("");
	}

	/** Throws an AssertionFailureException containing the given message*/
	protected void fail(String message) {
		assertionFailed(message);
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
