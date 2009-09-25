package gwtunite.testing.client;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import opera.io.Utils;

/**
 * The base class that knows about all test.  This is built by deferred binding 
 *
 */
public abstract class TestCaseExecutorRegistry {
	private final Map<String, TestCaseExecutor> testCases = new HashMap<String, TestCaseExecutor>();
	
	public void registerTestExecutor(TestCaseExecutor testCase)  {
		testCases.put(testCase.getTestCaseName(), testCase);
	}
	
	/** Runs a single test case and returns the results of the individual tests */
	public Map<String, TestResult> runTestCase(String testCase) throws Exception {
		Utils.log("Getting test "+testCase);
		TestCaseExecutor testExecutor = testCases.get(testCase);
		Utils.log("Executed test "+testCase);
		return testExecutor.getResults();
	}
	
	public Map<String, TestResult> runTest(String testCase, String testName) throws Exception {
		TestCaseExecutor testExecutor = testCases.get(testCase);
		testExecutor.executeTest(testName);
		
		return testExecutor.getResults();
	}
	
	public Collection<TestCaseExecutor> getExecutors() {
		return testCases.values();
	}
}
