package opera.io;

import com.google.gwt.core.client.EntryPoint;

/** Extend to create your service */
public abstract class OperaUniteService implements EntryPoint{

	public void onModuleLoad() {
		try {
			WebServer webServer = opera.io.WebServer.getInstance();
			init(webServer);
		} catch(Exception e) {
			Utils.handleException(e);
		}
	}
	
	
	public abstract void init(WebServer webServer);
}
