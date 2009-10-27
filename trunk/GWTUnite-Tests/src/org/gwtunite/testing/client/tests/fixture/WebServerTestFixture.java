package org.gwtunite.testing.client.tests.fixture;

import java.util.HashMap;
import java.util.Map;

import org.gwtunite.client.commons.Logging;
import org.gwtunite.client.net.WebServer;
import org.gwtunite.client.net.WebServerEventHandler;
import org.gwtunite.client.net.WebServerRequest;
import org.gwtunite.client.net.WebServerResponse;
import org.gwtunite.testing.client.framework.TestCase;
import org.gwtunite.testing.client.framework.TestCase.AssertionFailureException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;

public class WebServerTestFixture {
	private static final String PASS_RESULT = "PASS";
	private static final String ASSERTION_FAILED_RESULT = "ASSERTION_FAILED";
	private static final String ERROR_RESULT = "ERROR";
	private static final String RESULT_HEADER = "RESULT";
	
	private final Map<String, WebServerEventHandler> handlers = new HashMap<String, WebServerEventHandler>();
	private final TestCase testCase;
	
	private String serverURL;
	private WebServer webServer;

	public WebServerTestFixture(TestCase testCase) {
		this.testCase = testCase;
	}
	
	public void setUp() {
		webServer = WebServer.getInstance();
		serverURL =  GWT.getModuleBaseURL();
	}
	
	public void invokeTestURLViaGet(final String localURL) throws Exception {
		RequestBuilder requestBuilder = getLocalRequestBuilder(RequestBuilder.GET, localURL);
	
		requestBuilder.setCallback(new TestHttpHandler());
		Logging.log("Calling "+requestBuilder.getUrl());
		requestBuilder.send();
	}
	
	public void invokeTestURLViaPost(String localURL, String requestData) throws Exception {
		RequestBuilder requestBuilder = getLocalRequestBuilder(RequestBuilder.POST, localURL);
		requestBuilder.setHeader("Content-Type","application/x-www-form-urlencoded");
		requestBuilder.setRequestData(requestData);
		
		requestBuilder.setCallback(new TestHttpHandler());
		
		requestBuilder.setTimeoutMillis(3000);
		requestBuilder.send();
	}	
	
	public RequestBuilder getLocalRequestBuilder(RequestBuilder.Method method, String localURL) {
		return new RequestBuilder(method, serverURL+localURL);
	}
	
	public void addTestHandler(String pathFragment, final WebServerEventHandler handler) {
		// The handler which will collection and relay the results of the tests
		WebServerEventHandler wrappingHandler = new WebServerEventHandler() {
			public void onConnection(WebServerRequest request, WebServerResponse response) {
				try {
					handler.onConnection(request, response);
					response.setHeader(RESULT_HEADER, PASS_RESULT);
				} catch(Exception e) {
					if (e instanceof AssertionFailureException) {
						response.setHeader(RESULT_HEADER, ASSERTION_FAILED_RESULT);
						response.write(e.getMessage());
					} else {
						response.setHeader(RESULT_HEADER, ERROR_RESULT);
						response.write(e.getMessage());
					}
				} finally {
					if (response != null)
						response.close();
				}
			}
		};
		
		handlers.put(pathFragment, wrappingHandler);
		webServer.addEventListener(pathFragment, wrappingHandler, false);
	}
	
	public void addHandler(String pathFragment, final WebServerEventHandler handler) {
		handlers.put(pathFragment, handler);
		webServer.addEventListener(pathFragment, handler, false);
	}
	
	public void removeHandler(String pathFragment) {
		webServer.removeEventListener(pathFragment, handlers.get(pathFragment), false);
		handlers.remove(pathFragment);
	}

	public WebServer getWebServer() {
		return webServer;
	}
	
	public void tearDown() {
		for (Map.Entry<String, WebServerEventHandler> entry : handlers.entrySet()) {
			webServer.removeEventListener(entry.getKey(), entry.getValue(), false);
		}
		
		handlers.clear();
	}
	
	/**
	 * HTTP Handler to interpret the results from a test handler
	 */
	public class TestHttpHandler implements RequestCallback{
		@Override public void onError(Request request, Throwable exception) {
			testCase.handleException(exception);
		}
		@Override public void onResponseReceived(Request request, Response response) {
			String result = response.getHeader(RESULT_HEADER);
			
			if (result.equals(PASS_RESULT)) {
				testCase.finishTest();
			} else if (result.equals(ERROR_RESULT)) {
				testCase.handleException(new Exception(response.getText()));
			} else if (result.equals(ASSERTION_FAILED_RESULT)) {
				testCase.handleException(new TestCase.AssertionFailureException(response.getText()));
			} else {
				testCase.handleException(new Exception("Unknown result back  ("+result+") status:"+response.getStatusCode()));
			}
		}
	}
}
