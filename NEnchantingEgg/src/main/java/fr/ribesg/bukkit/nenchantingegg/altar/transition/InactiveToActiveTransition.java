/***************************************************************************
 * Project file:    NPlugins - NEnchantingEgg - InactiveToActiveTransition.java
 * Full Class name: fr.ribesg.bukkit.nenchantingegg.altar.transition.InactiveToActiveTransition
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nenchantingegg.altar.transition;

import fr.ribesg.bukkit.nenchantingegg.NEnchantingEgg;
import fr.ribesg.bukkit.nenchantingegg.altar.AltarState;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.bean.RelativeBlock;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.bean.RelativeEffect;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.bean.RelativeSound;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.step.BlockStep;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.step.EffectStep;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.step.SoundStep;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.step.Step;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;

import java.util.HashSet;
import java.util.Set;

public class InactiveToActiveTransition extends Transition {

	public InactiveToActiveTransition(NEnchantingEgg plugin) {
		super(plugin);
	}

	@Override
	protected void setFromToStates() {
		fromState = AltarState.INACTIVE;
		toState = AltarState.ACTIVE;
	}

	@Override
	protected Set<Step> createSteps() {

		final Set<Step> steps = new HashSet<>(); // Result

		final int t = 10; // Time between block changes

		final Effect effectSound = Effect.BLAZE_SHOOT;
		final Effect effectGraphic = Effect.ENDER_SIGNAL;

		steps.add(new SoundStep(0, new RelativeSound(-3, 4, 0, Sound.ENDERMAN_STARE, 1.5f, 0.75f)));

		// ##########################################

		// Y = 1
		//
		// -3-2-1 0 1 2 3 Z/X
		//
		//    G       G     2
		//
		// G -> Glowstone

		steps.add(new BlockStep(t, new RelativeBlock(2, 1, -2, Material.GLOWSTONE)));
		steps.add(new EffectStep(t, new RelativeEffect(2, 1, -2, effectSound)));
		steps.add(new EffectStep(t, new RelativeEffect(2, 1, -2, effectGraphic)));

		steps.add(new BlockStep(t, new RelativeBlock(2, 1, 2, Material.GLOWSTONE)));
		steps.add(new EffectStep(t, new RelativeEffect(2, 1, 2, effectSound)));
		steps.add(new EffectStep(t, new RelativeEffect(2, 1, 2, effectGraphic)));

		// ##########################################

		// Y = 2
		//
		// -3-2-1 0 1 2 3 Z/X
		//
		//  G           G   0
		//
		// G -> Glowstone

		steps.add(new BlockStep(2 * t, new RelativeBlock(0, 2, -3, Material.GLOWSTONE)));
		steps.add(new EffectStep(2 * t, new RelativeEffect(0, 2, -3, effectSound)));
		steps.add(new EffectStep(2 * t, new RelativeEffect(0, 2, -3, effectGraphic)));

		steps.add(new BlockStep(2 * t, new RelativeBlock(0, 2, 3, Material.GLOWSTONE)));
		steps.add(new EffectStep(2 * t, new RelativeEffect(0, 2, 3, effectSound)));
		steps.add(new EffectStep(2 * t, new RelativeEffect(0, 2, 3, effectGraphic)));

		// ##########################################

		// Y = 3
		//
		// -2-1 0 1 2 Z/X
		//
		//  G       G  -2
		//
		// G -> Glowstone

		steps.add(new BlockStep(3 * t, new RelativeBlock(-2, 3, -2, Material.GLOWSTONE)));
		steps.add(new EffectStep(3 * t, new RelativeEffect(-2, 3, -2, effectSound)));
		steps.add(new EffectStep(3 * t, new RelativeEffect(-2, 3, -2, effectGraphic)));

		steps.add(new BlockStep(3 * t, new RelativeBlock(-2, 3, 2, Material.GLOWSTONE)));
		steps.add(new EffectStep(3 * t, new RelativeEffect(-2, 3, 2, effectSound)));
		steps.add(new EffectStep(3 * t, new RelativeEffect(-2, 3, 2, effectGraphic)));

		// ##########################################

		// Y = 4
		//
		// 0 Z/X
		//
		// G  -3
		//
		// G -> Glowstone

		steps.add(new BlockStep(4 * t, new RelativeBlock(-3, 4, 0, Material.GLOWSTONE)));
		steps.add(new EffectStep(4 * t, new RelativeEffect(-3, 4, 0, effectSound)));
		steps.add(new EffectStep(4 * t, new RelativeEffect(-3, 4, 0, effectGraphic)));

		// ##########################################

		// Y = 0
		// Lava
		//
		// -4-3-2-1 0 1 2 3 4 Z/X
		//
		//      L L X L L      -4
		//    L L       L L    -3
		//  L L           L L  -2
		//  L               L  -1
		//  X               X   0
		//  L               L   1
		//  L L           L L   2
		//    L L       L L     3
		//      L L X L L       4
		//
		// X,L -> Lava

		// X
		steps.add(new BlockStep(6 * t, new RelativeBlock(-4, 0, 0, Material.LAVA)));
		steps.add(new BlockStep(6 * t, new RelativeBlock(0, 0, -4, Material.LAVA)));
		steps.add(new BlockStep(6 * t, new RelativeBlock(4, 0, 0, Material.LAVA)));
		steps.add(new BlockStep(6 * t, new RelativeBlock(0, 0, 4, Material.LAVA)));

		steps.add(new SoundStep(6 * t, new RelativeSound(-4, 0, 0, Sound.LAVA, 1f, 1f)));
		steps.add(new SoundStep(6 * t, new RelativeSound(0, 0, -4, Sound.LAVA, 1f, 1f)));
		steps.add(new SoundStep(6 * t, new RelativeSound(4, 0, 0, Sound.LAVA, 1f, 1f)));
		steps.add(new SoundStep(6 * t, new RelativeSound(0, 0, 4, Sound.LAVA, 1f, 1f)));

		// L
		for (int i = -1; i <= 1; i += 2) { // -1 ; 1
			for (int j = -1; j <= 1; j += 2) { // -1 ; 1
				steps.add(new BlockStep(6 * t, new RelativeBlock(i * 1, 0, j * 4, Material.LAVA)));
				steps.add(new BlockStep(6 * t, new RelativeBlock(i * 2, 0, j * 4, Material.LAVA)));
				steps.add(new BlockStep(6 * t, new RelativeBlock(i * 2, 0, j * 3, Material.LAVA)));
				steps.add(new BlockStep(6 * t, new RelativeBlock(i * 3, 0, j * 3, Material.LAVA)));
				steps.add(new BlockStep(6 * t, new RelativeBlock(i * 3, 0, j * 2, Material.LAVA)));
				steps.add(new BlockStep(6 * t, new RelativeBlock(i * 4, 0, j * 2, Material.LAVA)));
				steps.add(new BlockStep(6 * t, new RelativeBlock(i * 4, 0, j * 1, Material.LAVA)));
			}
		}

		return steps;
	}
}
