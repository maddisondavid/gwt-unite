package opera.io;

import com.google.gwt.core.client.JavaScriptObject;

/**
* Event fired when requests are made to the Web server.
*
* <p>See {@link WebServer#addEventListener(String, WebServerEventHandler, boolean)} for details on different event names.</p>
*/
final public class WebServerRequestEvent extends JavaScriptObject {
	protected WebServerRequestEvent() {
	}

	/**
	 * The connection this request event was generated from.
	 *
	 * <p>Use this property to access the incoming request and outgoing response:</p>
	 * 
	 * <pre><code>req = e.getConnection().getRequest();
	 *res = e.getConnection().getResponse();</code></pre>
	 *
	 * <p>If this event was fired for a <code>_close</code> request, this property will be <code>null</code>.</p>
	 */
	native public WebServerConnection getConnection() /*-{
		return this.connection;
	}-*/;

	/**
	 * Id of the connection this request was generated from.
	 *
	 * If this event was fired for a <code>_close</code> request, this property will hold the id of the closed connection.
	 */
	public native int getId() /*-{
		return this.id
	}-*/;
}
