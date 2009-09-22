package opera.io;

import com.google.gwt.core.client.JavaScriptObject;

/** Connection made to the Web server.
*
* <p>WebServerConnection holds the incoming request and gives the user access to the outgoing response,
* as well maintaining the state of the connection.</p>
*/
final public class WebServerConnection extends JavaScriptObject {
	
	protected WebServerConnection() {
	}

	public native int getId() /*-{
		return this.id;
	}-*/;

    /**
     * The incoming HTTP request on this connection.
     */
	public native WebServerRequest getRequest() /*-{
		return this.request;
	}-*/;

    /**
     * The outgoing HTTP response that will be send to the client.
     */
	public native WebServerResponse getResponse() /*-{
		return this.response;
	}-*/;

    /**
     * Whether or not this connection has been closed.
     */
	public native boolean isClosed() /*-{
		return this.closed;
	}-*/;
	
    /**
     * Whether or not this connection is made directly through the Opera instance.
     */
	public native boolean isLocal() /*-{
		return this.isLocal;
	}-*/;

    /**
     * Whether this connection is made from a page with a URL on the admin subdomain in the same instance running the service.
     *
     * <p>You can use this property to determine if the connection is coming from the owner (typically yourself) of the service and
     * therefore whether to, for example, grant it special privileges. This is the case when the request is the result of accessing a 
     * URL with the admin subdomain.</p>
     */
	public native boolean isOwner() /*-{
		return this.isOwner;
	}-*/;

    /**
     * Whether or not this connection is made through the proxy.
     *
     * <p>This property will be false if you access the services through a local URL.</p>
     */
	public native boolean isProxied() /*-{
		return this.isProxied;
	}-*/;
}
