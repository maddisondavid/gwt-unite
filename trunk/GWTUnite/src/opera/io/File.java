package opera.io;

import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;

final public class File extends JavaScriptObject {

	protected File() {
	}
	
	public native File getParent() /*-{
		return this.parent;
	}-*/;
	
	public native boolean isReadOnly() /*-{
		return this.readOnly;
	}-*/; 
	
	public native boolean exists() /*-{
		return this.exists;
	}-*/;
	
	public native boolean isFile() /*-{
		return this.isFile;
	}-*/;
	
	public native boolean isDirectory() /*-{
		return this.isDirectory;
	}-*/;
	
	public native boolean isArchive() /*-{
		return this.isArchive;
	}-*/;
	
	public native String getMetaData() /*-{
		return this.metaData;
	}-*/;
	
    public native Date getCreated() /*-{
    	return this.created;
    }-*/;

    public native Date getModified() /*-{
        return this.modified;
    }-*/;

    public native String getName() /*-{
    	return this.name;
    }-*/;
    
    public native String getPath() /*-{
    	return this.path;
    }-*/;
    
    public native String getNativePath() /*-{
    	return this.nativePath;
    }-*/;
    
    public native int getMaxPathLength() /*-{
    	return this.maxPathLength;
    }-*/;
    
    public native int getFileSize() /*-{
    	return this.fileSize;
    }-*/;
    
    public native int getLength() /*-{
    	return this.length;
    }-*/;
    
    public native FileStream open(String path, int mode) /*-{
    	return this.open(path, mode);
    }-*/;

    public native File copyTo(String path, boolean overwrite) /*-{
		return this.copyTo(path, overwrite);
	}-*/;
    
    public native File copyTo(String path, boolean overwrite, Object callback) /*-{
    	return this.copyTo(path, overwrite, callback);
    }-*/;
    
    public native File moveTo(String path, boolean overwrite) /*-{
		return this.moveTo(path, overwrite);
	}-*/;

    public native File moveTo(String path, boolean overwrite, Object callback) /*-{
		return this.moveTo(path, overwrite, callback);
	}-*/;  

    public native File createDirectory(String dirFile) /*-{
		return this.createDirectory(dirFile);
	}-*/;
    
    public native File createDirectory(File dirFile) /*-{
    	return this.createDirectory(dirFile);
    }-*/;
    
    public native boolean deleteDirectory(String directory, boolean recursive) /*-{
		return this.deleteDirectory(directory, recursive);
	}-*/;    
    
    public native boolean deleteDirectory(File directory, boolean recursive) /*-{
    	return this.deleteDirectory(directory, recursive);
    }-*/;
    
    public native boolean deleteFile(String file) /*-{
		return this.deleteFile(file);
	}-*/;    

    public native boolean deleteFile(File file) /*-{
		return this.deleteFile(file);
	}-*/;
    
    public native File[] getContents() /*-{
    	return this;
    }-*/;
    
    public native void refresh() /*-{
    	this.refresh();
    }-*/;
    
    public native File resolve(String path) /*-{
    	return this.resolve(path);
    }-*/;
    
    /** _ prefix required as toString clashes with JavaScriptObject::toString() */
    public native String _toString() /*-{
    	return this.toString();
    }-*/;
}