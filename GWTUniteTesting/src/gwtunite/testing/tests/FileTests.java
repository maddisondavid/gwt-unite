package gwtunite.testing.tests;

import opera.io.Utils;
import gwtunite.testing.framework.Test;
import gwtunite.testing.framework.TestCase;

public class FileTests extends TestCase{

	public void setUp() throws Exception {
		Utils.log("This is the setup method");
	}
	
	@Test
	public void anExampleTest() throws Exception {
		Utils.log("This is a simple Test");
	}
	
	@Test
	public void aTestThatShouldAlwaysFail() throws Exception {
		assertTrue(false,"This should fail all the time");
	}

	@Test
	public void aTestThatShouldPass() throws Exception {
		assertTrue(true);
	}
	
	public void tearDown() throws Exception {
		Utils.log("This is the teardown method");
	}
}
