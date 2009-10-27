package org.gwtunite.testing.client.tests;

import org.gwtunite.client.commons.FileUtils;
import org.gwtunite.testing.client.framework.Test;
import org.gwtunite.testing.client.framework.TestCase;

public class FileUtilsTests extends TestCase {

	@Test
	public void generalTests() throws Exception {
		assertEquals("5 bytes",FileUtils.byteCountToDisplaySize(5));
		assertEquals("5 KB",FileUtils.byteCountToDisplaySize(5120));
		assertEquals("5 MB",FileUtils.byteCountToDisplaySize(5242880));
		assertEquals("5 GB",FileUtils.byteCountToDisplaySize(5368709120l));
	}
}
