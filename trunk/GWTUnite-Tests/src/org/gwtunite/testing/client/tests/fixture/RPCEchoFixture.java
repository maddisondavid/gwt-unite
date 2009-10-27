package org.gwtunite.testing.client.tests.fixture;

import java.io.Serializable;
import java.util.Arrays;

import org.gwtunite.client.commons.Logging;
import org.gwtunite.testing.client.framework.TestCase;
import org.gwtunite.testing.client.tests.rpc.TestRPCService;
import org.gwtunite.testing.client.tests.rpc.TestRPCServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.StatusCodeException;

public class RPCEchoFixture {
	private final TestCase testCase;
	
	public RPCEchoFixture(TestCase testCase) {
		this.testCase = testCase;
	}
	
	public void setUp() {
	}
	
	public void tearDown() {
	}
	
	public void assertEquals(final Serializable object) {
		TestRPCServiceAsync testService = GWT.create(TestRPCService.class);
		testCase.delayTestFinish(5000);
		testService.echo(object, new AsyncCallback<Serializable>() {
			@Override public void onFailure(Throwable caught) {
				if (caught instanceof StatusCodeException)
					testCase.handleException(new Exception("HttpStatusCodeException "+((StatusCodeException)caught).getStatusCode()+":"+caught.getMessage()));
				
				Logging.log("Logging FAILURE! "+caught.getMessage());
				
				testCase.handleException(caught);
			}

			@Override
			public void onSuccess(Serializable result) {
				try {
					testCase.assertEquals(object, result);
					testCase.finishTest();
				}catch(Exception e) {
					testCase.handleException(e);
				}
			}
		});
	}
	
	public void assertArrayEquals(final Serializable[] object) {
		TestRPCServiceAsync testService = GWT.create(TestRPCService.class);
		testCase.delayTestFinish(5000);
		testService.echoArray(object, new AsyncCallback<Serializable[]>() {
			@Override public void onFailure(Throwable caught) {
				if (caught instanceof StatusCodeException)
					testCase.handleException(new Exception("HttpStatusCodeException "+((StatusCodeException)caught).getStatusCode()+":"+caught.getMessage()));
				
				Logging.log("Logging FAILURE! "+caught.getMessage());
				
				testCase.handleException(caught);
			}

			@Override
			public void onSuccess(Serializable[] result) {
				try {
					testCase.assertTrue(Arrays.equals(result, object));
					testCase.finishTest();
				}catch(Exception e) {
					testCase.handleException(e);
				}
			}
		});
	}
	
}
