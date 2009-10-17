package org.gwtunite.client.file;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Class representing files and directories.
 *
 * <p>Objects of this class can refer to regular files, directories or archives of files. In the
 * two latter cases, the object contains references to its subdirectories and files.</p>
 *
 * <p>{@link FileSystem#getMountPoints()}</code> and any mounted directories are <code>File</code> objects. You
 * may make a <code>File</code> object by calling the <code>resolve()</code> method on any of these.</p>
 *
 * <p>If the file specifies a directory, the {@link File#listFiles()} method provides a list of all the files in the 
 * directory.  The following shows an example of this :</p>
 * <pre><code>
 * 		File dir = mp.resolve("path/to/dir"); //Get a File object referring to the directory
 *		for (File file : dir.listFiles())  {
 *    		response.write(file.getName());
 *		}
 * </code></pre>
 */
final public class File extends JavaScriptObject {
	
	public enum FileMode {
		READ(1),
		WRITE(2),
		APPEND(4),
		UPDATE(8);
		
		int value;
		private FileMode(int value) {
			this.value = value;
		}
		public int getValue() {
			return value;
		}
	}
	
	
	protected File() {
	}
	
	/**
     * The parent File of this File, or null if it has no parent.
     *
     * <p>In most cases, the parent will be a directory. It can also be an archive. For
     * Files that are mount points, this property is <code>null</code>.</p>
     * 
     * @return A file object representing the parent file (an archive or directory) or null if there is no 
     *         parent (i.e. A mount point)
     */
	public native File getParentFile() /*-{
		return this.parent;
	}-*/;
	
    /**
     * Tests whether or not this File is read only.
     *
     * <p>Files mounted in the application directory, using {@link FileSystem#mountApplicationFileSystem()} are
     * not writeable. Otherwise, the physical file system determines whether or not the File is writeable.</p>
     * 
     * @return true if this file is read only 
     */
	public native boolean isReadOnly() /*-{
		return this.readOnly;
	}-*/; 
	
    /**
     * Tests whether or not this File exists in the physical file system.
	 *
     * @return true if the file exists on the physical file system
     */
	public native boolean exists() /*-{
		return this.exists;
	}-*/;
	
    /**
     * Test whether or not this is a regular file.
     * 
     * @return true if this object represents a file
     */
	public native boolean isFile() /*-{
		return !this.isDirectory;
	}-*/;
	
    /**
     * Tests whether or not this is a directory.
     * 
     * @return true if this object represents a directory
     */
	public native boolean isDirectory() /*-{
		return this.isDirectory;
	}-*/;
	
    /**
     * Tests whether or not this File is a compressed archive, like a zip or gzip file.
     *
     * <p>Note that archives will also have the <code>isDirectory</code> and </code>isFile</code>
     * properties set. You may both use {@link #resolve(String)} to resolve files inside the archive, or open
     * the archive file using {@link #open(FileMode...)}.</p>
     *
     * <p class="ni">Currently only a subset of the ZIP format is currently supported as archives.</p>
     * 
     * @return true if this object represents an archive file (zip or gzip)
     */
	public native boolean isArchive() /*-{
		return this.isArchive;
	}-*/;
	
    /**
     * Meta data for this file.
     *
     * <p>This property holds meta data for special types of files, for example the file name of an uploaded
     * file. For normal files, this property is <code>null.</code>
     * 
     * @return special meta data for this file
     */
	public native String getMetaData() /*-{
		return this.metaData;
	}-*/;
	
    /**
     * The time and date this File was created.
     * 
     * @return the date this file was created
     */
    public native Date getCreated() /*-{
    	return this.created;
    }-*/;

    /**
     * The time and date this File was last modified.
     * 
     * @return the date this file was last modified
     */
    public native Date getModified() /*-{
        return this.modified;
    }-*/;

    /**
     * The name of this File as a URL encoded String.
     *
     * <p>Anything that occurs after the last '/' in the path of this File. If the file has the path 
     * <code>/foo/bar</code>, the name is <code>bar</code>. There is no trailing path separator if
     * this File is a directory.</p>
     * 
     * @return the name of this file
     */
    public native String getName() /*-{
    	return this.name;
    }-*/;
    
    /**
     * The path to this File in the virtual file system as a URL encoded String.
     *
     * <p>The full path of this File in the virtual file system, starting with the name of the mount
     * point and including the full file name of this file or directory. There is no trailing
     * path separator if this File is a directory.</p>
     * 
     * @return the virtual path of this file in teh {@link FileSystem} mount points
     */
    public native String getVirtualPath() /*-{
    	return this.path;
    }-*/;
    
    /**
     * The path to this File in the physical file system.
     *
     * <p>The full path of this File in the physical file system, including trailing slash or backslash
     * for directories. If you mount a directory <code>c:\foo\</code> as <code>foo</code> and this directory 
     * contains a file <code>bar.txt</code>, the <code>nativePath</code> of this File will be <code>c:\foo\bar.txt</code>.
     * Note that the path separator of the underlying operating system is used in the path.</p>
     *
     * <p>For the mount points mounted by <code>mountSystemDirectory()</code>, and for all files under them,
     * this property will be empty to avoid exposing system information to the application.</p>
     *
     * <p>This property is <strong>not</strong> URL encoded, i.e. it is not modified in any way from how
     * the underlying file system would represent the path.</p>
     */
    public native String getNativePath() /*-{
    	return this.nativePath;
    }-*/;
    
    /**
     * The maximum number of characters a path reference can contain.
     *
     * <p>This number is the maximum path length supplied by the operating system, minus
     * the length of the actual path to the file in the actual file system. If
     * <code>c:\foo\bar</code> is mounted as <code>bar</code>, and assuming
     * the operating system has a maximum path length of 128, {@link #getMaxPathLength()}
     * of the File would be 128 - 10 = 110.</p>
     * 
     * @return the maxumum number of characters a path reference can contain
     */
    public native int getMaxPathLength() /*-{
    	return this.maxPathLength;
    }-*/;
    
    /**
     * The number of bytes in this File.
     *
     * <p>Would be 0 for any directories.</p>
     * 
     * @return the number of bytes in this file
     */
    public native int getFileSize() /*-{
    	return this.fileSize;
    }-*/;
    
    /**
     * Open this File for reading or writing.
     *
     * <p>The file can be opened in {@link FileMode#READ}, {@link FileMode#WRITE}, {@link FileMode#APPEND} or {@link FileMode#UPDATE} mode</p>
     *
     * <p>Multiple modes can be specified, i.e READ and WRITE</code>.
     *
     * <p>If the file does not exist when opened in WRITE or APPEND mode, it is immediately created. The entire path to the file is created if this does not exist.</p> 
     *
     * <p>If the file does not exist when opened in READ or UPDATE mode, a {@link FileNotFoundException} is thrown.</p> 
     *
     * <p>The the following is an extract from 
     * <a href="http://no2.php.net/fopen">http://no2.php.net/fopen</a> and explains possible combinations:</p>
     *
     * <p>If a file is opened in an invalid mode, for example opening a read-online file in WRITE mode, a {@link IOException} is thrown.</p>
     *
     * <dl>
     * <dt>'r'</dt><dd>Open for reading only; place the file pointer at the beginning of the file.</dd>
     * <dt>'r+'</dt><dd>Open for reading and writing; place the file pointer at the beginning of the file. </dd>
     * <dt>'w'</dt><dd>Open for writing only; place the file pointer at the beginning of the file and truncate the
     * file to zero length. If the file does not exist, attempt to create it.</dd>
     * <dt>'w+</dt><dd>Open for reading and writing; place the file pointer at the beginning of the file and truncate
     * the file to zero length. If the file does not exist, attempt to create it.</dd>
     * <dt>'a'</dt><dd>Open for writing only; place the file pointer at the end of the file. If the file does not
     * exist, attempt to create it.</dd>
     * <dt>'a+'</dt><dd></dd>
     * <dt></dt><dd>Open for reading and writing; place the file pointer at the end of the file. If the file does not
     * exist, attempt to create it.</dd>
     * </dl>
     *
     * <p>Note that {@link FileMode#UPDATE} represents 'r+'.</p>
     *
     * @param modes A list of {@link FileMode}'s that this file will be opened as
     * @return A FileStream pointing to the given file, or null if no File with the given path can be resolved.
     * 
     * @throws FileNotFoundException If the given path is not a valid File
     * @throws IOException If the given path is not valid for opening, for example if it is a directory.
     * @throws IOException If opening the file is not permitted, for example if it is readonly and opened in write mode.
     * @throws FileNotFoundException If the filemode requires that a file must exist before accessing, such as READ or UPDATE, and it doesn't.
     */
    public native FileStream open(FileMode... modes) throws FileNotFoundException, IOException/*-{
    	var mode=0;
    	for (var f=0;f<modes.length;f++)
    		mode = mode | modes[f].@org.gwtunite.client.file.File.FileMode::getValue()();

    	try { 
    		return this.open(null, mode);
    	}catch(e) {
    		if (e=="FILE_NOT_FOUND_ERR" ||
    		 	e=="GENERICL_ERR" ||
    			e=="WRONG_ARGUMENTS_ERR") {
    			throw @org.gwtunite.client.file.FileNotFoundException::new(Ljava/lang/String;)(this.name);
    		} else if (e=="WRONG_ARGUMENTS_ERR") {
    			throw @org.gwtunite.client.file.IOException::new(Ljava/lang/String;)("Attempt to open a directory");
    		} else if (e=="SECURITY_ERR") {
    			throw @org.gwtunite.client.file.IOException::new(Ljava/lang/String;)("Attempt to open a read only file in write mode");
    		} else {
    			throw e;
    		}
    	}
    }-*/;    
        
    /**
     * Copy this File to the given File path.
     *
     * <p>Calling this function will copy all the contents of this File to the given target location, given
     * as either a <code>File</code> object.</p>
     *
     * <p>If the target location exists, this operation will fail with an exception. Use the 
     * <code>overwrite</code> argument to replace existing files in target location.</p>
     *
     * <p>This operation will be performed synchronously</p>
     *
     * @param targetFile The target location to copy this File to.
     * @param overwrite Whether or not to overwrite any content present in the target path. Optional, default false.
     * @return File object representing the location of the copy.
     * @throws FileNotFoundException If the destination File already exists and the <code>overwrite</code> argument is <code>false</code>.
     */
    public native File copyTo(File targetFile, boolean overwrite) /*-{
    	try {
			return this.copyTo(targetFile, overwrite);
    	}catch(e) {
    		if (e=="FILE_NOT_FOUND_ERR" || 
    			e=="GENERICL_ERR" ||
    			e=="SECURITY_ERR" || 
    			e=="WRONG_TYPE_OF_OBJECT_ERR" || 
    			e=="WRONG_ARGUMENTS_ERR") {
    			throw @org.gwtunite.client.file.FileNotFoundException::new(Ljava/lang/String;)(this.name);
    		} else {
    			throw e;
    		}
    	}
	}-*/;
    
    /**
     * Copy this File to the given File path.
     *
     * <p>Calling this function will copy all the contents of this File to the given target location, given
     * as either a <code>File</code> object or a String containing the path.</p>
     *
     * <p>If the target location exists, this operation will fail with an exception. Use the 
     * <code>overwrite</code> argument to replace existing files in target location.</p>
     *
     * <p>This operation will be asynchronous, and the method
     * will immediately return a <code>File</code> object representing the copy of the File, regardless of whether the
     * operation is complete. The callback is called when the copy operation is complete, with the copy of the
     * File as an argument. If the operation fails, the callback is called with a <code>null</code> argument.</p>
     *
     * @param targetFile The target location to copy this File to.
     * @param overwrite Whether or not to overwrite any content present in the target path. Optional, default false.
     * @param callback Object to call back when the copy has finished.
     * @return File object representing the location of the copy.
     * @throws FileNotFoundException If the destination File already exists and the <code>overwrite</code> argument is <code>false</code>.
     */
    public native File copyTo(File targetFile, boolean overwrite, FileOperationCompletedHandler callback) throws FileNotFoundException /*-{
    	try {
	    	return this.copyTo(targetFile, overwrite, new function(file) {
	    		callback.@org.gwtunite.client.file.File.FileOperationCompletedHandler::onComplete(Lorg/gwtunite/client/file/File;)();
	    	});
    	}catch(e) {
    		if (e=="FILE_NOT_FOUND_ERR" || 
    			e=="GENERICL_ERR" ||
    			e=="SECURITY_ERR" || 
    			e=="WRONG_TYPE_OF_OBJECT_ERR" || 
    			e=="WRONG_ARGUMENTS_ERR") {
    			throw @org.gwtunite.client.file.FileNotFoundException::new(Ljava/lang/String;)(this.name);
    		} else {
    			throw e;
    		}
    	}
    }-*/;
    
    /**
     * Move this File to the given File path.
     *
     * <p>Calling this function will move all the contents of this File to the given File target location.</p>
     *
     * <p>If the target location exists, this operation will fail with an exception. Use the 
     * <code>overwrite</code> argument to replace existing files in target location.</p>
     *
     * <p>This operation will be performed synchronously</p>
     *
     * @param path The target location to move this File to, as a File.
     * @param overwrite Whether or not to overwrite any content present in the target path. Optional, default false.
     * @return File object representing the location of the new file.
     * @throws IOException If the destination File already exists and the <code>overwrite</code> argument is <code>false</code>.
     */
    public native File moveTo(File path, boolean overwrite) throws FileNotFoundException /*-{
    	try {
			return this.moveTo(path, overwrite);
		}catch(e) {
			if (e=="GENERICL_ERR") {
    			throw @org.gwtunite.client.file.IOException::new(Ljava/lang/String;)("Destination file already exists");
    		} else {
    			throw e;
    		}
    	}
	}-*/;    
    
    /**
     * Move this File to the given File path.
     *
     * <p>Calling this function will move all the contents of this File to the given File target location.</p>
     *
     * <p>If the target location exists, this operation will fail with an exception. Use the 
     * <code>overwrite</code> argument to replace existing files in target location.</p>
     *
     * <p>The operation will be performed asynchronously, and the method
     * will immediately return a <code>File</code> object representing the new File regardless of whether the
     * operation is complete. The callback is called with the new File as an argument. If the
     * operation fails, the callback is called with a <code>null</code> argument.</p>
     *
     * @param directory The target location to move this File to.
     * @param overwrite Whether or not to overwrite any content present in the target path. Optional, default false.
     * @param callback Function to call when the move is competed. 
     * @return File object representing the location of the new file.
     * @throws IOException If the destination File already exists and the <code>overwrite</code> argument is <code>false</code>.
     */
    public native File moveTo(File directory, boolean overwrite, FileOperationCompletedHandler callback) /*-{
    	try {
			return this.moveTo(directory, overwrite, new function(file) {
	    		callback.@org.gwtunite.client.file.File.FileOperationCompletedHandler::onComplete(Lorg/gwtunite/client/file/File;)();
	    	});
		}catch(e) {
    		if (e=="GENERICL_ERR") {
    			throw @org.gwtunite.client.file.IOException::new(Ljava/lang/String;)("Destination file already exists");
    		} else {
    			throw e;
    		}
    	}		
	}-*/;  

    /**
     * Create a new directory.
     *
     * <p>Create a new directory using either a File object or a URL encoded String with a path to the new directory. All 
     * non-existing parent directories are created along with it.</p>
     *
     * <h2>Examples:</h2>
     *
     * <pre><code>
     *    File file = mountPoint.createDirectory(somePath);
     *    File file = mountPoint.createDirectory(mountPoint.resolve(somePath));</code>
     * </pre>
     *
     * @return File pointing to the new directory.
     * @throws IOException If the directory or any of its parent directories could not be created.
     */
    public native File mkDir() throws FileNotFoundException  /*-{
    	try {   		
			return this.createDirectory(this);
		}catch(e) {
    		if (e=="GENERICL_ERR") {
    			throw @org.gwtunite.client.file.IOException::new(Ljava/lang/String;)("Directory (or parents) could not be created");
    		} else {
    			throw e;
    		}
    	}	
	}-*/;
        
    /**
     * Delete the given directory.
     *
     * @return true if the directory and all its content was deleted, false if the directory or any part of its contents was not deleted.
     */
    public native boolean delete() /*-{
		if (this.isArchive) {
			return this.deleteFile(this);
		} else if (this.isDirectory) {
			return this.deleteDirectory(this, true);
		} else {
			return this.deleteFile(this);
		}
	}-*/;	

    /**
     * Returns the list of files contained in either a directory or archive
     * 
     * @return a list of all files/directories in this directory or archive
     * @throws IOException if this object represents a file
     */
    public native File[] listFiles() throws IOException /*-{
    	if (!this.exists)
    		throw @org.gwtunite.client.file.IOException::new(Ljava/lang/String;)("attempt to listFiles on a non existent directory '"+this.name+"'");
    		
    	if (!this.isDirectory)
    		throw @org.gwtunite.client.file.IOException::new(Ljava/lang/String;)("attempt to listFiles on a file");
    
    	this.refresh();
    	if (this.length == 0) 
    		return [];
	
		return this;
    }-*/;
    
    /**
     * Returns the list of accepted files accepted contained in either a directory or archive.  
     * 
     * <p>For example, the following lists all .txt files in the MyFiles directory:</p>
     * 
     * <pre><code>
     * File artifacts = mountPoint.resolve("MyFiles");
	 * File[] files = artifacts.listFiles(new FileFilter() {
	 *						public boolean accept(File pathname) {
	 *							return pathname.getName().endsWith(".txt");
	 *						}
	 *					});
	 *</code></pre>
	 *
	 * @param fileFilter the callback object that will decide which files to include in the list
	 * @return a list of all files/directories in this directory or archive
	 * @throws IOException if this object does not represents a directory or archive
     */
    public File[] listFiles(FileFilter fileFilter) throws IOException {
    	Collection<File> acceptedFiles = new ArrayList<File>();
    	for (File file :  listFiles() )
    		if (fileFilter.accept(file))
    			acceptedFiles.add(file);
        	
    	return acceptedFiles.toArray(new File[acceptedFiles.size()]);
    }
    
    /**
     * Resolve a path to a file.
     *
     * <p>This function will take a URL encoded String with a path and attempt to resolve the path.
     * If the path is valid, a <code>File</code> object representing it is returned. The File may
     * point to a non-existing file or directory, as long as the path is valid.</p>
     *
     * <p>If the path is invalid, i.e. pointing to something outside an existing sandbox, an
     * IOException is thrown. You may resolve paths with characters that are not recommended
     * and get a File, though exceptions will typically be thrown if you attempt to read from
     * or write to such files.</p>
     *
     * @param path URL encoded String with path of the file to resolve.
     * @return File resolved by the given path.
     * @throws IOException If the path points to something outside an existing sandbox. 
     */
    public native File resolve(String path) /*-{
    	try {
    		return this.resolve(path);
		}catch(e) {
    		if (e=="GENERICL_ERR") {
    			throw @org.gwtunite.client.file.IOException::new(Ljava/lang/String;)("Attempt to resolve a path outside an existing sandbox");
    		} else {
    			throw e;
    		}
    	}	
    }-*/;
    
    /** 
     * String representation of this File.
     *
     * <p>This method will return the absolute path to the File in the virtual file system, including
     * the file name as an URL encoded String.</p>
     *
     * @return URL encoded String with the path of the File.
     */
    public native String _toString() /*-{
    	return this.toString();
    }-*/;
    
    public static interface FileOperationCompletedHandler {
    	public void onComplete(File file);
    }
}