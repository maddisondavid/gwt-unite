package gwtunite.examples.client;

import org.gwtunite.client.net.OperaUniteApplication;
import org.gwtunite.client.net.WebServer;
import org.gwtunite.client.net.WebServerEventHandler;
import org.gwtunite.client.net.WebServerRequest;
import org.gwtunite.client.net.WebServerResponse;


public class HelloWorld extends OperaUniteApplication {

	@Override
	public void init(WebServer webServer) {
		webServer.addEventListener(WebServer.INDEX_PATH, new WebServerEventHandler() {
			public void onConnection(WebServerRequest request, WebServerResponse response) {
				response.writeLine("Hello World");
				response.close();
			}
		}, false);
	}
}
