package org.gwtunite.testing.client.tests;

import org.gwtunite.client.commons.Logging;
import org.gwtunite.client.file.File;
import org.gwtunite.client.file.FileStream;
import org.gwtunite.client.file.FileSystem;
import org.gwtunite.client.file.File.FileMode;
import org.gwtunite.client.net.WebServer;
import org.gwtunite.client.net.WebServerEventHandler;
import org.gwtunite.client.net.WebServerRequest;
import org.gwtunite.client.net.WebServerResponse;
import org.gwtunite.testing.client.framework.Test;
import org.gwtunite.testing.client.framework.TestCase;
import org.gwtunite.testing.client.tests.fixture.WebServerTestFixture;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;

public class WebServerTests extends TestCase {
	private WebServerTestFixture fixture;

	public void setUp() throws Exception {
		fixture = new WebServerTestFixture(this);
		fixture.setUp();
		Logging.log("Setup Called");
	}
	
	public void tearDown() throws Exception {
		super.tearDown();
		fixture.tearDown();
		Logging.log("teardown Called");
	}
	
	@Test
	public void listenersRegisterUnregisteredCorrectly() throws Exception {
		final String handler = "webservertest";
		fixture.addHandler(handler, new WebServerEventHandler() {
			public void onConnection(WebServerRequest request, WebServerResponse response) {
				if (response != null)
					response.close();
			}
		});
		
		RequestBuilder builder = fixture.getLocalRequestBuilder(RequestBuilder.GET, handler);
		builder.setCallback(new RequestCallback() {
			@Override public void onError(Request request, Throwable exception) {
				handleException((Exception)exception);
			}

			@Override public void onResponseReceived(Request request, Response response) {
				try {
					assertEquals(Response.SC_OK, response.getStatusCode());
					
					// Now unregister it
					fixture.removeHandler(handler);

					// And test that it's now gone!
					RequestBuilder builder = fixture.getLocalRequestBuilder(RequestBuilder.GET, handler);
					builder.setCallback(new RequestCallback() {
						@Override public void onError(Request request, Throwable exception) {
							handleException((Exception)exception);
						}

						@Override public void onResponseReceived(Request request, Response response) {
							try {
								assertEquals(Response.SC_NOT_FOUND, response.getStatusCode());
								finishTest();
							} catch(Exception e) {
								handleException(e);
							}
						}
					});
					builder.send();
				} catch(Exception e) {
					handleException(e);
				}
			}
		});
		
		delayTestFinish(1000);
		builder.send();
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
	public void shareFileWorks() throws Exception {
		File sharedDir = FileSystem.getInstance().mountSharedFileSystem();
		
		// Write a shared file
		final File newFile = sharedDir.resolve("FileToShare.txt");
		FileStream fileStream = newFile.open(FileMode.WRITE);
		fileStream.write("Hello");
		fileStream.close();
		
		// Share it
		final WebServer webserver = fixture.getWebServer();
		final String shareName = "fileToShareTest";
		webserver.shareFile(newFile, shareName);
		delayTestFinish(1000);
		RequestBuilder builder = fixture.getLocalRequestBuilder(RequestBuilder.GET, shareName);
		builder.setCallback(new RequestCallback() {
			@Override public void onError(Request request, Throwable exception) {
				handleException((Exception)exception);
			}
			
			@Override public void onResponseReceived(Request request, Response response) {
				try {
					assertEquals(Response.SC_OK, response.getStatusCode());
					
					// Now Unshare it
					webserver.unshareFile(newFile);
				
					// And another request builder to test it's not there any more
					RequestBuilder builder = fixture.getLocalRequestBuilder(RequestBuilder.GET, shareName);
					builder.setCallback(new RequestCallback() {
						@Override public void onError(Request request, Throwable exception) {
							handleException((Exception)exception);
						}
						
						@Override public void onResponseReceived(Request request, Response response) {
							try {
								assertEquals(Response.SC_NOT_FOUND, response.getStatusCode());
								finishTest();
							} catch(Exception e) {
								handleException(e);
							}
						}
					});
					builder.send();
				} catch(Exception e) {
					handleException(e);
				}
			}
		});
		builder.send();
	}
}
