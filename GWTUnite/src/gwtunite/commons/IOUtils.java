package gwtunite.commons;


import opera.io.File;
import opera.io.FileMode;
import opera.io.FileStream;

/** A set of common IO Utilities 
 * 
 * Note: For these utilities to work, the File feature must be installed 
 */
public class IOUtils {

	/**
	 * Reads the complete contents of the given file and returns it as a string 
	 * 
	 * @param directory The directory in which the file can be found
	 * @param path The path of the file within the given directory
	 * @return Complete contents of the file
	 */
	public static String getFileContentsAsString(File directory, String path) {
		return getFileContentsAsString(directory, path, null);
	}
	
	/**
	 * Reads the complete contents of the given file and returns it as a string 
	 * 
	 * @param directory The directory in which the file can be found
	 * @param path The path of the file within the given directory
	 * @param charset The character set to use when reading the file
	 * @return Complete contents of the file
	 */
	public static String getFileContentsAsString(File directory, String path, String charset) {
		File actualFile = directory.resolve(path);
		if (!actualFile.exists())
			throw new RuntimeException("File " + directory._toString() + path + " cannot be found");

		FileStream stream = directory.open(path, FileMode.READ);
		try {
			if (charset == null) {
				return stream.read(stream.getBytesAvailable());
			} else {
				return stream.read(stream.getBytesAvailable(), charset);
			}
		} finally {
			stream.close();
		}
	}
	
	public static void writeStringToFile(File directory, String path, String contents) {
		writeStringToFile(directory, path, contents, FileMode.WRITE);
	}
	
	public static void appendStringToFile(File directory, String path, String contents) {
		writeStringToFile(directory, path, contents, FileMode.APPEND);
	}
	
	private static void writeStringToFile(File directory, String path, String contents, int fileMode) {
		File newFile = directory.resolve(path);
		FileStream stream = newFile.open(fileMode);
		try {
			stream.write(contents);
		} finally {
			stream.close();
		}
	}
}
