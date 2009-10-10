package org.gwtunite.client.commons;

import com.google.gwt.core.client.JavaScriptException;

public class Logging {

	public static native void log(String msg) /*-{
		opera.postError(msg);
	}-*/;
	
	public static void handleException(Throwable e) {
		if (e instanceof JavaScriptException) {
			log("Unhandled Exception : "+e.getMessage());
		} else {
			log("Unhandled Exception : "+e);
		}
	}
}
