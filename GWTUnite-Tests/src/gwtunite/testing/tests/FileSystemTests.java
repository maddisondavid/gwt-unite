package gwtunite.testing.tests;

import gwtunite.testing.framework.Test;
import gwtunite.testing.framework.TestCase;
import opera.io.File;
import opera.io.FileSystem;

public class FileSystemTests extends TestCase {

	@Test
	public void removeMountPointWorks() throws Exception {
		FileSystem fileSystem = FileSystem.getInstance();
		File mountPoints = fileSystem.getMountPoints();
		
		assertEquals(0, mountPoints.listFiles().length);
		
		fileSystem.mountSharedFileSystem();
		assertEquals(1, mountPoints.listFiles().length);
		
		fileSystem.removeMountPoint("application");
		assertEquals(0, mountPoints.listFiles().length);
	}
}
