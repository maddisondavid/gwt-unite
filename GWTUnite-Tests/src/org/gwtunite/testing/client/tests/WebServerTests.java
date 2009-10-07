package org.gwtunite.testing.client.tests;

import org.gwtunite.client.commons.Logging;
import org.gwtunite.client.net.WebServer;
import org.gwtunite.client.net.WebServerEventHandler;
import org.gwtunite.client.net.WebServerRequest;
import org.gwtunite.client.net.WebServerResponse;
import org.gwtunite.testing.client.framework.Test;
import org.gwtunite.testing.client.framework.TestCase;


import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;

public class WebServerTests extends TestCase {

	WebServerEventHandler handler = new WebServerEventHandler() {
		public void onConnection(WebServerRequest request, WebServerResponse response) {
			Logging.log("HERE : "+response);
			response.write("Hello");
			response.close();
//			finishTest();
		}
	};
	
	public void setUp() {
		Logging.log("Setup Called");
	}
	
	public void tearDown() {
		WebServer.getInstance().removeEventListener("webservertest", handler, false);
		Logging.log("teardown Called");
	}
	
	@Test
	public void webServerAttributesAreCorrect() throws Exception {
		WebServer webserver = WebServer.getInstance();
		
		assertNotNull(webserver.getCurrentServiceName(),"CurrentServiceName");
		assertNotNull(webserver.getCurrentServicePath(),"CurrentServicePath");
		assertNotNull(webserver.getDeviceName(),"DeviceName");
		assertNotNull(webserver.getHostName(),"HostName");		
		assertNotNull(webserver.getOriginURL(),"OriginURL");
		assertNotNull(webserver.getPort(),"Port");
		assertNotNull(webserver.getProxyName(),"ProxyName");
		assertNotNull(webserver.getPublicPort(),"PublicPort");
		assertNotNull(webserver.getUserName(), "UserName");
//		assertNotNull(webserver.getPublicIP(),"PublicIP");
		
		assertTrue(webserver.getServices().length != 0);
	}
	
	@Test
	public void listenersRegisterCorrectly() throws Exception {
		WebServer webServer = WebServer.getInstance();
		
		final TestHandler testHandler = new TestHandler();

		webServer.addEventListener("webservertest", testHandler, false);

		
		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,"http://"+webServer.getHostName()+"/"+webServer.getCurrentServicePath()+"webservertest");
		builder.setCallback(new RequestCallback() {
			@Override
			public void onError(Request arg0, Throwable arg1) {
				handleException((Exception)arg1);
			}

			@Override
			public void onResponseReceived(Request arg0, Response arg1) {
				try {
					Logging.log("Response REcieved");
					assertEquals(200, arg1.getStatusCode());
					finishTest();
				}catch(Exception e) {
					handleException(e);
				}
			}
		});
		
		delayTestFinish(2000);
		builder.send();
	}
	
	private class TestHandler extends WebServerEventHandler {
		@Override
		protected void onConnection(WebServerRequest request, WebServerResponse response) {
			Logging.log("I've BEen CaLled!");
			if (response != null)
				response.close();
		}
	}
}
