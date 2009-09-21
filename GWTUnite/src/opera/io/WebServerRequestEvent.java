package opera.io;

import com.google.gwt.core.client.JavaScriptObject;

final public class WebServerRequestEvent extends JavaScriptObject {
	protected WebServerRequestEvent() {
	}
	
	native public WebServerConnection getConnection() /*-{
		return this.connection;
	}-*/;

	public native int getId() /*-{
		return this.id
	}-*/;
}
