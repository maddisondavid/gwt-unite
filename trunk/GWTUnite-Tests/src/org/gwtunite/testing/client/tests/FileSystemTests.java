package org.gwtunite.testing.client.tests;

import org.gwtunite.client.commons.Logging;
import org.gwtunite.client.file.File;
import org.gwtunite.client.file.FileSystem;
import org.gwtunite.testing.client.framework.Test;
import org.gwtunite.testing.client.framework.TestCase;

public class FileSystemTests extends TestCase {

	@Test
	public void removeMountPointWorks() throws Exception {
		FileSystem fileSystem = FileSystem.getInstance();
		File mountPoints = fileSystem.getMountPoints();
		
		assertEquals(0, mountPoints.listFiles().length);
		
		fileSystem.mountSharedFileSystem();
		assertEquals(1, mountPoints.listFiles().length);

		fileSystem.removeMountPoint("shared");
		assertEquals(0, mountPoints.listFiles().length);
	}
}
