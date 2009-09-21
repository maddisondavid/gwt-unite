package gwtunite.examples.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import opera.io.OperaUniteService;
import opera.io.WebServer;
import opera.io.WebServerEventHandler;
import opera.io.WebServerRequest;
import opera.io.WebServerResponse;

/**
 * A GWT Unite version of the Opera Unite Example Service described in the Developers primer
 * 
 */
public class BlogService extends OperaUniteService {
	private static final String TITLE_INPUT = "title";
	private static final String TEXT_INPUT = "text";
	
	private final List<BlogEntry> entries = new ArrayList<BlogEntry>();
	private WebServer webServer;
	
	private WebServerEventHandler entryListHandler = new WebServerEventHandler() {
		@Override protected void onConnection(WebServerRequest request, WebServerResponse response) {
			    response.write( "<!DOCTYPE html>"+
			        			"<html><head><title>Entries</title></head>" +
			        			"<body><ul>");

			    for (int i=0;i<entries.size();i++) {
			    	BlogEntry entry = entries.get(i);
			    	response.write("<li>"+entry.date+": <a href=\"entry?id="+i+"\">"+entry.title+"</a></li>");
			    }
			    
			    response.write("</ul>" +
			    			   "<p><a href=\"form\">Add entry</a>.</p>" +
			    			   "</body></html>");
			    response.close();
			}
	};	
	
	private WebServerEventHandler entryHandler = new WebServerEventHandler() {
		@Override protected void onConnection(WebServerRequest request, WebServerResponse response) {
		    int index = Integer.parseInt(request.getQueryItem("id")[0]);
		    BlogEntry entry = entries.get(index);
		    
		    response.write("<!DOCTYPE html>" +
		    			  "<html><head><title>"+entry.title+"</title></head>" +
		    			  "<body><h1>"+entry.title+"</h1>" +
		    			  "<p>"+entry.date+"</p>" +
		    			  "<div>"+entry.text+"</div>" +
		        		  "</body></html>");
		    response.close();			
		}
	};		
	
	private WebServerEventHandler formHandler = new WebServerEventHandler() {
		@Override protected void onConnection(WebServerRequest request, WebServerResponse response) {
		    response.write("<!DOCTYPE html>" +
		    			   "<html><head><title>Add entry</title></head>" +
		    			   "<body><h1>Add entry</h1>" +
		    			   "<form method=\"post\" action=\"save\">" +
		    			   "<p><label for=\"namefield\">Title</label> <input id=\"nameField\" type=\"text\" name=\"title\"></p>" +
		    			   "<p><label for=\"textArea\">Text</label> <textarea id=\"textArea\" name=\"text\"></textarea></p>" +
		    			   "<p><input type=\"submit\" name=\"Add entry\"></p>" +
		    			   "</form>" +
		    			   "</body></html>");
		    response.close();
		}
	};		
	
	private WebServerEventHandler saveHandler = new WebServerEventHandler() {
		@Override protected void onConnection(WebServerRequest request, WebServerResponse response) {
		    String title = request.getBodyItem("title")[0];
		    String text = request.getBodyItem("text")[0];

		    entries.add(new BlogEntry(title, text, new Date()));

		    //Redirect back to the index of the service
		    response.setStatusCode(302);
		    response.setResponseHeader("Location", webServer.getCurrentServicePath());
		    response.close();
		}
	};	
	
	public void init(WebServer webServer) {
		this.webServer = webServer;
		webServer.addEventListener(WebServer.ALL_REQUESTS_PATH, entryListHandler,false);
		webServer.addEventListener("entry", entryHandler,false);
		webServer.addEventListener("form", formHandler,false);
		webServer.addEventListener("save", saveHandler,false);
	}
	
	private static class BlogEntry {
		private final String title;
		private final String text;
		private final Date date;
		
		public BlogEntry(String title, String text, Date date){
			this.title = title;
			this.text = text;
			this.date = date;
		}
		
		public String getTitle() {
			return title;
		}
		
		public String getText() {
			return text;
		}
		
		public String getDate() {
			return date.toString();
		}
	}
}
