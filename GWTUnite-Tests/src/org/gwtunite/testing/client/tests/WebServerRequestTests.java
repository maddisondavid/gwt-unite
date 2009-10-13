package org.gwtunite.testing.client.tests;

import java.util.HashMap;
import java.util.Map;

import org.gwtunite.client.net.WebServerEventHandler;
import org.gwtunite.client.net.WebServerRequest;
import org.gwtunite.client.net.WebServerResponse;
import org.gwtunite.testing.client.framework.Test;
import org.gwtunite.testing.client.framework.TestCase;
import org.gwtunite.testing.client.tests.fixture.WebServerTestFixture;

public class WebServerRequestTests extends TestCase {
	WebServerTestFixture fixture;
	
	public void setUp() throws Exception {
		super.setUp();
		fixture = new WebServerTestFixture(this);
		fixture.setUp();
	}
	
	public void tearDown() throws Exception {
		super.tearDown();
		fixture.tearDown();
	}
	
	@Test
	public void requestAttributesSet() throws Exception {
		String handler = "attributeTest";
		fixture.addTestHandler(handler, new WebServerEventHandler() {
			@Override
			public void onConnection(WebServerRequest request, WebServerResponse response) {
				assertNotNull(request.getHost());
				assertEquals("GET",request.getMethod());
				assertEquals("http",request.getProtocol());
				assertNotNull(request.getRemoteIP());
				assertNotNull(request.getUri());
			}
		});
		
		delayTestFinish(500);
		fixture.invokeTestURLViaGet(handler);
	}
	
	@Test
	public void headersAreRecieved() throws Exception {
		fixture.addTestHandler("headerTest", new WebServerEventHandler() {
			@Override
			public void onConnection(WebServerRequest request, WebServerResponse response) {
				assertTrue(request.hasHeader("Host"));
				assertTrue(request.getHeaderNames().length > 0);
				assertNotNull(request.getHeader("Host"),"Host null");
				assertEquals(1,request.getHeader("Host").length,"Testing Host Header");
			}
		});
		
		delayTestFinish(500);
		fixture.invokeTestURLViaGet("headerTest");
	}
	
	@Test
	public void queryStringParamsAreRecieved() throws Exception {
		final Map<String, String[]> testParameters = new HashMap<String, String[]>();
		testParameters.put("param1",new String[]{"one","two","three"});
		testParameters.put("param2",new String[]{"one+two"});
		
		String handler = "queryParamsTest";
		fixture.addTestHandler(handler, new WebServerEventHandler() {
			@Override
			public void onConnection(WebServerRequest request, WebServerResponse response) {
				assertEquals(testParameters.keySet().size(),request.getQueryItemNames().length);
				for (String name : request.getQueryItemNames()) {
					assertTrue(request.hasQueryItem(name));
					assertTrue(testParameters.containsKey(name),"["+name+"]");
					assertNotNull(request.getQueryItem(name),"["+name+"]");
					assertEquals(testParameters.get(name).length, request.getQueryItem(name).length,"["+name+"]");
				}
			}
		});
		
		StringBuilder queryString = new StringBuilder();
		for (Map.Entry<String, String[]> entry : testParameters.entrySet()) {
			for (String value : entry.getValue()) {
				queryString.append(entry.getKey()+"="+value+"&");
			}
		}
		
		delayTestFinish(500);
		fixture.invokeTestURLViaGet(handler+"?"+queryString.toString());
	}
	
	@Test 
	public void bodyTextRecieved()  throws Exception {
		final String handler = "bodyTest";
		final String bodyText = "Hello, I'm some text!";
		
		fixture.addTestHandler(handler, new WebServerEventHandler() {
			@Override
			public void onConnection(WebServerRequest request, WebServerResponse response) {
				assertEquals(bodyText, request.getBodyAsText());
			}
		});
		
		delayTestFinish(2000);
		fixture.invokeTestURLViaPost(handler, bodyText);
	}
	
	@Test
	public void bodyParamsAreRecieved() throws Exception {
		final Map<String, String[]> testParameters = new HashMap<String, String[]>();
		testParameters.put("param1",new String[]{"one","two","three"});
		testParameters.put("param2",new String[]{"one+two"});
		
		String handler = "bodyParamsTest";
		fixture.addTestHandler(handler, new WebServerEventHandler() {
			@Override
			public void onConnection(WebServerRequest request, WebServerResponse response) {
				assertEquals(testParameters.keySet().size(),request.getBodyItemNames().length, "["+request.getBodyItemNames()+"]");
				for (String name : request.getBodyItemNames()) {
					assertTrue(request.hasBodyItem(name));
					assertTrue(testParameters.containsKey(name),"["+name+"]");
					assertNotNull(request.getBodyItem(name),"["+name+"]");
					assertEquals(testParameters.get(name).length, request.getBodyItem(name).length,"["+name+"]");
				}
			}
		});
		
		StringBuilder bodyData = new StringBuilder();
		for (Map.Entry<String, String[]> entry : testParameters.entrySet()) {
			for (String value : entry.getValue()) {
				bodyData.append(entry.getKey()+"="+value+"&");
			}
		}
		
		delayTestFinish(5000);
		fixture.invokeTestURLViaPost(handler,bodyData.toString());
	}
	
	@Test
	public void sessionIsAvailable() throws Exception {
		String handler = "sessionTest";
		fixture.addTestHandler(handler, new WebServerEventHandler() {
			@Override
			public void onConnection(WebServerRequest request, WebServerResponse response) {
				assertNotNull(request.getSession());
				assertNotNull(request.getSession().getId(),"ID");
				assertNotNull(request.getSession().getType(),"Type");
				assertNotNull(request.getSession().getUserName(),"UserName");
				
				request.getSession().logout();
			}
		});
		
		delayTestFinish(5000);
		fixture.invokeTestURLViaGet(handler);
	}	
}
