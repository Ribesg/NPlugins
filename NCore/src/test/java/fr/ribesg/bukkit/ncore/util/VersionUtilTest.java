/***************************************************************************
 * Project file:    NPlugins - NCore - VersionUtilTest.java                *
 * Full Class name: fr.ribesg.bukkit.ncore.util.VersionUtilTest            *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.util;
import org.junit.Assert;
import org.junit.Test;

public class VersionUtilTest {

	final String snapshotString1 = "v0.4.2-SNAPSHOT";
	final String snapshotString2 = "v0.4.2-SNAPSHOT (FooBar)";
	final String releaseString1  = "v0.4.2";
	final String releaseString2  = "v0.4.2 (FooBar)";

	@Test
	public void testVersionUtils() {
		Assert.assertEquals("Checking if " + snapshotString1 + " is a Snapshot version", true, VersionUtil.isSnapshot(snapshotString1));
		Assert.assertEquals("Checking if " + snapshotString2 + " is a Snapshot version", true, VersionUtil.isSnapshot(snapshotString2));
		Assert.assertEquals("Checking if " + releaseString1 + " is a Snapshot version", false, VersionUtil.isSnapshot(releaseString1));
		Assert.assertEquals("Checking if " + releaseString2 + " is a Snapshot version", false, VersionUtil.isSnapshot(releaseString2));

		Assert.assertEquals("Checking if " + snapshotString1 + " is a Release version", false, VersionUtil.isRelease(snapshotString1));
		Assert.assertEquals("Checking if " + snapshotString2 + " is a Release version", false, VersionUtil.isRelease(snapshotString2));
		Assert.assertEquals("Checking if " + releaseString1 + " is a Release version", true, VersionUtil.isRelease(releaseString1));
		Assert.assertEquals("Checking if " + releaseString2 + " is a Release version", true, VersionUtil.isRelease(releaseString2));

		Assert.assertEquals("v0.4.2", VersionUtil.getVersion(snapshotString1));
		Assert.assertEquals("v0.4.2", VersionUtil.getVersion(snapshotString2));
		Assert.assertEquals("v0.4.2", VersionUtil.getVersion(releaseString1));
		Assert.assertEquals("v0.4.2", VersionUtil.getVersion(releaseString2));
	}

}
