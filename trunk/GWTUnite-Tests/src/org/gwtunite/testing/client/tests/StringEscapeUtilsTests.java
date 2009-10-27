package org.gwtunite.testing.client.tests;

import org.gwtunite.client.commons.StringEscapeUtils;
import org.gwtunite.testing.client.framework.Test;
import org.gwtunite.testing.client.framework.TestCase;

public class StringEscapeUtilsTests extends TestCase {

	@Test
	public void generalTests() throws Exception {
		assertEquals("&lt;HTML&gt;",StringEscapeUtils.escapeHTML("<HTML>"));
		assertEquals("<HTML>",StringEscapeUtils.unescapeHTML("&lt;HTML&gt;"));
	}
}
