package opera.discovery;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Metadata about a device
 *
 * <p>A device is most commonly a computer, but can also be cell phones, media centre consoles and the likes.
 * Objects of this class give you access to meta information about the device, such as a name and description and
 * the URL the device is exposed on.</p>
 *
 * <p>Devices are exposed in a {@link DeviceList} object through the {@link opera#nearbyDevices} property.</p>
 *
 * <p>Each device can run zero or more services, exposed in a {@link ServiceList} object in the {@link #services} property.</p>
 */
final public class DeviceDescriptor extends JavaScriptObject {
	public final static int ONLINE = 0;
	public final static int OFFLINE = 1;
	
	protected DeviceDescriptor() {
	}

	/**
     * Unique identifier of this device.
     */
	public native String getId() /*-{
		return this.id;
	}-*/;
	
    /**
     * URL this device is exposed on.
     *
     * This URL points to the root of the device. Use it to generate links to the device, or to generate URLs in XHR calls to it.
     *
     * <p>Example:</p>
     *
     * <pre><code>a = document.createElement('a');
     *a.href = device.getUrl();
     *a.textContent = device.getName()</code></pre>
     */
	public native String getUrl() /*-{
		return this.url;
	}-*/;
	
    /**
     * Human-readable name of this device.
     */
	public native String getName() /*-{
		return this.name;
	}-*/;
	
    /**
     * Human-readable description of this device.
     */
	public native String getDescription() /*-{
		return this.description;
	}-*/;
	
	/**
     * Opera Unite username of the owner of this device.
     *
     * If the owner of this device is not an Opera Unite user, this property will be null.
     */
	public native String getUniteUser() /*-{
		return this.uniteUser;
	}-*/;
	
    /**
     * Opera Unite device name for this device.
     *
     * If the owner of this device is not an Opera Unite user, this property will be null.
     */
	public native String getUniteDeviceName() /*-{
		return this.uniteDeviceName;
	}-*/;
	
    /**
     * The availability status of this device.
     *
     * <p>Typically whether the device is on- or offline. The value of this property is one of {@link #ONLINE} or {@link #OFFLINE}.
     * The list of constants expanded may be included in the future to include statuses like "Busy".</p>
     *
     * <p>Example:</p>
     *
     * <pre><code>if ( device.getStatus() == DeviceDecriptor.OFFLINE )
     *{
     *    widget.showNotification(device.getName() + " has been been switched off.");
     *}</code></pre>
     */
	public native int getStatus() /*-{
		return this.status
	}-*/;
	
    /**
     * Services running on this device.
     *
     * <p>If this device is not running any services, the list is empty.</p>
     */
	public native ServiceList getServices() /*-{
		return this.services;
	}-*/;
}
