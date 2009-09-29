package gwtunite.testing.tests;

import gwtunite.testing.framework.Test;
import gwtunite.testing.framework.TestCase;
import opera.io.File;
import opera.io.FileMode;
import opera.io.FileStream;
import opera.io.FileSystem;

public class FileTests extends TestCase{

	public void setUp() throws Exception {
		File appDir = FileSystem.getInstance().mountSystemDirectory(FileSystem.SHARED_SYSTEM_DIRECTORY);
		appDir.refresh();
		for (File file : appDir.getContents()) {
			if (file.isDirectory()) {
				file.deleteDirectory(file, true);
			} else {
				file.deleteFile(file);
			}
		}
		FileSystem.getInstance().removeMountPoint(FileSystem.SHARED_SYSTEM_DIRECTORY);
	}
	
	@Test
	public void canReadAnApplicationFile() throws Exception {
		File appDir = FileSystem.getInstance().mountSystemDirectory(FileSystem.APPLICATION_SYSTEM_DIRECTORY);
		FileStream testFile = appDir.open("TestArtifacts/ATestFile.txt", FileMode.READ);
		assertEquals("Hello".length(), testFile.getBytesAvailable());
		String string = testFile.read(testFile.getBytesAvailable());
		assertEquals("Hello", string);
		testFile.close();
	}
	
	@Test 
	public void resetingPositionIsReflectedInFileStream() throws Exception {
		File appDir = FileSystem.getInstance().mountSystemDirectory(FileSystem.APPLICATION_SYSTEM_DIRECTORY);
		FileStream testFile = appDir.open("TestArtifacts/ATestFile.txt", FileMode.READ);
		
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
		File appDir = FileSystem.getInstance().mountSystemDirectory(FileSystem.APPLICATION_SYSTEM_DIRECTORY);
		FileStream testFile = appDir.open("TestArtifacts/ATestFile.txt", FileMode.READ);
		
		assertEquals("Hello".length(), testFile.getBytesAvailable());
		String string = testFile.read(testFile.getBytesAvailable());
		assertEquals("Hello", string);
		assertTrue(testFile.isEof());
		
		testFile.close();
	}
	
	@Test
	public void readingSeveralCharactersLeavesStreamInKnownPosition() throws Exception {
		File appDir = FileSystem.getInstance().mountSystemDirectory(FileSystem.APPLICATION_SYSTEM_DIRECTORY);
		FileStream testFile = appDir.open("TestArtifacts/ATestFile.txt", FileMode.READ);
		
		assertEquals("Hello".length(), testFile.getBytesAvailable());
		String string = testFile.read(2);
		assertEquals("He", string);
		assertEquals(3, testFile.getBytesAvailable());
		assertFalse(testFile.isEof());
		
		testFile.close();
	}
	
	@Test
	public void fileAttributesAreCorrect() throws Exception {
		File appDir = FileSystem.getInstance().mountSystemDirectory(FileSystem.APPLICATION_SYSTEM_DIRECTORY);
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
		assertEquals("mountpoint://application/TestArtifacts/ATestFile.txt", testFile.getPath());
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
		File sharedDir = FileSystem.getInstance().mountSystemDirectory(FileSystem.SHARED_SYSTEM_DIRECTORY);
		sharedDir.refresh();
		assertEquals(0, sharedDir.getContents().length, "sharedDir not empty");
		
		final String testString = "HelloWorld";

		// Write Shared File
		File newFile = sharedDir.resolve("NewFile");
		{
		FileStream stream = newFile.open(FileMode.WRITE);
		stream.write(testString);
		stream.close();
		}
		
		// Check directory listing
		sharedDir.refresh();
		assertEquals(1,sharedDir.getContents().length);
		assertEquals(1,sharedDir.getLength());
		assertEquals("NewFile", sharedDir.getContents()[0].getName());
		
		// Read File back in
		{
		FileStream stream = newFile.open(FileMode.READ);
		assertEquals(testString.length(), stream.getBytesAvailable());
		assertEquals(testString, stream.read(stream.getBytesAvailable()));
		stream.close();
		}
		
		// Delete File
		{
		newFile.deleteFile(newFile);
		sharedDir.refresh();
		assertEquals(0,sharedDir.getContents().length);
		}
	}
	
	@Test
	public void createDirectoryWorks()  throws Exception {
		File sharedDir = FileSystem.getInstance().mountSystemDirectory(FileSystem.SHARED_SYSTEM_DIRECTORY);
		
		// Delete With File object
		{
		File newDir = sharedDir.createDirectory("testMe");
		sharedDir.refresh();
		
		assertEquals(1, sharedDir.getLength());
		sharedDir.deleteDirectory(newDir, true);
		sharedDir.refresh();
		assertEquals(0, sharedDir.getLength());
		}
		
		// Delete With String Name
		{
		File newDir = sharedDir.createDirectory("testMe");
		sharedDir.refresh();
		
		assertEquals(1, sharedDir.getLength());
		sharedDir.deleteDirectory("testMe", true);
		sharedDir.refresh();
		assertEquals(0, sharedDir.getLength());
		}
	}
	
	@Test
	public void copyMethodsWork() throws Exception {
		File sharedDir = FileSystem.getInstance().mountSystemDirectory(FileSystem.SHARED_SYSTEM_DIRECTORY);
		
		File newDir1 = sharedDir.createDirectory("testMe");
		File newDir2 = sharedDir.createDirectory("testMe2");
		
		File newFile = newDir1.resolve("TestFile");
		FileStream fileStream = newFile.open(FileMode.WRITE);
		fileStream.write("Hello");
		fileStream.close();
		
		newDir1.refresh();
		assertEquals(1,newDir1.getLength());
		
		newFile.copyTo(newDir2, false);

		newDir1.refresh();
		assertEquals(1,newDir1.getLength());

		
		newDir2.refresh();
		assertEquals(1, newDir2.getLength());
		assertEquals("TestFile", newDir2.getContents()[0].getName());
		
		// Cleanup
		newDir1.deleteDirectory(newDir1, true);
		newDir2.deleteDirectory(newDir2, true);
		
		sharedDir.refresh();
		assertEquals(0, sharedDir.getLength());
	}
	
	@Test
	public void moveMethodsWork() throws Exception {
		File sharedDir = FileSystem.getInstance().mountSystemDirectory(FileSystem.SHARED_SYSTEM_DIRECTORY);
		
		File newDir1 = sharedDir.createDirectory("testMe");
		File newDir2 = sharedDir.createDirectory("testMe2");
		
		File newFile = newDir1.resolve("TestFile");
		FileStream fileStream = newFile.open(FileMode.WRITE);
		fileStream.write("Hello");
		fileStream.close();
		
		newDir1.refresh();
		assertEquals(1,newDir1.getLength());
		
		newFile.moveTo(newDir2, false);

		newDir1.refresh();
		assertEquals(0,newDir1.getLength());

		
		newDir2.refresh();
		assertEquals(1, newDir2.getLength());
		assertEquals("TestFile", newDir2.getContents()[0].getName());
		
		// Cleanup
		newDir1.deleteDirectory(newDir1, true);
		newDir2.deleteDirectory(newDir2, true);
		
		sharedDir.refresh();
		assertEquals(0, sharedDir.getLength());
	}
	
	@Test
	public void copyMethodsWithCallbackWork() {
		File sharedDir = FileSystem.getInstance().mountSystemDirectory(FileSystem.SHARED_SYSTEM_DIRECTORY);
		
		File newDir1 = sharedDir.createDirectory("testMe");
		File newDir2 = sharedDir.createDirectory("testMe2");
		
		File newFile = newDir1.resolve("TestFile");
		FileStream fileStream = newFile.open(FileMode.WRITE);
		fileStream.write("Hello");
		fileStream.close();
		
		newDir1.refresh();
		assertEquals(1,newDir1.getLength());

		delayTestFinish(5000);
		newFile.copyTo(newDir2, true, new File.CompletedHandler() {
			public void onComplete() {
				finishTest();
			}
		});
	}
}
