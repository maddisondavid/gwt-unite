package org.gwtunite.testing.client.tests;

import org.gwtunite.client.commons.FileUtils;
import org.gwtunite.client.file.File;
import org.gwtunite.client.file.FileFilter;
import org.gwtunite.client.file.FileStream;
import org.gwtunite.client.file.FileSystem;
import org.gwtunite.client.file.File.FileMode;
import org.gwtunite.client.file.File.FileOperationCompletedHandler;
import org.gwtunite.testing.client.framework.Test;
import org.gwtunite.testing.client.framework.TestCase;

public class FileTests extends TestCase{

	public void setUp() throws Exception {
		File sharedDir = FileSystem.getInstance().mountSharedFileSystem();
		FileUtils.deleteDirectoryContents(sharedDir);
		FileSystem.getInstance().removeMountPoint("shared");
	}

	@Test
	public void listFilesProvidesAllFiles() throws Exception {
		File mountPoint = FileSystem.getInstance().mountApplicationFileSystem();
		File artifacts = mountPoint.resolve("TestArtifacts");
		File[] files = artifacts.listFiles();
		
		assertEquals(4, files.length);
	}
	
	@Test
	public void listFilesWithFilterGivesOnlyCertainFiles() throws Exception {
		File mountPoint = FileSystem.getInstance().mountApplicationFileSystem();
		File artifacts = mountPoint.resolve("TestArtifacts");
		File[] files = artifacts.listFiles(new FileFilter() {
							@Override
							public boolean accept(File pathname) {
								return pathname.getName().endsWith(".txt");
							}
						});
		
		assertEquals(1, files.length);
		assertEquals("ATestFile.txt", files[0].getName());
	}
	
	@Test
	public void canReadAnApplicationFile() throws Exception {
		File appDir = FileSystem.getInstance().mountApplicationFileSystem();
		FileStream testFile = appDir.resolve("TestArtifacts/ATestFile.txt").open(FileMode.READ);
		assertEquals("Hello".length(), testFile.getBytesAvailable());
		String string = testFile.read(testFile.getBytesAvailable());
		assertEquals("Hello", string);
		testFile.close();
	}
	
	@Test 
	public void resetingPositionIsReflectedInFileStream() throws Exception {
		File appDir = FileSystem.getInstance().mountApplicationFileSystem();
		FileStream testFile = appDir.resolve("TestArtifacts/ATestFile.txt").open(FileMode.READ);
		
		{
		assertEquals("Hello".length(), testFile.getBytesAvailable());
		String string = testFile.read(testFile.getBytesAvailable());
		assertEquals("Hello", string);
		}
		
		testFile.setPosition(0);
		
		{
		assertEquals("Hello".length(), testFile.getBytesAvailable());
		String string = testFile.read(testFile.getBytesAvailable());
		assertEquals("Hello", string);
		}
		
		testFile.setPosition(-5);
		
		{
		assertEquals("Hello".length(), testFile.getBytesAvailable());
		String string = testFile.read(testFile.getBytesAvailable());
		assertEquals("Hello", string);
		}

		testFile.setPosition(10);
		assertEquals(0, testFile.getBytesAvailable());
		
		testFile.close();
	}
	
	@Test
	public void eofFlagSetCorrectly() throws Exception {
		File appDir = FileSystem.getInstance().mountApplicationFileSystem();
		FileStream testFile = appDir.resolve("TestArtifacts/ATestFile.txt").open(FileMode.READ);
		
		assertEquals("Hello".length(), testFile.getBytesAvailable());
		String string = testFile.read(testFile.getBytesAvailable());
		assertEquals("Hello", string);
		assertTrue(testFile.isEof());
		
		testFile.close();
	}
	
	@Test
	public void readingSeveralCharactersLeavesStreamInKnownPosition() throws Exception {
		File appDir = FileSystem.getInstance().mountApplicationFileSystem();
		FileStream testFile = appDir.resolve("TestArtifacts/ATestFile.txt").open(FileMode.READ);
		
		assertEquals("Hello".length(), testFile.getBytesAvailable());
		String string = testFile.read(2);
		assertEquals("He", string);
		assertEquals(3, testFile.getBytesAvailable());
		assertFalse(testFile.isEof());
		
		testFile.close();
	}
	
	@Test
	public void fileAttributesAreCorrect() throws Exception {
		File appDir = FileSystem.getInstance().mountApplicationFileSystem();
		File testArtifictsDir = appDir.resolve("TestArtifacts");

		// Dir Tests
		assertTrue(testArtifictsDir.isDirectory()); 
		assertFalse(testArtifictsDir.isFile(),"Checking that directory not file"); // Currently this is broken
		assertFalse(testArtifictsDir.isArchive());
		assertTrue(testArtifictsDir.isReadOnly());
		assertEquals("TestArtifacts", testArtifictsDir.getName());
		assertEquals(null, testArtifictsDir.getCreated());
		assertTrue(testArtifictsDir.exists());
		
		// File Tests
		{
		File testFile = testArtifictsDir.resolve("ATestFile.txt");
		assertFalse(testFile.isDirectory(),"isDirectory");
		assertTrue(testFile.isFile(),"isFile");
		assertFalse(testFile.isArchive(),"isArchive");
		assertTrue(testFile.isReadOnly(),"isReadOnly");
		assertTrue(testFile.exists());
		assertEquals("ATestFile.txt", testFile.getName());
		assertEquals("mountpoint://application/TestArtifacts/ATestFile.txt", testFile.getVirtualPath());
		assertEquals("Hello".length(), testFile.getFileSize());
	//	assertEquals("TestArtifacts/ATestFile.txt", testFile.getNativePath()); // Currently Broken
		}

		// Archive Tests
		{
		File testFile = testArtifictsDir.resolve("TestArchive.zip");
		assertFalse(testFile.isDirectory(),"isDirectory");
		assertTrue(testFile.isFile(),"isFile");
		assertTrue(testFile.isArchive(),"isArchive");
		assertTrue(testFile.isReadOnly(),"isReadOnly");
		assertEquals("TestArchive.zip", testFile.getName());
		assertTrue(testFile.exists());
//		assertNotNull(testFile.getCreated(),"Checking created date not null"); // Broken for now!
//		Date created = testFile.getCreated();
//		assertEquals(2009, created.getYear());
		
		//assertNotNull(testFile.getModified(),"Testing getModified not null");
		}		
		
		{
		File testFile = testArtifictsDir.resolve("DoesNotExist");
		assertFalse(testFile.exists());
		}
	}
	
	@Test
	public void canWriteAndReadFilesInSharedSpace() throws Exception {
		File sharedDir = FileSystem.getInstance().mountSharedFileSystem();
		assertEquals(0, sharedDir.listFiles().length, "sharedDir not empty");
		
		final String testString = "HelloWorld";

		// Write Shared File
		File newFile = sharedDir.resolve("NewFile");
		{
		FileStream stream = newFile.open(FileMode.WRITE);
		stream.write(testString);
		stream.close();
		}
		
		// Check directory listing
		assertEquals(1,sharedDir.listFiles().length);
		assertEquals("NewFile", sharedDir.listFiles()[0].getName());
		
		// Read File back in
		{
		FileStream stream = newFile.open(FileMode.READ, FileMode.APPEND);
		assertEquals(testString.length(), stream.getBytesAvailable(), "Testing file size");
		assertEquals(testString, stream.read(stream.getBytesAvailable()), "Testing file contents");
		stream.close();
		}
		
		// Delete File
		{
		newFile.delete();
		assertEquals(0,sharedDir.listFiles().length);
		}
	}
	
	@Test
	public void createDirectoryWorks()  throws Exception {
		File sharedDir = FileSystem.getInstance().mountSharedFileSystem();
		
		// Delete With File object
		{
		File newDir = sharedDir.resolve("testMe").mkDir();
		
		assertEquals(1, sharedDir.listFiles().length);
		newDir.delete();
		assertEquals(0, sharedDir.listFiles().length);
		}
		
		// Delete With String Name
		{
		File newDir = sharedDir.resolve("testMe").mkDir();
		
		assertEquals(1, sharedDir.listFiles().length);
		newDir.delete();
		assertEquals(0, sharedDir.listFiles().length);
		}
	}
	
	@Test
	public void copyMethodsWork() throws Exception {
		File sharedDir = FileSystem.getInstance().mountSharedFileSystem();
		
		File newDir1 = sharedDir.resolve("testMe").mkDir();
		File newDir2 = sharedDir.resolve("testMe2").mkDir();
		
		File newFile = newDir1.resolve("TestFile");
		FileStream fileStream = newFile.open(FileMode.WRITE);
		fileStream.write("Hello");
		fileStream.close();
		
		assertEquals(1,newDir1.listFiles().length);
		
		newFile.copyTo(newDir2, false);

		assertEquals(1,newDir1.listFiles().length);

		
		assertEquals(1, newDir2.listFiles().length);
		assertEquals("TestFile", newDir2.listFiles()[0].getName());
		
		// Cleanup
		newDir1.delete();
		newDir2.delete();
		
		assertEquals(0, sharedDir.listFiles().length);
	}
	
	@Test
	public void moveMethodsWork() throws Exception {
		File sharedDir = FileSystem.getInstance().mountSharedFileSystem();
		
		File newDir1 = sharedDir.resolve("testMe").mkDir();
		File newDir2 = sharedDir.resolve("testMe2").mkDir();
		
		File newFile = newDir1.resolve("TestFile");
		FileStream fileStream = newFile.open(FileMode.WRITE);
		fileStream.write("Hello");
		fileStream.close();
		
		assertEquals(1,newDir1.listFiles().length,"Listing Contents of newDir1");
		
		newFile.moveTo(newDir2, false);

		assertEquals(0,newDir1.listFiles().length);

		assertEquals(1, newDir2.listFiles().length,"Listing Contents of newDir2 (afterMove)");
		assertEquals("TestFile", newDir2.listFiles()[0].getName());
		
		// Cleanup
		newDir1.delete();
		newDir2.delete();
		
		assertEquals(0, sharedDir.listFiles().length);
	}
	
	@Test
	public void copyMethodsWithCallbackWork() throws Exception{
		// Callbacks are not currently, erm, called back (see Opera bug US-1160)
		
		File sharedDir = FileSystem.getInstance().mountSharedFileSystem();
		
		File newDir1 = sharedDir.resolve("testMe").mkDir();
		File newDir2 = sharedDir.resolve("testMe2").mkDir();
		
		File newFile = newDir1.resolve("TestFile");
		FileStream fileStream = newFile.open(FileMode.WRITE);
		fileStream.write("Hello");
		fileStream.close();
		
		assertEquals(1,newDir1.listFiles().length);

		delayTestFinish(1000);
		newFile.copyTo(newDir2, true, new FileOperationCompletedHandler() {
			@Override public void onComplete(File file) {
				try {
					assertNotNull(file);
					assertEquals("MyFile",file.getName());
					finishTest();
				} catch(Exception e) {
					handleException(e);
				}
			}
		});
	}
}
