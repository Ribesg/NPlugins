package fr.ribesg.bukkit.nenchantingegg.altar.transition;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.BlockFace;

import fr.ribesg.bukkit.nenchantingegg.altar.AltarState;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.bean.BlockData;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.bean.RelativeBlock;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.bean.RelativeSkullBlock;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.step.BlockStep;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.step.Step;

// TODO
public class ActiveToEggProvidedTransition extends Transition {

    private static ActiveToEggProvidedTransition instance;

    public static ActiveToEggProvidedTransition getInstance() {
        if (instance == null) {
            instance = new ActiveToEggProvidedTransition();
        }
        return instance;
    }

    @Override
    protected void setFromToStates() {
        fromState = AltarState.ACTIVE;
        toState = AltarState.EGG_PROVIDED;
    }

    @Override
    protected Set<Step> createSteps() {

        final Set<Step> steps = new HashSet<Step>(); // Result

        final int t = 42; // Time between block changes

        // ##########################################

        // Central portal frame block

        steps.add(new BlockStep(t / 4, new RelativeBlock(0, 0, 0, Material.ENDER_PORTAL_FRAME)));

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

        steps.add(new BlockStep(t / 4, new RelativeBlock(-2, 0, -1, Material.ENDER_PORTAL)));
        steps.add(new BlockStep(t / 4, new RelativeBlock(-2, 0, 0, Material.ENDER_PORTAL)));
        steps.add(new BlockStep(t / 4, new RelativeBlock(-2, 0, 1, Material.ENDER_PORTAL)));

        for (int x = -1; x <= 1; x++) {
            for (int z = -2; z <= 2; z++) {
                if (x == 0 && z == 0) {
                    continue;
                } else {
                    steps.add(new BlockStep(t / 4, new RelativeBlock(x, 0, z, Material.ENDER_PORTAL)));
                }
            }
        }

        steps.add(new BlockStep(t / 4, new RelativeBlock(2, 0, -1, Material.ENDER_PORTAL)));
        steps.add(new BlockStep(t / 4, new RelativeBlock(2, 0, 1, Material.ENDER_PORTAL)));

        // ##########################################

        // Y = 1
        //
        // -3-2-1 0 1 2 3 Z/X
        //
        //    Q       Q     2
        //
        // Q -> Quartz Pillar (vertical)

        steps.add(new BlockStep(3 * t, new RelativeBlock(2, 1, -2, Material.QUARTZ_BLOCK, BlockData.QUARTZ_PILLAR_VERTICAL)));
        steps.add(new BlockStep(3 * t, new RelativeBlock(2, 1, 2, Material.QUARTZ_BLOCK, BlockData.QUARTZ_PILLAR_VERTICAL)));

        // ##########################################

        // Y = 1-2
        //
        // -3-2-1 0 1 2 3 Z/X
        //
        //  Q           Q   0
        //
        // Q -> Quartz Pillar (vertical)

        for (int i = 1; i <= 2; i++) {
            steps.add(new BlockStep(3 * t + t / 8, new RelativeBlock(0, i, -3, Material.QUARTZ_BLOCK, BlockData.QUARTZ_PILLAR_VERTICAL)));
            steps.add(new BlockStep(3 * t + t / 8, new RelativeBlock(0, i, 3, Material.QUARTZ_BLOCK, BlockData.QUARTZ_PILLAR_VERTICAL)));
        }

        // ##########################################

        // Y = 1-3
        //
        // -2-1 0 1 2 Z/X
        //
        //  Q       Q  -2
        //
        // Q -> Quartz Pillar (vertical)

        for (int i = 1; i <= 3; i++) {
            steps.add(new BlockStep(3 * t + 2 * t / 8, new RelativeBlock(-2, i, -2, Material.QUARTZ_BLOCK, BlockData.QUARTZ_PILLAR_VERTICAL)));
            steps.add(new BlockStep(3 * t + 2 * t / 8, new RelativeBlock(-2, i, 2, Material.QUARTZ_BLOCK, BlockData.QUARTZ_PILLAR_VERTICAL)));
        }

        // ##########################################

        // Y = 1-4
        //
        // 0 Z/X
        //
        // Q  -3
        //
        // Q -> Quartz Pillar (vertical)

        for (int i = 1; i <= 4; i++) {
            steps.add(new BlockStep(3 * t + 3 * t / 8, new RelativeBlock(-3, i, 0, Material.QUARTZ_BLOCK, BlockData.QUARTZ_PILLAR_VERTICAL)));
        }

        // ##########################################

        // Y = 2
        // Skeleton head
        //
        // 0 Z/X
        //
        // H  -2
        //
        // H -> Skeleton Head

        steps.add(new BlockStep(4 * t, new RelativeBlock(-2, 1, 0, Material.QUARTZ_BLOCK, BlockData.QUARTZ_PILLAR_VERTICAL)));
        steps.add(new BlockStep(4 * t, new RelativeSkullBlock(-2, 2, 0, SkullType.SKELETON, BlockFace.EAST)));

        return steps;
    }
}
