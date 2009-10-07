package org.gwtunite.testing.client.framework;

public interface TestResultListener {

	void onPass();
	void onFailed(String message);
	void onError(Exception exception);
}
