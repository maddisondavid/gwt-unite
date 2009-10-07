package org.gwtunite.testing.client.tests;


import java.util.Arrays;

import org.gwtunite.client.file.File;
import org.gwtunite.client.file.FileStream;
import org.gwtunite.client.file.FileSystem;
import org.gwtunite.client.file.File.FileMode;
import org.gwtunite.testing.client.framework.Test;
import org.gwtunite.testing.client.framework.TestCase;


public class FileStreamTests extends TestCase {

	@Test
	public void ableToReadAndWriteBytes() throws Exception {
		File sharedDir = FileSystem.getInstance().mountSharedFileSystem();
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
		
		binFile.delete();
		assertFalse(binFile.exists());
	}
	
	@Test
	public void base64MethodsWork() throws Exception {
		File sharedDir = FileSystem.getInstance().mountSharedFileSystem();
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
		
		
		binFile.delete();
	}
}
