package opera.discovery;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Placeholder for Opera-specific functionality.
 */
public final class Opera extends JavaScriptObject {

	protected Opera() {
	}
	
  /**
   * Local devices available to this Opera instance.
   *
   * <p>This list contains all the devices this Opera instance has discovered on the local network.</p>
   *
   * <p>You can access it as a regular JavaScript object, using both an index and an identifier:</p>
   *
   * <pre><code>device = opera.getNearbyDevices().getDeviceByIndex(3);
   *device = opera.getNearbyDevices().getDeviceById(deviceId);</code></pre>
   */
	public static native DeviceList getNearbyDevices() /*-{
		return opera.nearbyDevices;
	}-*/;
}
