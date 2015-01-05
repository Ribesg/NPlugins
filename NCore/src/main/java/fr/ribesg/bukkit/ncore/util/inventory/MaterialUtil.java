/***************************************************************************
 * Project file:    NPlugins - NCore - MaterialUtil.java                   *
 * Full Class name: fr.ribesg.bukkit.ncore.util.inventory.MaterialUtil     *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.util.inventory;

import java.lang.reflect.Field;

import org.bukkit.Material;

public class MaterialUtil {

    /**
     * Gets a Material from a String, if able to recognize anything in the
     * String. For now, only checks for ID and Material enum value.
     * Note: For now, there is no real gain of this over using
     * {@link Material#matchMaterial(String)}.
     *
     * @param idString the String representing a Material
     *
     * @return the associated Material or null if not found
     *
     * @throws InventoryUtilException if something goes wrong
     */
    public static Material getMaterial(final String idString) throws InventoryUtilException {
        try {
            final int id = Integer.parseInt(idString);
            for (final Material m : Material.values()) {
                if (m.getId() == id && !isMaterialDeprecated(m)) {
                    return m;
                }
            }
            return null;
        } catch (final NumberFormatException e) {
            final String filtered = idString.toUpperCase().replaceAll("\\s+", "_").replaceAll("\\W", "");
            return Material.getMaterial(filtered);
        }
    }

    /**
     * Checks if a Material is deprecated.
     *
     * @param material the Material to check
     *
     * @return true if deprecated, false otherwise
     *
     * @throws InventoryUtilException if something goes wrong
     */
    public static boolean isMaterialDeprecated(final Material material) throws InventoryUtilException {
        try {
            final Field f = Material.class.getField(material.name());
            return f.isAnnotationPresent(Deprecated.class);
        } catch (final NoSuchFieldException e) {
            throw new InventoryUtilException("Material not found: " + material.name(), e);
        }
    }
}
