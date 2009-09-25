package gwtunite.testing.client;

import java.util.Map;

import opera.io.OperaUniteService;
import opera.io.Utils;
import opera.io.WebServer;
import opera.io.WebServerEventHandler;
import opera.io.WebServerRequest;
import opera.io.WebServerResponse;

/**
 * This is the main service for testing all the relevant GWTUnite API's
 */
public class TestService extends OperaUniteService {
	TestCaseExecutorRegistry testReg = new GeneratedTestCaseRegistry();
	
	@Override
	public void init(WebServer webServer) {
		webServer.addEventListener(WebServer.INDEX_PATH, new WebServerEventHandler() {
			@Override
			protected void onConnection(WebServerRequest request, WebServerResponse response) {
				response.write("<html><head><title>GWT-Unite Tests</title></head><body>");

				response.write("<ul>");
				for (TestCaseExecutor testCase : testReg.getExecutors()) {
					response.write("<li><a href=\"runTest?testCase="+testCase.getTestCaseName()+"\">"+testCase.getTestCaseName()+"</a>");
					
					response.write("<ul>");
					for (String test : testCase.getTestNames()) {
						response.write("<li><a href=\"runTest?testCase="+testCase.getTestCaseName()+"&test="+test+"\">"+test+"</li>");
					}	
					response.write("</ul>");
					response.write("</li>");
				}
				
				response.write("</ul>");
				
				response.write("</body></html>");
				response.close();
			}
		}, false);
		
		webServer.addEventListener("runTest", new WebServerEventHandler() {

			@Override
			protected void onConnection(WebServerRequest request, WebServerResponse response) {
				String testCaseName = request.getQueryItem("testCase")[0];
				String testName = null;
				if (request.getQueryItem("test") != null) {
					testName = request.getQueryItem("test")[0];
				}
				
				try {
					Map<String, TestResult> testResults = null;
					if (testName == null) {
						testResults = testReg.runTestCase(testCaseName);
					} else {
						testResults = testReg.runTest(testCaseName, testName);
					}
					for (Map.Entry<String, TestResult> result : testResults.entrySet()) {
						response.writeLine(result.getKey()+"="+result.getValue().getResult()+"</br>");
						if (result.getValue().getResult() == TestResult.Result.FAILED) {
							response.write(result.getValue().getException() + "<br/>");
						}
					}
				} catch(Exception e) {
					response.write("Unhandled Exception : "+e);
				} finally {
					response.close();
				}
			}
		}, false);
	}
}
