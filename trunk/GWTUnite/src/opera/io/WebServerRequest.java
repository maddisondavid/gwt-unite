package opera.io;

import com.google.gwt.core.client.JavaScriptObject;

final public class WebServerRequest extends JavaScriptObject {
	
	protected WebServerRequest() {
	}
	
	public native String getBody() /*-{
		return this.body;
	}-*/;

	public native String[] getBodyItemNames() /*-{
		var keys = new Array();
		
		for (key in this.bodyItems)
			keys.push(key);
			
		return keys;
	}-*/;
	
	public native String[] getBodyItem(String bodyItem) /*-{
		return this.bodyItems[bodyItem];
	}-*/;

	public native WebServerConnection getConnection() /*-{
		return this.connection;
	}-*/;

	public native File getFiles() /*-{
		return this.files;
	}-*/;

	public native String[] getHeaderNames() /*-{
		var keys = new Array();
		
		for (key in this.headers)
			keys.push(key);
			
		return keys;
	}-*/;

	public native String[] getHeaderName(String header) /*-{
		return this.headers[header];
	}-*/;
	
	public native String getHost() /*-{
		return this.host;
	}-*/;
	
	public native String getIP() /*-{
		return this.ip;
	}-*/;

	public native String[] getItem(String requestItem) /*-{ 
		return this.getItem(requestItem);
	}-*/;
	
	public native String[] getItem(String requestItem, String method) /*-{ 
		return this.getItem(requestItem, method);
	}-*/;

	public native String getMethod() /*-{
		return this.method;
	}-*/;

	public native String getProtocol()/*-{
		return this.protocol;
	}-*/;

	public native String[] getQueryItemNames() /*-{
		var keys = new Array();
		
		for (key in this.queryItems)
			keys.push(key);
			
		return keys;
	}-*/;
	
	public native String[] getQueryItem(String name) /*-{
		return this.queryItems[name];
	}-*/;

	public native String getRequestHeader(String requestHeader) /*-{
		return this.getRequestHeader(requestHeader);
	}-*/;

	public native String getUri() /*-{
		return this.uri;
	}-*/;
	
	public native void setUri(String uri) /*-{
		this.uri = uri;
	}-*/;
}
