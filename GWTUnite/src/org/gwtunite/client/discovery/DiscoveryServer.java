package org.gwtunite.client.discovery;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Server that discovers other devices running in the OperaUnite network
 */
public final class DiscoveryServer extends JavaScriptObject {

	protected DiscoveryServer() {
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
