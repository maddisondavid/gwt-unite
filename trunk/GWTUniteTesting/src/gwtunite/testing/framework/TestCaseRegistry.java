package gwtunite.testing.framework;


/**
 * The base class that knows about all test.  This is built by deferred binding 
 *
 */
public interface TestCaseRegistry {
	
	public TestCaseExecutor newExecutorInstance(String testCaseName);
	public TestCaseInfo[] getTestCaseInfos();
}
