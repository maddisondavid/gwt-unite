package opera.io;

import com.google.gwt.core.client.JavaScriptObject;

final public class WebServerServiceDescriptor extends JavaScriptObject {

	protected WebServerServiceDescriptor() {
		
	}
	
	public native boolean isAuthenticated() /*-{
		return this.authenticated;
	}-*/;
	
	public native String getOriginURL() /*-{
		return this.originURL
	}-*/;

	public native String getServicePath() /*-{
		return this.servicePath;
	}-*/;

	public native String getName() /*-{
		return this.name;
	}-*/;

	public native String getDescription() /*-{
		return this.description;
	}-*/;

	public native String getAuthor() /*-{
		return this.author;
	}-*/;

	public native String getUri() /*-{
		return this.uri;
	}-*/;
}
