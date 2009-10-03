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

	/**
	 * The ID of this connection
	 * 
	 * @return the ID of this connection
	 */
	public native int getId() /*-{
		return this.id;
	}-*/;

    /**
     * The incoming HTTP request on this connection.
     * 
     * @return the request made on this connection
     */
	public native WebServerRequest getRequest() /*-{
		return this.request;
	}-*/;

    /**
     * The outgoing HTTP response that will be send to the client.
     * 
     * @return the response to the client for this connection
     */
	public native WebServerResponse getResponse() /*-{
		return this.response;
	}-*/;

    /**
     * Tests whether or not this connection has been closed.
     * 
     * @return true if this connection has already been closed
     */
	public native boolean isClosed() /*-{
		return this.closed;
	}-*/;
	
    /**
     * Tests whether or not this connection is made directly through the Opera instance.
     * 
     * @return true if the request was made through the local browser running the service
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
     * 
     * @return true if the connection was made from a page in the admin subdomain
     */
	public native boolean isOwner() /*-{
		return this.isOwner;
	}-*/;

    /**
     * Whether or not this connection is made through the proxy.
     *
     * @return true if the connection was made through the OperaUnite proxies.  
     *         false if this connection was made through a local URL
     */
	public native boolean isProxied() /*-{
		return this.isProxied;
	}-*/;
}
