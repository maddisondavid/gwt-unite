package opera.io;

import com.google.gwt.core.client.JavaScriptObject;

/** Base class for all WebServer Event handlers */
public abstract class WebServerEventHandler {
	private JavaScriptObject jsniEventHandler;
	
	void setJSNIEventHandler(JavaScriptObject obj) {
		jsniEventHandler = obj;
	}
	
	JavaScriptObject getJSNIEventHandler() {
		return jsniEventHandler;
	}
	
	public void onConnection(WebServerRequestEvent webServerRequest) {
		onConnection(webServerRequest.getConnection().getRequest(), webServerRequest.getConnection().getResponse());
	}
	
	protected abstract void onConnection(WebServerRequest request, WebServerResponse response);
}
