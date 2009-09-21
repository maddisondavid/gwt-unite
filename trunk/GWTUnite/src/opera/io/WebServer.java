package opera.io;

import com.google.gwt.core.client.JavaScriptObject;

final public class WebServer extends JavaScriptObject {
	public final static String INDEX_PATH = "_index";
	public final static String ALL_REQUESTS_PATH = "_request";
	public final static String CLOSE_PATH = "_close";
	
	protected WebServer() {
	}

	public static native boolean isAvailable() /*-{
		if (opera.io.webserver) 
			return true
		else 
			return false;
	}-*/;
	
	public static WebServer getInstance() {
		if (!isAvailable())
			throw new FeatureUnavailableException("WebServer is not installed");
		
		return jsniGetInstance();
	}
	
	private static native WebServer jsniGetInstance() /*-{
		return opera.io.webserver;
	}-*/;
	
	public void addEventListener(String pathFragment, WebServerEventHandler handler, boolean useCapture) {
		handler.setJSNIEventHandler(createEventHandlerFunction(handler));
		jsniAddEventListener(pathFragment, handler.getJSNIEventHandler(), useCapture);
	};

	private native void jsniAddEventListener(String pathFragment, JavaScriptObject handler, boolean useCapture) /*-{
		opera.io.webserver.addEventListener(pathFragment, handler, useCapture);
	}-*/;
	
	private native JavaScriptObject createEventHandlerFunction(WebServerEventHandler handler) /*-{
		return function(webServerRequestEvent) {
				try {
					handler.@opera.io.WebServerEventHandler::onConnection(Lopera/io/WebServerRequestEvent;)(webServerRequestEvent);
				} catch(exception) {
					webServerRequestEvent.connection.response.close();
					@opera.io.Utils::handleException(Ljava/lang/Exception;)(exception);
				}
			}
	}-*/;
	
	public native String getCurrentServiceName() /*-{
		return opera.io.webserver.currentServiceName;
	}-*/;

	public native String getConnection() /*-{
		return this.connection;
	}-*/;

	public native String getContentType(File file) /*-{
		return this.getContentType(file);
	}-*/;

	public native String getCurrentServicePath() /*-{
		return opera.io.webserver.currentServicePath;
	}-*/;

	public native String getDeviceName() /*-{
		return opera.io.webserver.deviceName;
	}-*/;

	public native String getHostName() /*-{
		return opera.io.webserver.hostName;
	}-*/;

	public native String getOriginURL() /*-{
		return opera.io.webserver.originURL;
	}-*/;
	
	public native String getPort() /*-{
		return opera.io.webserver.port;
	}-*/;

	public native String getProxyName() /*-{
		return opera.io.webserver.proxyName;
	}-*/;

	public native String getPublicIP() /*-{
		return opera.io.webserver.publicIP;
	}-*/;

	public native int getPublicPort() /*-{
		return opera.io.webserver.publicPort;
	}-*/;

	public native WebServerServiceDescriptor[] getServices() /*-{
		return opera.io.webserver.services;
	}-*/;

	public native String getUserName() /*-{
		return opera.io.webserver.userName;
	}-*/;

	public void removeEventListener(String pathFragment, WebServerEventHandler handler, boolean useCapture) {
		jsniRemoveEventListener(pathFragment,handler.getJSNIEventHandler(), useCapture);
	}
	
	private native void jsniRemoveEventListener(String pathFragment, JavaScriptObject handler, boolean useCapture) /*-{
		opera.io.webserver.removeEventListener(pathFragment, handler, useCapture);
	}-*/;

	public native void shareFile(File file, String path) /*-{
		this.shareFile(file, path);
	}-*/;

	public native void unshareFile(File file) /*-{
		this.unshareFile(file);
	}-*/;
}
