package opera.io;

public class IOException extends Exception {
	private static final long serialVersionUID = 1L;

	public IOException() {
	}

	public IOException(String arg0) {
		super(arg0);
	}

	public IOException(Throwable arg0) {
		super(arg0);
	}

	public IOException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
}
