package org.gwtunite.client.discovery;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * This class has no public constructor.
 * A list of services.
 *
 * <p>ServicesLists are exposed as the {@link DeviceDescriptor#getServices()} method.</p>
 *
 * <pre><code>service = device.getServices().getServiceByIndex(3);
 *service = device.getServices().getServiceByServiceId(serviceId);
 * </code></pre>
 */
public final class ServiceList extends JavaScriptObject {
	
	protected ServiceList() {
		
	}
	
    /**
     * Number of services in this list.
     */
	public native int getSize() /*-{
		return this.size;
	}-*/;
	
    /**
     * Get the service at the given position in the list.
     * @param index Positive integer denoting the position of the service in the list.
     * @return Service at the given position, or undefined if the index is out of bounds.
     */
	public native ServiceDescriptor getServiceByIndex(int index) /*-{
		return this.getServiceByIndex(index);
	}-*/;
	
    /**
     * Get the service with the given id from the list.
     * @param id Identifier of the service to retrieve.
     * @return Service with the given identifier, or undefined if no service has the given identifier.
     */
	public native ServiceDescriptor getServiceById(String id) /*-{
		return this.getServiceById(id);
	}-*/;
}
