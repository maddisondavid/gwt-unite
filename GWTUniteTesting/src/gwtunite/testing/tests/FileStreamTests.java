package gwtunite.testing.tests;

import java.util.Arrays;

import gwtunite.testing.framework.Test;
import gwtunite.testing.framework.TestCase;
import opera.io.File;
import opera.io.FileMode;
import opera.io.FileStream;
import opera.io.FileSystem;

public class FileStreamTests extends TestCase {

	@Test
	public void ableToReadAndWriteBytes() throws Exception {
		File sharedDir = FileSystem.getInstance().mountSystemDirectory(FileSystem.SHARED_SYSTEM_DIRECTORY);
		File binFile = sharedDir.resolve("BinaryFile");
		
		byte[] testBytes = {1,2,3,4,5};
		FileStream newStream = binFile.open(FileMode.WRITE);
		newStream.writeBytes(testBytes, testBytes.length);
		newStream.close();
		
		assertEquals(testBytes.length, binFile.getFileSize());
		
		FileStream inStream = binFile.open(FileMode.READ);
		byte[] inBytes = inStream.readBytes(inStream.getBytesAvailable());
		assertTrue(Arrays.equals(testBytes, inBytes));
		
		inStream.close();
		
		binFile.deleteFile(binFile);
	}
	
	@Test
	public void base64MethodsWork() throws Exception {
		
	}
}
