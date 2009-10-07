package org.gwtunite.testing.client.tests;

import org.gwtunite.client.commons.Logging;
import org.gwtunite.client.discovery.DeviceDescriptor;
import org.gwtunite.client.discovery.DiscoveryServer;
import org.gwtunite.testing.client.framework.Test;
import org.gwtunite.testing.client.framework.TestCase;


public class DiscoveryTests extends TestCase {

	@Test
	public void nearbyDevicesAreListed() {
		Logging.log(""+DiscoveryServer.getNearbyDevices().getAllDevices().length);
		
		DeviceDescriptor[] devices = DiscoveryServer.getNearbyDevices().getAllDevices();
		assertEquals(1, devices.length);
		assertEquals("home", devices[0].getUniteDeviceName());
	}
}
