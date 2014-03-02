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
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.Map;

public class UpdaterListener implements Listener {

	private final NCore plugin;

	public UpdaterListener(final NCore instance) {
		plugin = instance;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(final PlayerJoinedEvent event) {
		final Player p = event.getPlayer();

		if (Perms.hasUpdaterNotice(p) && plugin.getUpdater() != null) {
			final Map<String, String> updates = plugin.getUpdater().getUpdateAvailable();
			if (updates.isEmpty()) {
				return;
			}

			final StringBuilder updatesString = new StringBuilder();

			int count = 0;
			for (final Map.Entry<String, String> update : updates.entrySet()) {
				if (++count != updates.size()) {
					updatesString.append(ChatColor.GOLD).append(plugin.getUpdater().getPlugins().get(update.getKey()).getName());
					updatesString.append(ChatColor.DARK_GREEN).append(" (").append(update.getValue()).append("), ");
				} else {
					updatesString.append(ChatColor.GOLD).append(plugin.getUpdater().getPlugins().get(update.getKey()).getName());
					updatesString.append(ChatColor.DARK_GREEN).append(" (").append(update.getValue()).append(").");
				}
			}

			if (updates.size() == 1) {
				p.sendMessage(plugin.getUpdater().getMessagePrefix() + ChatColor.GREEN + "An update for the following node is available:");
			} else {
				p.sendMessage(plugin.getUpdater().getMessagePrefix() + ChatColor.GREEN + "Updates for the following nodes are available:");
			}

			p.sendMessage(plugin.getUpdater().getMessagePrefix() + updatesString.toString());
		}
	}
}