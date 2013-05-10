package fr.ribesg.bukkit.nenchantingegg.altar.transition.bean;

import lombok.Getter;

import org.bukkit.Effect;

public class RelativeEffect extends Relative {

    @Getter private final Effect effect;
    @Getter private final int    effectData;
    private boolean              hasRadius;
    @Getter private int          radius;

    public RelativeEffect(final int x, final int y, final int z, final Effect effect) {
        this(x, y, z, effect, (byte) 0);
    }

    public RelativeEffect(final int x, final int y, final int z, final Effect effect, final int effectData) {
        super(x, y, z);
        this.effect = effect;
        this.effectData = effectData;
        hasRadius = false;
        radius = -1;
    }

    public RelativeEffect setRadius(final int radius) {
        hasRadius = true;
        this.radius = radius;
        return this; // Chain call
    }

    public boolean hasRadius() {
        return hasRadius;
    }
}
