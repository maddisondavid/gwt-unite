package opera.io;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Request made to the Web server.
 *
 * <p>WebServerRequest holds information about the incoming request, such as its URI, method and any data 
 * sent along with it.</p>
 */
final public class WebServerRequest extends JavaScriptObject {
	
	protected WebServerRequest() {
	}
	
    /**
     * The full body of the HTTP request as a String.
     *
     * If the body is non existintant or binary, this property is null.
     */
	public native String getBody() /*-{
		return this.body;
	}-*/;

    /**
     * The names of items in the body of this request, meaning data sent as POST.
     */
	public native String[] getBodyItemNames() /*-{
		var keys = new Array();
		
		for (key in this.bodyItems)
			keys.push(key);
			
		return keys;
	}-*/;
	
    /**
     * @return the values of the body item.  This is an array as a body item can be specified 
     * mulitple times
     */
	public native String[] getBodyItem(String bodyItem) /*-{
		return this.bodyItems[bodyItem];
	}-*/;

    /**
     * @return The connection this request was sent through.
     */
	public native WebServerConnection getConnection() /*-{
		return this.connection;
	}-*/;

    /**
     * Files uploaded with this request.
     *
     * <p>This File object represents a virtual directory which points to the
     * uploaded files. Access the individual files by getting the listing
     * from the file.</p>
     *
     * <p>The name of the file is the filename the user selected when uploading.</p>
     *
     * <p>Request headers for these files are available through the metaData property:
     * <code>req.getFiles().getContents()[0].getMetaData().headers['some header'];</code></p>
     * 
     * FIXME: The above comment is wrong (and so is possibly the meta data property in File!)
     */
	public native File getFiles() /*-{
		return this.files;
	}-*/;

    /**
     * @return The names of all HTTP headers sent with this request.
     */
	public native String[] getHeaderNames() /*-{
		var keys = new Array();
		
		for (key in this.headers)
			keys.push(key);
			
		return keys;
	}-*/;

    /**
     * @return The values for a particular header.  This is an array as a header could be 
     * specified multiple times
     */
	public native String[] getHeader(String headerName) /*-{
		return this.headers[header];
	}-*/;
	
    /**
     * The value of the Host header sent with this request.
     *
     * As opposed to the {@link #getUri()} metho, this will give you the host name the request
     * was made to, which can be used for among other things redirects. This may contain the
     * port of the request, i.e. 'foo.bar:80'.
     */
	public native String getHost() /*-{
		return this.host;
	}-*/;
	
    /**
     * The IP address of the client that sent this request.
     *
     * <p class="ni">This currently only gives you the IP address of the proxy.</p>
     */
	public native String getIP() /*-{
		return this.ip;
	}-*/;

    /**
     * Get the value of a request item.
     *
     * <p>This method gets the values of items sent in a query string (typically through GET requests)
     * or in the body of the request (typically through POST requests). Each item may occur multiple times
     * with different values, both in the query string and the body. The optional second argument
     * <code>method</code> can be used to limit the selection to either of those two.</p>
     *
     * @param requestItem Name of the request item to get
     * @returns An Array of values for the given request item, or null if there are no values for the given request item.
     */
	public native String[] getItem(String requestItem) /*-{ 
		return this.getItem(requestItem);
	}-*/;

    /**
     * Get the value of a request item.
     *
     * <p>This method gets the values of items sent in a query string (typically through GET requests)
     * or in the body of the request (typically through POST requests). Each item may occur multiple times
     * with different values, both in the query string and the body. The optional second argument
     * <code>method</code> can be used to limit the selection to either of those two.</p>
     *
     * @param requestItem Name of the request item to get
     * @param method Optional argument with the method of the request item to get, either GET or POST.
     * @returns An Array of values for the given request item, or null if there are no values for the given request item.
     */
	public native String[] getItem(String requestItem, String method) /*-{ 
		return this.getItem(requestItem, method);
	}-*/;

    /**
     * @return The HTTP method of this request, one of GET, POST, PUT or DELETE. Readonly.
     */
	public native String getMethod() /*-{
		return this.method;
	}-*/;

    /**
     * The protocol this request was made to.
     *
     * This is either 'http' or 'https'. Use it to construct links
     * and redirects and preserve the correct security qualifications of the URIs.
     */
	public native String getProtocol()/*-{
		return this.protocol;
	}-*/;

    /**
     * The names of items sent as part of the query string in this request, meaning data sent as GET.
     */
	public native String[] getQueryItemNames() /*-{
		var keys = new Array();
		
		for (key in this.queryItems)
			keys.push(key);
			
		return keys;
	}-*/;

	/**
	 *    
     * The values of a query string parameter sent as part of the query string in this request, 
     * meaning data sent as GET.
     */
	public native String[] getQueryItem(String name) /*-{
		return this.queryItems[name];
	}-*/;

    /**
     * Get the values of a HTTP header in the request.
     *
     * <p>The returned object is a collection of headers matching the given header name.</p>
     *
     * <h3>Example:</h3>
     *
     * <p>Assuming that the client sent a request with a header of <code>Foo</code>, access it as follows:</p>
     *
     * <pre><code>
     * headers = request.getRequestHeader('Foo');
     * if ( headers )
     * {
     *     opera.postError(headers[0]);
     * }</code></pre>
     *
     * @param requestHeader Name of the HTTP header to get.
     * @returns An Array with headers matching the given name, or null if there are no headers with the given name.
     */
	public native String[] getRequestHeader(String requestHeader) /*-{
		return this.getRequestHeader(requestHeader);
	}-*/;

    /**
     * The Uniform Resource Identifier this request was made to.
     *
     * <p>Relative URI the request is made out to, starting with '/' and the name of the service, 
     * e.g. http://work.john.operaunite.com/wiki/add becomes '/wiki/add'. It is rewritable to allow 
     * redispatching of the request. See the {@link WebServerResponse#closeAndRedispatch} method.</p>
     *
     * <p>Setting this property will throw a SECURITY_ERR if an invalid URI is set or if the URI 
     * points to a different service than the one the request was issued from.</p>
     */
	public native String getUri() /*-{
		return this.uri;
	}-*/;

    /**
     * Set the Uniform Resource Identifier this request was made to.
     *
     * <p>Will throw a SECURITY_ERR if an invalid URI is set or if the URI 
     * points to a different service than the one the request was issued from.</p>
     */
	public native void setUri(String uri) /*-{
		this.uri = uri;
	}-*/;
}
