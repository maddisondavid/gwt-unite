/**
 * 
 */
package org.gwtunite.client.rpc;

import org.gwtunite.client.commons.IOUtils;
import org.gwtunite.client.commons.Logging;
import org.gwtunite.client.file.File;
import org.gwtunite.client.file.FileSystem;
import org.gwtunite.client.net.WebServerEventHandler;
import org.gwtunite.client.net.WebServerRequest;
import org.gwtunite.client.net.WebServerResponse;

import com.google.gwt.user.client.rpc.impl.ClientSerializationStreamReader;
import com.google.gwt.user.client.rpc.impl.Serializer;

/**
 * Represents a Remote Service
 * 
 */
public class RemoteService extends WebServerEventHandler {
	protected final Object VOID = new Object();
	
	@Override
	public void onConnection(WebServerRequest request, WebServerResponse response) {
		//File appDir = FileSystem.getInstance().mountApplicationFileSystem();
		//File testStuff = appDir.resolve("TestStuff");
		try {
//			String encodedRequest = IOUtils.getFileContentsAsString(testStuff, "personPayload.txt");
			String encodedRequest = request.getBodyAsText();
			
//			GwtUniteSerializationStreamReader reader = new GwtUniteSerializationStreamReader(getSerializer());
//			reader.prepareToRead(payLoad);
//			
//			Logging.log("STream VERSIOn : "+reader.getVersion());
//			Logging.log("STream FLAGS : "+reader.getFlags());

			RPCRequest rpcRequest = RPC.decodeRequest(encodedRequest, getSerializer());
			Logging.log("Got RPC Request : "+rpcRequest);
			
			try {
				Object methodResponse = callMethod(rpcRequest.getMethodName(), rpcRequest.getParameters());
				
				if (methodResponse == VOID) {
					Logging.log("VOID method response");
				} else {
					Logging.log("Method Response="+methodResponse);
				}
				String responseToSend = RPC.encodeResponseForSuccess(methodResponse, getSerializer());
				Logging.log("Returning Response : "+responseToSend);
				
				response.writeLine(responseToSend);
				
			} catch(Exception e) {
				Logging.log("An exception was thrown! "+e.getMessage());
			}
		} catch (Exception e) {
			Logging.handleException(e);
			response.close();
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
