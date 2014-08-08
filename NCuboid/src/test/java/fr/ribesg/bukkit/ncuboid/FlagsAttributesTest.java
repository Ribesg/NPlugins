/***************************************************************************
 * Project file:    NPlugins - NCuboid - FlagsAttributesTest.java          *
 * Full Class name: fr.ribesg.bukkit.ncuboid.FlagsAttributesTest           *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid;

import fr.ribesg.bukkit.ncore.common.AbstractReflectionTest;
import fr.ribesg.bukkit.ncuboid.beans.Attribute;
import fr.ribesg.bukkit.ncuboid.beans.Flag;
import fr.ribesg.bukkit.ncuboid.beans.Flags;

import java.util.EnumMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class FlagsAttributesTest extends AbstractReflectionTest {

	@Test
	@SuppressWarnings("unchecked")
	public void checkPermForEachFlag() {
		// Initialize maps by first call
		this.executeStaticMethod(Perms.class, "getFlagPermission", new Class[]{Flag.class}, new Object[]{null});
		this.executeStaticMethod(Perms.class, "getAttributePermission", new Class[]{Attribute.class}, new Object[]{null});

		// Get maps
		final Map<Flag, String> flagPermissions = (Map<Flag, String>)this.getStaticFieldValue(Perms.class, "flagPermissions");
		final Map<Attribute, String> flagAttributesPermissions = (Map<Attribute, String>)this.getStaticFieldValue(Perms.class, "attributesPermissions");

		// Check content. We only need to check the size as those are Maps, so there's no duplicated key. If
		// the size is ok then everything is here.
		Assert.assertEquals("Missing Flag somewhere!", Flag.values().length, flagPermissions.size());
		Assert.assertEquals("Missing Attribute somewhere!", Attribute.values().length, flagAttributesPermissions.size());
	}

	@Test
	@SuppressWarnings("unchecked")
	public void checkDefaultFlagsMap() {
		// Get the map
		final EnumMap<Flag, String> defaultFlagMap = (EnumMap<Flag, String>)this.executeStaticMethod(Flags.class, "getDefaultFlagMap", null);

		// Check content. We only need to check the size as it's an EnumMap, so there's no duplicated key. If
		// the size is ok then everything is here.
		Assert.assertEquals("Missing Flag somewhere!", Flag.values().length, defaultFlagMap.size());
	}
}
