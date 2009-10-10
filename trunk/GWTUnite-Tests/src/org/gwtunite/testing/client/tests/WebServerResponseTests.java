package org.gwtunite.testing.client.tests;

import org.gwtunite.client.commons.Logging;
import org.gwtunite.client.file.ByteArray;
import org.gwtunite.client.file.File;
import org.gwtunite.client.file.FileStream;
import org.gwtunite.client.file.FileSystem;
import org.gwtunite.client.file.File.FileMode;
import org.gwtunite.client.net.WebServerEventHandler;
import org.gwtunite.client.net.WebServerRequest;
import org.gwtunite.client.net.WebServerResponse;
import org.gwtunite.client.net.WebServerResponse.CloseCallback;
import org.gwtunite.client.net.WebServerResponse.FlushCallback;
import org.gwtunite.testing.client.framework.Test;
import org.gwtunite.testing.client.framework.TestCase;
import org.gwtunite.testing.client.tests.fixture.WebServerTestFixture;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.DOM;

public class WebServerResponseTests extends TestCase {
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
	public void writeTests() throws Exception {
		final String testString = "TestString";
		String handler = "testWrite";
		
		fixture.addHandler(handler, new WebServerEventHandler() {
			@Override
			public void onConnection(WebServerRequest request, WebServerResponse response) {
				try {
					response.write(testString);
				} finally {
					if (response != null)
						response.close();
				}
			}
		});
		
		RequestBuilder builder = fixture.getLocalRequestBuilder(RequestBuilder.GET, handler);
		builder.setCallback(new RequestCallback() {
			@Override public void onError(Request request, Throwable exception) {
				fail("HTTP Request threw Exception "+exception.getMessage());
			}

			@Override public void onResponseReceived(Request request, Response response) {
				try {
					assertEquals(200, response.getStatusCode());
					assertEquals("OK",response.getStatusText());
					assertEquals(testString, response.getText());
					finishTest();
				} catch(Exception e) {
					handleException(e);
				}
			}
		});
		
		delayTestFinish(1000);
		builder.send();
	}

	@Test
	public void writeLineTests() throws Exception {
		final String[] testLines = {"TestString1","TestString2"};
		String handler = "testWriteLine";
		
		fixture.addHandler(handler, new WebServerEventHandler() {
			@Override
			public void onConnection(WebServerRequest request, WebServerResponse response) {
				try {
					for (String line : testLines) {
						response.writeLine(line);
					}
				} finally {
					if (response != null)
						response.close();
				}
			}
		});
		
		RequestBuilder builder = fixture.getLocalRequestBuilder(RequestBuilder.GET, handler);
		builder.setCallback(new RequestCallback() {
			@Override public void onError(Request request, Throwable exception) {
				fail("HTTP Request threw Exception "+exception.getMessage());
			}

			@Override public void onResponseReceived(Request request, Response response) {
				try {
					assertEquals(200, response.getStatusCode());
					assertEquals("OK",response.getStatusText());
			
					String[] lines = response.getText().split("\n");
					assertEquals(testLines.length, lines.length);
					for (int f=0;f<testLines.length;f++) 
						assertEquals(testLines[f], lines[f]);
					
					finishTest();
				} catch(Exception e) {
					handleException(e);
				}
			}
		});
		
		delayTestFinish(1000);
		builder.send();
	}
	
	@Test
	public void writeFileTests() throws Exception {
		String handler = "testWriteFile";
		
		fixture.addHandler(handler, new WebServerEventHandler() {
			@Override
			public void onConnection(WebServerRequest request, WebServerResponse response) {
				try {
					FileSystem fileSystem = FileSystem.getInstance();
					File appSystem = fileSystem.mountApplicationFileSystem();
					File testFile = appSystem.resolve("TestArtifacts/ATestFile.txt");
					
					response.writeFile(testFile);
				} finally {
					if (response != null)
						response.close();
				}
			}
		});
		
		RequestBuilder builder = fixture.getLocalRequestBuilder(RequestBuilder.GET, handler);
		builder.setCallback(new RequestCallback() {
			@Override public void onError(Request request, Throwable exception) {
				fail("HTTP Request threw Exception "+exception.getMessage());
			}

			@Override public void onResponseReceived(Request request, Response response) {
				try {
					assertEquals(200, response.getStatusCode());
					assertEquals("OK",response.getStatusText());
					
					assertEquals("Hello", response.getText());
					
					finishTest();
				} catch(Exception e) {
					handleException(e);
				}
			}
		});
		
		delayTestFinish(1000);
		builder.send();
	}

	@Test
	public void writeImageTests() throws Exception {
		String handler = "testWriteImage";
		
		fixture.addTestHandler(handler, new WebServerEventHandler() {
			@Override
			public void onConnection(WebServerRequest request, WebServerResponse response) {
				FileSystem fileSystem = FileSystem.getInstance();
				File appSystem = fileSystem.mountApplicationFileSystem();
				File testFile = appSystem.resolve("TestArtifacts/TestPass.png");
				
				ImageElement img = Document.get().createImageElement();
				img.setSrc(testFile.getVirtualPath());
				Logging.log(img.getSrc());
				response.writeImage(img);
			}
		});
		
		delayTestFinish(1000);
		fixture.invokeTestURLViaGet(handler);
	}
	
	@Test
	public void writeBytesTests() throws Exception {
		File appDir = FileSystem.getInstance().mountApplicationFileSystem();
		File imgFile = appDir.resolve("TestArtifacts/TestPass.png");
		FileStream imgStream = imgFile.open(FileMode.READ);
		final ByteArray imgBytes = imgStream.readBytes(imgStream.getBytesAvailable());
		
		String handler = "testWriteBytes";
		
		// Since we can't manipulate Bytes AND we can't check them using the Google HTTP Request object, the best we can do is make
		// sure no errors are thrown
		fixture.addTestHandler(handler, new WebServerEventHandler() {
			@Override
			public void onConnection(WebServerRequest request, WebServerResponse response) {
				response.writeBytes(imgBytes);
			}
		});
				
		delayTestFinish(1000);
		fixture.invokeTestURLViaGet(handler);
	}
	
	@Test
	public void responseAttributesAreCorrect() throws Exception {
		String handler = "testResponseAttributes";
		
		fixture.addTestHandler(handler, new WebServerEventHandler() {
			@Override
			public void onConnection(WebServerRequest request, WebServerResponse response) {
				assertTrue(response.isChunked(),"isChunked");
				response.setChunked(false);
				assertFalse(response.isChunked(),"isChunked After");
				
				assertFalse(response.isImplicitFlush(),"isImplicitFlush");
				response.setImplicitFlush(true);
				assertTrue(response.isImplicitFlush(),"isImplicitFlush After");

				assertFalse(response.isClosed(),"isClosed");
			}
		});
		
		delayTestFinish(1000);
		fixture.invokeTestURLViaGet(handler);
	}	
	
	@Test
	public void flushCallbackIsCalled() throws Exception {
		String handler = "testFlushCallback";
		
		fixture.addHandler(handler, new WebServerEventHandler() {
			@Override
			public void onConnection(WebServerRequest request, final WebServerResponse response) {
				response.flush(new FlushCallback() {
					@Override public void onFlush() {
						response.write("Flushed");
						response.close();
					}
				});
			}
		});
		
	
		RequestBuilder builder = fixture.getLocalRequestBuilder(RequestBuilder.GET, handler);
		builder.setCallback(new RequestCallback() {
			@Override public void onError(Request request, Throwable exception) {
				fail("HTTP Request threw Exception "+exception.getMessage());
			}

			@Override public void onResponseReceived(Request request, Response response) {
				try {
					assertEquals(200, response.getStatusCode());
					assertEquals("OK",response.getStatusText());
					
					assertEquals("Flushed", response.getText());
					
					finishTest();
				} catch(Exception e) {
					handleException(e);
				}
			}
		});
		
		delayTestFinish(1000);
		builder.send();
	}

	@Test
	public void closeCallbackIsCalled() throws Exception {
		String handler = "testCloseCallback";
		
		fixture.addHandler(handler, new WebServerEventHandler() {
			@Override
			public void onConnection(WebServerRequest request, final WebServerResponse response) {
				response.close(new CloseCallback() {
					@Override public void onClose() {
						finishTest();
					}
				});
			}
		});
		
	
		RequestBuilder builder = fixture.getLocalRequestBuilder(RequestBuilder.GET, handler);
		builder.setCallback(new RequestCallback() {
			@Override public void onError(Request request, Throwable exception) {
				fail("HTTP Request threw Exception "+exception.getMessage());
			}

			@Override public void onResponseReceived(Request request, Response response) {
				try {
					assertEquals(200, response.getStatusCode());
					assertEquals("OK",response.getStatusText());
					
				} catch(Exception e) {
					handleException(e);
				}
			}
		});
		
		delayTestFinish(1000);
		builder.send();
	}
}
