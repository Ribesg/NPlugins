package fr.ribesg.bukkit.nenchantingegg.altar.transition;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import fr.ribesg.bukkit.ncore.utils.Utils;
import fr.ribesg.bukkit.nenchantingegg.NEnchantingEgg;
import fr.ribesg.bukkit.nenchantingegg.altar.Altar;
import fr.ribesg.bukkit.nenchantingegg.altar.AltarState;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.step.Step;

public abstract class Transition {

    protected AltarState                  fromState;
    protected AltarState                  toState;

    private final Map<Integer, Set<Step>> stepsPerDelay;
    private final int                     maxDelay;

    protected Transition() {
        final Set<Step> steps = createSteps();

        stepsPerDelay = new HashMap<Integer, Set<Step>>();

        for (final Step step : steps) {
            if (stepsPerDelay.containsKey(step.getDelay())) {
                stepsPerDelay.get(step.getDelay()).add(step);
            } else {
                final Set<Step> stepsForThisDelay = new HashSet<Step>();
                stepsForThisDelay.add(step);
                stepsPerDelay.put(step.getDelay(), stepsForThisDelay);
            }
        }

        int max = Integer.MIN_VALUE;
        for (final int i : stepsPerDelay.keySet()) {
            if (max < i) {
                max = i;
            }
        }
        maxDelay = max;
        setFromToStates();
    }

    public void doTransition(final Altar altar) {
        final NEnchantingEgg plugin = NEnchantingEgg.getInstance();
        if (plugin != null) {
            if (altar.getState() != fromState) {
                // TODO Exception ?
                final Logger log = plugin.getLogger();
                log.severe("Unable to do Transition !");
                log.severe("Altar Location: " + Utils.toString(altar.getCenterLocation()));
                log.severe("Altar state: " + altar.getState().toString());
                log.severe("Transition to state " + toState.toString() + " failed because the Altar was not in state " + fromState.toString());
                log.severe("Try to rebuild the Altar?");
            } else {
                altar.setState(AltarState.IN_TRANSITION);
                for (final Entry<Integer, Set<Step>> e : stepsPerDelay.entrySet()) {
                    Bukkit.getScheduler().runTaskLater(plugin, new BukkitRunnable() {

                        @Override
                        public void run() {
                            for (final Step step : e.getValue()) {
                                step.doStep(altar);
                            }
                        }
                    }, e.getKey());
                }
                Bukkit.getScheduler().runTaskLater(plugin, new BukkitRunnable() {

                    @Override
                    public void run() {
                        altar.setState(toState);
                    }
                }, maxDelay);
            }
        }
    }

    protected abstract Set<Step> createSteps();

    protected abstract void setFromToStates();
}
