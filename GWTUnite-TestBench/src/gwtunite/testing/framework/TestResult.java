package gwtunite.testing.framework;

/**
 * Records the results of a test 
 *
 */
public class TestResult {
	public static enum Result {
		PASSED,
		FAILED,
		ERROR
	}

	private final String testName;
	private final Result result;
	private String failureMessage;
	private Exception exception;
	
	public static TestResult pass(String testName) {
		return new TestResult(testName, Result.PASSED);
	}

	public static TestResult failed(String testName, String failureMessage) {
		return new TestResult(testName, Result.FAILED, failureMessage);
	}

	public static TestResult error(String testName, Exception exception) {
		return new TestResult(testName, Result.ERROR, exception);
	}
	
	private TestResult(String testName, Result result) {
		this.testName = testName;
		this.result = result;
	}
	
	private TestResult(String testName, Result result, Exception e) {
		this(testName, result);
		this.exception = e;
	}

	private TestResult(String testName, Result result, String failedMessage) {
		this.testName = testName;
		this.result = result;
		this.failureMessage = failedMessage;
	}
	
	public String getTestName() {
		return testName;
	}
	
	public Result getResult() {
		return result;
	}
	
	public String getFailureMessage() {
		return failureMessage;
	}
	
	public Exception getException() {
		return exception;
	}
}

