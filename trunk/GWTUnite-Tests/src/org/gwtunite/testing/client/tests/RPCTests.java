package org.gwtunite.testing.client.tests;

import java.util.ArrayList;
import java.util.HashMap;

import org.gwtunite.testing.client.framework.Test;
import org.gwtunite.testing.client.framework.TestCase;
import org.gwtunite.testing.client.tests.fixture.RPCEchoFixture;
import org.gwtunite.testing.client.tests.rpc.Person;

public class RPCTests extends TestCase {
	private RPCEchoFixture echoFixture;
	
	@Override
	public void setUp() {
		echoFixture = new RPCEchoFixture(this);
	}
	
	@Test public void testInt() {
		echoFixture.assertEquals(12);
	}

	@Test public void testString() {
		echoFixture.assertEquals("Hello");
	}

	@Test public void testDouble() {
		echoFixture.assertEquals(10d);
	}

	@Test public void testBoolean() {
		Boolean b = true;
		echoFixture.assertEquals(b);
	}
	
	@Test public void testLong() {
		echoFixture.assertEquals(10l);
	}
	
	@Test public void testIntArray() {
		Integer[] toSend = new Integer[]{12,13,14};
		echoFixture.assertArrayEquals(toSend);
	}
	
	@Test public void testStringArray() {
		String[] toSend = new String[]{"Hello","Fred"};
		echoFixture.assertArrayEquals(toSend);
	}
	
	@Test public void testObjects() {
		Person person = new Person("mr","smith");
		echoFixture.assertEquals(person);
	}
	
	@Test public void testObjectArrays() {
		Person[] personArray = new Person[]{new Person("Mr","Smith"),
											new Person("Mrs","Fred")};
		echoFixture.assertArrayEquals(personArray);
	}

	@Test public void testList() {
		ArrayList<Person> list = new ArrayList<Person>();
		list.add(new Person("Mr","Smith"));
		list.add(new Person("Mrs","Fred"));
		echoFixture.assertEquals(list);
	}
	
	@Test public void testMap() {
		HashMap<String, Person> map = new HashMap<String, Person>();
		map.put("Hello", new Person("Mr","Smith"));
		map.put("Hello2", new Person("Mrs","Smith"));
		echoFixture.assertEquals(map);
	}
}
