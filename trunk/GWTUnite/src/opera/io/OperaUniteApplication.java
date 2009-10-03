package opera.io;

import com.google.gwt.core.client.EntryPoint;

/** Base class of all OperaUnite application */
public abstract class OperaUniteApplication implements EntryPoint{

	public void onModuleLoad() {
		try {
			WebServer webServer = opera.io.WebServer.getInstance();
			init(webServer);
		} catch(Exception e) {
			Utils.handleException(e);
		}
	}
	
	/**
	 * Called during the startup phase of this service.  Services should use
	 * this opportunity to register any required handlers. See {@link WebServer#addEventListener(String, WebServerEventHandler, boolean)}
	 */
	public abstract void init(WebServer webServer);
}
