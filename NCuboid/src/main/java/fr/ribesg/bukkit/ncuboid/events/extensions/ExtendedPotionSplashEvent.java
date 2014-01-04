/***************************************************************************
 * Project file:    NPlugins - NCuboid - ExtendedPotionSplashEvent.java    *
 * Full Class name: fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPotionSplashEvent
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.events.extensions;

import fr.ribesg.bukkit.ncuboid.beans.GeneralRegion;
import fr.ribesg.bukkit.ncuboid.beans.RegionDb;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

	private final Map<LivingEntity, GeneralRegion> entityRegionsMap;
	private boolean hasNegativeEffect = false;

	public ExtendedPotionSplashEvent(final RegionDb db, final PotionSplashEvent event) {
		super(event);
		final ThrownPotion potion = event.getPotion();
		for (final PotionEffect e : potion.getEffects()) {
			if (getNegativeEffects().contains(e.getType())) {
				hasNegativeEffect = true;
				break;
			}
		}
		entityRegionsMap = new HashMap<>();
		for (final LivingEntity e : event.getAffectedEntities()) {
			final GeneralRegion cuboid = db.getPriorByLocation(e.getLocation());
			if (cuboid != null) {
				entityRegionsMap.put(e, cuboid);
			}
		}

	}

	public boolean hasNegativeEffect() {
		return hasNegativeEffect;
	}

	public Map<LivingEntity, GeneralRegion> getEntityRegionsMap() {
		return entityRegionsMap;
	}
}
