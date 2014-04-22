/***************************************************************************
 * Project file:    NPlugins - NCore - StringUtilTest.java                 *
 * Full Class name: fr.ribesg.bukkit.ncore.util.StringUtilTest             *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.util;
import org.junit.Assert;
import org.junit.Test;

public class StringUtilTest {

	@Test
	public void testSplitKeepEmpty() {
		Assert.assertEquals(1, StringUtil.splitKeepEmpty(";;;;", ".").length);
		Assert.assertEquals(5, StringUtil.splitKeepEmpty(";;;;", ";").length);
		Assert.assertEquals(5, StringUtil.splitKeepEmpty("blah;foo;;bar;test", ";").length);
		Assert.assertEquals(5, StringUtil.splitKeepEmpty("blah;;foo;;;;bar;;test", ";;").length);
		Assert.assertEquals(5, StringUtil.splitKeepEmpty(";;;;;;;;", ";;").length);
	}

	@Test
	public void testCount() {
		Assert.assertEquals(0, StringUtil.count("test", ";"));
		Assert.assertEquals(1, StringUtil.count(";", ";"));
		Assert.assertEquals(4, StringUtil.count(";;;;", ";"));
		Assert.assertEquals(4, StringUtil.count(";;test;test;", ";"));
		Assert.assertEquals(4, StringUtil.count(";;;;;;;;;", ";;"));
	}

}
