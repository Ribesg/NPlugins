/***************************************************************************
 * Project file:    NPlugins - NCuboid - ExtendedPotionSplashEvent.java    *
 * Full Class name: fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPotionSplashEvent
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.events.extensions;

import fr.ribesg.bukkit.ncuboid.beans.RegionDb;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ExtendedPotionSplashEvent extends AbstractExtendedEvent {

    private static Set<PotionEffectType> negativeEffects;

    private static Set<PotionEffectType> getNegativeEffects() {
        if (negativeEffects == null) {
            negativeEffects = new HashSet<>();
            negativeEffects.add(PotionEffectType.BLINDNESS);
            negativeEffects.add(PotionEffectType.CONFUSION);
            negativeEffects.add(PotionEffectType.HARM);
            negativeEffects.add(PotionEffectType.HUNGER);
            negativeEffects.add(PotionEffectType.POISON);
            negativeEffects.add(PotionEffectType.SLOW);
            negativeEffects.add(PotionEffectType.SLOW_DIGGING);
            negativeEffects.add(PotionEffectType.WEAKNESS);
            negativeEffects.add(PotionEffectType.WITHER);
        }
        return negativeEffects;
    }

    private boolean hasNegativeEffect;

    public ExtendedPotionSplashEvent(final RegionDb db, final PotionSplashEvent event) {
        super(db.getPlugin(), event);
        final ThrownPotion potion = event.getPotion();
        for (final PotionEffect e : potion.getEffects()) {
            if (getNegativeEffects().contains(e.getType())) {
                this.hasNegativeEffect = true;
                break;
            }
        }
        this.getPlugin().info(String.valueOf(this.hasNegativeEffect));
    }

    public boolean hasNegativeEffect() {
        return this.hasNegativeEffect;
    }
}
