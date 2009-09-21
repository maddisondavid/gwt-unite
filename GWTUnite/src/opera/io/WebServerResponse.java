package opera.io;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.ImageElement;

final public class WebServerResponse extends JavaScriptObject {
	protected WebServerResponse() {
	}
	
	public native void close() /*-{
		this.close();
	}-*/;

	public native void close(CloseCallback callback) /*-{
		this.close(function() {
			try {
				callback.@opera.io.WebServerResponse.CloseCallback::onClose()();
			} catch(exception) {
				@opera.io.Utils::handleException(Ljava/lang/Exception;)(exception);
			}
		});
	}-*/;

	public native void closeAndRedispatch() /*-{
		this.closeAndRedispatch();
	}-*/;

	public native void flush() /*-{
		this.flush();
	}-*/;

	public native void flush(FlushCallback callback) /*-{
		this.close(function() {
			try {
				callback.@opera.io.WebServerResponse.FlushCallback::onFlush()();
			} catch(exception) {
				@opera.io.Utils::handleException(Ljava/lang/Exception;)(exception);
			}
		});	
	}-*/;

	public native boolean isChunked() /*-{
		return this.chunked;
	}-*/;

	public native WebServerConnection getConnection() /*-{
		return this.connection;
	}-*/;

	public native boolean isImplicitFlush() /*-{
		return this.implicitFlush;
	}-*/;

	public native void setImplicitFlush(boolean value) /*-{
		this.implicitFlush = value;
	}-*/;

	public native boolean isClosed() /*-{
		return this.closed;
	}-*/;

	public native void setChunked(boolean value) /*-{
		this.chunked = value;
	}-*/;

	public native void setProtocolString(String protocolString) /*-{
		this.setProtocolString(protocolString);
	}-*/;

	public native void setResponseHeader(String name, String value) /*-{
		this.setResponseHeader(name, value);
	}-*/;

	public native void setStatusCode(int statusCode) /*-{
		this.setStatusCode(statusCode);
	}-*/;
	
	public native void setStatusCode(int statusCode, String text) /*-{
		this.setStatusCode(statusCode, text);
	}-*/;

	public native void write(String data) /*-{
		this.write(data);
	}-*/;

	public native void writeBytes(byte[] data) /*-{
		this.writeBytes(data);
	}-*/;

	public native void writeFile(File file) /*-{
		this.writeFile(file);
	}-*/;

	public native void writeImage(ImageElement image) /*-{
		this.writeImage(image);
	}-*/;

	public native void writeLine(String data) /*-{
		this.writeLine(data);
	}-*/;
	
	public interface CloseCallback {
		void onClose();
	}
	
	public interface FlushCallback {
		void onFlush();
	}
}
