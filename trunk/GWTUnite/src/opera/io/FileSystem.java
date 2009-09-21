package opera.io;

import com.google.gwt.core.client.JavaScriptObject;

/** Only Opera Unite supported methods are implemented */
public final class FileSystem extends JavaScriptObject {

	protected FileSystem() {
	}
	
	/** Returns true if the FileSytem feature is installed */
	public static native boolean isFeatureInstalled() /*-{
		if (opera.io.filesystem) 
			return true
		else 
			return false;
	}-*/;
	
	/** Returns the singleton instance of the opera.io.filesystem object */
	public static FileSystem getInstance() {
		if (!isFeatureInstalled())
			throw new FeatureUnavailableException("Filesystem is not installed");
		
		return jsniGetInstance();
	}
	
	private static native FileSystem jsniGetInstance() /*-{
	}-*/;
	
	public native File getMountPoints() /*-{
		return this.mountPoints	
	}-*/;
	
	public native File mountSystemDirectory(String location, String name) /*-{
		return this.mountSystemDirectory(location, name);
	}-*/;
	
	public native void removeMountPoint(String mountPoint) /*-{
		this.removeMountPoint(mountPoint);
	}-*/;
}
