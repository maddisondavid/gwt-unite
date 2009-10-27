package org.gwtunite.testing.client.tests.rpc;

import java.io.Serializable;

import org.gwtunite.client.rpc.GwtUniteRemoteService;

public class TestRPCServiceImpl extends GwtUniteRemoteService implements TestRPCService {

	@Override public Serializable echo(Serializable object) {
		return object;
	}
	
	@Override public Serializable[] echoArray(Serializable[] array) {
		return array;
	}
}
