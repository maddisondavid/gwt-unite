package gwtunite.testing.client;

import gwtunite.testing.client.tests.FileTestsTestExecutor;

public class GeneratedTestCaseRegistry extends TestCaseExecutorRegistry {

	public GeneratedTestCaseRegistry() {
		registerTestExecutor(new FileTestsTestExecutor());
	}
}
