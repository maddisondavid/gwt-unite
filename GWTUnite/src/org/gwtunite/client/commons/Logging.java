package org.gwtunite.client.commons;

public class Logging {

	public static native void log(String msg) /*-{
		opera.postError(msg);
	}-*/;
	
	public static void handleException(Throwable e) {
		log("Unhandled Exception : "+e);
	}
}
