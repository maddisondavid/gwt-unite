package gwtunite.testing.framework;

import gwtunite.testing.framework.TestCaseExecutor.TestCompleteHandler;

import java.util.Map;

import opera.io.OperaUniteService;
import opera.io.Utils;
import opera.io.WebServer;
import opera.io.WebServerEventHandler;
import opera.io.WebServerRequest;
import opera.io.WebServerResponse;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;

/**
 * The main Opera Unite Service for the Test Bench
 */
public class TestBenchService extends OperaUniteService {
	TestCaseRegistry testReg = GWT.create(TestCaseRegistry.class);
	
	@Override
	public void init(WebServer webServer) {
		// Handler to redirect to the TestBench HTML page
		webServer.addEventListener(WebServer.INDEX_PATH, new WebServerEventHandler() {
			@Override
			protected void onConnection(WebServerRequest request, WebServerResponse response) {
				response.setStatusCode(302);
				response.setResponseHeader("Location", "TestBench.html");
				response.close();
			}
		}, false);

		// Handler to list out all the tests
		webServer.addEventListener("testList", new WebServerEventHandler() {
			@Override
			protected void onConnection(WebServerRequest request, WebServerResponse response) {
				response.writeLine("<html><link rel=\"stylesheet\" type=\"text/css\" href=\"Testing.css\"/><body><h2>Available Tests</h2>");
				for (TestCaseInfo testCaseInfo : testReg.getTestCaseInfos()) {
					response.writeLine("<img src=\"images/WhiteArrow.png\">&nbsp;<img src=\"images/runTests.png\">&nbsp;<a target=\"testResults\" href=\"runTests?testCase="+testCaseInfo.getName()+"\">"+testCaseInfo.getName()+"</a>");
					response.writeLine("<ul>");
					for (String testName : testCaseInfo.getTests()) {
						response.writeLine("<li><img src=\"images/runTest.png\">&nbsp;<a target=\"testResults\" href=\"runTests?testCase="+testCaseInfo.getName()+"&test="+testName+"\"/>"+testName+"</a></li>");
					}
					
					response.writeLine("</ul>");
				}
					
				response.writeLine("</body> </html>");
				response.close();
			}
		},false);
		
		// Handler to run specific tests
		webServer.addEventListener("runTests", new WebServerEventHandler() {
			@Override
			protected void onConnection(final WebServerRequest request, final WebServerResponse response) {
				final String testCaseName = request.getQueryItem("testCase")[0];
				String testName = null;
				if (request.getQueryItem("test") != null) {
					testName = request.getQueryItem("test")[0];
				}
			 
				final TestCompleteHandler testCompleteHandler = new TestCompleteHandler() {
				@Override public void onTestComplete(final TestCaseExecutor testExecutor) {
						response.writeLine("<html> <link rel=\"stylesheet\" type=\"text/css\" href=\"Testing.css\"/> <body> <h2>Test Results</h2>");
						response.write("<h3 class=\""+testExecutor.getOveralResult()+"\">"+testCaseName+"</h3>");
						response.writeLine("<table style=\"margin-left:30px\">");
						
						for (Map.Entry<String, TestResult> result : testExecutor.getResults().entrySet()) {
							switch(result.getValue().getResult()) {
								case PASSED:
									response.writeLine("<tr> <td><img src=\"images/TestPass.png\"/></td><td>"+result.getValue().getTestName()+"</td> </tr>");
									break;
								case ERROR:
									response.writeLine("<tr> <td><img src=\"images/TestError.png\"/></td><td>"+result.getValue().getTestName()+"</td> </tr>");
									response.writeLine("<tr> <td>&nbsp;</td><td>"+result.getValue().getException()+"</td> </tr>");
									break;
								case FAILED:
									response.writeLine("<tr> <td><img src=\"images/TestFailed.png\"/></td><td>"+result.getValue().getTestName()+"</td> </tr>");
									response.writeLine("<tr> <td>&nbsp;</td><td>"+result.getValue().getException()+"</td> </tr>");
									break;
							}
						}
						response.writeLine("</table>");		
						response.write("</body></html>");
						response.close();
					}
				};
				
				TestCaseExecutor testExecutor = testReg.newExecutorInstance(testCaseName);
				try {
					if (testName == null) {
						testExecutor.executeAllTests(testCompleteHandler);
					} else {
						testExecutor.executeTest(testName, testCompleteHandler);
					}
				} catch(Exception e) {
					response.writeLine("Unhandled Test Exception :"+ e);
					response.close();
				}
				
			}
		}, false);
	}
}