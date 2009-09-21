package gwtunite.examples.client;

import java.util.ArrayList;
import java.util.List;

import opera.io.OperaUniteService;
import opera.io.Utils;
import opera.io.WebServer;
import opera.io.WebServerEventHandler;
import opera.io.WebServerRequest;
import opera.io.WebServerResponse;
import opera.widget.Widget;
import opera.widget.Widget.NotificationCallback;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;

public class BlogService extends OperaUniteService {
	private static final String TITLE_INPUT = "title";
	private static final String TEXT_INPUT = "text";
	
	private final List<BlogEntry> entries = new ArrayList<BlogEntry>();
	private WebServer webServer;
	
	private WebServerEventHandler indexHandler = new WebServerEventHandler() {
		@Override protected void onConnection(WebServerRequest request, WebServerResponse response) {
			Utils.log("Starting");
//			Document d = Markuper.getInstance().getHtmlHelper().parseFromString("<html><head><title>My Test</title></head><body></body></html>");
	//		log(d.getTitle());
			Utils.log("Dispatching to "+webServer.getCurrentServicePath() + "showEntries");
			request.setUri(webServer.getCurrentServicePath() + "showEntries");
			response.closeAndRedispatch();
		}
	};

	private WebServerEventHandler showEntriesHandler = new WebServerEventHandler() {
		@Override protected void onConnection(WebServerRequest request, WebServerResponse response) {
			response.writeLine("<html><head><title>My Blog Service</title></head><body>");
			response.writeLine("<h2>Entries:</h2>");
			
			for (BlogEntry entry : entries) {
				response.writeLine("<table>");
				response.writeLine("<tr><td>"+entry.getTitle()+"</td></tr>");
				response.writeLine("<tr><td>"+entry.getText()+"</td></tr>");
				response.writeLine("</table>");
				response.writeLine("<hr/>");
			}
			response.writeLine("<a href=\"enterEntry\">Add New Entry</a>");
			response.writeLine("</body></html>");
			response.close();
		}
	};	
	
	private WebServerEventHandler entryHandler = new WebServerEventHandler() {
		@Override protected void onConnection(WebServerRequest request, WebServerResponse response) {
			response.writeLine("<html><head><title>My Blog Service</title></head><body>");
			
			response.writeLine("<form method=\"POST\" action=\"postEntry\">");
			response.writeLine("Title: <input type=\"text\" name=\""+TITLE_INPUT+"\"/><br/>");
			response.writeLine("Entry: <input type=\"text\" name=\""+TEXT_INPUT+"\"/><br/>");
			response.writeLine("<input type=\"submit\" name=\"addEntry\" value=\"Add Entry\"/>");
			response.writeLine("</form>");
			
			response.writeLine("</body></html>");
			response.close();
		}
	};	

	private WebServerEventHandler addHandler = new WebServerEventHandler() {
		@Override protected void onConnection(WebServerRequest request, WebServerResponse response) {
			String title = request.getItem(TITLE_INPUT)[0];
			String bodyText = request.getItem(TEXT_INPUT)[0];
		
			entries.add(new BlogEntry(title, bodyText));
			
			request.setUri(webServer.getCurrentServicePath() + "_index");
			response.closeAndRedispatch();
		}
	};		
	
	private WebServerEventHandler fetchPageHandler = new WebServerEventHandler() {
		@Override protected void onConnection(WebServerRequest request, WebServerResponse response) {
			final Widget widgetRuntime = Widget.getInstance();
//			Method method = new Method
			RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, "http://www.google.co.uk");
			try {
				Utils.log("starting fetch from "+builder.getUrl());
				builder.sendRequest(null, new RequestCallback() {
					public void onError(Request arg0, Throwable arg1) {
						Utils.log("ERROR !");
					}

					public void onResponseReceived(Request req, Response rsp) {
						Utils.log("Got response");
						Utils.log(rsp.getText());
						widgetRuntime.showNotification("Got Response",new NotificationCallback() {
							public void onNotificationClicked() {
								Utils.log("Notification clicked!");
								widgetRuntime.openURL("http://www.yahoo.com");
							}
						});
					}
				});
			} catch (RequestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				response.close();
			}
			
		}
	};		 
	
	public void init(WebServer webServer) {
		Utils.log("Started");
		this.webServer = webServer;
		webServer.addEventListener("showEntries", showEntriesHandler,false);
		webServer.addEventListener("enterEntry", entryHandler,false);
		webServer.addEventListener("postEntry", addHandler,false);
		webServer.addEventListener("fetch", fetchPageHandler,false);
		webServer.addEventListener(WebServer.INDEX_PATH, indexHandler,false);
	}
	
	private static class BlogEntry {
		private final String title;
		private final String text;
		
		public BlogEntry(String title, String text){
			this.title = title;
			this.text = text;
		}
		
		public String getTitle() {
			return title;
		}
		
		public String getText() {
			return text;
		}
	}
}
