package opera.io;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.ImageElement;

public final class FileStream extends JavaScriptObject {

	protected FileStream() {
	}
	
    /**
     * The current byte index position of this <code>FileStream</code>.
     *
     * You may set the position programmatically. If it is set to &lt; 0, the position will be 0.
     * If it is set to &lt; <code>{@link File#getFileSize()}</code>, the position will be <code>{@link File#getFileSize()}</code>.
     */
	public native int getPosition() /*-{
		return this.position;
	}-*/;

    /**
     * Set the position programmatically. If it is set to &lt; 0, the position will be 0.
     * If it is set to &lt; <code>{@link File#getFileSize()}</code>, the position will be <code>{@link File#getFileSize()}</code>.
     */
	public native void setPosition(int position) /*-{
		return this.position = position;
	}-*/;
	
    /**
     * Number of of bytes available from the current position to the end of the <code>FileStream</code>.
     *
     * The value of this property is effectively <code>fileSize</code> - <code>position</code>.
     */
	public native int getBytesAvailable() /*-{
		return this.bytesAvailable;
	}-*/;

    /**
     * Whether ot not the end of the <code>FileStream</code> has been reached.
     *
     * If the <code>FileStream</code> is unreadable, this property is <code>true</code>.
     */
	public native boolean isEof() /*-{
		return this.eof;
	}-*/;

    /**
     * The encoding of this <code>FileStream</code>.
     *
     * This property defaults to UTF-8. Change it to override the default encoding
     * used when writing to the <code>FileStream</code>. This can be overriden on a case-by-case
     * basis by supplying the <code>charset</code> argument to the various methods 
     * which write characters.
     */
	public native String getEncoding() /*-{
		return this.encoding;
	}-*/;
	
    /**
     * The system default character for separating lines in a file.
     */
	public native String getSystemNewLine() /*-{
		return this.systemNewLine;
	}-*/;

    /**
     * Newline character used for this particular <code>FileStream</code>.
     */
	public native String getNewLine() /*-{
		return this.newLine;
	}-*/;

    /**
     * Newline character used for this particular <code>FileStream</code>.
     *
     * This is the same as {@link #getSystemNewLine()} when the <code>FileStream</code> is created.
     * This can be set to override the default character used for splitting lines when calling 
     * {@link readLine()} or {@link writeLine()}.
     */
	public native void setNewLine(String character) /*-{
		this.newLine = character;
	}-*/;

    /**
     * Close the <code>FileStream</code> for reading or writing.
     */
	public native void close() /*-{
		return this.close();
	}-*/;

    /**
     * Read a number of characters from the FileStream.
     *
     * <p>This function will read <em><code>length</code></em> number 
     * of characters from the stream.</p>
     *
     * <p>If there are less than <em><code>length</code></em> characters left
     * in the file, only the remaining characters in the file are
     * read, and <code>{@link #isEof()}</code> will be <code>true</code>.</p>
     *
     * <p>If <code>{@link #isEof()}</code> is <code>true</code> when this method is called,
     * null will be returned.</p>
     *
     * <p>The resulting String is encoded with the charset in the 
     * <code>FileStream.getEncoding()</code> property.
     *
     * @param length Number of characters to read.
     * @returns A String of characters, or null if there are no more characters left in the File.
     * @throws GENERIC_ERR If it is not possible to read from the stream.
     */
	public native String read(int length) throws IOException /*-{
		try {
			return this.read(length);
    	}catch(e) {
    		if (e=="GENERICL_ERR") {
    			throw @opera.io.IOException::new(Ljava/lang/String;)(this.name);
    		} else {
    			throw e;
    		}
    	}
	}-*/;

    /**
     * Read a number of characters from the FileStream.
     *
     * <p>This function will read <em><code>length</code></em> number 
     * of characters from the stream.</p>
     *
     * <p>If there are less than <em><code>length</code></em> characters left
     * in the file, only the remaining characters in the file are
     * read, and <code>{@link #isEof()}</code> will be <code>true</code>.</p>
     *
     * <p>If <code>{@link #isEof()}</code> is <code>true</code> when this method is called,
     * null will be returned.</p>
     *
     * <p>The resulting String is encoded with the charset in the 
     * <code>FileStream.encoding</code> property unless the optional <code>charset</code>
     * argument is given.
     *
     * @param length Number of characters to read.
     * @param charset The character set to use when reading.
     * @returns A String of characters, or null if there are no more characters left in the File.
     * @throws GENERIC_ERR If it is not possible to read from the stream.
     */
	public native String read(int length, String charset) throws IOException /*-{
		try {
			return this.read(length, charset);
		}catch(e) {
    		if (e=="GENERICL_ERR") {
    			throw @opera.io.IOException::new(Ljava/lang/String;)(this.name);
    		} else {
    			throw e;
    		}
    	}
	}-*/;
	
    /**
     * Read a line of characters from the <code>FileStream</code>
     *
     * <p>This functions will read all characters up to and including the next 
     * newline in the <code>FileStream</code> as defined by the {@link #newLine} property. 
     * If there are no newlines left in the stream, the resulting string will 
     * not have a newline character and the <code>eof</code> property is set 
     * to <code>true</code>.</p>
     *
     * <p>If <code>eof</code> is <code>true</code> when this method is called,
     * null is returned.</p>
     *
     * <p>The resulting String is encoded with the charset in the 
     * <code>FileStream.encoding</code> property
     *
     * @returns {String} A String of characters, or null if there are no data to read.
     * @throws GENERIC_ERR If it is not possible to read from the stream.
     */
	public native String readLine(String charset) throws IOException /*-{
		try {
			return this.readLine(charset);
		}catch(e) {
    		if (e=="GENERICL_ERR") {
    			throw @opera.io.IOException::new(Ljava/lang/String;)(this.name);
    		} else {
    			throw e;
    		}
    	}
	}-*/;
	
    /**
     * Read a line of characters from the <code>FileStream</code>
     *
     * <p>This functions will read all characters up to and including the next 
     * newline in the <code>FileStream</code> as defined by the {@link #newLine} property. 
     * If there are no newlines left in the stream, the resulting string will 
     * not have a newline character and the <code>eof</code> property is set 
     * to <code>true</code>.</p>
     *
     * <p>If <code>eof</code> is <code>true</code> when this method is called,
     * null is returned.</p>
     *
     * <p>The resulting String is encoded with the charset argument is given.
     *
     * @param {String} charset The character set to use when reading. Optional.
     * @returns {String} A String of characters, or null if there are no data to read.
     * @throws GENERIC_ERR If it is not possible to read from the stream.
     */
	public native String readLine() throws IOException /*-{
		try {
			return this.readLine();
		}catch(e) {
    		if (e=="GENERICL_ERR") {
    			throw @opera.io.IOException::new(Ljava/lang/String;)(this.name);
    		} else {
    			throw e;
    		}
    	}
	}-*/;
	
    /**
     * Read a number of bytes from the <code>FileStream</code>
     *
     * <p>This function will read <em><code>length</code></em> number 
     * of bytes from the <code>FileStream</code>.</p>
     *
     * <p>If there are less than <em><code>length</code></em> bytes left
     * in the file, only the remaining bytes in the file are
     * read, and the <code>eof</code> property is set to <code>true</code>.</p>
     *
     * <p>If <code>eof</code> is <code>true</code> when this method is called,
     * null will be returned.</p>
     *
     * @param length The number of bytes to read.
     * @returns A Byte Array with the bytes read from the FileStream, or null if there are no data to read.
     * @throws GENERIC_ERR If it is not possible to read from the stream.
     */
	public native byte[] readBytes(int length) throws IOException /*-{
		try {
			return this.readBytes(length);
		}catch(e) {
    		if (e=="GENERICL_ERR") {
    			throw @opera.io.IOException::new(Ljava/lang/String;)(this.name);
    		} else {
    			throw e;
    		}
    	}
	}-*/;
	
    /**
     * Read bytes from the <code>FileStream</code> and encode it as Base64
     *
     * <p>This method will read <code>length</code> number of
     * bytes from the <code>FileStream</code> and return the data as a Base64 
     * encoded String.</p>
     *
     * <p>This is typically used to encode data from binary files 
     * in order to transfer them for example over <code>XMLHttpRequest</code>.</p>
     *
     * <p>As the method will read a number of bytes as specified
     * in the length argument, and then encode it, a call to 
     * <code>stream.readBase64(100)</code> will not necessarily end 
     * up as a String with a length of 100.</p>
     *
     * <p>If there are less than <em><code>length</code></em> bytes left
     * in the file, only the remaining bytes in the file are
     * read, and the <code>eof</code> property is set to <code>true</code>.</p>
     *
     * <p>If <code>eof</code> is <code>true</code> when this method is called,
     * null will be returned.</p>
     *
     * @param length Number of bytes to read.
     * @returns The content of the <code>FileStream</code> as a Base64 encoded String, or null if there are no data to read.
     * @throws GENERIC_ERR If it is not possible to read from the stream.
     */
	public native String readBase64(int length) throws IOException /*-{
		try { 
			return this.readBase64(length);
		}catch(e) {
    		if (e=="GENERICL_ERR") {
    			throw @opera.io.IOException::new(Ljava/lang/String;)(this.name);
    		} else {
    			throw e;
    		}
    	}
	}-*/;

    /**
     * Write a string of characters to the <code>FileStream</code>
     *
     * This method will write the given String of characters to the <code>FileStream</code>, using
     * the given <code>charset</code> or the charset in the <code>FileStream.encoding</code> property.
     *
     * <p class="ni">The <code>charset</code> argument is currently ignored.</p>
     *
     * @param string The String of characters to write.
     * @throws GENERIC_ERR If it is not possible to write to the stream.
     */
	public native void write(String string) throws IOException /*-{
		try {
			this.write(string);
		}catch(e) {
    		if (e=="GENERICL_ERR") {
    			throw @opera.io.IOException::new(Ljava/lang/String;)(this.name);
    		} else {
    			throw e;
    		}
    	}
	}-*/;

    /**
     * Write a string of characters to the <code>FileStream</code>
     *
     * This method will write the given String of characters to the <code>FileStream</code>, using
     * the given <code>charset</code> or the charset in the <code>FileStream.encoding</code> property.
     *
     * @param string The String of characters to write.
     * @param charset The charset to use when writing. Optional.
     * @throws GENERIC_ERR If it is not possible to write to the stream.
     * @deprecated Not currently used as the charset is ignored
     */
	@Deprecated
	public native void write(String string, String charset) throws IOException /*-{
		try {
			this.write(string, charset);
		}catch(e) {
    		if (e=="GENERICL_ERR") {
    			throw @opera.io.IOException::new(Ljava/lang/String;)(this.name);
    		} else {
    			throw e;
    		}
    	}
	}-*/;

    /**
     * Write a line of characters to the FileStream
     *
     * <p>This method will write the given String with an appended newline character taken from
     * the {@link #newLine} property to the <code>FileStream</code>, using the given charset or the charset 
     * in the <code>FileStream.encoding</code> property.</p>
     *
     * <p class="ni">The <code>charset</code> argument is currently ignored.</p>
     *
     * @param {String} string The string of characters to write.
     * @throws GENERIC_ERR If it is not possible to write to the stream.
     */
	public native void writeLine(String string) throws IOException /*-{
		try {
			this.writeLine(string);
		}catch(e) {
    		if (e=="GENERICL_ERR") {
    			throw @opera.io.IOException::new(Ljava/lang/String;)(this.name);
    		} else {
    			throw e;
    		}
    	}		
	}-*/;	

    /**
     * Write a line of characters to the FileStream
     *
     * <p>This method will write the given String with an appended newline character taken from
     * the {@link #newLine} property to the <code>FileStream</code>, using the given charset or the charset 
     * in the <code>FileStream.encoding</code> property.</p>
     *
     * @param string The string of characters to write.
     * @param charset The <code>charset</code> to use when writing. Optional.
     * @throws GENERIC_ERR If it is not possible to write to the stream.
     * @deprecated Not curently used as the charset is currently ignored
     */
	public native void writeLine(String string, String charset) throws IOException /*-{
		try {
			this.writeLine(string, charset);
		}catch(e) {
    		if (e=="GENERICL_ERR") {
    			throw @opera.io.IOException::new(Ljava/lang/String;)(this.name);
    		} else {
    			throw e;
    		}
    	}		
	}-*/;
	
    /**
     * Write a set of bytes to the <code>FileStream</code>
     *
     * This method will write the <em><code>length</code></em> first bytes
     * from the given <code>ByteArray</code> to the stream.
     *
     * @param bytes The bytes to write.
     * @param length The number of bytes to write.
     * @throws GENERIC_ERR If it is not possible to write to the stream.
     */
	public native void writeBytes(byte[] bytes, int length) throws IOException /*-{
		try {
			this.writeBytes(bytes, length);
		}catch(e) {
    		if (e=="GENERICL_ERR") {
    			throw @opera.io.IOException::new(Ljava/lang/String;)(this.name);
    		} else {
    			throw e;
    		}
    	}		
	}-*/;
	
    /**
     * Decode a Base64 encoded string and write the data to the <code>FileStream</code>.
     *
     * This method takes a String encoded as Base64, decodes it and writes
     * the resulting data to the <code>FileStream</code>. It is typically used for
     * binary data encoded as Base64 when its transferred for example
     * over <code>XMLHttpRequest</code>.
     *
     * @param string Base64 encoded String to write.
     * @throws GENERIC_ERR If it is not possible to write to the stream.
     */
	public native void writeBase64(String string) throws IOException /*-{
		try {
			this.writeBase64(string);
		}catch(e) {
    		if (e=="GENERICL_ERR") {
    			throw @opera.io.IOException::new(Ljava/lang/String;)(this.name);
    		} else {
    			throw e;
    		}
    	}		
	}-*/;
	
    /**
     * Write a File to the <code>FileStream</code>.
     *
     * This will write the entire contents of the given File
     * to the <code>FileStream</code>.
     *
     * @param file The File to write.
     * @throws GENERIC_ERR If it is not possible to write to the stream.
     */
	public native void writeFile(File file) throws IOException /*-{
		try {
			this.writeFile(file);
		}catch(e) {
    		if (e=="GENERICL_ERR") {
    			throw @opera.io.IOException::new(Ljava/lang/String;)(this.name);
    		} else {
    			throw e;
    		}
    	}		
	}-*/;
	
    /**
     * Write an image to the <code>FileStream</code>.
     *
     * This function will take either an <code>HTMLImageElement</code> or an <code>HTMLCanvasElement</code>
     * and write the binary data to the <code>FileStream</code>. In the case of <code>HTMLCanvasElement</code>,
     * the image is first encoded as a PNG image.
     *
     * @param image The <code>ImageElement</code> to write.
     * @throws GENERIC_ERR If it is not possible to write to the stream.
     */
	public native void writeImage(ImageElement image) throws IOException /*-{
		try {
			this.writeImage(image);
		}catch(e) {
    		if (e=="GENERICL_ERR") {
    			throw @opera.io.IOException::new(Ljava/lang/String;)(this.name);
    		} else {
    			throw e;
    		}
    	}		
	}-*/;
}
