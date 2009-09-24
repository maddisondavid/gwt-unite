package opera.io;

public class Utils {

	public static native void log(String msg) /*-{
		opera.postError(msg);
	}-*/;
	
	public static void handleException(Exception e) {
		log("Unhandled Exception : "+e);
	}
}
