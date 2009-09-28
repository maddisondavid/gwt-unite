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
		mountPoints.refresh();
		
		assertEquals(0, mountPoints.getLength());
		
		fileSystem.mountSystemDirectory(FileSystem.APPLICATION_SYSTEM_DIRECTORY);
		assertEquals(1, mountPoints.getLength());
		
		fileSystem.removeMountPoint(FileSystem.APPLICATION_SYSTEM_DIRECTORY);
		assertEquals(0, mountPoints.getLength());
	}
}
