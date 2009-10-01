package gwtunite.testing.framework;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * The base class that knows about all test.  This is built by deferred binding 
 *
 */
public abstract class TestCaseRegistry {
	private Map<String, TestCaseInfo> testCaseInfos;
	
	public TestCaseRegistry() {
		testCaseInfos = new HashMap<String, TestCaseInfo>();
	}
	
	protected void registerInfo(TestCaseInfo testCaseInfo) {
		testCaseInfos.put(testCaseInfo.getName(), testCaseInfo);
	}
	
	public TestCaseInfo getTestCaseInfo(String testCaseName) {
		if (!testCaseInfos.containsKey(testCaseName))
			throw new RuntimeException("Test Case "+testCaseName+" not found");
		
		return testCaseInfos.get(testCaseName);
	}
	
	public Collection<TestCaseInfo> getAllTestCaseInfo() {
		return testCaseInfos.values();
	}
	public abstract TestCase newTestCaseInstance(String testCaseName);
}
