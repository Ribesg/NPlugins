/***************************************************************************
 * Project file:    NPlugins - NEnchantingEgg - Transition.java            *
 * Full Class name: fr.ribesg.bukkit.nenchantingegg.altar.transition.Transition
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nenchantingegg.altar.transition;

import fr.ribesg.bukkit.nenchantingegg.NEnchantingEgg;
import fr.ribesg.bukkit.nenchantingegg.altar.Altar;
import fr.ribesg.bukkit.nenchantingegg.altar.AltarState;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.step.Step;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public abstract class Transition {

	protected final NEnchantingEgg plugin;

	protected AltarState fromState;
	protected AltarState toState;

	private final Map<Integer, Set<Step>> stepsPerDelay;
	private final int                     maxDelay;

	protected Transition(final NEnchantingEgg plugin) {
		this.plugin = plugin;
		final Set<Step> steps = createSteps();

		stepsPerDelay = new HashMap<>();

		for (final Step step : steps) {
			if (stepsPerDelay.containsKey(step.getDelay())) {
				stepsPerDelay.get(step.getDelay()).add(step);
			} else {
				final Set<Step> stepsForThisDelay = new HashSet<>();
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
		doTransition(altar, false);
	}

	public void doTransition(final Altar altar, final boolean force) {
		if (plugin != null) {
			if (!force && altar.getState() != fromState) {
				// TODO Exception ?
				plugin.error("Unable to do Transition !");
				plugin.error("Altar Location: " + altar.getCenterLocation().toString());
				plugin.error("Altar state: " + altar.getState().toString());
				plugin.error("Transition to state " +
				             toState.toString() +
				             " failed because the Altar was not in state " +
				             fromState.toString());
				plugin.error("Try to rebuild the Altar?");
			} else {
				altar.setPreviousState(altar.getState());
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
						afterTransition(altar);
					}
				}, maxDelay + 1);
			}
		}
	}

	protected abstract Set<Step> createSteps();

	protected abstract void setFromToStates();

	protected void afterTransition(final Altar altar) {
	}
}
