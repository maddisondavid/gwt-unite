package gwtunite.testing.client.tests;

import opera.io.Utils;
import gwtunite.testing.client.TestCase;

public class FileTests extends TestCase{

	public void setUp() throws Exception {
		Utils.log("This is the setup method");
	}
	
	public void test_ExampleTest() throws Exception {
		Utils.log("This is a simple Test");
	}
	
	public void testThatShouldFail() throws Exception {
		assertTrue(false);
	}
	
	public void tearDown() throws Exception {
		Utils.log("This is the teardown method");
	}
}
