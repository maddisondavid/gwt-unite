package opera.io;

public class FeatureUnavailableException extends RuntimeException {
	private static final long serialVersionUID = -3724934282568336650L;

	public FeatureUnavailableException(String msg) {
		super(msg);
	}
	
	public FeatureUnavailableException(String msg, Exception reason) {
		super(msg, reason);
	}
}
