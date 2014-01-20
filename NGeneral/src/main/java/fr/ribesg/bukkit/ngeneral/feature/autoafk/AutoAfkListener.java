/***************************************************************************
 * Project file:    NPlugins - NGeneral - AutoAfkListener.java             *
 * Full Class name: fr.ribesg.bukkit.ngeneral.feature.autoafk.AutoAfkListener
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral.feature.autoafk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class AutoAfkListener implements Listener {

	private final AutoAfkFeature feature;

	public AutoAfkListener(final AutoAfkFeature feature) {
		this.feature = feature;
	}

	@EventHandler
	public void onPlayerMove(final PlayerMoveEvent event) {
		final Player player = event.getPlayer();
		feature.update(player.getName());
		if (feature.isAfk(player)) {
			feature.setAfk(player.getName(), false, null);
		}
	}

	@EventHandler
	public void onPlayerInteract(final PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		feature.update(player.getName());
		if (feature.isAfk(player)) {
			feature.setAfk(player.getName(), false, null);
		}
	}

	@EventHandler
	public void onPlayerChat(final AsyncPlayerChatEvent event) {
		final Player player = event.getPlayer();
		feature.update(player.getName());
		if (feature.isAfk(player)) {
			feature.setAfk(player.getName(), false, null);
		}
	}

	@EventHandler
	public void onPlayerCommandPreprocess(final PlayerCommandPreprocessEvent event) {
		final Player player = event.getPlayer();
		feature.update(player.getName());
		if (!event.getMessage().startsWith("/afk")) {
			if (feature.isAfk(player)) {
				feature.setAfk(player.getName(), false, null);
			}
		}
	}
}
