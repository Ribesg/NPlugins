/***************************************************************************
 * Project file:    NPlugins - NGeneral - SpyModeListener.java             *
 * Full Class name: fr.ribesg.bukkit.ngeneral.feature.spymode.SpyModeListener
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral.feature.spymode;
import fr.ribesg.bukkit.ncore.event.PlayerJoinedEvent;
import fr.ribesg.bukkit.ngeneral.Perms;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.UUID;

public class SpyModeListener implements Listener {

	private final SpyModeFeature feature;

	public SpyModeListener(final SpyModeFeature feature) {
		this.feature = feature;
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerJoined(final PlayerJoinedEvent event) {
		final Player player = event.getPlayer();
		if (Perms.hasSpy(player)) {
			if (feature.hasSpyMode(player.getUniqueId())) {
				for (final Player other : Bukkit.getOnlinePlayers()) {
					other.hidePlayer(player);
				}
			}
		} else {
			for (final UUID id : feature.getSpyPlayers().keySet()) {
				final Player hidden = Bukkit.getPlayer(id);
				if (hidden != null) {
					player.hidePlayer(hidden);
				}
			}
		}
	}
}
