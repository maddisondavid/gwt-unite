package org.gwtunite.client.net.handlers;

import org.gwtunite.client.net.WebServerEventHandler;
import org.gwtunite.client.net.WebServerRequest;
import org.gwtunite.client.net.WebServerResponse;

/**
 * A handler that redirects the client to the given URL 
 * 
 */
public class RedirectHandler extends WebServerEventHandler {
	private final String url;
	
	public RedirectHandler(String url) {
		this.url = url;
	}
	
	@Override
	public void onConnection(WebServerRequest request, WebServerResponse response) { 
		super.onConnection(request, response);
		
		response.sendRedirect(url);
		response.close();
	}
}
