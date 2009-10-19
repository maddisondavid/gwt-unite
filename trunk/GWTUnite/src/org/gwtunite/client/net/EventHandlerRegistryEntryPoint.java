package org.gwtunite.client.net;

import java.util.Map;

import com.google.gwt.core.client.GWT;

/**
 * Entry point which automatically registers all the WebServerEventHandlers that are marked
 * with the correct annotation
 */
class EventHandlerRegistryEntryPoint extends OperaUniteEntryPoint {

	@Override
	public void init(WebServer webServer) {
		WebServerEventHandlerRegistry registry = GWT.create(WebServerEventHandlerRegistry.class);
		
		for (Map.Entry<String, WebServerEventHandler> entry : registry.getHandlers().entrySet()) {
			webServer.addEventListener(entry.getKey(),entry.getValue(), false);
		}
	}
}
