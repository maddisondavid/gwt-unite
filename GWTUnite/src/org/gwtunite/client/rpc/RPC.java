package org.gwtunite.client.rpc;

import org.gwtunite.client.commons.Logging;

import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.impl.Serializer;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.google.gwt.user.server.rpc.impl.ServerSerializationStreamWriter;

public class RPC {

	//public static RPCRequest decodeRequest(String encodedRequest, Class<?> type, SerializationPolicyProvider serializationPolicyProvider) {
	public static RPCRequest decodeRequest(String encodedRequest, Serializer serializer) {
		if (encodedRequest == null) {
			throw new NullPointerException("encodedRequest cannot be null");
		}

		if (encodedRequest.length() == 0) {
			throw new IllegalArgumentException("encodedRequest cannot be empty");
		}

		try {
			GwtUniteSerializationStreamReader streamReader = new GwtUniteSerializationStreamReader(serializer);
//			ServerSerializationStreamReader streamReader = new ServerSerializationStreamReader(
//					classLoader, serializationPolicyProvider);
			streamReader.prepareToRead(encodedRequest);

			// Read the name of the RemoteService interface
			String serviceIntfName = streamReader.readString();

//			if (type != null) {
//				if (!implementsInterface(type, serviceIntfName)) {
//					// The service does not implement the requested interface
//					throw new IncompatibleRemoteServiceException(
//							"Blocked attempt to access interface '"
//									+ serviceIntfName
//									+ "', which is not implemented by '"
//									+ printTypeName(type)
//									+ "'; this is either misconfiguration or a hack attempt");
//				}
//			}

//			SerializationPolicy serializationPolicy = streamReader
//					.getSerializationPolicy();
//			Class<?> serviceIntf;
//			try {
//				serviceIntf = getClassFromSerializedName(serviceIntfName,
//						classLoader);
//				if (!RemoteService.class.isAssignableFrom(serviceIntf)) {
//					// The requested interface is not a RemoteService interface
//					throw new IncompatibleRemoteServiceException(
//							"Blocked attempt to access interface '"
//									+ printTypeName(serviceIntf)
//									+ "', which doesn't extend RemoteService; this is either misconfiguration or a hack attempt");
//				}
//			} catch (ClassNotFoundException e) {
//				throw new IncompatibleRemoteServiceException(
//						"Could not locate requested interface '"
//								+ serviceIntfName + "' in default classloader",
//						e);
//			}

			String serviceMethodName = streamReader.readString();
Logging.log("Got MethodName"+serviceMethodName);
			int paramCount = streamReader.readInt();
Logging.log("Got ParameterCount="+paramCount);			
			String[] parameterTypes = new String[paramCount];

			// Read Parameter Types
			for (int i = 0; i < parameterTypes.length; i++) {
				parameterTypes[i] = streamReader.readString();
				Logging.log("Read parameter type "+i+"=" +parameterTypes[i]);
			}

			// Read parameter values
			Object[] parameterValues = new Object[parameterTypes.length];
			for (int i = 0; i < parameterValues.length; i++) {
				parameterValues[i] = streamReader.deserializeValue(parameterTypes[i]);
			}

			return new RPCRequest(serviceMethodName, parameterValues);

		} catch (SerializationException ex) {
			throw new IncompatibleRemoteServiceException(ex.getMessage(), ex);
		}
	}
	
	public static String encodeResponseForSuccess(Object response, Serializer serializer) throws SerializationException{
		return encodeResponse(response, serializer, false);
	}
	
	private static String encodeResponse(Object object, Serializer serializer, boolean wasThrown) throws SerializationException {	
		GwtUniteSerializationStreamWriter stream = new GwtUniteSerializationStreamWriter(serializer);
		stream.prepareToWrite();
		
		try {
			stream.writeObject(object);
		} catch (SerializationException e) {
			Logging.log("Serialization Exception "+e.getMessage());
		}
		
		return (wasThrown ? "//EX" : "//OK") + stream.toString();
	}	
}
