package org.gwtunite.testing.client.tests;


import org.gwtunite.client.file.ByteArray;
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
		File appDir = FileSystem.getInstance().mountApplicationFileSystem();
		
		File imgFile = appDir.resolve("TestArtifacts/TestPass.png");
		File binFile = sharedDir.resolve("BinaryFile.png");

		FileStream imgStream = imgFile.open(FileMode.READ);
		ByteArray imgBytes = imgStream.readBytes(imgStream.getBytesAvailable());
		
		FileStream binStream = binFile.open(FileMode.WRITE);
		binStream.writeBytes(imgBytes, imgBytes.length());

		binStream.close();
		imgStream.close();
		
//		binFile.delete();
		assertEquals(binFile.getFileSize(), imgFile.getFileSize());
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
