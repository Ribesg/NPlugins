/***************************************************************************
 * Project file:    NPlugins - NGeneral - FeatureType.java                 *
 * Full Class name: fr.ribesg.bukkit.ngeneral.feature.FeatureType          *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral.feature;

import fr.ribesg.bukkit.ngeneral.feature.autoafk.AutoAfkFeature;
import fr.ribesg.bukkit.ngeneral.feature.flymode.FlyModeFeature;
import fr.ribesg.bukkit.ngeneral.feature.godmode.GodModeFeature;
import fr.ribesg.bukkit.ngeneral.feature.itemnetwork.ItemNetworkFeature;
import fr.ribesg.bukkit.ngeneral.feature.protectionsign.ProtectionSignFeature;
import fr.ribesg.bukkit.ngeneral.feature.spymode.SpyModeFeature;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ribesg
 */
public enum FeatureType {

	AUTO_AFK(AutoAfkFeature.class),
	FLY_MODE(FlyModeFeature.class),
	GOD_MODE(GodModeFeature.class),
	ITEM_NETWORK(ItemNetworkFeature.class),
	PROTECTION_SIGNS(ProtectionSignFeature.class),
	SPY_MODE(SpyModeFeature.class),;

	/**
	 * Maps Feature classes to appropriate FeatureType enum value
	 */
	private static final Map<Class<? extends Feature>, FeatureType> REVERSE_MAP = new HashMap<>();

	/**
	 * From a Feature class, returns the appropriate FeatureType enum value.
	 *
	 * @param clazz the Feature class
	 *
	 * @return the appropriate enum value
	 */
	public static FeatureType fromClass(final Class<? extends Feature> clazz) {
		if (REVERSE_MAP.isEmpty()) {
			for (final FeatureType type : FeatureType.values()) {
				REVERSE_MAP.put(type.toClass(), type);
			}
		}
		return REVERSE_MAP.get(clazz);
	}

	/**
	 * The Feature class linked to this FeatureType enum value
	 */
	private final Class<? extends Feature> clazz;

	private FeatureType(final Class<? extends Feature> clazz) {
		this.clazz = clazz;
	}

	/**
	 * Get the Feature class linked to this FeatureType enum value
	 */
	public Class<? extends Feature> toClass() {
		return this.clazz;
	}
}
