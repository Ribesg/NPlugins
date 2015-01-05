/***************************************************************************
 * Project file:    NPlugins - NEnchantingEgg - RelativeEffect.java        *
 * Full Class name: fr.ribesg.bukkit.nenchantingegg.altar.transition.bean.RelativeEffect
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nenchantingegg.altar.transition.bean;

import org.bukkit.Effect;

public class RelativeEffect extends RelativeLocation {

    private final Effect  effect;
    private final int     effectData;
    private       boolean hasRadius;
    private       int     radius;

    public RelativeEffect(final double x, final double y, final double z, final Effect effect) {
        this(x, y, z, effect, (byte)0);
    }

    public RelativeEffect(final double x, final double y, final double z, final Effect effect, final int effectData) {
        super(x, y, z);
        this.effect = effect;
        this.effectData = effectData;
        this.hasRadius = false;
        this.radius = -1;
    }

    public RelativeEffect setRadius(final int radius) {
        this.hasRadius = true;
        this.radius = radius;
        return this; // Chain call
    }

    public Effect getEffect() {
        return this.effect;
    }

    public int getEffectData() {
        return this.effectData;
    }

    public boolean hasRadius() {
        return this.hasRadius;
    }

    public int getRadius() {
        return this.radius;
    }
}
