package fr.ribesg.bukkit.ncuboid.events.extensions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lombok.Getter;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.ribesg.bukkit.ncuboid.beans.CuboidDB;
import fr.ribesg.bukkit.ncuboid.beans.GeneralCuboid;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;

public class ExtendedPotionSplashEvent extends AbstractExtendedEvent {

    private static Set<PotionEffectType> negativeEffects;

    private static Set<PotionEffectType> getNegativeEffects() {
        if (negativeEffects == null) {
            negativeEffects = new HashSet<PotionEffectType>();
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

    @Getter private final Map<LivingEntity, GeneralCuboid> entityCuboidsMap;
    private boolean                                        hasNegativeEffect = false;

    public ExtendedPotionSplashEvent(final CuboidDB db, final PotionSplashEvent event) {
        super(event);
        final ThrownPotion potion = event.getPotion();
        for (final PotionEffect e : potion.getEffects()) {
            if (getNegativeEffects().contains(e.getType())) {
                hasNegativeEffect = true;
                break;
            }
        }
        entityCuboidsMap = new HashMap<LivingEntity, GeneralCuboid>();
        for (final LivingEntity e : event.getAffectedEntities()) {
            final GeneralCuboid cuboid = db.getPriorByLoc(e.getLocation());
            if (cuboid != null) {
                entityCuboidsMap.put(e, cuboid);
            }
        }

    }

    public boolean hasNegativeEffect() {
        return hasNegativeEffect;
    }

}
