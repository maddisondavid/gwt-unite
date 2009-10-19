package org.gwtunite.client.net;

import java.util.HashMap;
import java.util.Map;

public class WebServerEventHandlerRegistry {
	private final Map<String, WebServerEventHandler> handlers = new HashMap<String, WebServerEventHandler>();
	
	protected void registerHandler(String pathFragment, WebServerEventHandler handler) {
		handlers.put(pathFragment, handler);
	}
	
	public Map<String, WebServerEventHandler> getHandlers() {
		return handlers;
	}
}
