package org.gwtunite.testing.client.framework;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
		testCaseMap.put(testCaseInfo.getClassName(), testCaseInfo);
		testCaseInfos.add(testCaseInfo);
	}
	
	public TestCaseInfo getTestCaseInfo(String testCaseName) {
		if (!testCaseMap.containsKey(testCaseName))
			throw new RuntimeException("Test Case "+testCaseName+" not found");
		
		return testCaseMap.get(testCaseName);
	}
	
	public Collection<TestCaseInfo> getAllTestCaseInfo() {
		Collections.sort(testCaseInfos, new Comparator<TestCaseInfo>() {
			public int compare(TestCaseInfo testCase1, TestCaseInfo testCase2) {
				return testCase1.getName().compareTo(testCase2.getName());
			}
		});
		
		return testCaseInfos;
	}
	public abstract TestCase newTestCaseInstance(String testCaseClass);
}
