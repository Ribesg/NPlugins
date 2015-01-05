/***************************************************************************
 * Project file:    NPlugins - NCore - MaterialUtilTest.java               *
 * Full Class name: fr.ribesg.bukkit.ncore.util.MaterialUtilTest           *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.util;

import fr.ribesg.bukkit.ncore.util.inventory.InventoryUtilException;
import fr.ribesg.bukkit.ncore.util.inventory.MaterialUtil;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Material;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author Ribesg
 */
@RunWith(Parameterized.class)
public class MaterialUtilTest {

    private final Material awaitedMaterial;
    private final String   idString;

    public MaterialUtilTest(final Material awaitedMaterial, final String idString) {
        this.awaitedMaterial = awaitedMaterial;
        this.idString = idString;
    }

    @Test
    public void testGetMaterial() throws InventoryUtilException {
        final String awaitedMaterialName = this.awaitedMaterial.name();

        final Material foundMaterial = MaterialUtil.getMaterial(this.idString);
        final String foundMaterialName = foundMaterial == null ? "null" : foundMaterial.name();
        Assert.assertEquals("Expected Material '" + awaitedMaterialName + "' but found '" + foundMaterialName + '\'', awaitedMaterialName, foundMaterialName);
    }

    @Parameters
    public static Collection<Object[]> data() throws InventoryUtilException {
        final Collection<Object[]> data = new ArrayList<>();
        for (final Material m : Material.values()) {
            if (!MaterialUtil.isMaterialDeprecated(m)) {
                data.add(new Object[]{
                        m,
                        m.name()
                });
                data.add(new Object[]{
                        m,
                        Integer.toString(m.getId())
                });
            }
        }
        return data;
    }
}
