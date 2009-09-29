package gwtunite.testing.framework;

import gwtunite.testing.framework.TestResult.Result;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import opera.io.Utils;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;

/**
 * A TestCase Executor is a proxy object that allows executing certain tests within a test case.
 * 
 * It's required because GWT applications cannot perform reflection, and thus a TestCaseExecutor is generated
 * for every test case via deferred binding at compile time.
 */
public abstract class TestCaseExecutor {
	private Map<String, TestResult> testResults = new HashMap<String, TestResult>();
	private Result overallResult = Result.PASSED;
	
	public abstract String getTestCaseName();
	public abstract String[] getTestNames();
	public abstract void executeAllTests(TestCompleteHandler testCompleteHandler) throws Exception;
	public abstract void executeTest(String testName, TestCompleteHandler testCompleteHandler) throws Exception;
	
	protected TestCaseExecutor() {
		
	}
	
	public Map<String, TestResult> getResults() {
		return Collections.unmodifiableMap(testResults);
	}
	
	public Result getOveralResult() {
		return overallResult;
	}
	
	protected void registerResult(String testName, TestResult testResult) {
		Utils.log("Result Registered");
		testResults.put(testName, testResult);
		
		if (testResult.getResult() == Result.ERROR) {
			overallResult = Result.ERROR;
		} else if (testResult.getResult() == Result.FAILED && overallResult == Result.PASSED) {
			overallResult = Result.FAILED;
		}
	}

	/** Runs the list of tests Asynchronously, calling the handler when all the tests have executed */
	protected void runTestsAsync(final String[] tests, final TestCompleteHandler testCompleteHandler) {
		final TestCompleteHandler completeHandler = new TestCompleteHandler() {
			int currentTest = 0;
			@Override public void onTestComplete(final TestCaseExecutor testCaseExecutor) {
				currentTest++;
				if (currentTest < tests.length) {
					// Invoke the next test in the chain
					runTestAsync(tests[currentTest], this);
				} else {
					// We've finished all tests, inform the original testCompleteHandler
					DeferredCommand.addCommand(new Command() {
						@Override public void execute() {
							Utils.log("Tests Finished");
							testCompleteHandler.onTestComplete(testCaseExecutor);
						}
					});
				}
			}
		};
		
		if (tests.length > 0)
			runTestAsync(tests[0], completeHandler);
	}
	
	/** Runs a test Asynchronously, calling the testCompleteHandler when it completes */
	private void runTestAsync(final String testName, final TestCompleteHandler testCompleteHandler) {
		DeferredCommand.addCommand(new Command() {
			@Override public void execute() {
				try {
					executeTest(testName, testCompleteHandler);
				}catch(Exception e) {
					Utils.log("Test "+testName+" threw an exception : "+e);
				}
			}
		});
	}
	
	/** Called when this test completes. */
	public static interface TestCompleteHandler {
		public void onTestComplete(TestCaseExecutor testCaseExecutor);
	}
}

