## Using GWT-Unite RPC ##
GWT Unite RPC has been written to be completely compatible with standard GWT RPC, in fact, if you already have a GWT client and server written, there is only one small code change to make (changing the RemoteServiceServlet to GwtUniteRemoteService).

Since everything is compatible, you develop a GWT-Unite service as you would a standard GWT RPC service.  For more information, see : http://code.google.com/webtoolkit/doc/1.6/DevGuideServerCommunication.html#DevGuideRemoteProcedureCalls

For completion, here is a complete tutorial on developing a simple RPC client and server.

## Module Structure ##
We're going to split the application into three distict modules :

  * ClientSide
  * ServerSide
  * Remote Interfaces

The ServerSide module will be compiled into an OperaUnite application, whereas the ClientSide module needs to be accessible to the browser, hence the reason they need to be in seperate modules, and compiled separately.  Both the client and server need access to the Remote interfaces and that's why these are placed in a common module.

So that we can keep everything nice and neatly together, we're also going to override the default GWT source package names.  This leaves us with the following package structure.

```
gwtunite.testrpc
gwtunite.testrpc.clientside  <-- Holds the client application
gwtunite.testrpc.common      <-- Holds the common RemoteService interfaces
gwtunite.testrpc.serverside  <-- Hosts the RemoteServices within OperaUnite
```

## Remote Interfaces ##
Remote interfaces are defined in exactly the same was as if you were using standard GWT RPC :

```
@RemoteServiceRelativePath("MyGreetService")
public interface GreetServer extends RemoteService {

	public String greet(String name);
	public String[] greetPeople(String greeting, Person[] people);
}
```

Our service has two methods, one that returned "Hello "+name and one that returns an array of greetings.  One important thing to note is the _@RemoteServiceRelativePath_ annotation.  Your interface MUST include this standard GWT annotation which tells GWTUnite the URL under which to register your server.  In this case our server will be registered under _http://[deviceurl](deviceurl.md)/[servicename](servicename.md)/MyGreetService_.  If you don't include this annotation, GWTUnite will log the problem and fail to compile your server.

When using GWT RPC you also have to define the asynchronous interface and ours looks as such :
```
public interface GreetServerAsync {

	public void greet(String name, AsyncCallback<String> callback);
	public void greetPeople(String greeting, Person[] people, AsyncCallback<String[]> callback);
}
```
To complete the example, here's the definition of the Person object which is just a simple POJO :
```
public class Person implements Serializable {
	private String salutation;
	private String surname;
	
	public Person() {} // Required by GWT RPC
	
	public Person(String salutation, String surname) {
		this.setSalutation(salutation);
		this.setSurname(surname);
	}

	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}

	public String getSalutation() {
		return salutation;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getSurname() {
		return surname;
	}
}
```
For completion we need a Common.gwt.xml module definition to be placed in _gwtunite.testrpc_:
```
<module>
	<source path="common"/>
</module>
```

## The Server ##
Again, the server code is written as if you were writing a standard GWT RPC service, but instead of the service extending the RemoteServiceServlet it needs to extend GwtUniteRemoteService.  This means our server code looks as follows:
```
public class MyTestGreetServer extends GwtUniteRemoteService implements GreetServer{

	public String greet(String name) {
		Logging.log("I got the message "+name);
		String s = "Hello "+name;
		Logging.log("Returning Hello "+name);
		return s;
	}

	public String[] greetPeople(String greeting, Person[] people) {
		String[] greetings = new String[people.length];
		
		for (int i=0;i<people.length;i++) {
			greetings[i]="Hello "+people[i].getSalutation()+" "+people[i].getSurname();
		}
		
		return greetings;
	}
}
```

Here there are two methods the client can call.  _Greet_ returns the string "Hello [Name](Name.md)" and _greetPeople_ returns an array of greetings for each _Person_ passed in.  This looks exactly the same as if it were a standard GWT RPC servlet except for the _GwtUniteRemoteService_ which must be extended.

The last thing is our ServerSide.gwt.xml module definition for Server which will be placed in _gwtunite\_testrpc_:
```
<module>
	<inherits name="org.gwtunite.RPC"/>
	<inherits name="gwtunite.testrpc.Common"/>
		
	<source path="serverside"/>
	
	<entry-point class="gwtunite.testrpc.serverside.ClientRedirector"/>
</module>
```
Notice that your module definition **MUST** inherit org.gwtunite.RPC.  This is required, otherewise the plumbing to register and invoke your RemoteService's won't be created.

Notice also that we have an _entry-point_ definition.  This isn't a requirement of an RPC application, GWT-Unite will create an entry point to register your service, however we must remember that we have a GWT client application that our service must also serve up.  If anybody accesses the _Index_ page our our server, we want the clients index.html page sent back to the server and thus we use the GWT-Unite built in RedirectorHandler.  The servers ClientRedirector looks as follows:
```
public class ClientRedirector extends OperaUniteEntryPoint {

	@Override
	public void init(WebServer webServer) {
		webServer.addEventListener(WebServer.INDEX_PATH, new RedirectHandler("index.html"), false);
	}
}
```
When a client accesses the index page, a redirect will be sent back for the index.html page.  Packaging the client and server is discussed further in the compilation section.

## The Client ##
At this point you could compile the server and you'd have an RPC service, but it's probably better if we have some client to call it!

The client code is written completely in standard GWT RPC, that is, as if it were communicating with a RemoteServiceServlet.  It's a bit long, but here is the complete Client code :
```
public class RPCTestApp implements EntryPoint {

	@Override
	public void onModuleLoad() {
		RootPanel.get().add(createPanel());
	}
	
	private Widget createPanel() {
		FlowPanel panel = new FlowPanel();
		
		Button greetBtn = new Button("Greet");
		greetBtn.addClickHandler(new ClickHandler() {
			@Override public void onClick(ClickEvent event) {
				greetName("Fred");
			}
		});
		
		panel.add(greetBtn);

		Button greetPeopleBtn = new Button("Greet People");
		greetPeopleBtn.addClickHandler(new ClickHandler() {
			@Override public void onClick(ClickEvent event) {
				greetPeople("Hello",new Person[]{
						new Person("Dr","No"),
						new Person("Mr","Smith"),
						new Person("Mrs","Smith")});
			}
		});
		
		panel.add(greetPeopleBtn);
		
		return panel;
	}
	
	private void greetName(String name) {
		GreetServerAsync server = GWT.create(GreetServer.class);
		server.greet(name, new AsyncCallback<String>() {
			@Override public void onFailure(Throwable caught) {
				Window.alert("I've Failed : "+caught.getMessage());
			}
			
			@Override public void onSuccess(String result) {
				Window.alert("Result="+result);
			}
		});
	}
	
	private void greetPeople(String greeting, Person[] people) {
		GreetServerAsync server = GWT.create(GreetServer.class);
		server.greetPeople(greeting, people, new AsyncCallback<String[]>() {
			@Override public void onFailure(Throwable caught) {
				Window.alert("I've Failed : "+caught.getMessage());
			}
			
			@Override public void onSuccess(String[] result) {
				StringBuffer buffer = new StringBuffer();
				for(String s : result) {
					buffer.append(s+"\n");
				}
				
				Window.alert(buffer.toString());
			}
		});
	}
}
```

As you can see, this is a standard GWT application, there's no GWT-Unite code anywhere in it.  It displays two buttons that invoke the particular server methods and displays the response in alert boxes.

Again, for completion, here is the ClientSide.gwt.xml module definition to be placed in gwtunite.testrpc :
```
<module>
	<inherits name="com.google.gwt.user.User" />
	<inherits name="gwtunite.testrpc.Common"/>
	
	<source path="clientside"/>
	<public path="clientpublic"/>
	
	<entry-point class="gwtunite.testrpc.clientside.RPCTestApp"/>
</module>
```

You'll notice we have a _public_ node in the module definition.  This node defines the path to artifacts which need to be included with the compiled application.  I've modified it from the standard public  In the case of the GWT client this is the bootstrap HTML page which will pull the compiled client code into the browser.

If we were to use the standard _public_ folder for this file, it would also be picked up by our ServerSide.gwt.xml module and thus overwrite the generated html file for the server.  For that reason we put the client public files in a clientpublic folder.

## Compilation ##
Compilation of the application can be a tricky thing to get your head around seeing as we really need to compile two GWT applications (client AND server).  After compilation, we want the resulting .UA file to have the following structure :

```
ROOT
 |--index.html <--- Server side boot strap
 |--config.xml <--- OperaUnite application configuration
 |--scripts
 |     |--script.js <--- Compiled Server side code
 |
 |--public_html
 |     |--index.html <--- Client bootstrap
 |     |--gwtunite.testrpc.ClientSide.nocache.js <--- Compiled Client code 

```

To compile, we'll use the standard gwt-unite-application-builder.xml which has been improved in V2 to handle client/server compilations.  A build.xml ANT script looks as follows:
```
<project name="RPCTestApp" basedir=".">
	<property name="serverSideModule" value="gwtunite.testrpc.ServerSide"/>
	<property name="clientSideModule" value="gwtunite.testrpc.ClientSide"/>
		
	<property name="applicationName" value="RPCTest"/>
	
	<ant antfile="gwt-unite-application-builder.xml" inheritall="true" target="package"/>
</project>
```

For completeness, our RPCTest-config.xml (which should be placed in your projects conf directory),  looks as follows :
```
<widget network="public">
   <widgetname>RPC Test</widgetname>
   <description>A GWT-Unite RPC Test Application</description>
   <author>
     <name>David Maddison</name>
     <organisation>org.gwtunite</organisation>
  </author>
  <feature name="http://xmlns.opera.com/webserver">
     <param name="type" value="service"/>
     <param name="servicepath" value="rpctest" />
  </feature>
</widget>
```