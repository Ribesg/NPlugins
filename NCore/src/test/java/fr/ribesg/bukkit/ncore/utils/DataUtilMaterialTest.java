/***************************************************************************
 * Project file:    NPlugins - NCore - DataUtilMaterialTest.java           *
 * Full Class name: fr.ribesg.bukkit.ncore.utils.DataUtilMaterialTest      *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.utils;
import org.bukkit.Material;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Collection;

/** @author Ribesg */
@RunWith(Parameterized.class)
public class DataUtilMaterialTest {

	private final Material awaitedMaterial;
	private final String   idString;

	public DataUtilMaterialTest(final Material awaitedMaterial, final String idString) {
		this.awaitedMaterial = awaitedMaterial;
		this.idString = idString;
	}

	@Test
	public void testGetMaterial() {
		final String awaitedMaterialName = awaitedMaterial.name();

		final Material foundMaterial = DataUtil.getMaterial(idString);
		final String foundMaterialName = foundMaterial == null ? "null" : foundMaterial.name();
		Assert.assertEquals("Expected Material '" + awaitedMaterialName + "' but found '" + foundMaterialName + "'",
		                    awaitedMaterialName,
		                    foundMaterialName);
	}

	@Parameters
	public static Collection<Object[]> data() {
		final Collection<Object[]> data = new ArrayList<>();
		for (final Material m : Material.values()) {
			if (!DataUtil.isMaterialDeprecated(m)) {
				data.add(new Object[] {
						m,
						m.name()
				});
				data.add(new Object[] {
						m,
						Integer.toString(m.getId())
				});
			}
		}
		return data;
	}
}
