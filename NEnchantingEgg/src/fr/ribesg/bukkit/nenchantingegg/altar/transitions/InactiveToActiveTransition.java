package fr.ribesg.bukkit.nenchantingegg.altar.transitions;

import fr.ribesg.bukkit.nenchantingegg.altar.transitions.steps.InactiveToActiveStep1;

public class InactiveToActiveTransition extends AltarTransition {

    @Override
    protected void createSteps() {
        steps.add(new InactiveToActiveStep1());
    }

}
