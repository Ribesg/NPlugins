/***************************************************************************
 * Project file:    NPlugins - NEnchantingEgg - AltarState.java            *
 * Full Class name: fr.ribesg.bukkit.nenchantingegg.altar.AltarState       *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nenchantingegg.altar;

import fr.ribesg.bukkit.nenchantingegg.altar.transition.bean.BlockData;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.bean.RelativeBlock;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.bean.RelativeSkullBlock;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.BlockFace;

/**
 * Represents the state of an Altar.
 * An Altar can only go to the "Next step", maybe this would need some
 * emergency thing, for example if the Player disconnect.
 * The center of the Altar is the initial place of the center Quartz Pillar.
 *
 * @author Ribesg
 */
public enum AltarState {

    /**
     * The Altar will be in this state if:
     * - It has just been constructed, detected by placing the With Skull
     * - It has just been destroyed
     */
    INVALID,

    /**
     * The Altar will be in this state if:
     * - It has just been validated
     * - It was in ACTIVE or LOCKED state and time passed from Night to Day (23999 -> 0)
     */
    INACTIVE,

    /**
     * The Altar will be in this state if:
     * - It was in INACTIVE state and time passed from Day to Night (11999 -> 12000)
     */
    ACTIVE,

    /**
     * The Altar will be in this state if:
     * - It was in ACTIVE state and a DragonEgg was placed at the correct position
     */
    EGG_PROVIDED,

    /**
     * The Altar will be in this state if:
     * - It was in EGG_PROVIDED state and an enchanted item was thrown in the Portal blocks
     * Some seconds after the item was provided, an Item will be dropped on the center pillar.
     * Note that the Altar will be in IN_TRANSITION state until this item is dropped
     */
    ITEM_PROVIDED,

    /**
     * The Altar will be in this state if:
     * - It was in ITEM_PROVIDED state and the Player picked the item up.
     */
    LOCKED,

    /**
     * The Altar will be in this state if:
     * - It is no longer in the state in which it was before and
     * - It is not yet in the state in which it will be.
     */
    IN_TRANSITION;

    private static Set<RelativeBlock> inactiveStateBlocks;

    public static Set<RelativeBlock> getInactiveStateBlocks() {
        if (inactiveStateBlocks == null) {
            inactiveStateBlocks = new HashSet<>();

            // Y = 0
            //
            // -4-3-2-1 0 1 2 3 4 Z/X
            //
            //      C Z Z Z C      -4
            //    C C O O O C C    -3
            //  C C O O O O O C C  -2
            //  X O O O O O O O X  -1
            //  X O O O V O O O X   0
            //  X O O O O O O O X   1
            //  C C O O E O O C C   2
            //    C C O O O C C     3
            //      C Z Z Z C       4
            //
            // X -> X-Axis Quartz Pillar (North-South)
            // Z -> Z-Axis Quartz Pillar (East-West)
            // V -> Vertical Quartz Pillar
            // C -> Chiseled Quartz Block
            // O -> Obsidian
            // E -> Emerald Block

            // The center Vertical Quartz Pillar
            inactiveStateBlocks.add(new RelativeBlock(0, 0, 0, Material.QUARTZ_BLOCK, BlockData.QUARTZ_PILLAR_VERTICAL));

            // The obsidian disk
            for (int z = -1; z <= 1; z++) {
                // Lznes -3 & 3
                inactiveStateBlocks.add(new RelativeBlock(-3, 0, z, Material.OBSIDIAN));
                inactiveStateBlocks.add(new RelativeBlock(3, 0, z, Material.OBSIDIAN));
            }
            for (int z = -2; z <= 2; z++) {
                // Lznes -2 & 2
                inactiveStateBlocks.add(new RelativeBlock(-2, 0, z, Material.OBSIDIAN));
                inactiveStateBlocks.add(new RelativeBlock(2, 0, z, z == 0 ? Material.EMERALD_BLOCK : Material.OBSIDIAN));
            }
            for (int z = -3; z <= 3; z++) {
                // Lznes -1 & 1
                inactiveStateBlocks.add(new RelativeBlock(-1, 0, z, Material.OBSIDIAN));
                inactiveStateBlocks.add(new RelativeBlock(1, 0, z, Material.OBSIDIAN));
            }
            for (int i = -1; i <= 1; i += 2) {
                // Line 0
                inactiveStateBlocks.add(new RelativeBlock(0, 0, i * 1, Material.OBSIDIAN));
                inactiveStateBlocks.add(new RelativeBlock(0, 0, i * 2, Material.OBSIDIAN));
                inactiveStateBlocks.add(new RelativeBlock(0, 0, i * 3, Material.OBSIDIAN));
            }

            // The Chiseled Quartz Blocks
            for (int i = -1; i <= 1; i += 2) {
                for (int j = -1; j <= 1; j += 2) {
                    inactiveStateBlocks.add(new RelativeBlock(2 * i, 0, 3 * j, Material.QUARTZ_BLOCK, BlockData.QUARTZ_CHISELED));
                    inactiveStateBlocks.add(new RelativeBlock(2 * i, 0, 4 * j, Material.QUARTZ_BLOCK, BlockData.QUARTZ_CHISELED));
                    inactiveStateBlocks.add(new RelativeBlock(3 * i, 0, 2 * j, Material.QUARTZ_BLOCK, BlockData.QUARTZ_CHISELED));
                    inactiveStateBlocks.add(new RelativeBlock(3 * i, 0, 3 * j, Material.QUARTZ_BLOCK, BlockData.QUARTZ_CHISELED));
                    inactiveStateBlocks.add(new RelativeBlock(4 * i, 0, 2 * j, Material.QUARTZ_BLOCK, BlockData.QUARTZ_CHISELED));
                }
            }

            // The other Quartz Pillars
            for (int x = -1; x <= 1; x++) {
                inactiveStateBlocks.add(new RelativeBlock(x, 0, -4, Material.QUARTZ_BLOCK, BlockData.QUARTZ_PILLAR_Z_AXIS));
                inactiveStateBlocks.add(new RelativeBlock(x, 0, 4, Material.QUARTZ_BLOCK, BlockData.QUARTZ_PILLAR_Z_AXIS));
            }
            for (int z = -1; z <= 1; z++) {
                inactiveStateBlocks.add(new RelativeBlock(-4, 0, z, Material.QUARTZ_BLOCK, BlockData.QUARTZ_PILLAR_X_AXIS));
                inactiveStateBlocks.add(new RelativeBlock(4, 0, z, Material.QUARTZ_BLOCK, BlockData.QUARTZ_PILLAR_X_AXIS));
            }

            // ##########################################

            // Y = 1
            //
            // -3-2-1 0 1 2 3 Z/X
            //
            //        O        -3
            //    O   O   O    -2
            //                 -1
            //  O           O   0
            //                  1
            //    O       O     2
            //
            // O -> Obsidian

            inactiveStateBlocks.add(new RelativeBlock(0, 1, 0, Material.AIR, (byte)0));

            inactiveStateBlocks.add(new RelativeBlock(-3, 1, 0, Material.OBSIDIAN));

            inactiveStateBlocks.add(new RelativeBlock(-2, 1, -2, Material.OBSIDIAN));
            inactiveStateBlocks.add(new RelativeBlock(-2, 1, 0, Material.OBSIDIAN));
            inactiveStateBlocks.add(new RelativeBlock(-2, 1, 2, Material.OBSIDIAN));

            inactiveStateBlocks.add(new RelativeBlock(0, 1, -3, Material.OBSIDIAN));
            inactiveStateBlocks.add(new RelativeBlock(0, 1, 3, Material.OBSIDIAN));

            inactiveStateBlocks.add(new RelativeBlock(2, 1, -2, Material.OBSIDIAN));
            inactiveStateBlocks.add(new RelativeBlock(2, 1, 2, Material.OBSIDIAN));

            // ##########################################

            // Y = 2
            //
            // -3-2-1 0 1 2 3 Z/X
            //
            //        O        -3
            //    O   S   O    -2
            //                 -1
            //  O           O   0
            //
            // O -> Obsidian
            // S -> Wither Skull

            inactiveStateBlocks.add(new RelativeBlock(-3, 2, 0, Material.OBSIDIAN));

            inactiveStateBlocks.add(new RelativeBlock(-2, 2, -2, Material.OBSIDIAN));
            inactiveStateBlocks.add(new RelativeSkullBlock(-2, 2, 0, SkullType.WITHER, BlockFace.EAST));
            inactiveStateBlocks.add(new RelativeBlock(-2, 2, 2, Material.OBSIDIAN));

            inactiveStateBlocks.add(new RelativeBlock(0, 2, -3, Material.OBSIDIAN));
            inactiveStateBlocks.add(new RelativeBlock(0, 2, 3, Material.OBSIDIAN));

            // ##########################################

            // Y = 3
            //
            // -2-1 0 1 2 Z/X
            //
            //      O      -3
            //  O       O  -2
            //
            // O -> Obsidian

            inactiveStateBlocks.add(new RelativeBlock(-3, 3, 0, Material.OBSIDIAN));

            inactiveStateBlocks.add(new RelativeBlock(-2, 3, -2, Material.OBSIDIAN));
            inactiveStateBlocks.add(new RelativeBlock(-2, 3, 2, Material.OBSIDIAN));

            // ##########################################

            // Y = 4
            //
            // 0 Z/X
            //
            // O  -3
            //
            // O -> Obsidian

            inactiveStateBlocks.add(new RelativeBlock(-3, 4, 0, Material.OBSIDIAN));
        }
        return inactiveStateBlocks;
    }
}
