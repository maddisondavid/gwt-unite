package opera.discovery;

import com.google.gwt.core.client.JavaScriptObject;

public final class DeviceList extends JavaScriptObject {

	protected DeviceList() {
	}
	
    /**
     * Number of devices in this list.
     */
	public native int getSize() /*-{
		return this.size;
	}-*/;
	
	public native DeviceDescriptor[] getAllDevices() /*-{
		return this;
	}-*/;
	
    /**
     * Get the device at the given position in the list
     * @param index Positive integer denoting the position of the device in the list.
     * @return Device at the given position, or undefined if the index is out of bounds.
     */
	public native DeviceDescriptor getDeviceByIndex(int index) /*-{
		return this.getDeviceByIndex(index);
	}-*/;
	
    /**
     * Get the device with the given id from the list.
     * @param id Identifier of the device to retrieve.
     * @return Device with the given identifier, or undefined if no device has the given identifier.
     */
	public native DeviceDescriptor getDeviceById(String id) /*-{
		return this.getDeviceById(id);
	}-*/;
}
