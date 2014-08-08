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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class Transition {

	protected final NEnchantingEgg plugin;

	protected AltarState fromState;
	protected AltarState toState;

	private final Map<Integer, Set<Step>> stepsPerDelay;
	private final int                     maxDelay;

	protected Transition(final NEnchantingEgg plugin) {
		this.plugin = plugin;
		final Set<Step> steps = this.createSteps();

		this.stepsPerDelay = new HashMap<>();

		for (final Step step : steps) {
			if (this.stepsPerDelay.containsKey(step.getDelay())) {
				this.stepsPerDelay.get(step.getDelay()).add(step);
			} else {
				final Set<Step> stepsForThisDelay = new HashSet<>();
				stepsForThisDelay.add(step);
				this.stepsPerDelay.put(step.getDelay(), stepsForThisDelay);
			}
		}

		int max = Integer.MIN_VALUE;
		for (final int i : this.stepsPerDelay.keySet()) {
			if (max < i) {
				max = i;
			}
		}
		this.maxDelay = max;
		this.setFromToStates();
	}

	public void doTransition(final Altar altar) {
		this.doTransition(altar, false);
	}

	public void doTransition(final Altar altar, final boolean force) {
		if (this.plugin != null) {
			if (!force && altar.getState() != this.fromState) {
				// TODO Exception ?
				this.plugin.error("Unable to do Transition !");
				this.plugin.error("Altar Location: " + altar.getCenterLocation());
				this.plugin.error("Altar state: " + altar.getState());
				this.plugin.error("Transition to state " +
				                  this.toState +
				                  " failed because the Altar was not in state " +
				                  this.fromState);
				this.plugin.error("Try to rebuild the Altar?");
			} else {
				altar.setPreviousState(altar.getState());
				altar.setState(AltarState.IN_TRANSITION);
				for (final Entry<Integer, Set<Step>> e : this.stepsPerDelay.entrySet()) {
					Bukkit.getScheduler().runTaskLater(this.plugin, new BukkitRunnable() {

						@Override
						public void run() {
							for (final Step step : e.getValue()) {
								step.doStep(altar);
							}
						}
					}, e.getKey());
				}
				Bukkit.getScheduler().runTaskLater(this.plugin, new BukkitRunnable() {

					@Override
					public void run() {
						altar.setState(fr.ribesg.bukkit.nenchantingegg.altar.transition.Transition.this.toState);
						fr.ribesg.bukkit.nenchantingegg.altar.transition.Transition.this.afterTransition(altar);
					}
				}, this.maxDelay + 1);
			}
		}
	}

	protected abstract Set<Step> createSteps();

	protected abstract void setFromToStates();

	protected void afterTransition(final Altar altar) {
	}
}
