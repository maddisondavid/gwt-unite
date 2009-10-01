package gwtunite.examples.client;

import opera.io.OperaUniteService;
import opera.io.WebServer;
import opera.io.WebServerEventHandler;
import opera.io.WebServerRequest;
import opera.io.WebServerResponse;

public class HelloWorld extends OperaUniteService {

	@Override
	public void init(WebServer webServer) {
		webServer.addEventListener(WebServer.INDEX_PATH, new WebServerEventHandler() {
			protected void onConnection(WebServerRequest request, WebServerResponse response) {
				response.writeLine("Hello World");
				response.close();
			}
		}, false);
	}
}
