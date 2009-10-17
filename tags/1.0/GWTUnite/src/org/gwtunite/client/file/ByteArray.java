package org.gwtunite.client.file;

import com.google.gwt.core.client.JavaScriptObject;

/** 
 * Represents a pointer to a ByteArray
 * 
 * <p>ByteArrays can't be used directly, but they can be read from a file and written to a file</p>
 */
final public class ByteArray extends JavaScriptObject {

	protected ByteArray() {
	}
	
	public native int length() /*-{
		return this.length;
	}-*/;
}
