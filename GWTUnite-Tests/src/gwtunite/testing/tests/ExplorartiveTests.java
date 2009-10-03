package gwtunite.testing.tests;

import gwtunite.testing.framework.Test;
import gwtunite.testing.framework.TestCase;

import java.util.Map;

public class ExplorartiveTests extends TestCase {

	enum Enum {
		TEST,
		TEST2
	}
	
	enum EnumInt {
		ONE(1),
		TWO(8);
		
		int value;
		private EnumInt(int value) {
			this.value = value;
		}
		public int getValue() {
			return value;
		}
	}
	
	public static class MyException extends Exception {
		public MyException(String msg) {
			super(msg);
		}
	}
	
	@Test
	public void testThrowException() {
		try {
			throwIt();
			fail("Expected an exception to be thrown");
		} catch(MyException e) {
			// Test Passed;
		} catch(Exception e) {
			fail("Unexpected Exception "+e.getMessage() +" type :"+e.getClass().getName());
		}
	}
	
	private native void throwIt() throws MyException /*-{
		throw(@gwtunite.testing.tests.ExplorartiveTests.MyException::new(Ljava/lang/String;)("Hello!")); 
	}-*/;
	
	@Test
	public void testEnum() {
		assertEquals(Enum.TEST,testIt(Enum.TEST));
	}
	
	@Test
	public void testIntEnum() {
		assertEquals(EnumInt.ONE.getValue() | EnumInt.TWO.getValue(),testIt2(EnumInt.ONE, EnumInt.TWO));
	}

	@Test
	public void testMap () {
		assertEquals(2,getItems().size());
		assertTrue(getItems().containsKey("fred"));
		assertEquals("pants",getItems().get("fred"));
	}
	
	private native Map<String, String> getItems() /*-{
		var testThing = new Object();
		testThing.hello = "key";
		testThing.fred = "pants"

		var map = @java.util.HashMap::new()();
		for(key in testThing) {
			map.@java.util.Map::put(Ljava/lang/Object;Ljava/lang/Object;)(key,testThing[key]);
			opera.postError(key);
		}
		
		return map;
	}-*/;
	
	private native int testIt2(EnumInt... en) /*-{
		var mode = 0;
		for (var f=0;f<en.length;f++)
			mode = mode | en[f].@gwtunite.testing.tests.ExplorartiveTests.EnumInt::getValue()();
		
		return mode;
	}-*/;

	
	private native String testIt(Enum en) /*-{
		return en;
	}-*/;
}
