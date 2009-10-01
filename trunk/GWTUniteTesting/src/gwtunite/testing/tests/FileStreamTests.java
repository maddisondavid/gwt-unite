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
		assertFalse(binFile.exists());
	}
	
	@Test
	public void base64MethodsWork() throws Exception {
		File sharedDir = FileSystem.getInstance().mountSystemDirectory(FileSystem.SHARED_SYSTEM_DIRECTORY);
		File binFile = sharedDir.resolve("BinaryFile");
		
		String test64 = "dGVzdA=="; // == Test
		FileStream newStream = binFile.open(FileMode.WRITE);
		newStream.writeBase64(test64);
		newStream.close();
		
		assertEquals(4, binFile.getFileSize());
		
		FileStream inStream = binFile.open(FileMode.READ);
		String in64 = inStream.readBase64(inStream.getBytesAvailable());
		inStream.close();
		assertEquals(in64, test64);
		
		
		binFile.deleteFile(binFile);
	}
}
