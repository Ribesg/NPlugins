/***************************************************************************
 * Project file:    NPlugins - NCore - NEventsListener.java                *
 * Full Class name: fr.ribesg.bukkit.ncore.common.event.NEventsListener    *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.common.event;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class NEventsListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(final PlayerJoinEvent event) {
		Bukkit.getPluginManager().callEvent(new PlayerJoinedEvent(event.getPlayer()));
	}
}
