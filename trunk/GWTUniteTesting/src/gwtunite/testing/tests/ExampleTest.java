package gwtunite.testing.tests;

import opera.io.Utils;
import gwtunite.testing.framework.Test;
import gwtunite.testing.framework.TestCase;

public class ExampleTest extends TestCase {

	public void testMe() {
		Utils.log("I'm an Example test");
		assertTrue(false, "This assertion is false");
	}
	
	@Test
	public void thisTestShouldFail() {
		throw new NullPointerException("I've Failed");
	}
}
