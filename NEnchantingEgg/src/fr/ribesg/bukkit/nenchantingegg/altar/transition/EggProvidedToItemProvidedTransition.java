package fr.ribesg.bukkit.nenchantingegg.altar.transition;

import fr.ribesg.bukkit.nenchantingegg.NEnchantingEgg;
import fr.ribesg.bukkit.nenchantingegg.altar.Altar;
import fr.ribesg.bukkit.nenchantingegg.altar.AltarState;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.bean.BlockData;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.bean.RelativeBlock;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.bean.RelativeEffect;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.bean.RelativeSound;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.step.BlockStep;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.step.EffectStep;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.step.FallingBlockStep;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.step.SoundStep;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.step.Step;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

// TODO
public class EggProvidedToItemProvidedTransition extends Transition {

	private static Map<Integer, Float> zeldaSound = null;

	private static Map<Integer, Float> getZeldaSound() {
		if (zeldaSound == null) {
			zeldaSound = new HashMap<Integer, Float>();
			zeldaSound.put(0, 1.06f); // High G     13
			zeldaSound.put(1, 1.00f); // High F#    12
			zeldaSound.put(2, 0.84f); // High D#    9
			zeldaSound.put(3, 0.60f); // Middle A   3
			zeldaSound.put(4, 0.56f); // Middle G#  2
			zeldaSound.put(5, 0.90f); // High E     10
			zeldaSound.put(6, 1.12f); // High G#    14
			zeldaSound.put(7, 1.42f); // High C     18
		}
		return zeldaSound;
	}

	public EggProvidedToItemProvidedTransition(NEnchantingEgg plugin) {
		super(plugin);
	}

	@Override
	protected void setFromToStates() {
		fromState = AltarState.EGG_PROVIDED;
		toState = AltarState.ITEM_PROVIDED;
	}

	@Override
	protected void afterTransition(final Altar altar) {
		altar.getBuilder().computeItem();
	}

	@Override
	protected Set<Step> createSteps() {

		final Set<Step> steps = new HashSet<Step>(); // Result

		final int t = 10; // Time between block changes

		final int spawnHeight = 0;

		// ##########################################
		// Egg
		steps.add(new BlockStep(0, new RelativeBlock(0, 1, 0, Material.AIR)));
		steps.add(new EffectStep(0, new RelativeEffect(0, 1, 0, Effect.MOBSPAWNER_FLAMES)));
		steps.add(new SoundStep(0, new RelativeSound(0, 1, 0, Sound.EXPLODE, 0.5f, 0.5f)));

		// ##########################################

		// Y = 0
		//
		// -4-3-2-1 0 1 2 3 4 Z/X
		//
		//      C X X X C      -4
		//    C Q C C C Q C    -3
		//  C Q Q Q Q Q Q Q C  -2
		//  Z C Q Z Q Z Q C Z  -1
		//  Z C Q Z V Z Q C Z   0
		//  Z C Q Z Q Z Q C Z   1
		//  C Q Q Q   Q Q Q C   2
		//    C Q C C C Q C     3
		//      C X X X C       4
		//
		// X -> X-Axis Quartz Pillar (North-South)
		// Z -> Z-Axis Quartz Pillar (East-West)
		// V -> Vertical Quartz Pillar
		// C -> Chiseled Quartz Block
		// Q -> Normal Quartz Block

		// Vertical Quartz Pillar
		steps.add(new BlockStep(t / 2, new RelativeBlock(0, 0, 0, Material.QUARTZ_BLOCK, BlockData.QUARTZ_PILLAR_VERTICAL)));

		// X-Axis Quartz Pillar
		for (int z = -1; z <= 1; z++) {
			steps.add(new BlockStep(t / 2, new RelativeBlock(-4, 0, z, Material.QUARTZ_BLOCK, BlockData.QUARTZ_PILLAR_X_AXIS)));
			steps.add(new BlockStep(t / 2, new RelativeBlock(4, 0, z, Material.QUARTZ_BLOCK, BlockData.QUARTZ_PILLAR_X_AXIS)));
		}

		// Z-Axis Quartz Pillar
		for (int x = -1; x <= 1; x++) {
			steps.add(new BlockStep(t / 2, new RelativeBlock(x, 0, -4, Material.QUARTZ_BLOCK, BlockData.QUARTZ_PILLAR_Z_AXIS)));
			steps.add(new BlockStep(t / 2, new RelativeBlock(x, 0, -1, Material.QUARTZ_BLOCK, BlockData.QUARTZ_PILLAR_Z_AXIS)));
			steps.add(new BlockStep(t / 2, new RelativeBlock(x, 0, 1, Material.QUARTZ_BLOCK, BlockData.QUARTZ_PILLAR_Z_AXIS)));
			steps.add(new BlockStep(t / 2, new RelativeBlock(x, 0, 4, Material.QUARTZ_BLOCK, BlockData.QUARTZ_PILLAR_Z_AXIS)));
		}

		// Chiseled & Normal Quartz Block, 4 corners
		for (int i = -1; i <= 1; i += 2) {
			for (int j = -1; j <= 1; j += 2) {
				// Chiseled
				steps.add(new BlockStep(t / 2, new RelativeBlock(1 * i, 0, 3 * j, Material.QUARTZ_BLOCK, BlockData.QUARTZ_CHISELED)));
				steps.add(new BlockStep(t / 2, new RelativeBlock(2 * i, 0, 4 * j, Material.QUARTZ_BLOCK, BlockData.QUARTZ_CHISELED)));
				steps.add(new BlockStep(t / 2, new RelativeBlock(3 * i, 0, 1 * j, Material.QUARTZ_BLOCK, BlockData.QUARTZ_CHISELED)));
				steps.add(new BlockStep(t / 2, new RelativeBlock(3 * i, 0, 3 * j, Material.QUARTZ_BLOCK, BlockData.QUARTZ_CHISELED)));
				steps.add(new BlockStep(t / 2, new RelativeBlock(4 * i, 0, 2 * j, Material.QUARTZ_BLOCK, BlockData.QUARTZ_CHISELED)));
				// Normal
				steps.add(new BlockStep(t / 2, new RelativeBlock(1 * i, 0, 2 * j, Material.QUARTZ_BLOCK, BlockData.QUARTZ_NORMAL)));
				steps.add(new BlockStep(t / 2, new RelativeBlock(2 * i, 0, 1 * j, Material.QUARTZ_BLOCK, BlockData.QUARTZ_NORMAL)));
				steps.add(new BlockStep(t / 2, new RelativeBlock(2 * i, 0, 2 * j, Material.QUARTZ_BLOCK, BlockData.QUARTZ_NORMAL)));
				steps.add(new BlockStep(t / 2, new RelativeBlock(2 * i, 0, 3 * j, Material.QUARTZ_BLOCK, BlockData.QUARTZ_NORMAL)));
				steps.add(new BlockStep(t / 2, new RelativeBlock(3 * i, 0, 2 * j, Material.QUARTZ_BLOCK, BlockData.QUARTZ_NORMAL)));
			}
		}

		// Chiseled & Normal Quartz Block, center cross
		for (int i = -1; i <= 1; i += 2) {
			// Z
			steps.add(new BlockStep(t / 2, new RelativeBlock(1 * i, 0, 0, Material.QUARTZ_BLOCK, BlockData.QUARTZ_CHISELED)));
			if (i == -1) {
				steps.add(new BlockStep(t / 2, new RelativeBlock(2 * i, 0, 0, Material.QUARTZ_BLOCK, BlockData.QUARTZ_NORMAL)));
			}
			steps.add(new BlockStep(t / 2, new RelativeBlock(3 * i, 0, 0, Material.QUARTZ_BLOCK, BlockData.QUARTZ_NORMAL)));
			// X
			steps.add(new BlockStep(t / 2, new RelativeBlock(0, 0, 2 * i, Material.QUARTZ_BLOCK, BlockData.QUARTZ_NORMAL)));
			steps.add(new BlockStep(t / 2, new RelativeBlock(0, 0, 3 * i, Material.QUARTZ_BLOCK, BlockData.QUARTZ_CHISELED)));
		}

		// ##########################################

		// Y = 1
		//
		// -3-2-1 0 1 2 3 Z/X
		//
		//    G       G     2
		//
		// G -> Glowstone

		steps.add(new FallingBlockStep(t, new RelativeBlock(2, 1, -2, Material.GLOWSTONE), spawnHeight, new Vector(-0.145, 1, 0.145)));
		steps.add(new FallingBlockStep(t, new RelativeBlock(2, 1, 2, Material.GLOWSTONE), spawnHeight, new Vector(-0.145, 1, -0.145)));

		// ##########################################

		// Y = 1-2
		//
		// -3-2-1 0 1 2 3 Z/X
		//
		//  G           G   0
		//
		// G -> 1 Vertical Quartz Pillar with a Glowstone on top

		steps.add(new FallingBlockStep(2 * t, new RelativeBlock(0, 2, -3, Material.GLOWSTONE), spawnHeight, new Vector(0, 1, 0.2)));
		steps.add(new FallingBlockStep(2 * t, new RelativeBlock(0, 2, 3, Material.GLOWSTONE), spawnHeight, new Vector(0, 1, -0.2)));

		// ##########################################

		// Y = 1-3
		//
		// -2-1 0 1 2 Z/X
		//
		//  G       G  -2
		//
		// G -> 2 Vertical Quartz Pillar with a Glowstone on top

		steps.add(new FallingBlockStep(3 * t, new RelativeBlock(-2, 3, -2, Material.GLOWSTONE), spawnHeight, new Vector(0.135, 1, 0.135)));
		steps.add(new FallingBlockStep(3 * t, new RelativeBlock(-2, 3, 2, Material.GLOWSTONE), spawnHeight, new Vector(0.135, 1, -0.135)));

		// ##########################################

		// Y = 1-4
		//
		// 0 Z/X
		//
		// G  -3
		//
		// G -> 3 Vertical Quartz Pillar with a Glowstone on top
		steps.add(new FallingBlockStep(4 * t, new RelativeBlock(-3, 4, 0, Material.GLOWSTONE), spawnHeight, 1));

		// ##########################################

		// Central Pillar
		steps.add(new FallingBlockStep(6 * t,
		                               new RelativeBlock(0, 1, 0, Material.QUARTZ_BLOCK, BlockData.QUARTZ_PILLAR_VERTICAL),
		                               -1,
		                               0.4));
		steps.add(new BlockStep(6 * t, new RelativeBlock(0, 0, 0, Material.QUARTZ_BLOCK, BlockData.QUARTZ_PILLAR_VERTICAL)));
		// ##########################################

		// Item pop effect
		for (final Entry<Integer, Float> e : getZeldaSound().entrySet()) {
			steps.add(new SoundStep(9 * t + e.getKey() * 4, new RelativeSound(-2, 2, 0, Sound.NOTE_PLING, 10.0f, e.getValue())));
		}
		steps.add(new EffectStep(12 * t, new RelativeEffect(0, 3, 0, Effect.ENDER_SIGNAL)));

		return steps;
	}
}
