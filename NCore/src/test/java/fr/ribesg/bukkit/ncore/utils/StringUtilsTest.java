/***************************************************************************
 * Project file:    NPlugins - NCore - StringUtilsTest.java                *
 * Full Class name: fr.ribesg.bukkit.ncore.utils.StringUtilsTest           *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.utils;
import org.junit.Assert;
import org.junit.Test;

public class StringUtilsTest {

	@Test
	public void testSplitKeepEmpty() {
		Assert.assertEquals(1, StringUtils.splitKeepEmpty(";;;;", ".").length);
		Assert.assertEquals(5, StringUtils.splitKeepEmpty(";;;;", ";").length);
		Assert.assertEquals(5, StringUtils.splitKeepEmpty("blah;foo;;bar;test", ";").length);
		Assert.assertEquals(5, StringUtils.splitKeepEmpty("blah;;foo;;;;bar;;test", ";;").length);
		Assert.assertEquals(5, StringUtils.splitKeepEmpty(";;;;;;;;", ";;").length);
	}

	@Test
	public void testCount() {
		Assert.assertEquals(0, StringUtils.count("test", ";"));
		Assert.assertEquals(1, StringUtils.count(";", ";"));
		Assert.assertEquals(4, StringUtils.count(";;;;", ";"));
		Assert.assertEquals(4, StringUtils.count(";;test;test;", ";"));
		Assert.assertEquals(4, StringUtils.count(";;;;;;;;;", ";;"));
	}

}
