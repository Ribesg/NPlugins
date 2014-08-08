/***************************************************************************
 * Project file:    NPlugins - NCuboid - JailFlagListener.java             *
 * Full Class name: fr.ribesg.bukkit.ncuboid.listeners.flag.JailFlagListener
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.listeners.flag;

import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.ncore.event.PlayerGridMoveEvent;
import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.beans.GeneralRegion;
import fr.ribesg.bukkit.ncuboid.beans.Jail;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerGridMoveEvent;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerTeleportEvent;
import fr.ribesg.bukkit.ncuboid.listeners.AbstractListener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerTeleportEvent;

public class JailFlagListener extends AbstractListener {

	public JailFlagListener(final NCuboid instance) {
		super(instance);
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerGridMove(final ExtendedPlayerGridMoveEvent ext) {
		final PlayerGridMoveEvent event = (PlayerGridMoveEvent)ext.getBaseEvent();
		if (!ext.isCustomCancelled()) {
			final Player player = event.getPlayer();
			if (this.getPlugin().isJailed(player.getUniqueId())) {
				final Jail jail = this.getPlugin().getJails().getJailForPlayer(player.getUniqueId());
				final GeneralRegion region = jail.getRegion();
				if (ext.getToRegions() == null || !ext.getToRegions().contains(region)) {
					player.teleport(NLocation.fixDirection(jail.getLocation().toBukkitLocation(), player.getLocation()));
					ext.setCustomCancelled(true);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerTeleport(final ExtendedPlayerTeleportEvent ext) {
		final PlayerTeleportEvent event = (PlayerTeleportEvent)ext.getBaseEvent();
		final Player player = event.getPlayer();
		if (this.getPlugin().isJailed(player.getUniqueId())) {
			final Jail jail = this.getPlugin().getJails().getJailForPlayer(player.getUniqueId());
			final GeneralRegion region = jail.getRegion();
			if (ext.getToRegions() == null || !ext.getToRegions().contains(region)) {
				event.setCancelled(true);
			}
		}
	}
}
