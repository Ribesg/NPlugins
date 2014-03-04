/***************************************************************************
 * Project file:    NPlugins - NCuboid - FlagsFlagAttributesTest.java      *
 * Full Class name: fr.ribesg.bukkit.ncuboid.FlagsFlagAttributesTest       *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid;

import fr.ribesg.bukkit.ncore.common.AbstractReflectionTest;
import fr.ribesg.bukkit.ncuboid.beans.Flag;
import fr.ribesg.bukkit.ncuboid.beans.FlagAtt;
import fr.ribesg.bukkit.ncuboid.beans.Flags;
import org.junit.Assert;
import org.junit.Test;

import java.util.EnumMap;
import java.util.Map;

public class FlagsFlagAttributesTest extends AbstractReflectionTest {

	@Test
	public void checkPermForEachFlag() {
		// Initialize maps by first call
		executeStaticMethod(Perms.class, "getFlagPermission", new Class[] {Flag.class}, new Object[] {null});
		executeStaticMethod(Perms.class, "getFlagAttributePermission", new Class[] {FlagAtt.class}, new Object[] {null});

		// Get maps
		final Map<Flag, String> flagPermissions = (Map<Flag, String>) getStaticFieldValue(Perms.class, "flagPermissions");
		final Map<FlagAtt, String> flagAttributesPermissions = (Map<FlagAtt, String>) getStaticFieldValue(Perms.class, "flagAttributesPermissions");

		// Check content. We only need to check the size as those are Maps, so there's no duplicated key. If
		// the size is ok then everything is here.
		Assert.assertEquals("Missing Flag somewhere!", Flag.values().length, flagPermissions.size());
		Assert.assertEquals("Missing FlagAtt somewhere!", FlagAtt.values().length, flagAttributesPermissions.size());
	}

	@Test
	public void checkDefaultFlagsMap() {
		// Get the map
		final EnumMap<Flag, String> defaultFlagMap = (EnumMap<Flag, String>) executeStaticMethod(Flags.class, "getDefaultFlagMap", null);

		// Check content. We only need to check the size as it's an EnumMap, so there's no duplicated key. If
		// the size is ok then everything is here.
		Assert.assertEquals("Missing Flag somewhere!", Flag.values().length, defaultFlagMap.size());
	}
}
