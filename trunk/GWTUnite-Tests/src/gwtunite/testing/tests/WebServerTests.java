package gwtunite.testing.tests;

import gwtunite.testing.framework.Test;
import gwtunite.testing.framework.TestCase;
import opera.io.Utils;
import opera.io.WebServer;
import opera.io.WebServerEventHandler;
import opera.io.WebServerRequest;
import opera.io.WebServerResponse;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;

public class WebServerTests extends TestCase {

	WebServerEventHandler handler = new WebServerEventHandler() {
		public void onConnection(WebServerRequest request, WebServerResponse response) {
			Utils.log("HERE : "+response);
			response.write("Hello");
			response.close();
//			finishTest();
		}
	};
	
	public void setUp() {
		Utils.log("Setup Called");
	}
	
	public void tearDown() {
		WebServer.getInstance().removeEventListener("webservertest", handler, false);
		Utils.log("teardown Called");
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
					Utils.log("Response REcieved");
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
			Utils.log("I've BEen CaLled!");
			if (response != null)
				response.close();
		}
	}
}
