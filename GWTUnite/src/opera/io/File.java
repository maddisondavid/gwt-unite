package opera.io;

import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Class representing files and directories.
 *
 * <p>Objects of this class can refer to regular files, directories or archives of files. In the
 * two latter cases, the object contains references to its subdirectories and files.</p>
 *
 * <p>The <code>fileSystem.mountPoint</code> property and any mounted directories are <code>File</code> objects. You
 * may make a <code>File</code> object by calling the <code>resolve()</code> method on any of these.</p>
 *
 * <p>The <code>File</code> class is special in that it doubles as an array-like object containing the <code>File</code> objects it refers to.
 * So if, a directory contains a series of files and directories, you can do the
 * following:</p>
 *
 * <pre><code>File dir = mp.resolve("path/to/dir"); //Get a File object refering to the directory
 *			  dir.refresh(); //Load the contents of the directory
 *			 for (File file : dir.getContents())  {
 *    			response.write(file.getName());
 *			}
 * </code></pre>
 */
final public class File extends JavaScriptObject {

	protected File() {
	}
	
	/**
     * The parent File of this File, or null if it has no parent.
     *
     * In most cases, the parent will be a directory. It can also be an archive. For
     * Files that are mount points, this property is <code>null</code>.
     */
	public native File getParent() /*-{
		return this.parent;
	}-*/;
	
    /**
     * Whether or not this File is read only.
     *
     * Files mounted in the application directory, using {@link FileSystem#mountSystemDirectory()} are
     * not writeable. Otherwise, the physical file system determines whether or not
     * the File is writeable.
     */
	public native boolean isReadOnly() /*-{
		return this.readOnly;
	}-*/; 
	
    /**
     * Whether or not this File exists in the physical file system.
     *
     * File objects created through <code>resolve()</code> or <code>browseForSave()</code>, 
     * may in some cases not exist in the file system.
     */
	public native boolean exists() /*-{
		return this.exists;
	}-*/;
	
    /**
     * Whether or not this File is a regular file.
     */
	public native boolean isFile() /*-{
		return !this.isDirectory;
	}-*/;
	
    /**
     * Whether or not this File is a directory.
     */
	public native boolean isDirectory() /*-{
		return this.isDirectory;
	}-*/;
	
    /**
     * Whether or not this File is a compressed archive, like a zip or gzip file.
     *
     * <p>Note that archives will also have the <code>isDirectory</code> and </code>isFile</code>
     * properties set. You may both use <code>resolve()</code> to resolve files inside the archive, or open
     * the archive file using <code>open()</code>.</p>
     *
     * <p class="ni">Currently only a subset of the ZIP format is currently supported as archives.</p>
     */
	public native boolean isArchive() /*-{
		return this.isArchive;
	}-*/;
	
    /**
     * Meta data for this file.
     *
     * This property holds meta data for special types of files, for example the file name of an uploaded
     * file. For normal files, this property is <code>null.</code>
     * 
     * WARNING : This method is likely to change in future versions!
     */
	public native String getMetaData() /*-{
		return this.metaData;
	}-*/;
	
    /**
     * The time and date this File was created.
     */
    public native Date getCreated() /*-{
    	return this.created;
    }-*/;

    /**
     * The time and date this File was last modified.
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
     */
    public native String getPath() /*-{
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
     * This number is the maximum path length supplied by the operating system, minus
     * the length of the actual path to the file in the actual file system. If
     * <code>c:\foo\bar</code> is mounted as <code>bar</code>, and assuming
     * the operating system has a maximum path length of 128, the <code>maxPathLength</code> property
     * of the File would be 128 - 10 = 110.
     */
    public native int getMaxPathLength() /*-{
    	return this.maxPathLength;
    }-*/;
    
    /**
     * The number of bytes in this File.
     *
     * If this File is a directory, it's size is 0. Use the <code>length</code> property to find out how
     * many files the directory contains.
     */
    public native int getFileSize() /*-{
    	return this.fileSize;
    }-*/;
    
    /**
     * The number of files and directories referenced by this File.
     *
     * <p>This property is used for array style lookup. If the <code>File</code> object is a regular file,
     * its length is 0. Use the <code>fileSize</code> property to get the size of regular
     * files in bytes.</p>
     *
     * <p>For directories or archives, this property is 0 until <code>refresh()</code> is called,
     * except for mount point <code>File</code> objects that are already loaded.</p>
     */
    public native int getLength() /*-{
    	return this.length;
    }-*/;
    
    /**
     * Open a File for reading or writing.
     *
     * <p>If the path argument is given as <code>null</code>, this File will be opened.</p>
     *
     * <p>The file can be opened in read, write, append or update mode, represented by the constants in {@link opera.io.filemode}.</p>
     *
     * <p>The mode argument is similar to PHPs <code>fopen()</code>, but implemented as constants which can be combined through a bitwise OR,
     * for example as <code>opera.io.filemode.APPEND | opera.io.filemode.READ</code>.
     *
     * <p>If the file does not exist when opened in WRITE or APPEND mode, it is immediately created. The entire path to the file is created if this does not exist.</p> 
     *
     * <p>If the file does not exist when opened in READ or UPDATE mode, a FILE_NOT_FOUND_ERR is thrown.</p> 
     *
     * <p class="note">The previous version of the API accepted a string equal to the ones described below. This is now deprecated 
     * in favor of the constants in {@link opera.io.filemode}.</p>
     *
     * <p>The the following is an extract from 
     * <a href="http://no2.php.net/fopen">http://no2.php.net/fopen</a> and explains possible combinations:</p>
     *
     * <p>If a file is opened in an invalid mode, for example opening a read-online file in WRITE mode, a SECURITY_ERR is thrown.</p>
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
     * <p>Note that {@link opera.io.filemode#UPDATE} represents 'r+'.</p>
     *
     * @param path File object to read, or a URL encoded String with the path to the file to read.
     * @param mode Whether to open the file for reading, writing, appending or a combination.
     * @returns A FileStream pointing to the given file, or null if no File with the given path can be resolved.
     * 
     * @throws WRONG_ARGUMENTS_ERR If the given path is not a valid File or if the mode argument is unrecognized.
     * @throws WRONG_TYPE_OF_OBJECT_ERR If the given path is not valid for opening, for example if it is a directory.
     * @throws SECURITY_ERR If opening the file is not permitted, for example if it is readonly and opened in write mode.
     * @throws FILE_NOT_FOUND_ERR If the filemode requires that a file must exist before accessing, such as READ or UPDATE, and it doesn't.
     */
    public native FileStream open(String path, int mode) /*-{
    	// FIXME : Take an Enum and convert to the correct mode
    	// FIXME : Catch exceptions and throw as Java Exception 
    	return this.open(path, mode);
    }-*/;

    /**
     * Open this File for reading or writing.
     *
     * <p>The file can be opened in read, write, append or update mode, represented by the constants in {@link opera.io.filemode}.</p>
     *
     * <p>The mode argument is similar to PHPs <code>fopen()</code>, but implemented as constants which can be combined through a bitwise OR,
     * for example as <code>opera.io.filemode.APPEND | opera.io.filemode.READ</code>.
     *
     * <p>If the file does not exist when opened in WRITE or APPEND mode, it is immediately created. The entire path to the file is created if this does not exist.</p> 
     *
     * <p>If the file does not exist when opened in READ or UPDATE mode, a FILE_NOT_FOUND_ERR is thrown.</p> 
     *
     * <p class="note">The previous version of the API accepted a string equal to the ones described below. This is now deprecated 
     * in favor of the constants in {@link opera.io.filemode}.</p>
     *
     * <p>The the following is an extract from 
     * <a href="http://no2.php.net/fopen">http://no2.php.net/fopen</a> and explains possible combinations:</p>
     *
     * <p>If a file is opened in an invalid mode, for example opening a read-online file in WRITE mode, a SECURITY_ERR is thrown.</p>
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
     * <p>Note that {@link opera.io.filemode#UPDATE} represents 'r+'.</p>
     *
     * @param mode Whether to open the file for reading, writing, appending or a combination.
     * @returns A FileStream pointing to the given file, or null if no File with the given path can be resolved.
     * 
     * @throws WRONG_ARGUMENTS_ERR If the given path is not a valid File or if the mode argument is unrecognized.
     * @throws WRONG_TYPE_OF_OBJECT_ERR If the given path is not valid for opening, for example if it is a directory.
     * @throws SECURITY_ERR If opening the file is not permitted, for example if it is readonly and opened in write mode.
     * @throws FILE_NOT_FOUND_ERR If the filemode requires that a file must exist before accessing, such as READ or UPDATE, and it doesn't.
     */
    public native FileStream open(int mode) /*-{
    	// FIXME : Take an Enum and convert to the correct mode
    	// FIXME : Catch exceptions and throw as Java Exception 
    	return this.open(null, mode);
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
     * <p>This operation will be performed synchronously</p>
     *
     * @param path The target location to copy this File to, as a URL encoded String with the path.
     * @param overwrite Whether or not to overwrite any content present in the target path. Optional, default false.
     * @returns File object representing the location of the copy.
     * @throws GENERIC_ERR If the destination File already exists and the <code>overwrite</code> argument is <code>false</code>.
     */
    public native File copyTo(String path, boolean overwrite) /*-{
		return this.copyTo(path, overwrite);
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
     * <p>This operation will be performed synchronously</p>
     *
     * @param path The target location to copy this File to, as a File.
     * @param overwrite Whether or not to overwrite any content present in the target path. Optional, default false.
     * @returns File object representing the location of the copy.
     * @throws GENERIC_ERR If the destination File already exists and the <code>overwrite</code> argument is <code>false</code>.
     */
    public native File copyTo(File path, boolean overwrite) /*-{
		return this.copyTo(path, overwrite);
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
     * @param {File} path The target location to copy this File to, as a URL encoded String with the path.
     * @param {boolean} overwrite Whether or not to overwrite any content present in the target path. Optional, default false.
     * @param {Function} callback Function to call when the copy is completed.
     * @returns {File} File object representing the location of the copy.
     * @throws GENERIC_ERR If the destination File already exists and the <code>overwrite</code> argument is <code>false</code>.
     */
    public native File copyTo(String path, boolean overwrite, Object callback) /*-{
    	return this.copyTo(path, overwrite, callback);
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
     * @param {File} path The target location to copy this File to, as a File.
     * @param {boolean} overwrite Whether or not to overwrite any content present in the target path. Optional, default false.
     * @param {Function} callback Function to call when the copy is completed.
     * @returns {File} File object representing the location of the copy.
     * @throws GENERIC_ERR If the destination File already exists and the <code>overwrite</code> argument is <code>false</code>.
     */
    public native File copyTo(File path, boolean overwrite, Object callback) /*-{
    	return this.copyTo(path, overwrite, callback);
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
     * @param path The target location to move this File to, as URL encoded String with the path.
     * @param overwrite Whether or not to overwrite any content present in the target path. Optional, default false.
     * @return File object representing the location of the new file.
     * @throws GENERICL_ERR If the destination File already exists and the <code>overwrite</code> argument is <code>false</code>.
     */
    public native File moveTo(String path, boolean overwrite) /*-{
		return this.moveTo(path, overwrite);
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
     * @throws GENERICL_ERR If the destination File already exists and the <code>overwrite</code> argument is <code>false</code>.
     */
    public native File moveTo(File path, boolean overwrite) /*-{
		return this.moveTo(path, overwrite);
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
     * @param path The target location to move this File to, as either a File or an URL encoded String with the path.
     * @param overwrite Whether or not to overwrite any content present in the target path. Optional, default false.
     * @param callback Function to call when the move is competed. 
     * @return File object representing the location of the new file.
     * @throws GENERICL_ERR If the destination File already exists and the <code>overwrite</code> argument is <code>false</code>.
     */
    public native File moveTo(String path, boolean overwrite, Object callback) /*-{
		return this.moveTo(path, overwrite, callback);
	}-*/;  

    /**
     * Create a new directory.
     *
     * <p>Create a new directory using either a File object or a URL encoded String with a path to the new directory. All 
     * non-existing parent directories are created along with it.</p>
     *
     * <h2>Examples:</h2>
     *
     * <pre><code>File file = mountPoint.createDirectory(somePath);
     *File file = mountPoint.createDirectory(mountPoint.resolve(somePath));</code></pre>
     *
     * @param directory a URL encoded String with the path to the directory.
     * @returns File pointing to the new directory.
     * @throws GENERIC_ERR If the directory or any of its parent directories could not be created.
     */
    public native File createDirectory(String dir) /*-{
		return this.createDirectory(dir);
	}-*/;

    /**
     * Create a new directory.
     *
     * <p>Create a new directory using either a File object or a URL encoded String with a path to the new directory. All 
     * non-existing parent directories are created along with it.</p>
     *
     * <h2>Examples:</h2>
     *
     * <pre><code>File file = mountPoint.createDirectory(somePath);
     *File file = mountPoint.createDirectory(mountPoint.resolve(somePath));</code></pre>
     *
     * @param directory a File referring to the desired directory
     * @returns File pointing to the new directory.
     * @throws GENERIC_ERR If the directory or any of its parent directories could not be created.
     */
    public native File createDirectory(File dir) /*-{
		return this.createDirectory(dir);
	}-*/;
    
    /**
     * Delete the given directory.
     *
     * <p>If the <code>recursive</code> argument is given as <code>true</code>, this method will attempt to delete the
     * directory and all of its content. If deleting individual files or directories in it fails, the method will continue
     * to delete the rest of the content.</p>
     *
     * <p>If the entire directory and all of its content is deleted, the method will return <code>true</code>. If parts
     * of the content, and thus also the directory itself could not be deleted, the method will return
     * <code>false</code>.</p>
     *
     * @param directory URL encoded String with the path to the directory to delete.
     * @param recursive Whether or not to recursively delete any content references by this File. Optional, default false.
     * @returns true if the directory and all its content was deleted, false if the directory or any part of its contents was not deleted.
     */
    public native boolean deleteDirectory(String directory, boolean recursive) /*-{
		return this.deleteDirectory(directory, recursive);
	}-*/;    
    
    /**
     * Delete the given directory.
     *
     * <p>If the <code>recursive</code> argument is given as <code>true</code>, this method will attempt to delete the
     * directory and all of its content. If deleting individual files or directories in it fails, the method will continue
     * to delete the rest of the content.</p>
     *
     * <p>If the entire directory and all of its content is deleted, the method will return <code>true</code>. If parts
     * of the content, and thus also the directory itself could not be deleted, the method will return
     * <code>false</code>.</p>
     *
     * @param directory File representing the directory to delete.
     * @param recursive Whether or not to recursively delete any content references by this File. Optional, default false.
     * @returns true if the directory and all its content was deleted, false if the directory or any part of its contents was not deleted.
     */
    public native boolean deleteDirectory(File directory, boolean recursive) /*-{
    	return this.deleteDirectory(directory, recursive);
    }-*/;
    
    /**
     * Delete the given file.
     *
     * This method takes either a <code>File</code> object or a URL encoded String with a path and deletes the
     * referenced file.
     *
     * @param file URL encoded String with the path to the file to delete.
     * @returns true if the file was successfully deleted, otherwise false.
     * @throws GENERIC_ERR If the file could not be deleted.
     */
    public native boolean deleteFile(String file) /*-{
		return this.deleteFile(file);
	}-*/;    

    /**
     * Delete the given file.
     *
     * This method takes either a <code>File</code> object or a URL encoded String with a path and deletes the
     * referenced file.
     *
     * @param file File representing the directory to delete.
     * @returns true if the file was successfully deleted, otherwise false.
     * @throws GENERIC_ERR If the file could not be deleted.
     */
    public native boolean deleteFile(File file) /*-{
		return this.deleteFile(file);
	}-*/;
    
    /**
     * If this is a directory, this returns the directory listing   
     */
    public native File[] getContents() /*-{
    	if (this.length == 0) 
    		return [];
    	
    	return this;
    }-*/;
    
    /**
     * Refresh the content in this File.
     *
     * Initially a File representing a directory is loaded without its actual content.
     * For directories you need to call this method at least once to load the content.
     * The File is then not live, i.e. if the underlying file system changes, these
     * changes are not propagated to this <code>File</code> object. You need to call this method
     * again to see the changes.
     */
    public native void refresh() /*-{
    	this.refresh();
    }-*/;
    
    /**
     * Resolve a path to a file.
     *
     * <p>This function will take a URL encoded String with a path and attempt to resolve the path.
     * If the path is valid, a <code>File</code> object representing it is returned. The File may
     * point to a non-existing file or directory, as long as the path is valid. The
     * resulting File can, for example, be used with the {@link File#createDirectory(String)} method.</p>
     *
     * <p>If the path is invalid, i.e. pointing to something outside en existing sandbox, an
     * exception is thrown. You may resolve paths with characters that are not recommended
     * and get a File, though exceptions will typically be thrown if you attempt to read from
     * or write to such files.</p>
     *
     * @param path URL encoded String with path of the file to resolve.
     * @returns File resolved by the given path.
     * @throws SECURITY_ERR If the path points to something outside an existing sandbox. 
     */
    public native File resolve(String path) /*-{
    	return this.resolve(path);
    }-*/;
    
    /** _ prefix required as toString clashes with JavaScriptObject::toString() (which is final!) */
    public native String _toString() /*-{
    	return this.toString();
    }-*/;
}