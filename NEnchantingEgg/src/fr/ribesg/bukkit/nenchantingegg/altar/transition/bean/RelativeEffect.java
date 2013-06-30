package fr.ribesg.bukkit.nenchantingegg.altar.transition.bean;

import org.bukkit.Effect;

public class RelativeEffect extends RelativeLocation {

    private final Effect  effect;
    private final int     effectData;
    private       boolean hasRadius;
    private       int     radius;

    public RelativeEffect(final double x, final double y, final double z, final Effect effect) {
        this(x, y, z, effect, (byte) 0);
    }

    public RelativeEffect(final double x, final double y, final double z, final Effect effect, final int effectData) {
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

    public Effect getEffect() {
        return effect;
    }

    public int getEffectData() {
        return effectData;
    }

    public boolean hasRadius() {
        return hasRadius;
    }

    public int getRadius() {
        return radius;
    }
}
