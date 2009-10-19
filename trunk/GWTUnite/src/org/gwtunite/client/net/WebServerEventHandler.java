package org.gwtunite.client.net;


import com.google.gwt.core.client.JavaScriptObject;

/** 
 * The base class for all WebServer Event handlers 
 * 
 * <p>Event handlers are registered using the {@link WebServer#addEventListener(String, WebServerEventHandler, boolean)} typically during the 
 * {@link OperaUniteEntryPoint#init(WebServer)} method.  The following code shows this:
 * code:</p>
 * <pre><code>
 * 	 public void init(WebServer webServer) {
 *		webServer.addEventListener(WebServer.INDEX_PATH, new WebServerEventHandler() {
 *			protected void onConnection(WebServerRequest request, WebServerResponse response) {
 *				response.write("Hello World!");
 *				response.close();
 *			}
 *		}, false);
 *   }
 * </code></pre>
 */
public abstract class WebServerEventHandler {
	private JavaScriptObject jsniEventHandler;
	
	/*Package Private*/
	void setJSNIEventHandler(JavaScriptObject obj) {
		jsniEventHandler = obj;
	}
	
	/*Package Private*/
	JavaScriptObject getJSNIEventHandler() {
		return jsniEventHandler;
	}
	
	/**
	 * Notification of an incoming connection 
	 * 
	 * @param request holds details of the client request
	 * @param response provides facilities to send a response back to the client
	 */
	public void onConnection(WebServerRequest request, WebServerResponse response) {
		if (request.getMethod().equals("GET")) {
			doGet(request, response);
		} else if (request.getMethod().equals("POST")) {
			doPost(request, response);
		}
	}
	
	/**
	 * Called when an HTTP POST request is received
	 * 
	 * <p>By default the method doesn't do anything, override it with an implementation</p>
	 * 
	 * @param request The request from the client
	 * @param response The response to send back to the client
	 */
	public void doPost(WebServerRequest request, WebServerResponse response) {
	}
	
	public void doGet(WebServerRequest request, WebServerResponse response) {
	}
}
