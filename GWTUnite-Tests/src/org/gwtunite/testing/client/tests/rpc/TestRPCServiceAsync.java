package org.gwtunite.testing.client.tests.rpc;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.AsyncCallback;


public interface TestRPCServiceAsync {

	public void echo(Serializable object, AsyncCallback<Serializable> callback);
	public void echoArray(Serializable[] object, AsyncCallback<Serializable[]> callback);
}
