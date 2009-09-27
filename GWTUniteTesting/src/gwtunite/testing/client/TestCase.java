package gwtunite.testing.client;



public abstract class TestCase {
	public void setUp() throws Exception{
		
	}
	
	public void tearDown() throws Exception {
		
	}
	
	protected void assertTrue(boolean condition) {
		if (!condition)
			throw new AssertionFailureException();
	}

	protected void assertTrue(boolean condition, String message) {
		if (!condition)
			throw new AssertionFailureException(message);
	}
	
	protected void assertFalse(boolean condition) {
		if (condition)
			throw new AssertionFailureException();
	}

	protected void assertFalse(boolean condition, String message) {
		if (condition)
			throw new AssertionFailureException();
	}

}
