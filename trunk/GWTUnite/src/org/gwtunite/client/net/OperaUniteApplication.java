package org.gwtunite.client.net;

import org.gwtunite.client.commons.Logging;

import com.google.gwt.core.client.EntryPoint;

/** Base class of all OperaUnite application 
 * 
 * 
 */
public abstract class OperaUniteApplication implements EntryPoint{

	public void onModuleLoad() {
		try {
			WebServer webServer = org.gwtunite.client.net.WebServer.getInstance();
			init(webServer);
		} catch(Exception e) {
			Logging.handleException(e);
		}
	}
	
	/**
	 * Called during the startup phase of this service.  Services should use
	 * this opportunity to register any required handlers. See {@link WebServer#addEventListener(String, WebServerEventHandler, boolean)}
	 */
	public abstract void init(WebServer webServer);
}
