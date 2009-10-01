package opera.io;
/**
 * JavaDoc comments based on comments from the original unite.js file (c)Opera Software ASA
 */

import com.google.gwt.core.client.JavaScriptObject;

/**
 * The WebServer on which a particular service is running.
 * 
 * This class cannot be instantiated, instead the {@link #getInstance()} method should be used
 */
final public class WebServer extends JavaScriptObject {
	public final static String INDEX_PATH = "_index";
	public final static String ALL_REQUESTS_PATH = "_request";
	public final static String CLOSE_PATH = "_close";
	
	protected WebServer() {
	}

	/** 
	 * @return true if the WebServer runtime is available 
	 */
	public static native boolean isAvailable() /*-{
		if (opera.io.webserver) 
			return true
		else 
			return false;
	}-*/;
	
	/** 
	 * @return The singleton instance of the WebServer
	 * @throws FeatureUnavailableException if the WebServer runtime isn't available
	 */
	public static WebServer getInstance() {
		if (!isAvailable())
			throw new FeatureUnavailableException("WebServer is not installed");
		
		return jsniGetInstance();
	}
	
	private static native WebServer jsniGetInstance() /*-{
		return opera.io.webserver;
	}-*/;
	
    /**
     * Add an event listener for incoming requests.
     *
     * <p>Listening for requests is done by registering event listeners on the
     * Web server. The "event name" corresponds to the path fragment of the URL after
     * the service name, e.g. 'add' as in http://work.john.operaunite.com/wiki/add.</p>
     *
     * <p>Registered event handlers are called with an event of the type {@link WebServerRequestEvent}.</p>
     *
     * <p>Any request names starting with underscore ('_') is reserved and cannot be used.</p>
     *
     * <p>The exceptions are the following request names, which have special meanings:</p>
     *
     * <dl>
     * <dt>{@link WebServer#INDEX_PATH}</dt>
     * <dd>Event fired when a user accesses the root of the service (i.e. http://work.john.operaunite.com/wiki). 
     * Use this to supply a default start page or similar for your service.</dd>
     * <dt>{@link WebServer#ALL_REQUESTS_PATH}</dt><dd>Event fired when a user accesses any URL under the service. Use this to catch 
     * all requests to the server in a general fashion. You'll need to use the 
     * {@link WebServerRequest#getUri()} to distinguish the actual request URI. Listening for this event will also catch
     * the <code>_index</code> request, but not <code>_close</code>.</dd>
     * <dt>{@link WebServer#CLOSE_PATH}</dt><dd>Event fired when a connection is closed. In this case the <code>connection</code> property
     * of the <code>event</code> object is null.</dd>
     * </dl>
     *
     * <p>Events for specific event listeners and _index events are fired before the _request event is fired. 
     * Consider the following code example:</p>
     *
     * <pre><code>WebServer.getInstance().addEventListener(WebServer.ALL_REQUESTS_PATH, generalhandler, false);
     *WebServer.getInstance().addEventListener("add", addhandler, false);</code></pre>
     *
     * <p>The handlers for a specific path, including _index is called before _request.</p>
     *
     * <p>If the user visits the URL http://work.john.operaunite.com/wiki/add, a {@link WebServerRequestEvent} is fired, 
     * and the <code>addhandler</code> is called, before the <code>generalhandler</code> 
     * method is called. This happens regardless of which event listener was registered first.</p>
     *
     * @param pathFragment Path fragment to add a listener for.
     * @param handler Event listener function to add.
     * @param useCapture Whether or not the capture phase should be used.
     */
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
					@opera.io.Utils::handleException(Ljava/lang/Throwable;)(exception);
					webServerRequestEvent.connection.response.close();
				}
			}
	}-*/;

    /**
     * The name of the service currently accessing the webserver object, for example 'File Sharing'. Readonly.
     *
     * <p>The name of the service, for example as defined in the <code>widgetname</code> element
     * in the <code>config.xml</code> of a Opera Unite service.</p>
     */
	public native String getCurrentServiceName() /*-{
		return opera.io.webserver.currentServiceName;
	}-*/;
	
    /** 
     * The current connections made to this Web server.
     *
     * <p>Connections remain in this collection even if they are closed. They are removed when there are
     * no longer any references to the connection elsewhere in the system.</p>
     */
	public native WebServerConnection[] getConnections() /*-{
		return this.connections;
	}-*/;

	/**
     * Get the MIME content type mapped to a particular file name.
     *
     * This function looks up the MIME content type associated with the given file name in Opera.
     * It can for example be used to set proper headers when serving special types of files. The file name
     * must contain a period ('.').
     *
     * @param file The file name to get a MIME type for, for example 'index.html'.
     * @returns The MIME content type mapped to the given file name.
     */
	public native String getContentType(File file) /*-{
		return this.getContentType(file);
	}-*/;

	/**
     * The path of the service currently accessing the webserver object, for example '/fileSharing/'.
     *
     * <p>The path of the service, for example as defined in the <code>servicePath</code> element
     * in the <code>config.xml</code> of a Opera Unite service. In contrast to the <code>serviceName</code>,
     * this name can only contain characters that are valid in an IRI.</p>
     *
     * <p>The path includes a leading and trailing slash.</p>
     *
     * <p>This way a service called "My Cool File Sharing (tm) (c)" can be
     * identified as simply "share" in it's URL, i.e. 
     * <code>http://work.john.operaunite.com/share</code>.</p>
     *
     * <p>In the example above this property would contain '/share/'.</p>
     *
     * <p>Note that anything after the first path component is handled by
     * the service.</p>
     */
	public native String getCurrentServicePath() /*-{
		return opera.io.webserver.currentServicePath;
	}-*/;

	
	/**
     * The name of the device the Web server is running on, for example 'work'.
     *
     * You may run a Web server on different devices, like two different computers
     * in your home network and your mobile phone, e.g. <code>work</code> as in http://work.john.operaunite.com.
     */
	public native String getDeviceName() /*-{
		return opera.io.webserver.deviceName;
	}-*/;

	/**
     * The hostname of the Webserver, for example 'work.john.operaunite.com'.
     *
     * <p>You may run Web servers on different devices. The hostname contains the device name,
     * username and proxy address, for example <code>work.john.operaunite.com</code>
     * as in <code>http://work.john.operaunite.com/wiki</code> and <code>http://home.john.operaunite.com/wiki</code>.</p>
     *
     *<p>Note that this will always be a host name which contains the proxy name.</p>
     */
	public native String getHostName() /*-{
		return opera.io.webserver.hostName;
	}-*/;

    /**
     * The URL the currently running service was downloaded from.
     *
     * <p>This property can be used to make a download link to the service and can also function
     * as part of an auto update scheme.</p>
     */
	public native String getOriginURL() /*-{
		return opera.io.webserver.originURL;
	}-*/;
	
    /**
     * The port this Web server is listening to, for example 8840.
     *
     * You may run multiple Web servers from the same computer by assigning different port
     * numbers and device names to each instance of Opera running the Web server in opera:config.
     * Valid ports are in the range 0-65536
     */
	public native String getPort() /*-{
		return opera.io.webserver.port;
	}-*/;

	/**
     * The name of the proxy the Web server is connected to, for example 'operaunite.com'.
     *
     * The proxy name is the last part of the full host name, 
     * e.g. <code>operaunite.com</code> as in <code>http://work.john.operaunite.com/wiki</code>
     */
	public native String getProxyName() /*-{
		return opera.io.webserver.proxyName;
	}-*/;

    /**
     * The public facing IP address of this Web server, as seen by the proxy.
     *
     * If the Web server does not accept direct connections, this property is <code>null</code>.
     */
	public native String getPublicIP() /*-{
		return opera.io.webserver.publicIP;
	}-*/;

	/**
	 * The public facing port of this Web server, as seen by the proxy.
	 *
	 * If the Web server does not accept direct connections, this property is <code>null</code>.
	 */
	public native int getPublicPort() /*-{
		return opera.io.webserver.publicPort;
	}-*/;

    /**
     * Services running on this Web server.
     *
     * An array of {@link WebServerServiceDescriptor} objects that describe the services currently running
     * on this device. You can use this property to discover and communicate with other services, and potentially share data 
     * with them.
     */
	public native WebServerServiceDescriptor[] getServices() /*-{
		return opera.io.webserver.services;
	}-*/;

    /**
     * The My Opera user name of the user owning the Web server, for example 'john'.
     *
     * For authentication purposes, a <a href="http://my.opera.com">My Opera</a> user name is required for connecting to the
     * proxy and publishing services.
     */
	public native String getUserName() /*-{
		return opera.io.webserver.userName;
	}-*/;

    /**
     * Remove an event listener from the server.
     * @param pathFragment Path fragment to remove a listener for.
     * @param handler Event listener function to remove.
     * @param useCapture Whether or not this applies to the capture phase.
     */
	public void removeEventListener(String pathFragment, WebServerEventHandler handler, boolean useCapture) {
		jsniRemoveEventListener(pathFragment,handler.getJSNIEventHandler(), useCapture);
	}
	
	private native void jsniRemoveEventListener(String pathFragment, JavaScriptObject handler, boolean useCapture) /*-{
		opera.io.webserver.removeEventListener(pathFragment, handler, useCapture);
	}-*/;

    /**
     * Shares a File
     *
     * <p>Shares a File from a mountpoint that has been acquired earlier, and makes
     * it available under the path specified in the second argument.</p>
     *
     * <p>The File can be a regular file, a directory or an archive.</p>
     *
     * <p>The share is automatically deleted when the service is closed.</p>
     *
     * <p>Example: If you have resolved a File to a given folder and then specify <code>opera.io.webserver.shareFile(myFile, 'share')</code>, 
     * it will be shared as the URL <code>http://device.user.proxy/service/share</code></p>
     *
     * @param file The File to share
     * @param path The path this file will be shared as on the web.
     */
	public native void shareFile(File file, String path) /*-{
		this.shareFile(file, path);
	}-*/;

    /**
     * Unshares a previously shared file
     *
     * When a file has been shared using shareFile, it can be unshared again
     * by calling this method with the same File reference used to share the file
     *
     * @param file The file to unshare.
     */
	public native void unshareFile(File file) /*-{
		this.unshareFile(file);
	}-*/;
}