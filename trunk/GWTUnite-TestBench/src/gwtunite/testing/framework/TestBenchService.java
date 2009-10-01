package gwtunite.testing.framework;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import opera.io.OperaUniteService;
import opera.io.WebServer;
import opera.io.WebServerEventHandler;
import opera.io.WebServerRequest;
import opera.io.WebServerResponse;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;

/**
 * The main Opera Unite Service for the Test Bench
 */
public class TestBenchService extends OperaUniteService {
	private static final String TEST_CASE_QUERY_ITEM = "testCase";
	private static final String TEST_NAME_QUERY_ITEM = "testName";
	private static final String TEST_LIST_CONTEXT = "testList";
	private static final String RUN_TESTS_CONTEXT = "runTests";
	
	private static final TestCaseRegistry testCaseRegistry = GWT.create(TestCaseRegistry.class);
	
	@Override
	public void init(WebServer webServer) {
		/**
		 * INDEX handler to redirect the browser to our main TestBench HTML
		 */
		webServer.addEventListener(WebServer.INDEX_PATH, new WebServerEventHandler() {
			@Override
			protected void onConnection(WebServerRequest request, WebServerResponse response) {
				response.setStatusCode(Response.SC_MOVED_TEMPORARILY);
				response.setResponseHeader("Location", "TestBench.html");
				response.close();
			}
		}, false);

		/**
		 * Handler to display a list of all the available tests
		 */
		webServer.addEventListener(TEST_LIST_CONTEXT, new WebServerEventHandler() {
			@Override
			protected void onConnection(WebServerRequest request, WebServerResponse response) {
				response.writeLine("<html><link rel=\"stylesheet\" type=\"text/css\" href=\"Testing.css\"/><body><image src=\"images/Banner.png\"/>");
				for (TestCaseInfo testCaseInfo : testCaseRegistry.getAllTestCaseInfo()) {
					response.writeLine("<img src=\"images/WhiteArrow.png\">&nbsp;<img src=\"images/runTests.png\">&nbsp;<a target=\"testResults\" href=\""+RUN_TESTS_CONTEXT+"?"+TEST_CASE_QUERY_ITEM+"="+testCaseInfo.getName()+"\">"+testCaseInfo.getName()+"</a>");
					response.writeLine("<ul>");
					for (String testName : testCaseInfo.getTests()) {
						response.writeLine("<li><img src=\"images/runTest.png\">&nbsp;<a target=\"testResults\" href=\""+RUN_TESTS_CONTEXT+"?"+TEST_CASE_QUERY_ITEM+"="+testCaseInfo.getName()+"&"+TEST_NAME_QUERY_ITEM+"="+testName+"\"/>"+testName+"</a></li>");
					}
					
					response.writeLine("</ul>");
				}
					
				response.writeLine("</body> </html>");
				response.close();
			}
		},false);

		
		/**
		 * Handler to run a specific test
		 */
		webServer.addEventListener(RUN_TESTS_CONTEXT, new WebServerEventHandler(){
			@Override protected void onConnection(WebServerRequest request, final WebServerResponse response) {
				final String testCaseName = request.getQueryItem(TEST_CASE_QUERY_ITEM)[0];
				if (testCaseName == null) {
					response.write(TEST_CASE_QUERY_ITEM+" must be supplied!");
					response.close();
					return;
				}
				
				String testName = null;
				if (request.getQueryItem(TEST_NAME_QUERY_ITEM) != null) {
					testName = request.getQueryItem(TEST_NAME_QUERY_ITEM)[0];
				}
				
				// Are we running one specific test, or multiple tests?
				String[] testsToRun = null;
				if(testName != null) {
					testsToRun = new String[]{testName};
				} else {
					testsToRun =  testCaseRegistry.getTestCaseInfo(testCaseName).getTests();
				}
				
				// Start the test(s)
				TestRunner testRunner = new TestRunner(testCaseName, testsToRun, response);
				testRunner.start();
			}
		}, false);
	}

	/** Writes the results out to the given WebServerResponse stream */
	private void writeResults(String testCaseName, Collection<TestResult> results, WebServerResponse response) {
		response.writeLine("<html> <link rel=\"stylesheet\" type=\"text/css\" href=\"Testing.css\"/> <body> <h2>Test Results</h2>");
		response.write("<h3 class=\"\">"+testCaseName+"</h3>");
		response.writeLine("<table style=\"margin-left:30px\">");
		
		for (TestResult result : results) {
			switch(result.getResult()) {
				case PASSED:
					response.writeLine("<tr> <td><img src=\"images/TestPass.png\"/></td><td>"+result.getTestName()+"</td> </tr>");
					break;
				case ERROR:
					response.writeLine("<tr> <td><img src=\"images/TestError.png\"/></td><td>"+result.getTestName()+"</td> </tr>");
					response.writeLine("<tr> <td>&nbsp;</td><td>"+result.getException()+"</td> </tr>");
					break;
				case FAILED:
					response.writeLine("<tr> <td><img src=\"images/TestFailed.png\"/></td><td>"+result.getTestName()+"</td> </tr>");
					response.writeLine("<tr> <td>&nbsp;</td><td>"+result.getFailureMessage()+"</td> </tr>");
					break;
			}
		}
		response.writeLine("</table>");		
		response.write("</body></html>");
		response.close();
	}
	
	/**
	 * The {@link TestRunner} is responsible for invoking each test in a test case.  
	 * 
	 * The runner waits asynchronously for a response before kicking off the next test.  Eventually it calls
	 * write results to display the actual results in the browser. 
	 */
	private class TestRunner implements TestResultListener {
		final TestResultListener _this = this;
		final WebServerResponse response;
		final String[] testsToRun;
		final String testCaseName;
		final List<TestResult> testResults = new ArrayList<TestResult>();

		final TestCase testCase;
		
		int currentTest = 0;
		
		public TestRunner(String testCaseName, String[] testsToRun, WebServerResponse response) {
			this.testCaseName = testCaseName;
			this.testsToRun = testsToRun;
			this.response = response;
			
			testCase = testCaseRegistry.newTestCaseInstance(testCaseName);
		}
		
		/** Should be called only once to start the tests running */
		public void start() {
			if (testsToRun.length > 0) {
				testCase.executeTest(testsToRun[currentTest], this);
			} else {
				startNextTestOrWriteResults();
			}
		}

		/** After a test has completed, this method should be invoked to either call the next test in the chain
		 * or write the results 
		 */
		void startNextTestOrWriteResults() {
			currentTest++;
			if (currentTest < testsToRun.length) {
				// Start the next test in a new thread (otherwise we could have a nasty big stack)
				DeferredCommand.addCommand(new Command() {
					public void execute() {
						testCase.executeTest(testsToRun[currentTest], _this);
					}
				});
			} else {
				writeResults(testCaseName, testResults, response);
			}
		}
		
		public void onError(Exception exception) {
			testResults.add(TestResult.error(testsToRun[currentTest], exception));
			startNextTestOrWriteResults();
		}

		public void onFailed(String message) {
			testResults.add(TestResult.failed(testsToRun[currentTest], message));
			startNextTestOrWriteResults();
		}

		public void onPass() {
			testResults.add(TestResult.pass(testsToRun[currentTest]));
			startNextTestOrWriteResults();
		}
	};
	
}