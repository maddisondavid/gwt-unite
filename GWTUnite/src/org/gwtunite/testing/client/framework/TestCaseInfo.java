package org.gwtunite.testing.client.framework;

/** 
 * Provides information about a particular test case.  
 */
public interface TestCaseInfo {

	public String getName();
	public String getClassName();
	public String[] getTests();
}
