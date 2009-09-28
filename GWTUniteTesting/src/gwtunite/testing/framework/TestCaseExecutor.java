package gwtunite.testing.framework;

import gwtunite.testing.framework.TestResult.Result;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import opera.io.Utils;

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
	public abstract void executeAllTests() throws Exception;
	public abstract void executeTest(String testName) throws Exception;
	
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
}

