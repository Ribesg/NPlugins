/***************************************************************************
 * Project file:    NPlugins - NEnchantingEgg - BlockStep.java             *
 * Full Class name: fr.ribesg.bukkit.nenchantingegg.altar.transition.step.BlockStep
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nenchantingegg.altar.transition.step;

import fr.ribesg.bukkit.nenchantingegg.altar.Altar;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.bean.RelativeBlock;

import org.bukkit.block.Block;

public class BlockStep extends Step {

    private final RelativeBlock block;

    public BlockStep(final int delay, final RelativeBlock block) {
        super(delay);
        this.block = block;
    }

    @Override
    public void doStep(final Altar altar) {
        final Block b = altar.getCenterLocation().toBukkitLocation().add(this.block.getRelativeLocation()).getBlock();
        b.setType(this.block.getBlockMaterial());
        b.setData(this.block.getBlockData());
        if (this.block.needAdditionalData()) {
            this.block.setAdditionalData(b);
        }
    }
}
