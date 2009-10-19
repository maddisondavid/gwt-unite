/**
 * 
 */
package org.gwtunite.client.rpc;

import org.gwtunite.client.commons.Logging;
import org.gwtunite.client.net.WebServerEventHandler;
import org.gwtunite.client.net.WebServerRequest;
import org.gwtunite.client.net.WebServerResponse;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.impl.Serializer;

/**
 * Represents a Remote Service
 * 
 */
public class RemoteService extends WebServerEventHandler {
	final static Object VOID_RETURN = new Object();
	
	@Override
	public void onConnection(WebServerRequest request, WebServerResponse response) {
		try {
			String encodedRequest = request.getBodyAsText();
			RPCRequest rpcRequest = RPC.decodeRequest(encodedRequest, getSerializer());
			
			Object methodResponse = callMethod(rpcRequest.getMethodName(), rpcRequest.getParameters());
			
			// What do we do about VOID response?
			response.write(RPC.encodeResponseForSuccess(methodResponse, getSerializer()));
		} catch (Exception e) {
			try {
				response.write(RPC.encodeResponseForFailure(e, getSerializer()));
			} catch(SerializationException se ) {
				Logging.log("Unable to send result back to client due to "+se);
			}
		}
		finally {
			response.close();
		}
	}
	
	protected Object callMethod(String methodName, Object[] parameters) throws Exception {
		throw new UnsupportedOperationException("Overrided at compile time");
	}
	
	protected Serializer getSerializer() {
		throw new UnsupportedOperationException("Overrided at compile time");
	}
}
