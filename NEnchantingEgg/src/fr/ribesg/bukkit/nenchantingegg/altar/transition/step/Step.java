package fr.ribesg.bukkit.nenchantingegg.altar.transition.step;

import lombok.Getter;
import fr.ribesg.bukkit.nenchantingegg.altar.Altar;

public abstract class Step {

    @Getter private final int delay;

    public Step(final int delay) {
        this.delay = delay;
    }

    public abstract void doStep(final Altar altar);
}
