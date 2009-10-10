package org.gwtunite.client.net;

import org.gwtunite.client.file.File;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * A Request made to the Web server.
 *
 * <p>WebServerRequest holds information about the incoming request, such as its URI, method and any data 
 * sent along with it.</p>
 * 
 * <p>An instance of this object will be sent to any {@link WebServerEventHandler} when the <code>onConnection</code> method
 * is called.</p>
 */
final public class WebServerRequest extends JavaScriptObject {
	
	protected WebServerRequest() {
	}
	
    /**
     * The full body of the HTTP request as a String.
     *
     * @return The full body as test or NULL if there is no body or it is binary
     */
	public native String getBodyAsText() /*-{
		return this.body;
	}-*/;

	/** 
	 * Tests whether the specified item has been POSTed in this request
	 * 
	 * @param name The name of the body item to test for
	 * @return true if a body item with the given name exists in this request 
	 */
	public native boolean hasBodyItem(String name) /*-{
		if (this.bodyItems[name])
			return true
		else
			return false;
	}-*/;
	
    /**
     * The names of items in the POST body of this request.
     * 
     * @return the names of all items posted in the Body of this request
     */
	public native String[] getBodyItemNames() /*-{
		if (!this.bodyItems)
			return [];
			
        var keys = new Array();
        for (key in this.bodyItems)
                keys.push(key);
                
        return keys;
	}-*/;
        
	/**
	 * Retrieves a specific POSTed Body item from this request
	 * 
	 * @param name The name of the body item to be retrieved
	 * @return A list of values for the body item or null if the body item does not exist in this request
	 */
	public native String[] getBodyItem(String name) /*-{
	        return this.bodyItems[name];
	}-*/;

	/**
	 * Test whether the specified item was sent in the query string of this request
	 * 
	 * @param name The name of the item to test for
	 * @return True if the item exists in this request
	 */
	public native boolean hasQueryItem(String name)/*-{
		if (this.queryItems[name]) 
			return true
		else 
			return false;
	}-*/;
	
    /**
     * The names of all items sent on the query string of this request
     * 
     * @return the names of all items sent on the query string of this request
     */
	public native String[] getQueryItemNames() /*-{
	        var keys = new Array();
	        for (key in this.queryItems)
	                keys.push(key);
	                
	        return keys;
	}-*/;

	/**
	 * Retrieves the values of a particular item from the query string sent with this request
	 * 
	 * @param name The name of the item to be retrieved
	 * @return The values for the specific item from the query string
     */
	public native String[] getQueryItem(String name) /*-{
	        return this.queryItems[name];
	}-*/;
	
	/**
	 * Tests whether the specified header was sent with this request
	 * 
	 * @param name The name of the header to test for
	 * @return true if the specified header was sent with this request
	 */
	public native boolean hasHeader(String name) /*-{
		if (this.headers[name])
			return true
		else 
			return false;
	}-*/;
	
    /**
     * Retrieves the names of all headers sent with this request
     * 
     * @return The names of all HTTP headers sent with this request.
     */
	public native String[] getHeaderNames() /*-{
	        var keys = new Array();
	        for (key in this.headers)
	                keys.push(key);
	                
	        return keys;
	}-*/;

    /**
     * Retrieves the values of the specified header
     * 
     * @param name The name of the header
     * @return The values for the specified header or Null if the specified header does not exist
     */
	public native String[] getHeader(String name) /*-{
	        return this.headers[name];
	}-*/;	
	
    /**
     * Retrieves the {@link WebServerConnection} this request was sent through
     * 
     * @return the connection this request was sent through
     */
	public native WebServerConnection getConnection() /*-{
		return this.connection;
	}-*/;

    /**
     * Retrieves the files uploaded and sent with this request
     * 
     * <p>The returned {@link File} object represents a virtual directory which points to the
     * uploaded files. Access the individual files using {@link File#listFiles()}.</p>
     *
     * <p>The name of the file is the filename the user selected when uploading.</p>
     *
     * <p>Request headers for these files are available through the file metadata</p>
     * 
     * @return A {@link File} object representing a virtual directory holding the uploaded files
     */
	public native File getUploadedFiles() /*-{
		return this.files;
	}-*/;
	
    /**
     * The value of the Host header sent with this request.
     *
     * As opposed to the {@link #getUri()} method, this will give you the host name the request
     * was made to, which can be used for among other things redirects. This may contain the
     * port of the request, i.e. 'foo.bar:80'.
     * 
     * @return the value of the Host header sent with this request
     */
	public native String getHost() /*-{
		return this.host;
	}-*/;
	
    /**
     * The IP address of the client that sent this request.
     *
     * <p>Note: This currently only gives you the IP address of the proxy.</p>
     * 
     * @return the IP address of the client that sent this request
     */
	public native String getRemoteIP() /*-{
		return this.ip;
	}-*/;

    /**
     * Get the value of a request item.
     *
     * <p>This method gets the values of items sent in a query string (typically through GET requests)
     * or in the body of the request (typically through POST requests). Each item may occur multiple times
     * with different values, both in the query string and the body.</p>
     *
     * @param name Name of the request item to retrieve
     * @return An Array of values for the given request item, or null if there are no values for the given request item.
     */
	public native String[] getItem(String name) /*-{ 
		return this.getItem(requestItem);
	}-*/;
	
    /**
     * Retrieves the method of this request, one of GET, POST, PUT or DELETE.
     * 
     * @return The HTTP method of this request
     */
	public native String getMethod() /*-{
		return this.method;
	}-*/;

    /**
     * The protocol this request was made over.
     *
     * <p>This is either 'http' or 'https'. Use it to construct links
     * and redirects and preserve the correct security qualifications of the URIs.</p>
     * 
     * @return the protocol that this request was made over.
     */
	public native String getProtocol()/*-{
		return this.protocol;
	}-*/;

    /**
     * The Uniform Resource Identifier this request was made to.
     *
     * <p>Relative URI the request is made out to, starting with '/' and the name of the service, 
     * e.g. http://work.john.operaunite.com/wiki/add becomes '/wiki/add'.</p>
     * 
     * @return The URI this request was made to
     */
	public native String getUri() /*-{
		return this.uri;
	}-*/;

    /**
     * Set the Uniform Resource Identifier this request was made to.
     * 
     * <p>Setting the URI is used when the request should be redispatched. 
     * See the {@link WebServerResponse#closeAndRedispatch} method.</p> 
     *
     * @param uri The URI to set
     * @throws MalformedURIException if an invalid URI is set or if the URI 
     * points to a different service than the one the request was issued from.</p>
     */
	public native void setUri(String uri) throws MalformedURIException /*-{
		try {
			this.uri = uri;
		}catch(SECURITY_ERR){
			throw(@org.gwtunite.client.net.MalformedURIException::new(Ljava/lang/String;)(uri));
		}
	}-*/;
}
