package fr.ribesg.bukkit.nenchantingegg.altar.transitions.steps;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import fr.ribesg.bukkit.nenchantingegg.NEnchantingEgg;
import fr.ribesg.bukkit.nenchantingegg.altar.Altar;

public abstract class AltarTransitionStep {

    @Getter private final int ticksAfterTransitionStart;

    public AltarTransitionStep(final int ticksAfterTransitionStart) {
        this.ticksAfterTransitionStart = ticksAfterTransitionStart;
    }

    public void exec(final Altar altar) {
        Bukkit.getScheduler().runTaskLater(NEnchantingEgg.getInstance(), new BukkitRunnable() {

            @Override
            public void run() {
                AltarTransitionStep.this.run(altar);
            }
        }, getTicksAfterTransitionStart());
    }

    protected abstract void run(final Altar altar);
}
