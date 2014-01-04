/***************************************************************************
 * Project file:    NPlugins - NCore - NEventsListener.java                *
 * Full Class name: fr.ribesg.bukkit.ncore.event.NEventsListener           *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.event;
import fr.ribesg.bukkit.ncore.NCore;
import fr.ribesg.bukkit.ncore.event.core.PlayerJoinedEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class NEventsListener implements Listener {

	private final NCore plugin;

	public NEventsListener(final NCore instance) {
		plugin = instance;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(final PlayerJoinEvent event) {
		Bukkit.getScheduler().runTaskLater(this.plugin, new BukkitRunnable() {

			@Override
			public void run() {
				Bukkit.getPluginManager().callEvent(new PlayerJoinedEvent(event.getPlayer()));
			}
		}, 1L);
	}
}
