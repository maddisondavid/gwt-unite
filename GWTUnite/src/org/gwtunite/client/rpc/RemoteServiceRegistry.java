package org.gwtunite.client.rpc;

import java.util.HashMap;
import java.util.Map;


public class RemoteServiceRegistry {
	private final Map<String, RemoteService> remoteServices = new HashMap<String, RemoteService>();
	
	protected void registerService(String endPointName, RemoteService serviceClass) {
		remoteServices.put(endPointName, serviceClass);
	}

	public Map<String, RemoteService> getServices() {
		return remoteServices;
	}
}
