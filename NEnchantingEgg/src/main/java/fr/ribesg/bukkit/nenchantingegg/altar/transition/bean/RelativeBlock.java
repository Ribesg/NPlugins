/***************************************************************************
 * Project file:    NPlugins - NEnchantingEgg - RelativeBlock.java         *
 * Full Class name: fr.ribesg.bukkit.nenchantingegg.altar.transition.bean.RelativeBlock
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nenchantingegg.altar.transition.bean;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class RelativeBlock extends RelativeLocation {

    private final Material blockMaterial;
    private final byte     blockData;

    public RelativeBlock(final double x, final double y, final double z, final Material blockMaterial) {
        this(x, y, z, blockMaterial, (byte)0);
    }

    public RelativeBlock(final double x, final double y, final double z, final Material blockMaterial, final byte blockData) {
        super(x, y, z);
        this.blockMaterial = blockMaterial;
        this.blockData = blockData;
    }

    public boolean needAdditionalData() {
        return false;
    }

    public void setAdditionalData(final Block theBlock) {
        throw new UnsupportedOperationException();
    }

    public byte getBlockData() {
        return this.blockData;
    }

    public Material getBlockMaterial() {
        return this.blockMaterial;
    }
}
