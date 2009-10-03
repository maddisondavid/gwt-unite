package opera.io;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Virtual file system implementation
 *
 * <p>The <code>FileSystem</code> class represents a virtual file system. Actual files are
 * connected to it by defining mount points from the actual file system. This
 * way file system access can be limited to a selected set of files rather
 * than allow unsecure operations on the local file system directly.</p>
 *
 * <p>Path references in the virtual file systems always use '/' as the path separator.</p>
 *
 * In order to retrieve an instance of this class, use the {@link #getInstance()} method
 *
 * NOTE: Currently ONLY methods that are supported by Opera Unite are implemented 
 */
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
	
	/** Returns the singleton instance of the FileSystem object */
	public static FileSystem getInstance() {
		if (!isFeatureInstalled())
			throw new FeatureUnavailableException("Filesystem is not installed");
		
		return jsniGetInstance();
	}
	
	private static native FileSystem jsniGetInstance() /*-{
		return opera.io.filesystem;
	}-*/;
	
    /**
     * The mount points currently attached to this <code>FileSystem</code>
     *
     * This is a special {@link File}</code> object that represents the root of the virtual file
     * system and serves to enumerate the existing mount points. Its path is
     * <code>/</code> and its name is empty. If you mount a directory as <code>foo</code>, the path
     * of the mount point is <code>/foo</code>.
     * 
     * To see all the mount points use {@link File#listFiles()}
     */
	public native File getMountPoints() /*-{
		return this.mountPoints	
	}-*/;

    /**
     * Mount the Application system directory.
     *
     * <p>The application directory contains the actual files and directories of
     * the current application accessing the API. For widgets, for example, the config.xml
     * and index.html and other files of the widget are found here. This directory is
     * mounted as readonly.</p>
     *
     * <p>These directories of the application are not mounted by default. You need to call this
     * method to mount and use them. Once mounted, they are available through the 
     * <code>mountPoints</code> property like other mount points. Files and directories under them 
     * can be accessed by resolving and using the mountpoint URL protocol as for normal 
     * files.</p>
     *
     * @returns <code>File</code> object representing the mounted Application directory
     */
	public native File mountApplicationFileSystem() /*-{
		return this.mountSystemDirectory("application");
	}-*/;

    /**
     * Mount the Application system directory.
     *
     * <p>The application directory contains the actual files and directories of
     * the current application accessing the API. For widgets, for example, the config.xml
     * and index.html and other files of the widget are found here. This directory is
     * mounted as readonly.</p>
     *
     * <p>These directories of the application are not mounted by default. You need to call this
     * method to mount and use them. Once mounted, they are available through the 
     * <code>mountPoints</code> property like other mount points. Files and directories under them 
     * can be accessed by resolving and using the mountpoint URL protocol as for normal 
     * files.</p>
     *
     * @param name The name the mount point should be known as
     * @returns <code>File</code> object representing the mounted Application directory
     */
	public native File mountApplicationFileSystemAs(String name) /*-{
		return this.mountSystemDirectory("application", name);
	}-*/;

    /**
     * Mount the shared system directory.
     *
     * <p>The shared directory is for sharing data from the regular file system. The directory 
     * is typically selected by the user when installing the application.</p>
     *
     * <p>This directory is mounted as read-write unless the underlying file system
     * defines it to be read-only. You should take care to protect your data by limiting access and
     * checking for exploitable code.</p>
     *
     * @returns <code>File</code> object representing the shared system directory
     */
	public native File mountSharedFileSystem() /*-{
		return this.mountSystemDirectory("shared");
	}-*/;

    /**
     * Mount the shared system directory.
     *
     * <p>The shared directory is for sharing data from the regular file system. The directory 
     * is typically selected by the user when installing the application.</p>
     *
     * <p>This directory is mounted as read-write unless the underlying file system
     * defines it to be read-only. You should take care to protect your data by limiting access and
     * checking for exploitable code.</p>
     *
     * @param name The name by which the mount point will be known 
     * @returns <code>File</code> object representing the shared system directory
     */
	public native File mountSharedFileSystemAs(String name) /*-{
		return this.mountSystemDirectory("shared", name);
	}-*/;

    /**
     * Mount the storage system directory.
     *
     * <p>The storage directory is for storing temporary files and configuration files specific
     * to the service, for example uploaded files. This directory and its contents are persisted until
     * the application is uninstalled.</p>
     *
     * @returns <code>File</code> object representing the mounted storage directory
     */
	public native File mountStorageFileSystem() /*-{
		return this.mountSystemDirectory("storage", name);
	}-*/;

    /**
     * Mount the storage system directory.
     *
     * <p>The storage directory is for storing temporary files and configuration files specific
     * to the service, for example uploaded files. This directory and its contents are persisted until
     * the application is uninstalled.</p>
     *
     * @param name The name by which the mount point will be known 
     * @returns <code>File</code> object representing the mounted storage directory
     */
	public native File mountStorageFileSystemAs(String name) /*-{
		return this.mountSystemDirectory("storage", name);
	}-*/;
	
    /**
     * Remove the given mount point.
     *
     * Removes a mount point from the virtual file system, either by referencing its symbolic name or by passing a 
     * <code>File</code> object representing the mount point. If the mount point is mounted as persistent, removing it will also
     * remove the persistence.
     *
     * @param mountpoint <code>File</code> object representing the mount point or a String with the name of the mount point.
     * @throws GENERIC_ERR If the given File or String doesn't represent a mount point.
     */
	public native void removeMountPoint(String mountPoint) throws IOException /*-{
		try {
			this.removeMountPoint(mountPoint);
		}catch(e) {
    		if (e=="GENERICL_ERR") {
    			throw @opera.io.IOException::new(Ljava/lang/String;)(this.name);
    		} else {
    			throw e;
    		}
    	}		
	}-*/;
}
