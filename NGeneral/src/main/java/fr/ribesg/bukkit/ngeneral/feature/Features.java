/***************************************************************************
 * Project file:    NPlugins - NGeneral - Features.java                    *
 * Full Class name: fr.ribesg.bukkit.ngeneral.feature.Features             *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral.feature;
import fr.ribesg.bukkit.ngeneral.NGeneral;
import fr.ribesg.bukkit.ngeneral.feature.autoafk.AutoAfkFeature;
import fr.ribesg.bukkit.ngeneral.feature.flymode.FlyModeFeature;
import fr.ribesg.bukkit.ngeneral.feature.godmode.GodModeFeature;
import fr.ribesg.bukkit.ngeneral.feature.itemnetwork.ItemNetworkFeature;
import fr.ribesg.bukkit.ngeneral.feature.protectionsign.ProtectionSignFeature;

import java.util.EnumMap;
import java.util.Map;

/**
 * @author Ribesg
 */
public class Features {

	private final Map<FeatureType, Feature> features;

	public Features(final NGeneral plugin) {
		this.features = new EnumMap<>(FeatureType.class);

		// Create Feature instances
		this.features.put(FeatureType.FLY_MODE, new FlyModeFeature(plugin));
		this.features.put(FeatureType.GOD_MODE, new GodModeFeature(plugin));
		this.features.put(FeatureType.ITEM_NETWORK, new ItemNetworkFeature(plugin));
		this.features.put(FeatureType.PROTECTION_SIGNS, new ProtectionSignFeature(plugin));
		this.features.put(FeatureType.AUTO_AFK, new AutoAfkFeature(plugin));
	}

	public void initialize() {
		for (final Feature feature : this.features.values()) {
			if (feature.isEnabled()) {
				feature.initialize();
			}
		}
	}

	public void terminate() {
		for (final Feature feature : this.features.values()) {
			if (feature.isEnabled()) {
				feature.terminate();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends Feature> T get(final Class<T> clazz) {
		return (T) this.features.get(FeatureType.fromClass(clazz));
	}
}
