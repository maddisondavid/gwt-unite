package org.gwtunite.client.commons;

import org.gwtunite.client.file.File;
import org.gwtunite.client.file.FileSystem;
import org.gwtunite.client.file.IOException;

public class FileUtils {
    private static final long ONE_KB = 1024;
    private static final long ONE_MB = ONE_KB * ONE_KB;
    private static final long ONE_GB = ONE_KB * ONE_MB;

    /**
     * Returns a human-readable version of the file size (original is in * bytes).
     *
     * NOTE : Taken from Apache Commons FileUtils
     *
     * @param size The number of bytes.
     * @return A human-readable display value (includes units).
     */
    public static String byteCountToDisplaySize(long size) {
        if (size / ONE_GB > 0) {
            return String.valueOf(size / ONE_GB) + " GB";
        } else if (size / ONE_MB > 0) {
        	return String.valueOf(size / ONE_MB) + " MB";
        } else if (size / ONE_KB > 0) {
        	return String.valueOf(size / ONE_KB) + " KB";
        } else {
        	return String.valueOf(size) + " bytes";
        }
    }
    
    public static void deleteDirectoryContents(File directory) throws IOException {
    	if (directory == null)
    		throw new NullPointerException("deleteDirectoryContents directory=null");
    	
    	if (!directory.isDirectory())
    		throw new IOException(directory.getName()+" is not a directory");
    	
		for (File file : directory.listFiles()) {
			file.delete();
		}
    }
}
