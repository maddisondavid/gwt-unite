package org.gwtunite.client.rpc;

import java.util.Map;

import org.gwtunite.client.commons.Logging;
import org.gwtunite.client.net.OperaUniteEntryPoint;
import org.gwtunite.client.net.WebServer;

import com.google.gwt.core.client.GWT;

public class RemoteServiceEntryPoint extends OperaUniteEntryPoint {
	@Override
	public void init(WebServer webServer) {
		final RemoteServiceRegistry registry = GWT.create(RemoteServiceRegistry.class);
		for (Map.Entry<String, RemoteService> entry : registry.getServices().entrySet()) {
			webServer.addEventListener(entry.getKey(), entry.getValue(), false);
		}
	}
}
