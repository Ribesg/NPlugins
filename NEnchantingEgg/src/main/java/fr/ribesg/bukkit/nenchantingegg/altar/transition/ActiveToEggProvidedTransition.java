/***************************************************************************
 * Project file:    NPlugins - NEnchantingEgg - ActiveToEggProvidedTransition.java
 * Full Class name: fr.ribesg.bukkit.nenchantingegg.altar.transition.ActiveToEggProvidedTransition
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
import fr.ribesg.bukkit.nenchantingegg.altar.transition.step.LightningStep;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.step.Step;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.BlockFace;

import java.util.HashSet;
import java.util.Set;

// TODO
public class ActiveToEggProvidedTransition extends Transition {

	public ActiveToEggProvidedTransition(final NEnchantingEgg plugin) {
		super(plugin);
	}

	@Override
	protected void setFromToStates() {
		fromState = AltarState.ACTIVE;
		toState = AltarState.EGG_PROVIDED;
	}

	@Override
	protected Set<Step> createSteps() {

		final Set<Step> steps = new HashSet<>(); // Result

		final int t = 42; // Time between block changes

		// ##########################################

		// Central portal frame block

		steps.add(new BlockStep(t / 4, new RelativeBlock(0, 0, 0, Material.ENDER_PORTAL_FRAME)));

		// ##########################################

		// Y = 1
		//
		// -3-2-1 0 1 2 3 Z/X
		//
		//    Q       Q     2
		//
		// Q -> Quartz Pillar (vertical)

		steps.add(new BlockStep(t, new RelativeBlock(2, 0, -2, Material.QUARTZ_BLOCK, BlockData.QUARTZ_PILLAR_VERTICAL)));
		steps.add(new BlockStep(t, new RelativeBlock(2, 0, 2, Material.QUARTZ_BLOCK, BlockData.QUARTZ_PILLAR_VERTICAL)));
		steps.add(new BlockStep(t, new RelativeBlock(2, 1, -2, Material.QUARTZ_BLOCK, BlockData.QUARTZ_PILLAR_VERTICAL)));
		steps.add(new BlockStep(t, new RelativeBlock(2, 1, 2, Material.QUARTZ_BLOCK, BlockData.QUARTZ_PILLAR_VERTICAL)));
		steps.add(new LightningStep(t, new RelativeLocation(2, 1, -2)));
		steps.add(new LightningStep(t, new RelativeLocation(2, 1, 2)));

		// ##########################################

		// Y = 0-2
		//
		// -3-2-1 0 1 2 3 Z/X
		//
		//  Q           Q   0
		//
		// Q -> Quartz Pillar (vertical)

		for (int i = 0; i <= 2; i++) {
			steps.add(new BlockStep(2 * t, new RelativeBlock(0, i, -3, Material.QUARTZ_BLOCK, BlockData.QUARTZ_PILLAR_VERTICAL)));
			steps.add(new BlockStep(2 * t, new RelativeBlock(0, i, 3, Material.QUARTZ_BLOCK, BlockData.QUARTZ_PILLAR_VERTICAL)));
		}
		steps.add(new LightningStep(2 * t, new RelativeLocation(0, 2, -3)));
		steps.add(new LightningStep(2 * t, new RelativeLocation(0, 2, 3)));

		// ##########################################

		// Y = 0-3
		//
		// -2-1 0 1 2 Z/X
		//
		//  Q       Q  -2
		//
		// Q -> Quartz Pillar (vertical)

		for (int i = 0; i <= 3; i++) {
			steps.add(new BlockStep(3 * t, new RelativeBlock(-2, i, -2, Material.QUARTZ_BLOCK, BlockData.QUARTZ_PILLAR_VERTICAL)));
			steps.add(new BlockStep(3 * t, new RelativeBlock(-2, i, 2, Material.QUARTZ_BLOCK, BlockData.QUARTZ_PILLAR_VERTICAL)));
		}
		steps.add(new LightningStep(3 * t, new RelativeLocation(-2, 3, -2)));
		steps.add(new LightningStep(3 * t, new RelativeLocation(-2, 3, 2)));

		// ##########################################

		// Y = 0-4
		//
		// 0 Z/X
		//
		// Q  -3
		//
		// Q -> Quartz Pillar (vertical)

		for (int i = 0; i <= 4; i++) {
			steps.add(new BlockStep(4 * t, new RelativeBlock(-3, i, 0, Material.QUARTZ_BLOCK, BlockData.QUARTZ_PILLAR_VERTICAL)));
		}
		steps.add(new LightningStep(4 * t, new RelativeLocation(-3, 4, 0)));

		// ##########################################

		// Y = 2
		// Skeleton head
		//
		// 0 Z/X
		//
		// H  -2
		//
		// H -> Skeleton Head

		steps.add(new BlockStep(5 * t, new RelativeBlock(-2, 0, 0, Material.QUARTZ_BLOCK, BlockData.QUARTZ_PILLAR_VERTICAL)));
		steps.add(new BlockStep(5 * t, new RelativeBlock(-2, 1, 0, Material.QUARTZ_BLOCK, BlockData.QUARTZ_PILLAR_VERTICAL)));
		steps.add(new BlockStep(5 * t, new RelativeSkullBlock(-2, 2, 0, SkullType.SKELETON, BlockFace.EAST)));
		steps.add(new LightningStep(5 * t, new RelativeLocation(-2, 2, 0)));

		// ##########################################

		// Y = 0
		// Portal blocks
		//
		// -2-1 0 1 2 Z/X
		//
		//    P P P    -2
		//  P P P P P  -1
		//  P P   P P   0
		//  P P P P P   1
		//    P   P     2
		//
		// P -> Portal block

		steps.add(new BlockStep(5 * t, new RelativeBlock(-2, 0, -1, Material.ENDER_PORTAL)));
		steps.add(new BlockStep(5 * t, new RelativeBlock(-2, 0, 0, Material.ENDER_PORTAL)));
		steps.add(new BlockStep(5 * t, new RelativeBlock(-2, 0, 1, Material.ENDER_PORTAL)));

		for (int x = -1; x <= 1; x++) {
			for (int z = -2; z <= 2; z++) {
				if (x != 0 || z != 0) {
					steps.add(new BlockStep(5 * t, new RelativeBlock(x, 0, z, Material.ENDER_PORTAL)));
				}
			}
		}

		steps.add(new BlockStep(5 * t, new RelativeBlock(2, 0, -1, Material.ENDER_PORTAL)));
		steps.add(new BlockStep(5 * t, new RelativeBlock(2, 0, 1, Material.ENDER_PORTAL)));

		return steps;
	}
}
