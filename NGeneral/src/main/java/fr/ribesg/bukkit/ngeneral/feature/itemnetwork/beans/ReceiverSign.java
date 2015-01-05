/***************************************************************************
 * Project file:    NPlugins - NGeneral - ReceiverSign.java                *
 * Full Class name: fr.ribesg.bukkit.ngeneral.feature.itemnetwork.beans.ReceiverSign
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral.feature.itemnetwork.beans;

import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.ncore.util.SignUtil;
import fr.ribesg.bukkit.ngeneral.NGeneral;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class ReceiverSign {

    private static final Pattern ACCEPTED_MATERIALS_REGEX = Pattern.compile("^\\*|\\d+(:\\d+)?(;\\d+(:\\d+)?)*$");

    private static Set<Material> chestMaterials;

    private static Set<Material> getChestMaterials() {
        if (chestMaterials == null) {
            chestMaterials = EnumSet.of(
                    Material.CHEST,
                    Material.TRAPPED_CHEST
            );
        }
        return chestMaterials;
    }

    private final NLocation         location;
    private final Set<Material>     acceptedMaterials;
    private final Set<MaterialData> acceptedMaterialDatas;
    private final boolean           acceptsAll;

    private       List<Block> chests;
    private final long        lastUpdateDate;

    /**
     * Builds a new ReceiverSign that accepts only some types of Materials.
     *
     * @param location      the Location of the ReceiverSign
     * @param acceptsString the Materials that this ReceiverSign accepts
     */
    public ReceiverSign(final NGeneral plugin, final NLocation location, final String acceptsString) {
        this.location = location;

        if (!ACCEPTED_MATERIALS_REGEX.matcher(acceptsString).matches()) {
            throw new IllegalArgumentException();
        }
        if ("*".equals(acceptsString)) {
            this.acceptsAll = true;
            this.acceptedMaterials = null;
            this.acceptedMaterialDatas = null;
        } else {
            this.acceptsAll = false;
            this.acceptedMaterials = EnumSet.noneOf(Material.class);
            this.acceptedMaterialDatas = new HashSet<>();
            for (final String s : acceptsString.split(";")) {
                if (s.contains(":")) {
                    this.acceptedMaterialDatas.add(new MaterialData(Integer.parseInt(s.split(":")[0]), (byte)Integer.parseInt(s.split(":")[1])));
                } else {
                    final Material material = Material.getMaterial(Integer.parseInt(s));
                    if (material != null) {
                        this.acceptedMaterials.add(material);
                    } else {
                        plugin.error("Unable to find Material enum value for id " + s);
                    }
                }
            }
        }

        this.chests = SignUtil.getBlocksForLocation(this.location.toBukkitLocation(), getChestMaterials());
        this.lastUpdateDate = System.currentTimeMillis();
    }

    public boolean accepts(final MaterialData matData) {
        return this.acceptsAll ||
               this.acceptedMaterials != null && this.acceptedMaterials.contains(matData.getItemType()) ||
               this.acceptedMaterialDatas != null && this.acceptedMaterialDatas.contains(matData);
    }

    public boolean accepts(final Material mat) {
        return this.acceptsAll || this.acceptedMaterials != null && this.acceptedMaterials.contains(mat);
    }

    public boolean acceptsAll() {
        return this.acceptsAll;
    }

    public ItemStack send(final ItemStack is) {
        if (System.currentTimeMillis() - this.lastUpdateDate > 1000L) {
            this.chests = SignUtil.getBlocksForLocation(this.location.toBukkitLocation(), getChestMaterials());
        }

        ItemStack[] result = {is};

        for (final Block b : this.chests) {
            final InventoryHolder holder = (InventoryHolder)b.getState();
            result = holder.getInventory().addItem(is).values().toArray(new ItemStack[1]);
            if (result[0] == null) {
                break;
            }
        }

        return result[0];
    }

    public NLocation getLocation() {
        return this.location;
    }

    public String getAcceptsString() {
        if (this.acceptsAll) {
            return "*";
        } else {
            final StringBuilder result = new StringBuilder();
            if (this.acceptedMaterials != null) {
                for (final Material m : this.acceptedMaterials) {
                    result.append(m.getId()).append(';');
                }
            }
            if (this.acceptedMaterialDatas != null) {
                for (final MaterialData md : this.acceptedMaterialDatas) {
                    result.append(md.getItemTypeId()).append(':').append(md.getData()).append(';');
                }
            }
            final String finalResult = result.toString();
            return finalResult.substring(0, finalResult.length() - 1);
        }
    }
}
