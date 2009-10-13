package com.sample.template.client;

import org.gwtunite.client.net.OperaUniteApplication;
import org.gwtunite.client.net.WebServer;
import org.gwtunite.client.net.WebServerEventHandler;
import org.gwtunite.client.net.WebServerRequest;
import org.gwtunite.client.net.WebServerResponse;

public class TemplateApp extends OperaUniteApplication {

	@Override
	public void init(WebServer webServer) {
		webServer.addEventListener(WebServer.INDEX_PATH, new WebServerEventHandler() {
			@Override
			public void onConnection(WebServerRequest request, WebServerResponse response) {
				response.writeLine("<html><head><title></title></head><link rel=\"stylesheet\" type=\"text/css\" href=\"template-style.css\"/><body>");
				response.writeLine("<h1>Template Application is Running!</h1>");
				
				response.writeLine("<h2>WebServer Details:</h2>");
				displayWebServerDetailsTable(request, response);
				
				response.writeLine("<h2>Request Details:</h2>");
				displayDetailsTable(request, response);

				response.writeLine("<h2>Headers Sent:</h2>");
				displayHeadersTable(request, response);				
				
				response.writeLine("<h2>Query String Details:</h2>");
				displayQueryTable(request, response);
		
				response.writeLine("<body></html>");
				response.close();
			}
		}, true);
	}
	
	private void displayWebServerDetailsTable(WebServerRequest request, WebServerResponse response){
		WebServer webServer = WebServer.getInstance();
		
		response.writeLine("<table border=\"1\">");
		displayTableRow(response, "DeviceName:", webServer.getDeviceName());
		displayTableRow(response, "HostName", webServer.getHostName());
		displayTableRow(response, "ServiceName", webServer.getCurrentServiceName());
		displayTableRow(response, "Service Path", webServer.getCurrentServicePath());
		displayTableRow(response, "Downloaded From", (webServer.getOriginURL() == null ? "NOT SPECIFIED":webServer.getOriginURL()));
		displayTableRow(response, "Port", webServer.getPort());
		displayTableRow(response, "Using Proxy", webServer.getProxyName());
		displayTableRow(response, "Public IP Address", (webServer.getPublicIP() == null ? "NOT SPECIFIED":webServer.getPublicIP()));
		displayTableRow(response, "Username", webServer.getUserName());
		displayTableRow(response, "Port", webServer.getPort());

		response.writeLine("</table>");
		
	}
	
	private void displayDetailsTable(WebServerRequest request, WebServerResponse response){
		response.writeLine("<table border=\"1\">");
		displayTableRow(response, "URI", request.getUri());
		displayTableRow(response, "Host", request.getHost());
		displayTableRow(response, "Method", request.getMethod());
		displayTableRow(response, "Protocol", request.getProtocol());
		displayTableRow(response, "RemoteIP", request.getRemoteIP());
		displayTableRow(response, "Was Unite Proxy Used?", request.getConnection().isProxied());
		displayTableRow(response, "Was Local Connection?", request.getConnection().isLocal());
		displayTableRow(response, "Was it the admin.XXX URL?", request.getConnection().isOwner());
		response.writeLine("</table>");
		
	}

	private void displayHeadersTable(WebServerRequest request, WebServerResponse response){
		response.writeLine("<table border=\"1\">");
			for (String name : request.getHeaderNames()) {
				for (String value : request.getHeader(name)) {
					displayTableRow(response, name, value);
				}
			}
		response.writeLine("</table>");
		
	}
	
	private void displayQueryTable(WebServerRequest request, WebServerResponse response){
		response.writeLine("<table border=\"1\">");
			for (String name : request.getQueryItemNames()) {
				for (String value : request.getQueryItem(name)) {
					displayTableRow(response, name, value);
				}
			}
		response.writeLine("</table>");
		
	}
	
	private void displayTableRow(WebServerResponse response, String label, Object data) {
		response.writeLine("<tr><th>"+label+"</th><td>"+data+"</td></tr>");
	}
}
