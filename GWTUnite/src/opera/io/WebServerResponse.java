package opera.io;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.ImageElement;

final public class WebServerResponse extends JavaScriptObject {
	public static final int SC_ACCEPTED = 202;
	public static final int SC_BAD_GATEWAY = 502;
	public static final int SC_BAD_REQUEST = 400;
	public static final int SC_CONFLICT = 409;
	public static final int SC_CONTINUE = 100;
	public static final int SC_CREATED = 201;
	public static final int SC_EXPECTATION_FAILED = 417;
	public static final int SC_FORBIDDEN = 403;
	public static final int SC_GATEWAY_TIMEOUT = 405;
	public static final int SC_GONE = 410;
	public static final int SC_HTTP_VERSION_NOT_SUPPORTED = 505;
	public static final int SC_INTERNAL_SERVER_ERROR = 500;
	public static final int SC_LENGTH_REQUIRED = 411;
	public static final int SC_METHOD_NOT_ALLOWED = 405;
	public static final int SC_MOVED_PERMANENTLY = 301;
	public static final int SC_MOVED_TEMPORARILY = 302;
	public static final int SC_MULTIPLE_CHOICES = 300;
	public static final int SC_NO_CONTENT = 204;
	public static final int SC_NON_AUTHORITATIVE_INFORMATION = 203;
	public static final int SC_NOT_ACCEPTABLE = 406;
	public static final int SC_NOT_FOUND = 404;
	public static final int SC_NOT_IMPLEMENTED = 501;
	public static final int SC_NOT_MODIFIED = 304;
	public static final int SC_OK = 200;
	public static final int SC_PARTIAL_CONTENT = 206;
	public static final int SC_PAYMENT_REQUIRED = 402;
	public static final int SC_PRECONDITION_FAILED = 412;
	public static final int SC_PROXY_AUTHENTICATION_REQUIRED = 407;
	public static final int SC_REQUEST_ENTITY_TOO_LARGE = 413;
	public static final int SC_REQUESTED_RANGE_NOT_SATISFIABLE = 416;
	public static final int SC_RESET_CONTENT = 205;
	public static final int SC_SEE_OTHER = 303;
	public static final int SC_SERVICE_UNAVAILABLE = 503;
	public static final int SC_SWITCHING_PROTOCOLS = 101;
	public static final int SC_TEMPORARY_REDIRECT = 307;
	public static final int SC_UNAUTHORIZED = 401;
	public static final int SC_UNSUPPORTED_MEDIA_TYPE = 415;
	public static final int SC_USE_PROXY = 305;
	  
	protected WebServerResponse() {
	}
	
    /**
     * Close the connection.
     *
     * <p>No writing to the the response is possible after <code>close()</code> is called and 
     * {@link WebServerConnection#isClosed()} will return true.</p>
     * 
     * <p>Note: The connection MAY not be closed immediately.  If you require notification of 
     * when the conncetion is actually closed, use {@link #close(CloseCallback)}</p>
     */
	public native void close() /*-{
		this.close();
	}-*/;

    /**
     * Close the connection.
     *
     * <p>No writing to the the response is possible after <code>close()</code> is called and 
     * {@link WebServerConnection#isClosed()} will return true. </p>
     *
     * <p>Closing is by default an asynchronous operation. You may catch when the
     * connection actually closes by supplying the callback.</p>
     *
     * @param callback object to call when the connection is closed.
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
     * @throws IOException If data has been written to this response before it is redispatched.
     */
	public native void closeAndRedispatch() throws IOException /*-{
		try {
			this.closeAndRedispatch();
		} catch(error) {
			if (error == "INVALID_STATE_ERR") {
				throw(@opera.io.IOException::new(Ljava/lang/String;)("Invalid State:Data has already been written"));
			} else {
				throw(error);
			}
		}		
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
     * 
     * @param callback the object to be called when the flush happens
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
     * Retrieves the connection this response will be sent back over
     * 
     * @return connection to the client
     */
	public native WebServerConnection getConnection() /*-{
		return this.connection;
	}-*/;

    /**
     * Whether or not to flush data written to the response automatically.
     *
     * <p>By default, you need to explicitly call <code>response.flush()</code> in order to
     * actually send data written to the response. Use {@link #setImplicitFlush(boolean)
     * to true, so that data is flushed immediately when it's written.</p>
     * 
     * @return true if data should be sent immediately to the client when written
     */
	public native boolean isImplicitFlush() /*-{
		return this.implicitFlush;
	}-*/;

    /**
     * Set whether or not to flush data written to the response automatically. (See {@link #isImplicitFlush()})
     * 
     * @param value the value for the implicit flush flag
     */
	public native void setImplicitFlush(boolean value) /*-{
		this.implicitFlush = value;
	}-*/;

    /**
     * Whether or not this response has been closed.
     *
     * <p>This property mirrors the {@link WebServerConnection#isClosed()} property.</p>
     * 
     * @return true if the connection underneath this response has been closed
     */
	public native boolean isClosed() /*-{
		return this.closed;
	}-*/;

    /**
     * Whether or not chunked encoding is currently being used.
     * 
     * @return true if chunked encoding is being used
     */
	public native boolean isChunked() /*-{
		return this.chunked;
	}-*/;
	
	/**
	 * Set whether or not to use chunked encoding in the response. (see {@link #isChunked()}
	 * 
     * <p>Chunked encoding is used when you don't know the length of the encoding when starting to 
     * send data and to avoid frequently closing and reopening connections. It is only recommended
     * to turn this off if clients are  not expected to support chunked encoding.</p>
     *
     * <p>Defaults to <code>true</code>.</p>
     * 
     * @param value the value to set the chunked encoding flag to
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
     * to the response. Failure to do so will result in an IOException.</p>
     *
     * @param protocolString Protocol to use in the response
     * @throws IOException if data has been written to the response before setting the protocol string.
     */
	public native void setProtocolString(String protocolString) throws IOException /*-{
		try {
			this.setProtocolString(protocolString);
		} catch(error) {
			if (error == "INVALID_STATE_ERR") {
				throw(@opera.io.IOException::new(Ljava/lang/String;)("Invalid State:Data has already been written"));
			} else {
				throw(error);
			}
		}		
	}-*/;

    /**
     * Set a HTTP response header.
     *
     * <p>Use this method to specify response headers sent back to the client.
     * For example setting the content type to XML, by doing:
     * <code>response.setHeader( "Content-type", "text/html" );</code></p>
     *
     * <p>Note that you must set any headers before you start writing 
     * to the response. Failure to do so will result in an IOException.</p>
     *
     * @param name Name of the HTTP response header to set, e.g. "Content-type"
     * @param value Value to set for the given HTTP response header.
     * @throws IOException If data has been written to the response before setting any headers.
     */
	public native void setHeader(String name, String value) /*-{
		try {
			this.setResponseHeader(name, value);
		} catch(error) {
			if (error == "INVALID_STATE_ERR") {
				throw(@opera.io.IOException::new(Ljava/lang/String;)("Invalid State:Data has already been written"));
			} else {
				throw(error);
			}
		}		
	}-*/;

    /**
     * Set the HTTP status code of the response.
     *
     * <p>If you do not set a status code, a 200 (OK) status 
     * code is silently set.</p>
     *
     * <p>Note that you must set any status before you start writing 
     * to the response. Failure to do so will result in an IOException.</p>
     *
     * @param statusCode Status code to set, e.g. 200 or 404
     * @throws IOException If data has been written to the response before setting the status code.
     */
	public native void setStatusCode(int statusCode) /*-{
		try {
			this.setStatusCode(statusCode);
		} catch(error) {
			if (error == "INVALID_STATE_ERR") {
				throw(@opera.io.IOException::new(Ljava/lang/String;)("Invalid State:Data has already been written"));
			} else {
				throw(error);
			}
		}		
	}-*/;

    /**
     * Set the HTTP status code of the response with the give status text. (See {@link #setStatusCode(int)}
     * 
     * <p>If you do not set a status code, a 200 (OK) status 
     * code is silently set.</p>
     *
     * <p>Note that you must set any status before you start writing 
     * to the response. Failure to do so will result in an IOException.</p>
     *
     * @param statusCode Status code to set, e.g. {@link #SC_OK} or {@link #SC_NOT_FOUND}
     * @param text Status text to set, e.g. "Success" or "Out of pidgeons".
     * @throws IOException If data has been written to the response before setting the status code.
     */
	public native void setStatusCode(int statusCode, String text) /*-{
		try {
			this.setStatusCode(statusCode, text);
		} catch(error) {
			if (error == "INVALID_STATE_ERR") {
				throw(@opera.io.IOException::new(Ljava/lang/String;)("Invalid State:Data has already been written"));
			} else {
				throw(error);
			}
		}		
	}-*/;

    /**
     * Write data to the response.
     * 
     * @param data String of data to write.
     */
	public native void write(String data)/*-{
		this.write(data);
	}-*/;

    /**
     * Write binary data to the response.
     *
     * TODO : Fix the type used in this method!
     *
     * @param data Binary data to write
     */
	public native void writeBytes(byte[] data) /*-{
		this.writeBytes(data);
	}-*/;

    /**
     * Write a File to the response.
     *
     * <p>This methods takes a <code>File</code> object and writes the
     * contents of the file to the response.</p>
     *
     * @param file File object to write
     */
	public native void writeFile(File file) /*-{
		this.writeFile(file);
	}-*/;
    /**
     * Write an image to the response.
     *
     * <p>The image or data referenced is serialized and written to the
     * response, encoded as a PNG image.</p>
     *
     * @param image HTML Image element to write.
     */
	public native void writeImage(ImageElement image) /*-{
		this.writeImage(image);
	}-*/;

    /**
     * Write data to the response and append a newline.
     * 
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
