package fr.ribesg.bukkit.nenchantingegg.altar.transitions;

import java.util.HashSet;
import java.util.Set;

import fr.ribesg.bukkit.nenchantingegg.altar.Altar;
import fr.ribesg.bukkit.nenchantingegg.altar.transitions.steps.AltarTransitionStep;

public abstract class AltarTransition {

    protected Set<AltarTransitionStep> steps;

    public AltarTransition() {
        steps = new HashSet<AltarTransitionStep>();
        createSteps();
    }

    public void doTransition(final Altar altar) {
        for (final AltarTransitionStep step : steps) {
            step.exec(altar);
        }
    }

    protected abstract void createSteps();
}
