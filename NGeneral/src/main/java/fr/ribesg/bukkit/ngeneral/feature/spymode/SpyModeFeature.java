/***************************************************************************
 * Project file:    NPlugins - NGeneral - SpyModeFeature.java              *
 * Full Class name: fr.ribesg.bukkit.ngeneral.feature.spymode.SpyModeFeature
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral.feature.spymode;
import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.ngeneral.NGeneral;
import fr.ribesg.bukkit.ngeneral.Perms;
import fr.ribesg.bukkit.ngeneral.feature.Feature;
import fr.ribesg.bukkit.ngeneral.feature.FeatureType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpyModeFeature extends Feature {

	private final Map<UUID, NLocation> spyPlayers;

	public SpyModeFeature(final NGeneral instance) {
		super(instance, FeatureType.SPY_MODE, instance.getPluginConfig().hasSpyModeFeature());
		spyPlayers = new HashMap<>();
	}

	@Override
	public void initialize() {
		final SpyModeListener listener = new SpyModeListener(this);
		final SpyModeCommandExecutor executor = new SpyModeCommandExecutor(this);

		Bukkit.getPluginManager().registerEvents(listener, plugin);
		plugin.setCommandExecutor("spy", executor);
	}

	public Map<UUID, NLocation> getSpyPlayers() {
		return spyPlayers;
	}

	public boolean hasSpyMode(final UUID playerId) {
		return spyPlayers.containsKey(playerId);
	}

	public void setSpyMode(final Player spy, final Player spied) {
		spyPlayers.put(spy.getUniqueId(), spied == null ? null : new NLocation(spy.getLocation()));
		for (final Player other : Bukkit.getOnlinePlayers()) {
			if (!Perms.hasSpy(other)) {
				other.hidePlayer(spy);
			}
		}
		if (spied != null) {
			Bukkit.getScheduler().runTaskLater(getPlugin(), new BukkitRunnable() {

				@Override
				public void run() {
					spy.teleport(spied.getLocation());
				}
			}, 1L);
		}
	}

	public void unSetSpyMode(final Player player) {
		// TODO Should check if we're not in an INVISIBLE NCuboid Region first
		final NLocation previousLocation = this.spyPlayers.remove(player.getUniqueId());
		if (previousLocation != null) {
			player.teleport(previousLocation.toBukkitLocation());
		}
		Bukkit.getScheduler().runTaskLater(getPlugin(), new BukkitRunnable() {

			@Override
			public void run() {
				for (final Player other : Bukkit.getOnlinePlayers()) {
					other.showPlayer(player);
				}
			}
		}, 1L);
	}
}
