package org.gwtunite.client.rpc;

import java.util.HashMap;
import java.util.Map;


public class RemoteServiceRegistry {
	private final Map<String, GwtUniteRemoteService> remoteServices = new HashMap<String, GwtUniteRemoteService>();
	
	protected void registerService(String endPointName, GwtUniteRemoteService serviceClass) {
		remoteServices.put(endPointName, serviceClass);
	}

	public Map<String, GwtUniteRemoteService> getServices() {
		return remoteServices;
	}
}
