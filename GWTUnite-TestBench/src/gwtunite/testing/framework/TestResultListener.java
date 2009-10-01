package gwtunite.testing.framework;

public interface TestResultListener {

	void onPass();
	void onFailed(String message);
	void onError(Exception exception);
}
