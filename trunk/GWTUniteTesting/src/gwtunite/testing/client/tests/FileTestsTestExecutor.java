package gwtunite.testing.client.tests;

import gwtunite.testing.client.FailureException;
import gwtunite.testing.client.TestCaseExecutor;
import gwtunite.testing.client.TestResult;

public class FileTestsTestExecutor extends TestCaseExecutor {
	
	public String getTestCaseName() {
		return "FileTests";
	}

	@Override
	public String[] getTestNames() {
		return new String[]{"test_ExampleTest"};
	}
	
	@Override
	public void executeAllTests() throws Exception{
		execute_test_ExampleTest();
	}

	@Override
	public void executeTest(String testName) throws Exception {
		if (testName.equals("test_ExampleTest"))
			execute_test_ExampleTest();
		
		throw new Exception("Test "+testName+" not found");
	}

	
	private void execute_test_ExampleTest() throws Exception {
		FileTests test = new FileTests();
		test.setUp();

		try {
			test.test_ExampleTest();
			registerResult("test_ExampleTest", new TestResult("test_ExampleTest",TestResult.Result.PASSED));
		} catch(Exception e) {
			if (e instanceof FailureException) {
				registerResult("test_ExampleTest", new TestResult("test_ExampleTest",TestResult.Result.FAILED, e));
			} else {
				registerResult("test_ExampleTest", new TestResult("test_ExampleTest",TestResult.Result.EXCEPTION, e));
			}
		}
		finally {
			test.tearDown();
		}
	}
}
