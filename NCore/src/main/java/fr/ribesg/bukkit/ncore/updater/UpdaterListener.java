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
import java.util.Map;
import java.util.SortedMap;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.entity.Player;

public class UpdaterListener implements Listener {

	private final NCore plugin;

	public UpdaterListener(final NCore instance) {
		plugin = instance;
	}

        @EventHandler(priority = EventPriority.HIGHEST)
        public void onJoin(final PlayerJoinedEvent event) {
                Player p = event.getPlayer();
                
                if (Perms.hasUpdateNotice(p)) {
                    SortedMap<String, String> updates = plugin.getPluginUpdater().getUpdateAvailable();
                    if (updates.size() == 0) {
                        return;
                    }
                    
                    StringBuilder updatesString = new StringBuilder();
                    
                    int count = 0;
                    for (Map.Entry<String, String> update : updates.entrySet()) {
                        if (count == updates.size()) {
                            updatesString.append("§e" + update.getKey() + "§7(§b" + update.getValue() + "§7)" + "§8, ");
                        } else {
                            updatesString.append("§e" + update.getKey() + "§7(§b" + update.getValue() + "§7)" + "§8.");
                        }
                        count ++;
                    }
                    
                    if (updates.size() == 1) {
                        p.sendMessage(plugin.getPluginUpdater().getMessagePrefix() + "§bThe following update is available:");
                    } else {
                        p.sendMessage(plugin.getPluginUpdater().getMessagePrefix() + "§bThe following updates are available:");
                    }
                    
                    p.sendMessage(plugin.getPluginUpdater().getMessagePrefix() + updatesString.toString());
                }
        }
}
