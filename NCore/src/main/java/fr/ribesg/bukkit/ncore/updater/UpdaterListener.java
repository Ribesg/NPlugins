/***************************************************************************
 * Project file:    NPlugins - NCore - UpdaterListener.java                *
 * Full Class name: fr.ribesg.bukkit.ncore.updater.UpdaterListener         *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.updater;

import fr.ribesg.bukkit.ncore.NCore;
import fr.ribesg.bukkit.ncore.Perms;
import fr.ribesg.bukkit.ncore.event.PlayerJoinedEvent;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class UpdaterListener implements Listener {

	private final NCore plugin;

	public UpdaterListener(final NCore instance) {
		this.plugin = instance;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoined(final PlayerJoinedEvent event) {
		final Player player = event.getPlayer();
		if (Perms.hasUpdaterNotice(player) && this.plugin.getUpdater() != null) {
			this.plugin.getUpdater().notice(player);
		}
	}
}