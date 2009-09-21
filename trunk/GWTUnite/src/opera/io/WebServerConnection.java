package opera.io;

import com.google.gwt.core.client.JavaScriptObject;

final public class WebServerConnection extends JavaScriptObject {
	
	protected WebServerConnection() {
	}
	
	public native boolean getClosed() /*-{
		return this.closed;
	}-*/;

	public native int getId() /*-{
		return this.id;
	}-*/;

	public native WebServerRequest getRequest() /*-{
		return this.request;
	}-*/;

	public native WebServerResponse getResponse() /*-{
		return this.response;
	}-*/;

	public native boolean isLocal() /*-{
		return this.isLocal;
	}-*/;

	public native boolean isOwner() /*-{
		return this.isOwner;
	}-*/;

	public native boolean isProxied() /*-{
		return this.isProxied;
	}-*/;
}
