## Testing ##
Testing is obviously important no matter what application your building, however with GWT-Unite it's even more important because we need to make sure that subsequent Opera Unite updates don't break our libraries.

To resolve this issue, GWT-Unite includes a sophisticated JUnit style testing framework call the TestBench.  The following explains how to write tests for this framework and how to run them.

## Writing a Simple Test ##
The Test Bench uses a similar style to JUnit which should be familar to most Java developers.  The following shows a very simple (trivial) test class:

```
public MyTestClass extends TestCase {
	String testFixture;

	@Override
	public void setUp() {
		testFixture = "Hello";
	}


	@Test
	public void helloWorld() throws Exception {
		assertEquals("Hello",testFixture);
	}

	@Override
	public void tearDown() {
		testFixture = null;
	}
}
```

As you can see, all test classes must extend _org.gwtunite.test.client.framework.TestCase_.  Methods in the class can be marked as tests by using the @Test annotation, and the test class can also include the optional setUp and tearDown methods.  A test class can include as many test methods as required, setUp will be called just before a test method is called, and tearDown called after the method finishes (regardless of the result).

## Assertions ##
The TestCase class includes all the common assertions found in standard JUnit, which are :

  * assertTrue
  * assertFalse
  * assertEquals
  * assertNotNull
  * fail

When using standard JUnit, an assertion will throw an exception and the stack trace will point to the Assertion that failed.  Unfortunately, when a GWT application, such as the Test Bench, is running within GWT, Java style stack traces aren't provided.  In order to identify the assertion that failed, each assertion method also includes a version which takes a message, allowing you to identify the assertion that failed, for example:

```

@Test
public void simpleTest() throws Exception {
	assertEquals("hello",myObject.getGreeting(), "test Greeting");
}
```

When this assertion fails, it will display a result as "Expected: Hello Actual:Goodbye test Greeting" which will allow you to identify the particular failing assertion.

## Compiling the TestBench ##
The TestBench is a standard GWT-Unite application, and therefore the TemplateApplication can be used as a base for tests.  The difference is that the application's module does not include an entry point, and the GWT module definition must inherit the TestBench module, i.e.:

```
<module>
	<inherits name="org.gwtunite.testing.TestBench"/>
</module>
```

Test classes must be available to GWT and therefore are placed in the standard client source package.  The application is then compiled in exactly the same manor as any normal GWT-Unite application, the output of which will be a .ua file.

## Running Tests ##
Once the application archive has been installed into OperaUnite (by dragging it over the Opera browser), your presented with two panels, one showing all your test classes.  To execute a test, either click on an actual test class, which will run all the tests in that class, or on a particular test to run just one test.  The output of the tests will be shown in the right hand panel.

## Asynchronous Tests ##
When writing simple tests, the code is executed sequentially, however it's not long before you actually want to test something and wait for a result.  Unfortunately JavaScript is single threaded and waiting for a result is not possible.

To get around this issue, the TestBench actually includes a sophisticated asynchronous model allowing the test case to actually finish, but the test to wait for a result.  Possibly the best way to explain this is with the following example in which we want to call a URL and assert the result:

```
public class MyTest extends TestCase {

	public void testURL() throws Exception {

		RequestBuilder req = new RequestBuilder(RequestBuilder.GET, "http://www.google.com");
		req.setCallback(new RequestCallback() {

			public void onError(Request request, Throwable exception) {
				handleException(exception);
			}

			public void onResponseReceived(Request request, Response response) {
				try{ // <<<<< 3
					assertEquals(Response.SC_OK, response.getStatusCode());

					finishTest(); //<<<<< 2
				} catch(Throwable e) {
					handleException(e);
				}										
			}
		});

		delayTestFinish(3000); //<<<<< 1
		req.send();  
	}
}
```

Under normal circumstances, this test would complete when the testURL method ends and a result returned, however, we actually want the test result to be delayed until we have a HTTP response back.  To accomplish this, we use the _delayTestFinish_ method, highlighted with the <<1 marker.  When this statement exectutes, a timeout timer is started, if the test hasn't completed by the time the timer expires, a "Timeout" failure occurs for the test.

A test is deemed finished when either :

  * An asserion fails
  * An Exception is thrown
  * finishTest is called.

At any of these points, the result is returned for the test.

## Catching Exceptions ##
One VERY important point to note about asynchronous tests it the try catch block marked with the << 3 marker.  When a test is executed synchronously, the TestBench automatically wraps the test in a try catch block and therefore is able to report on any failures, when running asynchronously this isn't available and therefore you must catch and then inform the TestCase of any failures.

It's important to include the try catch block EVEN if your code should thrown any exceptions, simply because AssertionFailures themselves are thrown as Exceptions.