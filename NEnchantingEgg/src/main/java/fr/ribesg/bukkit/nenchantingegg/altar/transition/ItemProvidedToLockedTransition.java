/***************************************************************************
 * Project file:    NPlugins - NEnchantingEgg - ItemProvidedToLockedTransition.java
 * Full Class name: fr.ribesg.bukkit.nenchantingegg.altar.transition.ItemProvidedToLockedTransition
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nenchantingegg.altar.transition;

import fr.ribesg.bukkit.nenchantingegg.NEnchantingEgg;
import fr.ribesg.bukkit.nenchantingegg.altar.AltarState;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.bean.BlockData;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.bean.RelativeBlock;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.bean.RelativeLocation;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.bean.RelativeSkullBlock;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.step.BlockStep;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.step.ExplosionStep;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.step.FallingBlockStep;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.step.Step;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.BlockFace;

import java.util.HashSet;
import java.util.Set;

// TODO
public class ItemProvidedToLockedTransition extends Transition {

	public ItemProvidedToLockedTransition(NEnchantingEgg plugin) {
		super(plugin);
	}

	@Override
	protected void setFromToStates() {
		fromState = AltarState.ITEM_PROVIDED;
		toState = AltarState.LOCKED;
	}

	@Override
	protected Set<Step> createSteps() {

		final Set<Step> steps = new HashSet<>(); // Result

		final int t = 30; // Time between block changes

		// ##########################################
		// Central falling pillar
		steps.add(new BlockStep(t - 5, new RelativeBlock(0, 0, 0, Material.AIR)));
		steps.add(new FallingBlockStep(t - 5, new RelativeBlock(0, 1, 0, Material.QUARTZ_BLOCK, BlockData.QUARTZ_PILLAR_VERTICAL), 0));

		// ##########################################
		// Obsidian blocks 1
		for (int i = -1; i <= 1; i += 2) {
			steps.add(new BlockStep(t, new RelativeBlock(i, 0, 0, Material.OBSIDIAN)));
			steps.add(new BlockStep(t, new RelativeBlock(0, 0, i, Material.OBSIDIAN)));
			// Quartz chiseled blocks for final result equals to Inactive state
			for (int j = -1; j <= 1; j += 2) {
				steps.add(new BlockStep(t, new RelativeBlock(3 * i, 0, 2 * j, Material.QUARTZ_BLOCK, BlockData.QUARTZ_CHISELED)));
				steps.add(new BlockStep(t, new RelativeBlock(2 * i, 0, 3 * j, Material.QUARTZ_BLOCK, BlockData.QUARTZ_CHISELED)));
			}
		}

		// ##########################################
		// Obsidian blocks 2
		for (int i = -1; i <= 1; i += 2) {
			if (i != 1) {
				steps.add(new BlockStep(2 * t, new RelativeBlock(2 * i, 0, 0, Material.OBSIDIAN)));
			}
			steps.add(new BlockStep(2 * t, new RelativeBlock(0, 0, 2 * i, Material.OBSIDIAN)));
			steps.add(new BlockStep(2 * t, new RelativeBlock(i, 0, i, Material.OBSIDIAN)));
			steps.add(new BlockStep(2 * t, new RelativeBlock(i, 0, -i, Material.OBSIDIAN)));
		}
		steps.add(new BlockStep(2 * t, new RelativeBlock(-2, 1, 0, Material.OBSIDIAN)));

		// ##########################################
		// Obsidian blocks 3
		for (int i = -1; i <= 1; i += 2) {
			steps.add(new BlockStep(3 * t, new RelativeBlock(3 * i, 0, 0, Material.OBSIDIAN)));
			steps.add(new BlockStep(3 * t, new RelativeBlock(0, 0, 3 * i, Material.OBSIDIAN)));
			steps.add(new BlockStep(3 * t, new RelativeBlock(0, 1, 3 * i, Material.OBSIDIAN)));
			for (int j = -1; j <= 1; j += 2) {
				steps.add(new BlockStep(3 * t, new RelativeBlock(2 * i, 0, j, Material.OBSIDIAN)));
				steps.add(new BlockStep(3 * t, new RelativeBlock(i, 0, 2 * j, Material.OBSIDIAN)));
			}
		}
		steps.add(new BlockStep(3 * t, new RelativeBlock(-3, 1, 0, Material.OBSIDIAN)));

		// ##########################################
		// Obsidian blocks 4
		for (int i = -1; i <= 1; i += 2) {
			steps.add(new BlockStep(4 * t, new RelativeBlock(-2, 1, 2 * i, Material.OBSIDIAN)));
			for (int j = -1; j <= 1; j += 2) {
				steps.add(new BlockStep(4 * t, new RelativeBlock(3 * i, 0, 1 * j, Material.OBSIDIAN)));
				steps.add(new BlockStep(4 * t, new RelativeBlock(2 * i, 0, 2 * j, Material.OBSIDIAN)));
				steps.add(new BlockStep(4 * t, new RelativeBlock(1 * i, 0, 3 * j, Material.OBSIDIAN)));
			}
		}

		// ##########################################
		// Obsidian blocks 5
		steps.add(new BlockStep(5 * t, new RelativeBlock(-2, 2, -2, Material.OBSIDIAN)));
		steps.add(new BlockStep(5 * t, new RelativeBlock(-2, 2, 2, Material.OBSIDIAN)));
		steps.add(new BlockStep(5 * t, new RelativeBlock(-3, 2, 0, Material.OBSIDIAN)));

		// ##########################################
		// Obsidian blocks 6
		steps.add(new BlockStep(6 * t, new RelativeBlock(-3, 3, 0, Material.OBSIDIAN)));

		// ##########################################
		steps.add(new BlockStep(7 * t, new RelativeSkullBlock(-2, 2, 0, SkullType.WITHER, BlockFace.EAST)));
		steps.add(new ExplosionStep(7 * t, new RelativeLocation(-2, 2, 0)));
		steps.add(new BlockStep(7 * t, new RelativeBlock(2, 1, -2, Material.OBSIDIAN)));
		steps.add(new BlockStep(7 * t, new RelativeBlock(2, 1, 2, Material.OBSIDIAN)));
		steps.add(new BlockStep(7 * t, new RelativeBlock(0, 2, -3, Material.OBSIDIAN)));
		steps.add(new BlockStep(7 * t, new RelativeBlock(0, 2, 3, Material.OBSIDIAN)));
		steps.add(new BlockStep(7 * t, new RelativeBlock(-2, 3, -2, Material.OBSIDIAN)));
		steps.add(new BlockStep(7 * t, new RelativeBlock(-2, 3, 2, Material.OBSIDIAN)));
		steps.add(new BlockStep(7 * t, new RelativeBlock(-3, 4, 0, Material.OBSIDIAN)));

		return steps;
	}
}
