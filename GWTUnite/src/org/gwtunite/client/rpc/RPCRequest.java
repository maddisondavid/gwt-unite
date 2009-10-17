package org.gwtunite.client.rpc;

public class RPCRequest {
	private final String methodName;
	private final Object[] parameters;
	
	public RPCRequest(String methodName, Object[] parameters) {
		this.methodName  = methodName;
		this.parameters = parameters;
	}
	
	public String getMethodName() {
		return methodName;
	}
	
	public Object[] getParameters() {
		return parameters;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("Method="+methodName+"\n");
		for (int i=0;i<parameters.length;i++) {
			buffer.append(i+"="+parameters[i]+"\n");
		}
		return buffer.toString();
	}
}
