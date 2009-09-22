package opera.discovery;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Metadata for a service
 *
 * <p>A service is an application that exposes itself through a Web interface and allows users to view it or interact with it programatically. 
 * Objects of this class give you access to information such as the name and description of a service and which URL it is exposed on.
 * This says nothing of the capabilities of the service, so other services will need to know about the internal workings of the service
 * in order to communicate with it.</p>
 *
 * <p>Services are exposed in a {@link ServiceList} object through the {@link DeviceDescriptor#services} property.</p>
 */
public final class ServiceDescriptor extends JavaScriptObject {
	public static final int OFFLINE = 0;
	public static final int ONLINE = 1;
	
	protected ServiceDescriptor() {
	}
	
    /**
     * The availability status of this service.
     *
     * <p>Typically whether the service is on- or offline. The value of this property is one of {@link #ONLINE} or {@link #OFFLINE}.
     * The list of constants expanded may be included in the future to include statuses like "Busy".</p>
     *
     * <p>Example:</p>
     *
     * <pre><code>if ( service.getStatus == ServiceDescriptor.OFFLINE )
     *{
     *    widget.showNotification(service.getName() + " has been disabled.");
     *}</code></pre>
     */
	public native int getStatus() /*-{
		return this.status;
	}-*/;
	
	/**
     * Unique identifier for this service.
     */
	public native String getId() /*-{
		return this.id;
	}-*/;
	
    /**
     * Human-redable name of this service.
     */
	public native String getName() /*-{
		return this.name;
	}-*/;
	
    /**
     * URL this service is exposed on. 
     *
     * <p>This URL points to the root of the service. Use it to generate links to the service, or to generate URLs in XHR calls to it.</p>
     */
	public native String getUrl() /*-{
		return this.url;
	}-*/;
	
    /**
     * Human-redable description of this service. 
     */
	public native String getDescription() /*-{
		return this.description;
	}-*/;
	
    /**
     * Whether this service is an Opera Unite service.
     */
	public native boolean isUniteService() /*-{
		return this.isUniteService;
	}-*/;
	
    /**
     * The device this service belongs to.
     */
	public native DeviceDescriptor getDevice() /*-{
		return this.device;
	}-*/;
}
