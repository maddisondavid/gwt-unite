package org.gwtunite.client.rpc;

import java.util.Map;

import org.gwtunite.client.net.OperaUniteApplication;
import org.gwtunite.client.net.WebServer;

import com.google.gwt.core.client.GWT;

public class RemoteServiceApplication extends OperaUniteApplication {
	@Override
	public void init(WebServer webServer) {
		final RemoteServiceRegistry registry = GWT.create(RemoteServiceRegistry.class);
		for (Map.Entry<String, RemoteService> entry : registry.getServices().entrySet()) {
			webServer.addEventListener(entry.getKey(), entry.getValue(), false);
		}
	}
}
