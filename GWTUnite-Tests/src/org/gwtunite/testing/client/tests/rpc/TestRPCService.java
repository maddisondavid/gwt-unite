package org.gwtunite.testing.client.tests.rpc;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath(value="TestRPC")
public interface TestRPCService extends RemoteService {

	public Serializable echo(Serializable object);
	public Serializable[] echoArray(Serializable[] arrray);
}
