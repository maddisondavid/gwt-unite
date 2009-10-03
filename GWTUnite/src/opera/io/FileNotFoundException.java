package opera.io;

public class FileNotFoundException extends IOException {
	private static final long serialVersionUID = 1944949881422565873L;
	private final String filename;
	
	public FileNotFoundException(String filename) {
		super("File "+filename+" not found");
		this.filename = filename;
	}
	
	public String getFilename() {
		return filename;
	}
}
