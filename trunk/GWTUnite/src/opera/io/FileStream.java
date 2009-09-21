package opera.io;

import com.google.gwt.core.client.JavaScriptObject;

public final class FileStream extends JavaScriptObject {

	protected FileStream() {
		
	}
	
	public native int getPosition() /*-{
		return this.position;
	}-*/;
	
	public native int bytesAvailable() /*-{
		return this.bytesAvailable;
	}-*/;
	
	public native boolean isEof() /*-{
		return this.eof;
	}-*/;
	
	public native String getEncoding() /*-{
		return this.encoding;
	}-*/;
	
	public native String getSystemNewLine() /*-{
		return this.systemNewLine;
	}-*/;
	
	public native String getNewLine() /*-{
		return this.newLine;
	}-*/;
	
	public native void close() /*-{
		return this.close();
	}-*/;
	
	public native String read(int length, String charset) /*-{
		return this.read(length, charset);
	}-*/;
	
	public native String readLine(String charset) /*-{
		return this.readLine(charset);
	}-*/;
	
	public native byte[] readBytes(int length) /*-{
		return this.readBytes(length);
	}-*/;
	
	public native String readBase64(int length) /*-{
		return this.readBase64(length);
	}-*/;

	public native void write(String string) /*-{
		this.write(string);
	}-*/;
	
	public native void write(String string, String charset) /*-{
		this.write(string, charset);
	}-*/;

	public native void writeLine(String string) /*-{
		this.writeLine(string);
	}-*/;	
	
	public native void writeLine(String string, String charset) /*-{
		this.writeLine(string, charset);
	}-*/;
	
	public native void write(byte[] bytes, int length) /*-{
		this.writeBytes(bytes, length);
	}-*/;
	
	public native void writeBase64(String string) /*-{
		this.writeBase64(string);
	}-*/;
	
	public native void writeFile(File file) /*-{
		this.writeFile(file);
	}-*/;
	
	public native void writeImage(Object image) /*-{
		this.writeImage(image);
	}-*/;
}
