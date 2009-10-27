package org.gwtunite.client.rpc;

import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.impl.Serializer;

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
			int paramCount = streamReader.readInt();
			String[] parameterTypes = new String[paramCount];

			// Read Parameter Types
			for (int i = 0; i < parameterTypes.length; i++) {
				parameterTypes[i] = streamReader.readString();
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
	
	public static String encodeResponseForSuccess(Object response, Serializer serializer, Class returnType) throws SerializationException{
		return encodeResponse(returnType, response, serializer, false);
	}

	public static String encodeResponseForFailure(Exception response, Serializer serializer) throws SerializationException{
		return encodeResponse(Exception.class, response, serializer, true);
	}
	
	private static String encodeResponse(Class responseClass, Object object, Serializer serializer, boolean wasThrown) throws SerializationException {	
		GwtUniteSerializationStreamWriter stream = new GwtUniteSerializationStreamWriter(serializer);
		stream.prepareToWrite();
		
		/**
		 * We can't just send back the type of the object, we need to look at the return
		 * type.  The reason for this is polymorphism.  Basically, the client doesn't know 
		 * how to decode types if the return type is less specific
		 */
		if (responseClass != void.class)
			stream.serializeValue(object, responseClass);
		
		return (wasThrown ? "//EX" : "//OK") + stream.toString();
	}	
}
