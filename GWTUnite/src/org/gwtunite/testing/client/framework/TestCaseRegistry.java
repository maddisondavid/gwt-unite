package org.gwtunite.testing.client.framework;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The base class that knows about all test.  This is built by deferred binding 
 *
 */
public abstract class TestCaseRegistry {
	private Map<String, TestCaseInfo> testCaseMap;
	private List<TestCaseInfo> testCaseInfos; // Keeps an ordered list of the testCases
	
	public TestCaseRegistry() {
		testCaseMap = new HashMap<String, TestCaseInfo>();
		testCaseInfos = new ArrayList<TestCaseInfo>();
	}
	
	protected void registerInfo(TestCaseInfo testCaseInfo) {
		testCaseMap.put(testCaseInfo.getName(), testCaseInfo);
		testCaseInfos.add(testCaseInfo);
	}
	
	public TestCaseInfo getTestCaseInfo(String testCaseName) {
		if (!testCaseMap.containsKey(testCaseName))
			throw new RuntimeException("Test Case "+testCaseName+" not found");
		
		return testCaseMap.get(testCaseName);
	}
	
	public Collection<TestCaseInfo> getAllTestCaseInfo() {
		return testCaseInfos;
	}
	public abstract TestCase newTestCaseInstance(String testCaseName);
}
