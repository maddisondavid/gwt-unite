package org.gwtunite.client.commons;


import org.gwtunite.client.file.File;
import org.gwtunite.client.file.FileStream;
import org.gwtunite.client.file.IOException;
import org.gwtunite.client.file.SecurityException;
import org.gwtunite.client.file.File.FileMode;

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
	 * @throws IOException 
	 */
	public static String getFileContentsAsString(File directory, String path) throws SecurityException, IOException {
		return getFileContentsAsString(directory, path, null);
	}
	
	/**
	 * Reads the complete contents of the given file and returns it as a string 
	 * 
	 * @param directory The directory in which the file can be found
	 * @param path The path of the file within the given directory
	 * @param charset The character set to use when reading the file
	 * @return Complete contents of the file
	 * @throws IOException 
	 */
	public static String getFileContentsAsString(File directory, String path, String charset) throws SecurityException, IOException{
		File actualFile = directory.resolve(path);
		if (!actualFile.exists())
			throw new RuntimeException("File " + directory._toString() + "/"+path + " cannot be found");

		FileStream stream = directory.resolve(path).open(FileMode.READ);
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
	
	public static void writeStringToFile(File directory, String path, String contents) throws SecurityException, IOException{
		writeStringToFile(directory, path, contents, FileMode.WRITE);
	}
	
	public static void appendStringToFile(File directory, String path, String contents) throws SecurityException, IOException{
		writeStringToFile(directory, path, contents, FileMode.APPEND);
	}
	
	private static void writeStringToFile(File directory, String path, String contents, FileMode fileMode) throws SecurityException, IOException{
		File newFile = directory.resolve(path);
		FileStream stream = newFile.open(fileMode);
		try {
			stream.write(contents);
		} finally {
			stream.close();
		}
	}
}
