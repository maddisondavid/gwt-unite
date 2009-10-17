package org.gwtunite.client.rpc;

import java.util.Map;

import org.gwtunite.client.commons.Logging;
import org.gwtunite.client.net.OperaUniteApplication;
import org.gwtunite.client.net.WebServer;
import org.gwtunite.client.net.WebServerEventHandler;
import org.gwtunite.client.net.WebServerRequest;
import org.gwtunite.client.net.WebServerResponse;

import com.google.gwt.core.client.GWT;

public class RemoteServiceApplication extends OperaUniteApplication {
	@Override
	public void init(WebServer webServer) {
		final RemoteServiceRegistry registry = GWT.create(RemoteServiceRegistry.class);
		Logging.log("Started Registration");
		webServer.addEventListener("_index", new WebServerEventHandler() {
			@Override
			public void onConnection(WebServerRequest request, WebServerResponse response) {
				response.writeLine("<html><body>");
				response.writeLine("Services:<ul>");
				for (Map.Entry<String, RemoteService> entry : registry.getServices().entrySet()) {
					response.writeLine("<li>"+"/"+entry.getKey()+"/");
				}
				response.writeLine("</ul>");
				response.writeLine("</body></html>");
				response.close();
			}
		}, false);
		
		for (Map.Entry<String, RemoteService> entry : registry.getServices().entrySet()) {
			webServer.addEventListener(entry.getKey(), entry.getValue(), false);
		}
	}
}
