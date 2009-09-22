package opera.io;
/**
 * JavaDoc comments based on comments in the original unite.js file (c)Opera Software ASA
 */

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Data about a service.
 *
 * <p>Objects of this class contain basic information about a service and how to reach it. Information about 
 * the service is usually taken from its config.xml.</p>
 */
final public class WebServerServiceDescriptor extends JavaScriptObject {

	protected WebServerServiceDescriptor() {
		
	}
	
	/**
	* @return true if this service requires HTTP Digest authentication.
	*/
	public native boolean requiresAuthentication() /*-{
		return this.authentication;
	}-*/;
	
    /**
     * @return The URL this service was originally downloaded from.
     */
	public native String getOriginURL() /*-{
		return this.originURL
	}-*/;

	/**
	 * @return The path to this service, as taken from the <code>widgetname</code> or <code>feature</code> elements
	 * in the config.xml file of the service.
	 */
	public native String getServicePath() /*-{
		return this.servicePath;
	}-*/;

	/**
	 * @return Name of this service.
	 */
	public native String getName() /*-{
		return this.name;
	}-*/;

	/**
	 * @return Description of this service.
	 */
	public native String getDescription() /*-{
		return this.description;
	}-*/;

	/**
	 * @return Author of this service.
	*/
	public native String getAuthor() /*-{
		return this.author;
	}-*/;

	/**
	 * @return This is the full URI of the service, e.g. http://work.john.operaunite.com/wiki
	 */
	public native String getUri() /*-{
		return this.uri;
	}-*/;
}
