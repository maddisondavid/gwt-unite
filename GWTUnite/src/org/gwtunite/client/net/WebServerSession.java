package org.gwtunite.client.net;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * NOTE: This is an experimental class, it is not included in the OperaUnite API,
 * however it DOES appear to be in the OperaUnite runtime 
 *
 */
final public class WebServerSession extends JavaScriptObject {
	
	protected WebServerSession() {
	}
	
	public native String getId() /*-{
		return this.id;
	}-*/;
	
	public native String getType() /*-{
		return this.type;
	}-*/;
	
	public native String getUserName() /*-{
		return this.userName;
	}-*/;
	
	public native void logout() /*-{
		this.logout();
	}-*/;
}
