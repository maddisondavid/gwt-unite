package gwtunite.testing.client;

/**
 * Records the results of a test 
 *
 */
public class TestResult {
	public static enum Result {
		PASSED,
		FAILED,
		EXCEPTION
	}

	private final String testName;
	private final Result result; 
	private final Throwable throwable;
	
	public TestResult(String testName, Result result) {
		this.testName = testName;
		this.result = result;
		throwable = null;
	}

	public TestResult(String testName, Result result, Throwable e) {
		this.testName = testName;
		this.result = result;
		this.throwable = e;
	}
	
	public String getTestName() {
		return testName;
	}
	
	public Result getResult() {
		return result;
	}
	
	public Throwable getException() {
		return throwable;
	}
}

