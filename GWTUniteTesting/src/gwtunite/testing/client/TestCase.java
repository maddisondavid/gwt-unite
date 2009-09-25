package gwtunite.testing.client;


public abstract class TestCase {

	public void setUp() throws Exception{
		
	}
	
	public void tearDown() throws Exception {
		
	}
	
	protected void assertTrue(boolean condition) {
		if (!condition)
			throw new FailureException();
	}
	
	protected void assertFalse(boolean condition) {
		if (condition)
			throw new FailureException();
	}

}
