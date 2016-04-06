## Looking at the Hello World in more detail ##
The HelloWorld service is VERY simple, however it does show a few important concepts.

### The Service ###
The following is all the code that's required for the Hello World Service :
```
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
```

As you can see, GWT-Unite services must extend OperaUniteService and must implement the init method.  The init method will be passed the WebServer instance when the service is started.

This code doesn't look to dissimilar to a JavaScript counterpart in the fact that the first thing the init method should do is add event listeners for each URL.  Here we can see that you must register an instance of WebServerEventHandler against a path (in this case WebServer.INDEX\_PATH which is _index_).

Unlike it's JavaScript counterpart, GWT Unite removes the requirement to always retrieve request and response from the WebServerEventHandler, and instead extracts these from the request and presents them to the WebServerEventHandlers onConnection method.  This not only removes duplicate code, but also makes the API work in a similar to the Java Servlet API.

## GWT module definition ##
The GWT module defintion for the Hello World service looks as follows:
```
<module rename-to="helloWorldService">
    <inherits name="opera.Unite" />

	<entry-point class="gwtunite.examples.client.HelloWorld"/>
 </module>
```
All Opera Unite services MUST inherit the opera.Unite module in order for the correct Opera Unite boot strap code to be produce.  The GWT entry point should also be set to the class which extends OperaUniteService (this is because OperaUniteService is in reality a  GWT Entry point)

## Service Configuration ##
Each Opera Unite service requires a config.xml file which provides Opera with information about the service.  In GWT-Unite, configuration files are created as standard xml files and incorporated into the service archive after the compilation phase.

## The Builder ##
gwt-unite-service-builder.xml contains a few simple targets that assist in the creation of an Opera Unite service.  The targets in the ANT file are NOT designed to be called directly, but rather from a parent script which sets up the necessary properties.  Build-helloworld.xml shows one of these parent build files.