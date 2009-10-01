package opera.io;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.ImageElement;

final public class WebServerResponse extends JavaScriptObject {
	protected WebServerResponse() {
	}
	
    /**
     * Close the connection.
     *
     * <p>Close the connection and set the {@link WebServerConnection#isClosed()} property of the
     * corresponding <code>WebServerConnection</code> object to false. No writing to the
     * the response is possible after <code>close()</code> is called.</p>
     */
	public native void close() /*-{
		this.close();
	}-*/;

    /**
     * Close the connection.
     *
     * <p>Close the connection and set the {@link WebServerConnection#isClosed()} property of the
     * corresponding <code>WebServerConnection</code> object to false. No writing to the
     * the response is possible after <code>close()</code> is called.</p>
     *
     * <p>Closing is by default an asynchronous operation. You may catch when the
     * connection actually closes by supplying an optional callback parameter.</p>
     *
     * @param callback Function to call when the connection is closed. Optional.
     */
	public native void close(CloseCallback callback) /*-{
		this.close(function() {
			try {
				callback.@opera.io.WebServerResponse.CloseCallback::onClose()();
			} catch(exception) {
				@opera.io.Utils::handleException(Ljava/lang/Throwable;)(exception);
			}
		});
	}-*/;

    /**
     * Close the connection and redispatch the request to the Web server.
     *
     * <p>Close the connection and put the request back into the request queue of 
     * the Web server. The <code>{@link WebServerRequest#setUri(String)</code> method may be used
     * for this purpose.</p>
     *
     * <p>If you have set the status code or added any headers to this response,
     * these will be sent with the new request. But if you redispatch after having
     * written to the response it will fail with a <code>INVALID_STATE_ERR</code>.</p>
     *
     * <p>If {@link WebServerRequest#getUri()} is not changed, redispatching the request
     * will bypass any JavaScript event listeners in the next run. If the URI points
     * to a shared file, this file will be served. Otherwise a 404 will be shown
     * to the user.</p>
     *
     * @throws INVALID_STATE_ERR If data has been written to this response before it is redispatched.
     */
	public native void closeAndRedispatch() /*-{
		this.closeAndRedispatch();
	}-*/;

    /**
     * Flush the data in the response.
     *
     * <p>Flush the data in the response and send them to the client immediately.
     * Used in combination with chunked encoding, where each flush sends a chunk to the 
     * client.</p>
     */
	public native void flush() /*-{
		this.flush();
	}-*/;

    /**
     * Flush the data in the response.
     *
     * <p>Flush the data in the response and send them to the client immediately.
     * Used in combination with chunked encoding, where each flush sends a chunk to the 
     * client.</p>
     *
     * <p>Flushing is by default an asynchronous operation. You may catch when the
     * the response is actually flushed by supplying an optional <code>callback</code> parameter.</p>
     */
	public native void flush(FlushCallback callback) /*-{
		this.close(function() {
			try {
				callback.@opera.io.WebServerResponse.FlushCallback::onFlush()();
			} catch(exception) {
				@opera.io.Utils::handleException(Ljava/lang/Throwable;)(exception);
			}
		});	
	}-*/;



    /**
     * @return Connection this response will be sent to.
     */
	public native WebServerConnection getConnection() /*-{
		return this.connection;
	}-*/;

    /**
     * Whether or not to flush data written to the response automatically.
     *
     * <p>By default, you need to explicitly call <code>response.flush()</code> in order to
     * actually send data written to the response. By setting this property
     * to true, data are flushed immediately when they are written.</p>
     *
     * <p>Defaults to <code>false</code>.</p>
     */
	public native boolean isImplicitFlush() /*-{
		return this.implicitFlush;
	}-*/;

    /**
     * Set whether or not to flush data written to the response automatically. (See {@link #isImplicitFlush()}) 
     *
     */
	public native void setImplicitFlush(boolean value) /*-{
		this.implicitFlush = value;
	}-*/;

    /**
     * Whether or not this response has been closed.
     *
     * This property mirrors the {@link WebServerConnection#isClosed()} property.
     */
	public native boolean isClosed() /*-{
		return this.closed;
	}-*/;

    /**
     * Whether or not to use chunked encoding in the response.
     *
     * <p>Chunked encoding is used when you don't know the length of the encoding when starting to 
     * send data and to avoid frequently closing and reopening connections. It is only recommended
     * to turn this off if clients are  not expected to support chunked encoding.</p>
     *
     * <p>Defaults to <code>true</code>.</p>
     */
	public native boolean isChunked() /*-{
		return this.chunked;
	}-*/;
	
	/**
	 * Set whether or not to use chunked encoding in the response. (see {@link #isChunked()}
	 */
	public native void setChunked(boolean value) /*-{
		this.chunked = value;
	}-*/;

	/**
     * Set the protocol version string of the response.
     *
     * <p>Usually the protocol version string will contain a version of HTTP, but it can
     * be used to set other protocols as well.</p>
     *
     * <p>Note that you must set a protocol string before you start writing 
     * to the response. Failure to do so will result in an INVALID_STATE_ERR.</p>
     *
     * @param protocolString   Protocol string to set.
     * @throws INVALID_STATE_ERR If data has been written to the response before setting the protocol string.
     */
	public native void setProtocolString(String protocolString) /*-{
		this.setProtocolString(protocolString);
	}-*/;

    /**
     * Set a HTTP response header.
     *
     * <p>Use this method to specify response headers sent back to the client.
     * For example setting the content type to XML, by doing:
     * <code>response.setResponseHeader( "Content-type", "text/html" );</code></p>
     *
     * <p>Note that you must set any headers before you start writing 
     * to the response. Failure to do so will result in an INVALID_STATE_ERR.</p>
     *
     * @param name Name of the HTTP response header to set, e.g. "Content-type"
     * @param value Value to set for the given HTTP response header.
     * @throws INVALID_STATE_ERR If data has been written to the response before setting any headers.
     */
	public native void setResponseHeader(String name, String value) /*-{
		this.setResponseHeader(name, value);
	}-*/;


    /**
     * Set the HTTP status code of the response.
     *
     * <p>This method sets the status code sent back to the client,
     * such as 404 (Not found), 200 (OK), etc. An optional argument
     * can be used to set a specific response text as well, allowing
     * for messages not in the HTTP specification.</p>
     *
     * <p>If you do not set a status code, a 200 (OK) status 
     * code is silently set.</p>
     *
     * <p>Note that you must set any status before you start writing 
     * to the response. Failure to do so will result in an INVALID_STATE_ERR.</p>
     *
     * @param {String} statusCode Status code to set, e.g. 200 or 404
     * @throws INVALID_STATE_ERR If data has been written to the response before setting the status code.
     */
	public native void setStatusCode(int statusCode) /*-{
		this.setStatusCode(statusCode);
	}-*/;
	

    /**
     * Set the HTTP status code of the response with the give status text. (See {@link #setStatusCode(int)}
     *
     * @param {String} statusCode Status code to set, e.g. 200 or 404
     * @param {String} text Status text to set, e.g. "Success" or "Out of pidgeons". Optional.
     * @throws INVALID_STATE_ERR If data has been written to the response before setting the status code.
     */
	public native void setStatusCode(int statusCode, String text) /*-{
		// FIXME : Catch INVALID_STATE_ERR and re-throw as a Java Exception
		this.setStatusCode(statusCode, text);
	}-*/;

    /**
     * Write data to the response.
     * @param {String} data String of data to write.
     */
	public native void write(String data) /*-{
		this.write(data);
	}-*/;

    /**
     * Write binary data to the response.
     *
     * @param data Binary data to write
     */
	public native void writeBytes(byte[] data) /*-{
		this.writeBytes(data);
	}-*/;

    /**
     * Write a File to the response.
     *
     * This methods takes a <code>File</code> object and writes the
     * contents of the file to the response.
     *
     * @param file File object to write
     */
	public native void writeFile(File file) /*-{
		this.writeFile(file);
	}-*/;
    /**
     * Write an image to the response.
     *
     * The image or data referenced is serialized and written to the
     * response, encoded as a PNG image.
     *
     * @param image HTML Image element to write.
     */
	public native void writeImage(ImageElement image) /*-{
		this.writeImage(image);
	}-*/;

    /**
     * Write data to the response and append a newline.
     * @param data String of data to write.
     */
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
