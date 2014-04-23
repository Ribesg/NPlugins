/***************************************************************************
 * Project file:    NPlugins - NGeneral - FlyModeFeature.java              *
 * Full Class name: fr.ribesg.bukkit.ngeneral.feature.flymode.FlyModeFeature
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral.feature.flymode;
import fr.ribesg.bukkit.ngeneral.NGeneral;
import fr.ribesg.bukkit.ngeneral.Perms;
import fr.ribesg.bukkit.ngeneral.feature.Feature;
import fr.ribesg.bukkit.ngeneral.feature.FeatureType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FlyModeFeature extends Feature {

	private final Set<UUID> flyPlayers;

	public FlyModeFeature(final NGeneral instance) {
		super(instance, FeatureType.FLY_MODE, instance.getPluginConfig().hasFlyModeFeature());
		flyPlayers = new HashSet<>();
	}

	@Override
	public void initialize() {
		final FlyModeListener listener = new FlyModeListener(this);
		final FlyModeCommandExecutor executor = new FlyModeCommandExecutor(this);

		Bukkit.getPluginManager().registerEvents(listener, plugin);
		plugin.setCommandExecutor("fly", executor);
	}

	@Override
	public void terminate() {
		for (final Player player : Bukkit.getOnlinePlayers()) {
			if (!Perms.hasFly(player) && this.hasFlyMode(player)) {
				player.setAllowFlight(false);
				this.setFlyMode(player, false);
				player.setFallDistance(-100f);
			}
		}
	}

	public Set<UUID> getFlyPlayers() {
		return flyPlayers;
	}

	public boolean hasFlyMode(final Player player) {
		return flyPlayers.contains(player.getUniqueId());
	}

	public void setFlyMode(final Player player, final boolean value) {
		if (value) {
			flyPlayers.add(player.getUniqueId());
			player.setAllowFlight(true);
		} else {
			flyPlayers.remove(player.getUniqueId());
			player.setAllowFlight(false);
		}
	}
}
